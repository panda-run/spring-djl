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
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


/**
 * ClassName: ImplTotenserAndSoftmaxClassifications
 * Description: 多线程分类推理
 * Author: James Zow
 * Date: 2020/10/21 16:55
 * Version:
 **/
public class ImplTotenserAndSoftmaxClassifications implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ImplTotenserAndSoftmaxClassifications.class);

    public ImplTotenserAndSoftmaxClassifications() {}

    public static void main(String[] args) {
        ImplTotenserAndSoftmaxClassifications a = new ImplTotenserAndSoftmaxClassifications();
        new Thread(a,"线程A 推理").start();
        new Thread(a,"线程B 推理").start();
        new Thread(a,"线程C 推理").start();
    }

    public static Classifications  predict() throws IOException, ModelException, TranslateException {
        // Path imagePath = Paths.get("src/test/resources/lion.jpg");
        Path imagePath = Paths.get("mxnet/src/test/resources/banana.jpg");
        Image image = ImageFactory.getInstance().fromFile(imagePath);

        Translator<Image, Classifications> translator = new Translator<Image, Classifications>() {

            // 分类对应的class
            private List<String> classes ;

            @Override
            public Batchifier getBatchifier() {
                return Batchifier.STACK;
            }

            @Override
            public Classifications processOutput(TranslatorContext translatorContext, NDList ndList) throws Exception {
                NDArray resultArray = ndList.singletonOrThrow();
                NDArray xexp = resultArray.exp();
                NDArray sum = xexp.sum(new int[]{0}, true);
                // div 是做 广播机制的 矩阵
                NDArray probabilities = xexp.div(sum);
                return new Classifications(classes, probabilities);
            }

            @Override
            public NDList processInput(TranslatorContext translatorContext, Image image) throws Exception {
                // 图片转换NDArray 并且修改尺寸等参数
                NDArray array = image.toNDArray(translatorContext.getNDManager(), Image.Flag.COLOR);
                array = NDImageUtils.resize(array, 224, 224);
                array = NDImageUtils.centerCrop(array, 224, 224);
                // totenser 操作
                array = array.expandDims(0);
                array = array.div(255.0D).transpose(new int[]{0, 3, 1, 2});
                array = array.reshape(new Shape(3, 224, 224)).expandDims(0);
                array = array.squeeze(0);
                // 由于输入的是unit8 类型 这里做一下转换
                array = array.toType(DataType.FLOAT32, true);
                // 归一化处理
                array = NDImageUtils.normalize(array, new float[] {0.435f, 0.456f, 0.406f}, new float[] {0.229f, 0.224f, 0.225f});
                return new NDList(array);
            }

        };

        Criteria<Image, Classifications> criteria =
                Criteria.builder()
                        .optApplication(Application.CV.IMAGE_CLASSIFICATION)
                        .setTypes(Image.class, Classifications.class)
                        // 自定义的translator
                        .optTranslator(translator)
                        .optModelUrls("mxnet/model/mxnet_resnet18")
                        .optModelName("resnet18_v1")
                        .build();

        try (ZooModel<Image, Classifications> model = ModelZoo.loadModel(criteria)) {
            try (Predictor<Image, Classifications> predictor = model.newPredictor()) {
                Classifications animalObjects = predictor.predict(image);
                return animalObjects;
            }
        }
    }

    @Override
    public void run()  {
        try {
            int count = 10;
            for (int i = 0; i < count; i++) {
                System.out.println(Thread.currentThread().getName() + "  运行count= " + count--);
                Classifications classifications = ImplTotenserAndSoftmaxClassifications.predict();
                logger.info("{}", classifications);
                try {
                    Thread.sleep((int) Math.random() * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
