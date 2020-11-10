package com.fastdjl.serviceImpl;

import com.fastdjl.common.inference.LinearRegressionInference;
import com.fastdjl.service.LinearRegression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * ClassName: LinearRegressionImpl
 * Description: TODD
 * Author: James Zow
 * Date: 2020/11/10 0010 21:10
 * Version:
 **/
@Service
public class LinearRegressionImpl implements LinearRegression {

    private Logger log = LoggerFactory.getLogger(LinearRegressionImpl.class);

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
    @Override
    public String LinearRegression(int numExamples, int numInputs, float trueBParameter, float[] trueWParameter, float learning, int numEpochs) {
        // 说明这些参数不一定填写全,但需要给他注明默认值，其实这里也就是调参过程，
        try {
            LinearRegressionInference.trainingModel(numExamples,numInputs,trueBParameter,trueWParameter,learning,numEpochs);
        }catch (Exception e){
            log.error(e.getMessage());
            return e.getMessage();
        }
        return null;
    }

    /**
     * 推理 线性代数接口 (简洁实现)
     * 说明：相比从0实现，该实现方法其实更侧重于对DJL已封装好的参数进行调用，比如loss等
     * https://zh.d2l.ai/chapter_deep-learning-basics/linear-regression-gluon.html
     * @param numExamples 样本数量
     * @param numInputs 特征数
     * @param trueBParameter 真实偏差参数
     * @param trueWParameter 真实权重参数
     * @param learning 学习率
     * @param numEpochs 迭代次数
     * @return
     */
    @Override
    public String ConciseLinearRegression(int numExamples, int numInputs, float trueBParameter, float[] trueWParameter, float learning, int numEpochs) {
        try {

        }catch (Exception e){
            log.error(e.getMessage());
            return e.getMessage();
        }
        return null;
    }
}
