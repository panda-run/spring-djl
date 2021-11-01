package org.pandarun.service;

/**
 * @author James Zow
 * @version 1.0
 * @description: TODO
 * @date 2021/11/1 14:00
 */
public interface ComputerVisionService {

    /**
     * mxnet_resnet18 模型图像分类接口
     * @param path
     * @return
     */
    String imageRecognition(String path);

    /**
     * 肺炎检测图像接口
     * @param path
     * @return
     */
    String pneumoniaDetection(String path);
}
