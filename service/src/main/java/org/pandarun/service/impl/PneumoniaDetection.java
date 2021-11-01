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
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Pipeline;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * @author James Zow
 * @version 1.0
 * @description: TODO
 * @date 2021/11/1 13:46
 */
@Service
public class PneumoniaDetection {

    private static final List<String> CLASSES = Arrays.asList("正常", "肺炎");

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

        try (ZooModel<Image, Classifications> model = ModelZoo.loadModel(criteria);
             Predictor<Image, Classifications> predictor = model.newPredictor()) {
            Classifications result = predictor.predict(image);
            return String.valueOf(result);
        }
    }

    /**
     * create Translator for DJL
     * @return
     */
    public Translator<Image, Classifications> createImageTranslator(){
        return ImageClassificationTranslator.builder()
                .addTransform(a -> NDImageUtils.resize(a, 224).div(255.0f))
                .optSynset(CLASSES)
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
                //.optModelUrls("https://djl-ai.s3.amazonaws.com/resources/demo/pneumonia-detection-model/saved_model.zip")
                // 这里的model 可以去上面地址进行下载然后存放本地,再通过本地引入 这样速度快一点
                .optModelUrls("C:/Users/HP/.djl.ai/cache/repo/model/undefined/ai/djl/localmodelzoo/d7c9caf57bfa4ea0f0bc18c3d3d776fb/saved_model")
                .optTranslator(translator)
                .optProgress(new ProgressBar())
                .build();
    }
}
