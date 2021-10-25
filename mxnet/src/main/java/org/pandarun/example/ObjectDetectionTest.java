/*
 * Copyright 2021 Apache All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.pandarun.example;

import ai.djl.Application;
import ai.djl.ModelException;
import ai.djl.basicmodelzoo.cv.object_detection.ssd.SingleShotDetection;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.MultiBoxDetection;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.SingleShotDetectionTranslator;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.nn.Block;
import ai.djl.nn.LambdaBlock;
import ai.djl.nn.SequentialBlock;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ai.djl.translate.TranslatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ObjectDetectionTest {

    private static final Logger logger = LoggerFactory.getLogger(ObjectDetectionTest.class);


    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        int detection = ObjectDetectionTest.predict();
        logger.info("{}", detection);
    }

    static SingleShotDetectionTranslator.Builder builder = SingleShotDetectionTranslator.builder()
            .addTransform(new Resize(244,244))
            .addTransform(new ToTensor())
            .optSynset(Collections.singletonList("pikachu")) // there only one class in Pikachu SSD
            .optThreshold(0.55f);

    static SingleShotDetectionTranslator translator = new SingleShotDetectionTranslator(builder) {

        @Override
        public NDList processInput(TranslatorContext ctx, Image input) {
            NDList list = super.processInput(ctx, input);
            NDArray array = list.get(0).expandDims(0);  // the block expect a batch dim
            return new NDList(array);
        }

        @Override
        public DetectedObjects processOutput(TranslatorContext ctx, NDList list) {
            NDArray anchors = list.get(0);
            NDArray classPredictions = list.get(1).softmax(-1).transpose(0, 2, 1);
            NDArray boundingBoxPredictions = list.get(2);
            MultiBoxDetection multiBoxDetection =
                    MultiBoxDetection.builder().build();
            NDList detections =
                    multiBoxDetection.detection(
                            new NDList(
                                    classPredictions,
                                    boundingBoxPredictions,
                                    anchors));
            list = detections.singletonOrThrow().split(new long[]{1, 2}, 2);
            NDList output = new NDList(list.size());
            for (NDArray array : list) {
                output.add(array.squeeze(0)); // remove batch dim
            }
            return super.processOutput(ctx, output);
        }

        @Override
        public Batchifier getBatchifier() {
            return null; // disable auto batch
        }
    };

    public static int predict() throws IOException, ModelException, TranslateException {
        Block block = getSsdTrainBlock(); // use the same trained block to load the model
        Criteria<Image, DetectedObjects> criteria = Criteria.builder()
                .optApplication(Application.CV.OBJECT_DETECTION)
                .setTypes(Image.class, DetectedObjects.class)
                .optBlock(block) // set the block in criteria
                .optModelUrls("mxnet/model/pikachu-ssd/")
                .optModelName("pikachu-ssd") // specify the parameter file name
                .optTranslator(translator)
                .build();

        try (ZooModel<Image, DetectedObjects> model = ModelZoo.loadModel(criteria);
             Predictor<Image, DetectedObjects> predictor = model.newPredictor(translator)) {
            Image image = ImageFactory.getInstance().fromUrl("mxnet/src/test/resources/pikachu.jpg");
            DetectedObjects detectedObjects = predictor.predict(image);
            image.drawBoundingBoxes(detectedObjects);
            Path out = Paths.get("mxnet/model/pikachu-ssd/").resolve("pikachu_output.png");
            image.save(Files.newOutputStream(out), "png");
            // return number of pikachu detected
            return detectedObjects.getNumberOfObjects();
        }
    }

    public static Block getSsdTrainBlock() {
        int[] numFilters = {16, 32, 64};
        SequentialBlock baseBlock = new SequentialBlock();
        for (int numFilter : numFilters) {
            baseBlock.add(SingleShotDetection.getDownSamplingBlock(numFilter));
        }

        List<List<Float>> sizes = new ArrayList<>();
        List<List<Float>> ratios = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ratios.add(Arrays.asList(1f, 2f, 0.5f));
        }
        sizes.add(Arrays.asList(0.2f, 0.272f));
        sizes.add(Arrays.asList(0.37f, 0.447f));
        sizes.add(Arrays.asList(0.54f, 0.619f));
        sizes.add(Arrays.asList(0.71f, 0.79f));
        sizes.add(Arrays.asList(0.88f, 0.961f));

        return SingleShotDetection.builder()
                .setNumClasses(1)
                .setNumFeatures(3)
                .optGlobalPool(true)
                .setRatios(ratios)
                .setSizes(sizes)
                .setBaseNetwork(baseBlock)
                .build();
    }

    public static Block getSsdPredictBlock(Block ssdTrain) {
        // 添加预测过程
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
}
