package com.common.staticvoidmainTest;

import ai.djl.Model;
import ai.djl.basicdataset.FashionMnist;
import ai.djl.metric.Metrics;
import ai.djl.ndarray.types.Shape;
import ai.djl.nn.Activation;
import ai.djl.nn.Blocks;
import ai.djl.nn.SequentialBlock;
import ai.djl.nn.core.Linear;
import ai.djl.training.DefaultTrainingConfig;
import ai.djl.training.EasyTrain;
import ai.djl.training.Trainer;
import ai.djl.training.dataset.Dataset;
import ai.djl.training.evaluator.Accuracy;
import ai.djl.training.initializer.NormalInitializer;
import ai.djl.training.listener.TrainingListener;
import ai.djl.training.loss.Loss;
import ai.djl.training.optimizer.Optimizer;
import ai.djl.training.tracker.Tracker;
import ai.djl.translate.TranslateException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * ClassName: MultilayerImpl
 * Description: 多层感知器实现
 * Author: James Zow
 * Date: 2020/10/27 0027 20:47
 * Version:
 **/
public class MultilayerImpl {

    /**
     * main method 入口
     * @param args
     */
    public static void main(String [] args){
        runExample();
    }


    public static void runExample(){

        SequentialBlock net = new SequentialBlock();
        net.add(Blocks.batchFlattenBlock(784));
        net.add(Linear.builder().setUnits(256).build());
        net.add(Activation::relu);
        net.add(Linear.builder().setUnits(10).build());
        net.setInitializer(new NormalInitializer());

        int batchSize = 256;
        int numEpochs = 10;
        double[] trainLoss;
        double[] testAccuracy;
        double[] epochCount;
        double[] trainAccuracy;

        trainLoss = new double[numEpochs];
        trainAccuracy = new double[numEpochs];
        testAccuracy = new double[numEpochs];
        epochCount = new double[numEpochs];

        FashionMnist trainIter = FashionMnist.builder()
                .optUsage(Dataset.Usage.TRAIN)
                .setSampling(batchSize, true)
                .build();


        FashionMnist testIter = FashionMnist.builder()
                .optUsage(Dataset.Usage.TEST)
                .setSampling(batchSize, true)
                .build();

        try {
            trainIter.prepare();
            testIter.prepare();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (TranslateException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < epochCount.length; i++) {
            epochCount[i] = (i + 1);
        }

        Map<String, double[]> evaluatorMetrics = new HashMap<>();
        Tracker lrt = Tracker.fixed(0.5f);
        Optimizer sgd = Optimizer.sgd().setLearningRateTracker(lrt).build();

        Loss loss = Loss.softmaxCrossEntropyLoss();

        DefaultTrainingConfig config = new DefaultTrainingConfig(loss)
                .optOptimizer(sgd) // Optimizer (loss function)
                .addEvaluator(new Accuracy()) // Model Accuracy
                .addTrainingListeners(TrainingListener.Defaults.logging()); // Logging

        try (Model model = Model.newInstance("mlp")) {
            model.setBlock(net);

            try (Trainer trainer = model.newTrainer(config)) {

                trainer.initialize(new Shape(1, 784));
                trainer.setMetrics(new Metrics());

                try {
                    EasyTrain.fit(trainer, numEpochs, trainIter, testIter);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (TranslateException e) {
                    e.printStackTrace();
                }
                // collect results from evaluators
                Metrics metrics = trainer.getMetrics();

                trainer.getEvaluators().stream()
                        .forEach(evaluator -> {
                            evaluatorMetrics.put("train_epoch_" + evaluator.getName(), metrics.getMetric("train_epoch_" + evaluator.getName()).stream()
                                    .mapToDouble(x -> x.getValue().doubleValue()).toArray());
                            evaluatorMetrics.put("validate_epoch_" + evaluator.getName(), metrics.getMetric("validate_epoch_" + evaluator.getName()).stream()
                                    .mapToDouble(x -> x.getValue().doubleValue()).toArray());
                        });
            }
        }
    }
}
