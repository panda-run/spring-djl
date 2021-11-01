/*
 * Copyright 2021 Panda Run Organization All Rights Reserved.
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
package org.pandarun.service;

/**
 * Description: CV 接口定义层 <br>
 * Date: 2021/11/1
 **/
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
