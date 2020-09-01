/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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
package com.djl.examples.training;

import ai.djl.Device;
import ai.djl.Model;
import ai.djl.basicdataset.Mnist;
import ai.djl.basicmodelzoo.basic.Mlp;
import ai.djl.training.DefaultTrainingConfig;
import ai.djl.training.EasyTrain;
import ai.djl.training.Trainer;
import ai.djl.training.TrainingResult;
import com.djl.examples.training.util.Arguments;
import ai.djl.translate.TranslateException;
import ai.djl.metric.Metrics;
import ai.djl.ndarray.types.Shape;
import ai.djl.nn.Block;
import ai.djl.training.dataset.Dataset;
import ai.djl.training.dataset.RandomAccessDataset;
import ai.djl.training.evaluator.Accuracy;
import ai.djl.training.hyperparameter.optimizer.HpORandom;
import ai.djl.training.hyperparameter.optimizer.HpOptimizer;
import ai.djl.training.hyperparameter.param.HpInt;
import ai.djl.training.hyperparameter.param.HpSet;
import ai.djl.training.listener.CheckpointsTrainingListener;
import ai.djl.training.listener.TrainingListener;
import ai.djl.training.loss.Loss;
import ai.djl.training.util.ProgressBar;
import ai.djl.util.Pair;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TrainWithHpo {

    private static final Logger logger = LoggerFactory.getLogger(TrainWithHpo.class);

    private TrainWithHpo() {}

    public static void main(String[] args) throws Exception, ParseException {
        TrainWithHpo.runExample(args);
    }

    public static TrainingResult runExample(String[] args) throws Exception, ParseException {
        Arguments arguments = Arguments.parseArgs(args);

        // get training and validation dataset
        RandomAccessDataset trainingSet = getDataset(Dataset.Usage.TRAIN, arguments);
        RandomAccessDataset validateSet = getDataset(Dataset.Usage.TEST, arguments);

        HpSet hyperParams =
                new HpSet(
                        "hp",
                        Arrays.asList(
                                new HpInt("hiddenLayersSize", 10, 100),
                                new HpInt("hiddenLayersCount", 2, 10)));
        HpOptimizer hpOptimizer = new HpORandom(hyperParams);

        final int hyperparameterTests = 50;

        for (int i = 0; i < hyperparameterTests; i++) {
            HpSet hpVals = hpOptimizer.nextConfig();
            Pair<Model, TrainingResult> trained =
                    train(arguments, hpVals, trainingSet, validateSet);
            trained.getKey().close();
            float loss = trained.getValue().getValidateLoss();
            hpOptimizer.update(hpVals, loss);
            logger.info(
                    "--------- hp 测试 {}/{} - 丢失 {} - {}", i, hyperparameterTests, loss, hpVals);
        }

        HpSet bestHpVals = hpOptimizer.getBest().getKey();
        Pair<Model, TrainingResult> trained =
                train(arguments, bestHpVals, trainingSet, validateSet);
        TrainingResult result = trained.getValue();
        float loss = result.getValidateLoss();
        try (Model model = trained.getKey()) {
            logger.info("--------- FINAL_HP - 丢失 {} - {}", loss, bestHpVals);

            model.setProperty("Epoch", String.valueOf(result.getEpoch()));
            model.setProperty(
                    "Accuracy", String.format("%.5f", result.getValidateEvaluation("Accuracy")));
            model.setProperty("Loss", String.format("%.5f", loss));
            model.save(Paths.get(arguments.getOutputDir()), "mlp");
        }
        return result;
    }

    private static Pair<Model, TrainingResult> train(
            Arguments arguments,
            HpSet hpVals,
            RandomAccessDataset trainingSet,
            RandomAccessDataset validateSet) throws Exception, TranslateException {
        // 构造神经网络
        int[] hidden = new int[(Integer) hpVals.getHParam("hiddenLayersCount").random()];
        Arrays.fill(hidden, (Integer) hpVals.getHParam("hiddenLayersSize").random());
        Block block = new Mlp(Mnist.IMAGE_HEIGHT * Mnist.IMAGE_WIDTH, Mnist.NUM_CLASSES, hidden);
        Model model = Model.newInstance("mlp");
        model.setBlock(block);

        // 设置培训配置
        DefaultTrainingConfig config = setupTrainingConfig(arguments);

        try (Trainer trainer = model.newTrainer(config)) {
            trainer.setMetrics(new Metrics());

            /*
             * MNIST是28x28灰度图像，预处理成28*28ndarray.
             * 第一个轴是批处理轴, 可以使用1进行初始化.
             */
            Shape inputShape = new Shape(1, Mnist.IMAGE_HEIGHT * Mnist.IMAGE_WIDTH);

            // 使用正确的输入形状初始化培训器
            trainer.initialize(inputShape);

            EasyTrain.fit(trainer, arguments.getEpoch(), trainingSet, validateSet);

            TrainingResult result = trainer.getTrainingResult();
            return new Pair<>(model, result);
        }
    }

    private static DefaultTrainingConfig setupTrainingConfig(Arguments arguments) {
        String outputDir = arguments.getOutputDir();
        CheckpointsTrainingListener listener = new CheckpointsTrainingListener(outputDir);
        listener.setSaveModelCallback(
                trainer -> {
                    TrainingResult result = trainer.getTrainingResult();
                    Model model = trainer.getModel();
                    float accuracy = result.getValidateEvaluation("Accuracy");
                    model.setProperty("Accuracy", String.format("%.5f", accuracy));
                    model.setProperty("Loss", String.format("%.5f", result.getValidateLoss()));
                });

        return new DefaultTrainingConfig(Loss.softmaxCrossEntropyLoss())
                .addEvaluator(new Accuracy())
                .optDevices(Device.getDevices(arguments.getMaxGpus()))
                .addTrainingListeners(TrainingListener.Defaults.logging(outputDir))
                .addTrainingListeners(listener);
    }

    private static RandomAccessDataset getDataset(Dataset.Usage usage, Arguments arguments)
            throws IOException {
        Mnist mnist =
                Mnist.builder()
                        .optUsage(usage)
                        .setSampling(arguments.getBatchSize(), true)
                        .optLimit(arguments.getLimit())
                        .build();
        mnist.prepare(new ProgressBar());
        return mnist;
    }
}
