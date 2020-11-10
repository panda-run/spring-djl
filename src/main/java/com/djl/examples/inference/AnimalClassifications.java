package com.djl.examples.inference;

import ai.djl.*;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;



/**
 * 分类
 */
public class AnimalClassifications {

    private static final Logger logger = LoggerFactory.getLogger(AnimalClassifications.class);

    public AnimalClassifications() {}

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
         Classifications classifications = AnimalClassifications.predict();
         logger.info("{}", classifications);
    }

    public static Classifications  predict() throws IOException, ModelException, TranslateException {
        // Path imagePath = Paths.get("src/test/resources/lion.jpg");
        Path imagePath = Paths.get("src/test/resources/banana.jpg");
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

            @Override
            public void prepare(NDManager manager, Model model) throws IOException {
                // 获取模型里面的classname
                classes =  model.getArtifact("synset.txt", Utils::readLines);;
            }
        };

        Criteria<Image, Classifications> criteria =
                Criteria.builder()
                        .optApplication(Application.CV.IMAGE_CLASSIFICATION)
                        .setTypes(Image.class, Classifications.class)
                        // 自定义的translator
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

}
