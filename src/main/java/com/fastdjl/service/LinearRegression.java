package com.fastdjl.service;

/**
 * ClassName: LinearRegression
 * Description: 线性回归接口
 * Author: James Zow
 * Date: 2020/11/10 0010 20:58
 * Version:
 **/
public interface LinearRegression {

    /**
     * 推理 线性代数接口 (从0开始实现)
     * @param numExamples 样本数量
     * @param numInputs 特征数
     * @param trueBParameter 真实偏差参数
     * @param trueWParameter 真实权重参数
     * @param learning 学习率
     * @param numEpochs 迭代次数
     * @return
     */
    public String LinearRegression(int numExamples, int numInputs,
                                   float trueBParameter, float[] trueWParameter,
                                   float learning, int numEpochs);


    /**
     * 推理 线性代数接口 (简洁实现)
     * 说明：相比从0实现，该实现方法其实更侧重于对DJL已封装好的参数进行调用，比如loss。
     * @param numExamples 样本数量
     * @param numInputs 特征数
     * @param trueBParameter 真实偏差参数
     * @param trueWParameter 真实权重参数
     * @param learning 学习率
     * @param numEpochs 迭代次数
     * @return
     */
    public String ConciseLinearRegression(int numExamples, int numInputs,
                                          float trueBParameter, float[] trueWParameter,
                                          float learning, int numEpochs);
}
