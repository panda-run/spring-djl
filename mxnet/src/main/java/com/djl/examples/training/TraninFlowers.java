package com.djl.examples.training;

import ai.djl.Model;
import ai.djl.basicdataset.ImageFolder;
import ai.djl.basicdataset.Mnist;
import ai.djl.basicmodelzoo.basic.Mlp;
import ai.djl.basicmodelzoo.cv.classification.ResNetV1;
import ai.djl.metric.Metrics;
import ai.djl.modality.cv.MultiBoxDetection;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.Shape;
import ai.djl.nn.Block;
import ai.djl.nn.LambdaBlock;
import ai.djl.nn.SequentialBlock;
import ai.djl.repository.Repository;
import ai.djl.training.*;
import ai.djl.training.dataset.RandomAccessDataset;
import ai.djl.training.evaluator.Accuracy;
import ai.djl.training.listener.TrainingListener;
import ai.djl.training.loss.Loss;
import ai.djl.translate.Pipeline;
import ai.djl.translate.TranslateException;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * ClassName: TraninFlowers
 * Description: 模型训练（花朵数据）
 * Author: James Zow
 * Date: 2020/11/8 15:34
 * Version: 1.0
 **/
public class TraninFlowers {

    private TraninFlowers() {}

    private static final Logger logger = LoggerFactory.getLogger(TrainWithHpo.class);


    public static void main(String[] args) throws Exception, ParseException {
        TraninFlowers.runExample(args);
    }


    private static TrainingResult runExample(String[] args)  {
        try {
            int resize_w = 224;
            int resize_h = 224;
            ImageFolder traindataset = init_trainDataset("flower-images-train");
            ImageFolder testdataset = init_testDataset("flower-images-test");
            ImageFolder validdataset = init_validDataset("flower-images-valid");
            RandomAccessDataset[] datasets = traindataset.randomSplit(8, 2);
            Loss loss = Loss.softmaxCrossEntropyLoss();
            TrainingConfig config = setupTrainingConfig(loss);
            try (Model model = Model.newInstance("flower-ssd")) {
                // 构造神经网络
                model.setBlock(getSsdTrainBlock());
                try (Trainer trainer = model.newTrainer(config)) {
                    trainer.setMetrics(new Metrics());
                    Shape inputShape = new Shape(1, 3, resize_h, resize_w);
                    trainer.initialize(inputShape);
                    EasyTrain.fit(trainer, 3, datasets[0], datasets[1]);
                    // save labels into model directory
                    return trainer.getTrainingResult();
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Block getSsdTrainBlock() {

        // Block is a composable unit that forms a neural network; combine them like Lego blocks
        // to form a complex network
        Block resNet50 =
                ResNetV1.builder() // construct the network
                        .setImageShape(new Shape(3, 224, 224))
                        .setNumLayers(50)
                        .setOutSize(4)
                        .build();

        // set the neural network to the model
        return resNet50;
    }

    public static Block getSsdPredictBlock(Block ssdTrain) {
        // add prediction process
        SequentialBlock ssdPredict = new SequentialBlock();
        ssdPredict.add(ssdTrain);
        ssdPredict.add(
                new LambdaBlock(
                        output -> {
                            NDArray anchors = output.get(0);
                            NDArray classPredictions = output.get(1).softmax(-1).transpose(0, 2, 1);
                            NDArray boundingBoxPredictions = output.get(2);
                            MultiBoxDetection multiBoxDetection =
                                    MultiBoxDetection.builder().build();
                            NDList detections =
                                    multiBoxDetection.detection(
                                            new NDList(
                                                    classPredictions,
                                                    boundingBoxPredictions,
                                                    anchors));
                            return detections.singletonOrThrow().split(new long[] {1, 2}, 2);
                        }));
        return ssdPredict;
    }

    private static TrainingConfig setupTrainingConfig(Loss loss) {
        return new DefaultTrainingConfig(loss)
                .addEvaluator(new Accuracy())
                .addTrainingListeners(TrainingListener.Defaults.logging());
    }

    private static ImageFolder init_trainDataset(String dataset) throws IOException, TranslateException {
        int batchSize = 32;
        ImageFolder trainDataset =
                ImageFolder.builder()
                        .setRepository(Repository.newInstance("flower_train", "mxent/data/flower_dataset/train/"))
                        .optPipeline(
                                new Pipeline()
                                        .add(new Resize(244, 244))
                                        .add(new ToTensor()))
                        .setSampling(batchSize, true)
                        .build();

        trainDataset.prepare();
        return trainDataset;
    }

    private static ImageFolder init_testDataset(String dataset) throws IOException, TranslateException {
        int batchSize = 32;
        ImageFolder testDataset =
                ImageFolder.builder()
                        .setRepository(Repository.newInstance("flower_test", "mxent/data/flower_dataset/test/"))
                        .optPipeline(
                                new Pipeline()
                                        .add(new Resize(244, 244))
                                        .add(new ToTensor()))
                        .setSampling(batchSize, true)
                        .build();
        testDataset.prepare();
        return testDataset;
    }

    private static ImageFolder init_validDataset(String dataset) throws IOException, TranslateException {
        int batchSize = 32;
        ImageFolder validDataset =
                ImageFolder.builder()
                        .setRepository(Repository.newInstance("flower_valid", "mxent/data/flower_dataset/valid/"))
                        .optPipeline(
                                new Pipeline()
                                        .add(new Resize(244, 244))
                                        .add(new ToTensor()))
                        .setSampling(batchSize, true)
                        .build();
        validDataset.prepare();
        return validDataset;
    }

}
