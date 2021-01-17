package com.common.example;

import ai.djl.Device;
import ai.djl.Model;
import ai.djl.metric.Metrics;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.nn.Block;
import ai.djl.nn.ParameterList;
import ai.djl.nn.SequentialBlock;
import ai.djl.nn.core.Linear;
import ai.djl.training.DefaultTrainingConfig;
import ai.djl.training.EasyTrain;
import ai.djl.training.Trainer;
import ai.djl.training.dataset.ArrayDataset;
import ai.djl.training.dataset.Batch;
import ai.djl.training.listener.TrainingListener;
import ai.djl.training.loss.Loss;
import ai.djl.training.optimizer.Optimizer;
import ai.djl.training.tracker.Tracker;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * ClassName: ConciseLinearRegression
 * Description: 简洁实现线性回归
 * Author: James Zow
 * Date: 2020/10/3 0003 17:57
 * Version: 1.0
 **/
public class ConciseLinearRegression {

    private NDArray X, y;

    public ConciseLinearRegression(NDArray X, NDArray y) {
        this.X = X;
        this.y = y;
    }

    public NDArray getX() {
        return X;
    }

    public NDArray getY() {
        return y;
    }


    public static void main(String[] args) {
        trainingModel();
    }

    /**
     * 生成数据集
     *
     * @param manager     Ndarray管理包
     * @param w           权重
     * @param b           偏差
     * @param numExamples 标本总数
     * @return 公式：y = X w + b + noise
     */
    public static LinearRegression autoData(NDManager manager, NDArray w, float b, int numExamples) {
        NDArray X = manager.randomNormal(new Shape(numExamples, w.size()));
        NDArray y = X.dot(w).add(b);
        y = y.add(manager.randomNormal(0, 0.01f, y.getShape(), DataType.FLOAT32, Device.defaultDevice()));
        return new LinearRegression(X, y);
    }

    /**
     * 读取数据集
     *
     * @param features
     * @param labels
     * @param batchSize 批量读取数据大小
     * @param shuffle   表示是否要对数据进行随机采样
     * @return
     */
    public static ArrayDataset loadArray(NDArray features, NDArray labels, int batchSize, boolean shuffle) {
        return new ArrayDataset.Builder()
                .setData(features)
                .optLabels(labels)
                .setSampling(batchSize, shuffle)
                .build();
    }

    public static void trainingModel() {
        NDManager ndManager = NDManager.newBaseManager();
        NDArray trueW = ndManager.create(new float[]{2, -3.4f});
        float trueB = 4.2f;
        LinearRegression dataPoint = ConciseLinearRegression.autoData(ndManager, trueW, trueB, 1000);
        NDArray feautres = dataPoint.getX();
        NDArray labels = dataPoint.getY();
        // 10批次
        int batchSize = 10;
        ArrayDataset arrayDataset = loadArray(feautres, labels, batchSize, false);
        try {
            for (Batch batch : arrayDataset.getData(ndManager)) {
                NDArray X = batch.getData().head();
                NDArray y = batch.getLabels().head();
                System.out.println(X);
                System.out.println(y);
                // 这里一定要收到关闭
                batch.close();
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 定义模型（这里就跟从0开始线性代数的class不一样了）
        Model model = Model.newInstance("lin-reg");
        SequentialBlock net = new SequentialBlock();
        Linear linearBlock = Linear.builder().optBias(true).setUnits(1).build();
        net.add(linearBlock);
        model.setBlock(net);
        // 定义损失函数
        Loss loss = Loss.l2Loss();
        // 定义优化算法
        Tracker ltr = Tracker.fixed(0.03f);
        Optimizer optimizer = Optimizer.sgd().setLearningRateTracker(ltr).build();
        // 实例化配置
        DefaultTrainingConfig config = new DefaultTrainingConfig(loss).optOptimizer(optimizer)
                .addTrainingListeners(TrainingListener.Defaults.logging()); // logging
        // 进行训练
        Trainer trainer = model.newTrainer(config);
        // 初始化模型参数
        trainer.initialize(new Shape(batchSize, 2));
        // DJL 指标
        Metrics metrics = new Metrics();
        trainer.setMetrics(metrics);
        // 开始训练
        int numEpochs = 3;
        try {
            for (int epoch = 1; epoch <= numEpochs; epoch++) {
                System.err.println("迭代次数：" + epoch);
                // 迭代数据集
                for (Batch batch : trainer.iterateDataset(arrayDataset)) {
                    // 更新损失函数和评估器
                    EasyTrain.trainBatch(trainer, batch);
                    // 更新参数
                    trainer.step();
                    // 手动关闭
                    batch.close();
                }
                // 在epoch结束时重置培训和验证评估器
                trainer.notifyListeners(listener -> listener.onEpoch(trainer));
            }
            // 校验模型训练完的数据和真实权重偏差大小比较
            Block layer = model.getBlock();
            ParameterList parames = layer.getParameters();
            // 这里的valueAt(0)表示权重valueAt(1)表示偏差
            NDArray wParam = parames.valueAt(0).getArray();
            NDArray bParam = parames.valueAt(1).getArray();
            System.err.println("真实权重值：" + trueW);
            System.err.println("计算出来的权重值：" + wParam);
            System.err.println("真实偏差值：" + trueB);
            System.err.println("计算出来的偏差值：" + bParam);
            // 最后保存模型
            Path modeDir = Paths.get("build/output");
            Files.createDirectories(modeDir);
            model.setProperty("Epoch", Integer.toString(numEpochs)); // 保存元数据
            model.save(modeDir, "lin-reg");
            System.err.println(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
