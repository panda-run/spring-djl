/*
 * Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.djl.examples.inference.benchmark.util;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.engine.Engine;
import com.djl.examples.inference.benchmark.MultithreadedBenchmark;
import ai.djl.metric.Metrics;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.Shape;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.listener.MemoryTrainingListener;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import java.io.IOException;
import java.time.Duration;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 抽象类，用于封装示例项目的命令行选项 */
public abstract class AbstractBenchmark {

    private static final Logger logger = LoggerFactory.getLogger(AbstractBenchmark.class);

    private Object lastResult;

    protected ProgressBar progressBar;

    /**
     * 必须由子类实现的抽象预测方法.
     *
     * @param arguments 命令行参数
     * @param metrics {@link Metrics} to collect statistic information
     * @param iteration 要运行的预测迭代数
     * @return 预测结果
     * @throws IOException 如果加载模型时发生io错误.
     * @throws ModelException 如果找不到指定的模型或存在参数错误
     * @throws TranslateException 如果在处理输入或输出时发生错误
     * @throws ClassNotFoundException 如果无法加载输入或输出类
     */
    protected abstract Object predict(Arguments arguments, Metrics metrics, int iteration)
            throws IOException, ModelException, TranslateException, ClassNotFoundException;

    /**
     * 返回命令行选项.
     *
     * <p>子类可以重写此方法并返回不同的命令行选项.
     *
     * @return 命令行选项
     */
    protected Options getOptions() {
        return Arguments.getOptions();
    }

    /**
     * 将命令行解析为参数.
     *
     * <p>子类可以重写此方法并返回扩展名 of {@link Arguments}.
     *
     * @param cmd 根据{@link Options}描述符分析的参数列表
     * @return 解析的参数
     */
    protected Arguments parseArguments(CommandLine cmd) {
        return new Arguments(cmd);
    }

    /**
     * 执行示例代码.
     *
     * @param args 输入原始参数
     * @return 示例执行成功完成
     */
    public final boolean runBenchmark(String[] args) {
        Options options = getOptions();
        try {
            DefaultParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args, null, false);
            Arguments arguments = parseArguments(cmd);

            long init = System.nanoTime();
            String version = Engine.getInstance().getVersion();
            long loaded = System.nanoTime();
            logger.info(
                    String.format(
                            "加载库%s(以%.3f毫秒为单位).", version, (loaded - init) / 1_000_000f));
            Duration duration = Duration.ofMinutes(arguments.getDuration());
            if (arguments.getDuration() != 0) {
                logger.info(
                        "正在运行 {} 时间: {}, 持续时间: {} 分钟.",
                        getClass().getSimpleName(),
                        Device.defaultDevice(),
                        duration.toMinutes());
            } else {
                logger.info(
                        "正在运行 {} 时间: {}.", getClass().getSimpleName(), Device.defaultDevice());
            }
            int numOfThreads = arguments.getThreads();
            int iteration = arguments.getIteration();
            if (this instanceof MultithreadedBenchmark) {
                iteration = Math.max(iteration, numOfThreads * 2);
            }
            while (!duration.isNegative()) {
                Metrics metrics = new Metrics(); // Reset Metrics for each test loop.
                progressBar = new ProgressBar("Iteration", iteration);
                long begin = System.currentTimeMillis();
                lastResult = predict(arguments, metrics, iteration);

                if (metrics.hasMetric("mt_start")) {
                    begin = metrics.getMetric("mt_start").get(0).getValue().longValue();
                }
                long totalTime = System.currentTimeMillis() - begin;

                logger.info("推理结果: {}", lastResult);
                String throughput = String.format("%.2f", iteration * 1000d / totalTime);
                logger.info(
                        "吞吐量: {}, {} 迭代 / {} ms.", throughput, iteration, totalTime);

                if (metrics.hasMetric("LoadModel")) {
                    long loadModelTime =
                            metrics.getMetric("LoadModel").get(0).getValue().longValue();
                    logger.info(
                            "模型加载时间: {} ms.",
                            String.format("%.3f", loadModelTime / 1_000_000f));
                }

                if (metrics.hasMetric("Inference") && iteration > 1) {
                    float totalP50 =
                            metrics.percentile("Total", 50).getValue().longValue() / 1_000_000f;
                    float totalP90 =
                            metrics.percentile("Total", 90).getValue().longValue() / 1_000_000f;
                    float totalP99 =
                            metrics.percentile("Total", 99).getValue().longValue() / 1_000_000f;
                    float p50 =
                            metrics.percentile("Inference", 50).getValue().longValue() / 1_000_000f;
                    float p90 =
                            metrics.percentile("Inference", 90).getValue().longValue() / 1_000_000f;
                    float p99 =
                            metrics.percentile("Inference", 99).getValue().longValue() / 1_000_000f;
                    float preP50 =
                            metrics.percentile("Preprocess", 50).getValue().longValue()
                                    / 1_000_000f;
                    float preP90 =
                            metrics.percentile("Preprocess", 90).getValue().longValue()
                                    / 1_000_000f;
                    float preP99 =
                            metrics.percentile("Preprocess", 99).getValue().longValue()
                                    / 1_000_000f;
                    float postP50 =
                            metrics.percentile("Postprocess", 50).getValue().longValue()
                                    / 1_000_000f;
                    float postP90 =
                            metrics.percentile("Postprocess", 90).getValue().longValue()
                                    / 1_000_000f;
                    float postP99 =
                            metrics.percentile("Postprocess", 99).getValue().longValue()
                                    / 1_000_000f;
                    logger.info(
                            String.format(
                                    "总计 P50: %.3f ms, P90: %.3f ms, P99: %.3f ms",
                                    totalP50, totalP90, totalP99));
                    logger.info(
                            String.format(
                                    "推论 P50: %.3f ms, P90: %.3f ms, P99: %.3f ms",
                                    p50, p90, p99));
                    logger.info(
                            String.format(
                                    "预处理 P50: %.3f ms, P90: %.3f ms, P99: %.3f ms",
                                    preP50, preP90, preP99));
                    logger.info(
                            String.format(
                                    "后置处理 P50: %.3f ms, P90: %.3f ms, P99: %.3f ms",
                                    postP50, postP90, postP99));

                    if (Boolean.getBoolean("collect-memory")) {
                        float heap = metrics.percentile("Heap", 90).getValue().longValue();
                        float nonHeap = metrics.percentile("NonHeap", 90).getValue().longValue();
                        float cpu = metrics.percentile("cpu", 90).getValue().longValue();
                        float rss = metrics.percentile("rss", 90).getValue().longValue();

                        logger.info(String.format("堆 P90: %.3f", heap));
                        logger.info(String.format("非堆 P90: %.3f", nonHeap));
                        logger.info(String.format("cpu P90: %.3f", cpu));
                        logger.info(String.format("rss P90: %.3f", rss));
                    }
                }
                MemoryTrainingListener.dumpMemoryInfo(metrics, arguments.getOutputDir());
                long delta = System.currentTimeMillis() - begin;
                duration = duration.minus(Duration.ofMillis(delta));
                if (!duration.isNegative()) {
                    logger.info(duration.toMinutes() + " 还剩分钟");
                }
            }
            return true;
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.setLeftPadding(1);
            formatter.setWidth(120);
            formatter.printHelp(e.getMessage(), options);
        } catch (Throwable t) {
            logger.error("意外错误", t);
        }
        return false;
    }

    /**
     * 返回上一个预测结果.
     *
     * <p>此方法仅用于单元测试.
     *
     * @return 上次预测结果
     */
    public Object getPredictResult() {
        return lastResult;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected ZooModel<?, ?> loadModel(Arguments arguments, Metrics metrics)
            throws ModelException, IOException, ClassNotFoundException {
        long begin = System.nanoTime();
        String artifactId = arguments.getArtifactId();
        Class<?> input = arguments.getInputClass();
        Class<?> output = arguments.getOutputClass();
        Shape shape = arguments.getInputShape();

        Criteria.Builder<?, ?> builder =
                Criteria.builder()
                        .setTypes(input, output)
                        .optFilters(arguments.getCriteria())
                        .optArtifactId(artifactId)
                        .optProgress(new ProgressBar());

        if (shape != null) {
            builder.optTranslator(
                    new Translator() {

                        /** {@inheritDoc} */
                        @Override
                        public NDList processInput(TranslatorContext ctx, Object input) {
                            return new NDList(ctx.getNDManager().ones(shape));
                        }

                        /** {@inheritDoc} */
                        @Override
                        public Object processOutput(TranslatorContext ctx, NDList list) {
                            return list.get(0).toFloatArray();
                        }

                        /** {@inheritDoc} */
                        @Override
                        public Batchifier getBatchifier() {
                            return null;
                        }
                    });
        }

        ZooModel<?, ?> model = ModelZoo.loadModel(builder.build());
        long delta = System.nanoTime() - begin;
        logger.info(
                "模型 {} 加载 in: {} ms.",
                model.getName(),
                String.format("%.3f", delta / 1_000_000f));
        metrics.addMetric("LoadModel", delta);
        return model;
    }
}
