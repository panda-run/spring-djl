package com.tensorflow.common.staticvoidmainTest;

import ai.djl.Application;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class PneumoniaDetection {

    private static final Logger logger = LoggerFactory.getLogger(PneumoniaDetection.class);

    private static final List<String> CLASSES = Arrays.asList("正常", "肺炎");

    public static void main(String[] args) throws IOException, TranslateException, ModelException {
        Path imagePath = Paths.get("tensorflow/src/test/resources/test.jpg");
        Image image = ImageFactory.getInstance().fromFile(imagePath);

        Translator<Image, Classifications> translator =
                ImageClassificationTranslator.builder()
                        .addTransform(a -> NDImageUtils.resize(a, 224).div(255.0f))
                        .optSynset(CLASSES)
                        .build();
        Criteria<Image, Classifications> criteria =
                Criteria.builder()
                        .optApplication(Application.CV.IMAGE_CLASSIFICATION)
                        .setTypes(Image.class, Classifications.class)
                        .optModelUrls("http://47.114.1.215:8083/model/tensorflow/pneumonia_model.zip")
                        // 这里的model 可以去上面地址进行下载然后存放本地,再通过本地引入 这样速度快一点
                        // .optModelUrls("tensorflow/model/saved_model/")
                        .optTranslator(translator)
                        .optProgress(new ProgressBar())
                        .build();

        try (ZooModel<Image, Classifications> model = ModelZoo.loadModel(criteria);
             Predictor<Image, Classifications> predictor = model.newPredictor()) {
            Classifications result = predictor.predict(image);
            logger.info("Diagnose: {}", result);
        }
    }
}
