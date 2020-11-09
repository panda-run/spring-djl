package com.djl.examples.inference;

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

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * 动物检测
 */
public class AnimalRecognition {

    private static final Logger logger = LoggerFactory.getLogger(AnimalRecognition.class);

    public AnimalRecognition() {}

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
         Classifications detection = AnimalRecognition.predict();
         logger.info("{}", detection);
    }

    public static Classifications  predict() throws IOException, ModelException, TranslateException {
        Path imagePath = Paths.get("src/test/resources/balana.jpg");
        Image image = ImageFactory.getInstance().fromFile(imagePath);

        String modelUrl = "https://alpha-djl-demos.s3.amazonaws.com/model/djl-blockrunner/mxnet_resnet18.zip?model_name=resnet18_v1";

        Translator<Image, Classifications> translator =
                ImageClassificationTranslator.builder()
                        .setPipeline(new Pipeline().add(new Resize(224, 224)).add(new ToTensor()))
                        .optApplySoftmax(true)
                        .build();

        Criteria<Image, Classifications> criteria =
                Criteria.builder()
                        .optApplication(Application.CV.IMAGE_CLASSIFICATION)
                        .setTypes(Image.class, Classifications.class)
                        .optTranslator(translator)
                        .optModelUrls("build/model/mxnet_resnet18")
                        .optModelName("resnet18_v1")
                        .build();

        try (ZooModel<Image, Classifications> model = ModelZoo.loadModel(criteria)) {
            try (Predictor<Image, Classifications> predictor = model.newPredictor()) {
                Classifications animalObjects = predictor.predict(image);
              //  saveBoundingBoxImage(image, ImageClassification);
                return animalObjects;
            }
        }
    }

    private static void saveBoundingBoxImage(Image img, ImageClassification detection)
            throws IOException {
        Path outputDir = Paths.get("build/output");
        Files.createDirectories(outputDir);

        // 使用alpha通道复制图像，因为原始图像是jpg
        Image newImage = img.duplicate(Image.Type.TYPE_INT_ARGB);
      //  newImage.drawBoundingBoxes(detection);

        Path imagePath = outputDir.resolve("cat.jpg");
        // OpenJDK不能用alpha通道保存jpg
        newImage.save(Files.newOutputStream(imagePath), "png");
        logger.info("检测到的对象图像已保存在: {}", imagePath);
    }


    public static URL conversion(String path){
        File file = new File(path);
        if (file.exists()) {
            System.out.println("PATH: " + file.getPath());

            // Convert file to URI
            URI uri = file.toURI();
            System.out.println("URI: " + uri.toString());

            // Convert URI to URL
            URL url = null;
            try {
                url = uri.toURL();
                System.out.println("URL: " + url.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return url;
        }
        return null;
    }

}
