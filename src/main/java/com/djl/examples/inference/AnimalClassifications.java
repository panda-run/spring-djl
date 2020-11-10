package com.djl.examples.inference;

import ai.djl.*;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.CenterCrop;
import ai.djl.modality.cv.transform.Normalize;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.*;
import ai.djl.util.Utils;
import javafx.stage.Modality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * 动物检测
 */
public class AnimalClassifications {

    private static final Logger logger = LoggerFactory.getLogger(AnimalClassifications.class);

    public AnimalClassifications() {}

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
         Classifications classifications = AnimalClassifications.predict();
         logger.info("{}", classifications);
    }

    public static Classifications  predict() throws IOException, ModelException, TranslateException {
        Path imagePath = Paths.get("src/test/resources/lion.jpg");
        Image image = ImageFactory.getInstance().fromFile(imagePath);

        Translator<Image, Classifications> translator = new Translator<Image, Classifications>() {

            private List<String> classes ;


            @Override
            public Batchifier getBatchifier() {
                return Batchifier.STACK;
            }

            @Override
            public Classifications processOutput(TranslatorContext translatorContext, NDList ndList) throws Exception {
                NDArray probabilities = ndList.singletonOrThrow().softmax(0);
                return new Classifications(classes, probabilities);
            }

            @Override
            public NDList processInput(TranslatorContext translatorContext, Image image) throws Exception {

                NDArray array = image.toNDArray(translatorContext.getNDManager(), Image.Flag.COLOR);
                array = NDImageUtils.resize(array, 256, 256);
                array = NDImageUtils.centerCrop(array, 224, 224);
                array = array.reshape(new Shape(3, 224, 224));
                array = NDImageUtils.normalize(array, new float[] {0.485f, 0.456f, 0.406f}, new float[] {0.229f, 0.224f, 0.225f});
                return new NDList(array);
            }

            @Override
            public void prepare(NDManager manager, Model model) throws IOException {
                classes =  model.getArtifact("synset.txt", Utils::readLines);;
            }
        };

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
                return animalObjects;
            }
        }
    }

    public static NDArray softmax(NDArray X){
        NDArray Xexp = X.exp();
        NDArray partiton = Xexp.sum(new int[]{1}, true);
        // div 返回这里用到矩阵广播机制
        return Xexp.div(partiton);
    }

}
