package com.common.example;

import ai.djl.Model;
import ai.djl.basicdataset.FashionMnist;
import ai.djl.metric.Metrics;
import ai.djl.ndarray.*;
import ai.djl.ndarray.types.*;
import ai.djl.ndarray.index.*;
import ai.djl.nn.Blocks;
import ai.djl.nn.SequentialBlock;
import ai.djl.nn.core.Linear;
import ai.djl.training.DefaultTrainingConfig;
import ai.djl.training.EasyTrain;
import ai.djl.training.Trainer;
import ai.djl.training.dataset.*;
import ai.djl.training.evaluator.Accuracy;
import ai.djl.training.listener.TrainingListener;
import ai.djl.training.loss.Loss;
import ai.djl.training.optimizer.Optimizer;
import ai.djl.training.tracker.Tracker;
import ai.djl.translate.TranslateException;

import java.io.IOException;

/**
 * ClassName: SoftmaxRegression
 * Description: softmax 回归基本实现
 * Author: James Zow
 * Date: 2020/10/24 11:51
 * Version: 1.0
 **/
public class SoftmaxRegression {

    /**
     * main method 入口
     * @param args
     */
    public static void main(String [] args){
        runExampleBySoftMax();
    }

    /**
     * softmax创建模型训练数据集
     */
    public static void runExampleBySoftMax(){
        // 迭代次数
        int numEpochs = 5;
        // 读取数据批次
        int batchSize = 216;
        boolean randomShuffle = true;
        // 获取FashionMnist数据
        RandomAccessDataset  trainingSet  =   getDataset(Dataset.Usage.TRAIN, batchSize, randomShuffle);
        RandomAccessDataset  validationSet  = getDataset(Dataset.Usage.TEST, batchSize, randomShuffle);
        // 模型构造
        Model model = Model.newInstance("softmax-regression");
        // 网络创建
        SequentialBlock net = new SequentialBlock();
        net.add(Blocks.batchFlattenBlock(28 * 28));
        net.add(Linear.builder().setUnits(10).build());
        model.setBlock(net);
        // 损失函数softmax
        Loss loss = Loss.softmaxCrossEntropyLoss();
        Tracker lrt = Tracker.fixed(0.1f);
        Optimizer sgd = Optimizer.sgd().setLearningRateTracker(lrt).build();
        DefaultTrainingConfig config = new DefaultTrainingConfig(loss)
                .optOptimizer(sgd)
                .addEvaluator(new Accuracy())
                .addTrainingListeners(TrainingListener.Defaults.logging());
        // 训练模型 28X28 image
        Trainer trainer = model.newTrainer(config);
        trainer.initialize(new Shape(1, 28 * 28));
        Metrics metrics = new Metrics();
        trainer.setMetrics(metrics);
        try {
            EasyTrain.fit(trainer, numEpochs, trainingSet, validationSet);
            trainer.getTrainingResult();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (TranslateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据集
     *
     * @param batchSize     批次大小
     * @param randomShuffle 是否分别随机地对数据进行随机排序
     * @return
     */
    public static ArrayDataset getDataset(Dataset.Usage usage, int batchSize, boolean randomShuffle) {
        try {
            FashionMnist fashionMnist = FashionMnist.builder().optUsage(usage)
                    .setSampling(batchSize, randomShuffle)
                    .build();
            fashionMnist.prepare();
            return fashionMnist;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * softmax 运算
     * @param X 向量
     * @return
     */
    public static NDArray softmax(NDArray X){
        NDArray Xexp = X.exp();
        NDArray partiton = Xexp.sum(new int[]{1}, true);
        // div 返回这里用到矩阵广播机制
        return Xexp.div(partiton);
    }

    /**
     * 交叉熵损失函数
     * @param yHat
     * @param y
     * @return
     */
    public static NDArray crossEntropylossfunction(NDArray yHat, NDArray y){
        return yHat.get(new NDIndex(":, {}", y.toType(DataType.FLOAT32, false))).log().neg();
    }


}
