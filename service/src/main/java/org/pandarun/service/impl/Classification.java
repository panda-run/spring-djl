/*
 * Copyright 2021 Panda Run Organization All Rights Reserved.
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
package org.pandarun.service.impl;

import ai.djl.Application;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.Pipeline;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Description: 分类实现类
 * Date: 2020/11/6
 **/
@Service
public class Classification {

    Logger log = LoggerFactory.getLogger(Classification.class);

    /**
     * 推理实现
     * @param imagePath
     * @return
     * @throws IOException
     * @throws ModelException
     * @throws TranslateException
     */
    public String predict(String imagePath)throws IOException, ModelException, TranslateException {
        Image image = null;
        // url/path 情况
        if (imagePath.contains("http") && imagePath.endsWith(".jpg") || imagePath.endsWith(".png")
                || imagePath.endsWith(".jpeg") || imagePath.endsWith(".gif")){
            image = ImageFactory.getInstance().fromUrl(imagePath);
        }else {
            Path path = Paths.get(imagePath);
            image = ImageFactory.getInstance().fromFile(path);
        }
        Criteria<Image, Classifications> criteria = createCriteria(createImageTranslator());

        try (ZooModel<Image, Classifications> model = ModelZoo.loadModel(criteria)) {
            try (Predictor<Image, Classifications> predictor = model.newPredictor()) {
                Classifications animalObjects = predictor.predict(image);
                return String.valueOf(animalObjects);
            }
        }
    }

    /**
     * create Translator for DJL
     * @return
     */
    public Translator<Image, Classifications> createImageTranslator(){
        return ImageClassificationTranslator.builder()
                .setPipeline(new Pipeline().add(new Resize(224, 224)).add(new ToTensor()))
                .optApplySoftmax(true)
                .build();
    }

    /**
     * create Criteria for DJL
     * @param translator
     * @return createCriteria
     */
    public Criteria<Image, Classifications> createCriteria(Translator translator){
        return Criteria.builder()
                .optApplication(Application.CV.IMAGE_CLASSIFICATION)
                .setTypes(Image.class, Classifications.class)
                .optTranslator(translator)
                // String modelUrl = "https://alpha-djl-demos.s3.amazonaws.com/model/djl-blockrunner/mxnet_resnet18.zip?model_name=resnet18_v1";
                .optModelUrls("mxnet/model/mxnet_resnet18")
                .optModelName("resnet18_v1")
                .build();
    }
}
