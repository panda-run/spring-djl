package com.djl.examples.training;

import ai.djl.basicdataset.ImageFolder;
import ai.djl.basicdataset.PikachuDetection;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.repository.Repository;
import ai.djl.repository.SimpleRepository;
import ai.djl.training.dataset.Dataset;
import ai.djl.training.dataset.RandomAccessDataset;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Pipeline;
import ai.djl.util.ZipUtils;
import com.djl.examples.training.util.Arguments;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * ClassName: TraninFlowers
 * Description: 模型训练（花朵数据）
 * Author: James Zow
 * Date: 2020/11/8 15:34
 * Version: 1.0
 **/
public class TraninFlowers {

    private static final Logger logger = LoggerFactory.getLogger(TrainWithHpo.class);

    private TraninFlowers() {}

    public static void main(String[] args) throws Exception, ParseException {
        runExample(args);
    }


    private static void runExample(String[] args) {
        try {
            URL url = new URL("https://d2l-java-resources.s3.amazonaws.com/flower_dataset.zip");
            ZipUtils.unzip(url.openStream(), Paths.get("build/data"));

            int batchSize = 32;
            float[] mean = {0.485f, 0.456f, 0.406f};
            float[] std = {0.229f, 0.224f, 0.225f};
            int resize_w = 224;
            int resize_h = 224;


            ImageFolder trainDataset =
                    ImageFolder.builder()
                            .setRepository(Repository.newInstance("flower_train", "https://d2l-java-resources.s3.amazonaws.com/flower_dataset.zip"))
                            .optPipeline(
                                    new Pipeline()
                                            .add(new Resize(244, 244))
                                            .add(new ToTensor()))
                            .setSampling(batchSize, true)
                            .build();

            ImageFolder testDataset =
                    ImageFolder.builder()
                            .setRepository(Repository.newInstance("flower_test", "https://d2l-java-resources.s3.amazonaws.com/flower_dataset.zip"))
                            .optPipeline(
                                    new Pipeline()
                                            .add(new Resize(244, 244))
                                            .add(new ToTensor()))
                            .setSampling(batchSize, true)
                            .build();

            trainDataset.prepare(new ProgressBar());
            testDataset.prepare(new ProgressBar());

            System.out.println(trainDataset.getSynset());
        }catch (Exception e){

        }
    }
}
