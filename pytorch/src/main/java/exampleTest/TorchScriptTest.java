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
package exampleTest;

import ai.djl.Application;
import ai.djl.MalformedModelException;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.testng.Assert;

public class TorchScriptTest {

    public static void main(String[] args){
        try {
          //  testDictInput();
          //  testProfiler();
            testMkldnn();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void testDictInput() throws ModelException, IOException, TranslateException {
        try (NDManager manager = NDManager.newBaseManager()) {
            Criteria<NDList, NDList> criteria =
                    Criteria.builder()
                            .setTypes(NDList.class, NDList.class)
                            .optModelUrls("https://resources.djl.ai/test-models/dict_input.zip")
                            .optProgress(new ProgressBar())
                            .build();

            Path modelFile;
            try (ZooModel<NDList, NDList> model = ModelZoo.loadModel(criteria);
                 Predictor<NDList, NDList> predictor = model.newPredictor()) {
                NDArray array = manager.ones(new Shape(2, 2));
                array.setName("input1.input");
                NDList output = predictor.predict(new NDList(array));
                Assert.assertEquals(output.singletonOrThrow(), array);

                modelFile = model.getModelPath().resolve(model.getName() + ".pt");
            }

            criteria =
                    Criteria.builder()
                            .setTypes(NDList.class, NDList.class)
                            .optProgress(new ProgressBar())
                            .build();

            try (ZooModel<NDList, NDList> model = ModelZoo.loadModel(criteria)) {
                Assert.assertEquals(model.getName(), "dict_input");
            }
        }
    }

    public static void testProfiler() throws MalformedModelException, ModelNotFoundException, IOException {
        try (NDManager manager = NDManager.newBaseManager()) {
            ImageClassificationTranslator translator =
                    ImageClassificationTranslator.builder().addTransform(new ToTensor()).build();

            Criteria<Image, Classifications> criteria =
                    Criteria.builder()
                            .setTypes(Image.class, Classifications.class)
                            .optApplication(Application.CV.IMAGE_CLASSIFICATION)
                            .optFilter("layers", "18")
                            .optTranslator(translator)
                            .optProgress(new ProgressBar())
                            .build();
            String outputFile = "build/profile.json";
            try (ZooModel<Image, Classifications> model = ModelZoo.loadModel(criteria)) {
                try (Predictor<Image, Classifications> predictor = model.newPredictor()) {
                    Image image =
                            ImageFactory.getInstance()
                                    .fromNDArray(
                                            manager.zeros(new Shape(3, 224, 224), DataType.UINT8));
            //        JniUtils.startProfile(false, true, true);
                    predictor.predict(image);

            //        JniUtils.stopProfile(outputFile);
                } catch (TranslateException e) {
                    e.printStackTrace();
                }
            }
            Assert.assertTrue(Files.exists(Paths.get(outputFile)), "The profiler file not found!");
        }
    }

    public static void testMkldnn() {
        System.setProperty("ai.djl.pytorch.use_mkldnn", "true");
        try (NDManager manager = NDManager.newBaseManager()) {
            NDArray[] arrays = {
                    manager.create(new float[] {0f, 1f, 3f, 4f}, new Shape(2, 2)),
                    manager.zeros(new Shape(2, 2)),
                    manager.ones(new Shape(2, 2)),
                    manager.ones(new Shape(2, 2)).duplicate(),
                    manager.full(new Shape(2, 2), 1f),
                    manager.zeros(new Shape(2, 2)).zerosLike(),
                    manager.zeros(new Shape(2, 2)).onesLike(),
                    manager.eye(2),
                    manager.randomNormal(new Shape(2, 2)),
                    manager.randomUniform(0, 1, new Shape(2, 2))
            };
            // run sanity check, if two arrays are on different layout, it will throw exception
            Arrays.stream(arrays).reduce(NDArray::add);
            Arrays.stream(arrays).forEach(NDArray::toString);
            arrays = new NDArray[] {manager.arange(4f), manager.linspace(0, 1, 4)};
            // run sanity check, if two arrays are on different layout, it will throw exception
            Arrays.stream(arrays).reduce(NDArray::add);
            Arrays.stream(arrays).forEach(NDArray::toString);
        } finally {
            System.setProperty("ai.djl.pytorch.use_mkldnn", "false");
        }
    }
}
