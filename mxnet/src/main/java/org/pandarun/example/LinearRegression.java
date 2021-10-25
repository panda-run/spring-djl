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

import ai.djl.Device;
import ai.djl.engine.Engine;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.training.GradientCollector;
import ai.djl.training.dataset.ArrayDataset;
import ai.djl.training.dataset.Batch;

/**
 * ClassName: LinearRegression
 * Description: 实现线性回归
 * Author: James Zow
 * Date: 2020/10/2 23:11
 * Version: 1.0
 **/
public class LinearRegression {

    private NDArray X, y;

    public LinearRegression(NDArray X, NDArray y) {
        this.X = X;
        this.y = y;
    }

    public NDArray getX() {
        return X;
    }

    public NDArray getY() {
        return y;
    }


    /**
     * main 程序入口调试方法
     *
     * @param args
     */
    public static void main(String[] args) {
        float [] x = {4.2f,-1.5f};
      trainingModel(1000, 2,3.5f,x,0.03f,5);
    }

    /**
     * 生成数据集
     *
     * @param manager     Ndarray管理包
     * @param w           权重
     * @param b           偏差
     * @param numExamples 标本总数
     * @param numInputs   特征数
     * @return 公式：y = X w + b + noise
     */
    public static LinearRegression autoData(NDManager manager, NDArray w, float b, int numExamples, int numInputs) {
        NDArray X = manager.randomNormal(new Shape(numExamples, numInputs));
        NDArray y = X.dot(w).add(b);
        y = y.add(manager.randomNormal(0, 0.01f, y.getShape(), DataType.FLOAT32, Device.cpu()));
        return new LinearRegression(X, y);
    }

    /**
     * 打印生成的数据集
     */
    public static void printFeaturesAndLabels() {
        NDManager manager = NDManager.newBaseManager();
        // 真实权重
        NDArray trueW = manager.create(new float[]{2, -3.4f});
        // 真实偏差
        float trueB = 4.2f;
        // 这里的1000 是样本
        LinearRegression lr = autoData(manager, trueW, trueB, 1000, 2);
        NDArray features = lr.getX();
        NDArray labels = lr.getY();
        System.out.printf("features: [%f, %f]\n", features.get(0).getFloat(0), features.get(0).getFloat(1));
        System.out.println("label: " + labels.getFloat(0));
    }

    /**
     * 生成散点图
     */
    public static void generateScatterPlot() {
        // 获取样本数据
        NDManager manager = NDManager.newBaseManager();
        NDArray trueW = manager.create(new float[]{2, -3.4f});
        float trueB = 4.2f;
        LinearRegression lr = autoData(manager, trueW, trueB, 1000, 2);
        NDArray features = lr.getX();
        NDArray labels = lr.getY();
        // 生成图
        float[] X = features.get(new NDIndex(":, 1")).toFloatArray();
        float[] y = labels.toFloatArray();

    }

    /**
     * 读取数据集
     */
    public static void readDataList() {
        NDManager manager = NDManager.newBaseManager();
        NDArray trueW = manager.create(new float[]{2, -3.4f});
        float trueB = 4.2f;
        LinearRegression lr = autoData(manager, trueW, trueB, 1000, 2);
        NDArray features = lr.getX();
        NDArray labels = lr.getY();
        // 定义批量读取样本的一次数量
        int batchSize = 10;
        ArrayDataset dataset = new ArrayDataset.Builder()
                .setData(features) // 设置特征
                .optLabels(labels) // 设置标签
                .setSampling(batchSize, false) // 将 批量大小 和 随机采样 设置为false
                .build();
        try {
            for (Batch batch : dataset.getData(manager)) {
                // 调用head() 以获取第一个NDArray
                NDArray X = batch.getData().head();
                NDArray y = batch.getLabels().head();
                System.out.println(X);
                System.out.println(y);
                // 这里要手动关闭数据集循环
                batch.close();
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 定义模型
     *
     * @param X 矩阵向量
     * @param w 权重
     * @param b 偏差
     * @return
     */
    public static NDArray linreg(NDArray X, NDArray w, NDArray b) {
        return X.dot(w).add(b);
    }

    /**
     * 定义损失函数
     * 将真实值y转换为预测值的形状yHat
     *
     * @param yHat 预测值
     * @param y    真实值
     * @return
     */
    public static NDArray squaredLoss(NDArray yHat, NDArray y) {
        return (yHat.sub(y.reshape(yHat.getShape()))).mul
                ((yHat.sub(y.reshape(yHat.getShape())))).div(2);
    }

    /**
     * 定义优化算法
     *
     * @param params    参数
     * @param lr        学习率
     * @param batchSize 读取数量的批次大小
     */
    public static void sgd(NDList params, float lr, int batchSize) {
        for (int i = 0; i < params.size(); i++) {
            NDArray param = params.get(i);
            // Update param
            // param = param - param.gradient * lr / batchSize
            param.subi(param.getGradient().mul(lr).div(batchSize));
        }
    }

    /**
     * 训练模型
     */
    public static void trainingModel(int numExamples, int numInputs,
                                     float trueBParameter, float[] trueWParameter,
                                     float learning, int numEpochs) {
        NDManager manager = NDManager.newBaseManager();
        // 真实权重
        NDArray trueW = manager.create(trueWParameter);
        // 真实偏差
        float trueB = trueBParameter;
        LinearRegression lc = autoData(manager, trueW, trueB, numExamples, numInputs);
        NDArray features = lc.getX();
        NDArray labels = lc.getY();
        // 定义批量读取样本的一次数量
        int batchSize = 10;
        ArrayDataset dataset = new ArrayDataset.Builder()
                .setData(features) // 设置特征
                .optLabels(labels) // 设置标签
                .setSampling(batchSize, false) // 将 批量大小 和 随机采样 设置为false
                .build();

        // 学习率
        float lr = learning;
        // 迭代次数（循环）
        // 均值为0且标准偏差为的正态分布中采样随机数来初始化权重  0.01
        NDArray w = manager.randomNormal(0, 0.01f, new Shape(2, 1), DataType.FLOAT32, Device.cpu());
        // 偏差b为0
        NDArray b = manager.zeros(new Shape(1));
        NDList params = new NDList(w, b);
        // 梯度
        for (NDArray param : params) {
          //  param.attachGradient();
        }
        try {
            for (int epoch = 0; epoch < numEpochs; epoch++) {
                // 假设示例的数量可以除以批大小，所有训练数据集中的例子在一个epoch中使用一次迭代。用X给出了小批量示例的特征和y标签.
                for (Batch batch : dataset.getData(manager)) {
                    NDArray X = batch.getData().head();
                    NDArray y = batch.getLabels().head();

                    try (GradientCollector gc = Engine.getInstance().newGradientCollector()) {
                        // X和y的小批损失 调用损失函数
                        NDArray l = squaredLoss(linreg(X, params.get(0), params.get(1)), y);
                        gc.backward(l);  // 计算梯度在l对w和b
                    }
                    sgd(params, lr, batchSize);  // 使用参数的梯度更新参数 调用优化算法

                    batch.close();
                }
                NDArray trainL = squaredLoss(linreg(features, params.get(0), params.get(1)), labels);
                System.out.printf("迭代次数 %d, 损失值 %f\n", epoch + 1, trainL.mean().getFloat());
            }

            float[] c = trueW.sub(params.get(0).reshape(trueW.getShape())).toFloatArray();
            System.out.printf("权重估计误差: [%f %f]\n", c[0], c[1]);
            System.out.printf("真实偏差值: %f\n", trueB);
            System.out.printf("计算出来的偏差值: %f\n", params.get(1).getFloat());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
