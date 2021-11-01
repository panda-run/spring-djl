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
package org.pandarun.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pandarun.service.ComputerVisionService;
import org.springframework.stereotype.Service;


/**
 * Description: CV 接口实现层 <br>
 * Date: 2021/11/1
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class ComputerVisionServiceImpl implements ComputerVisionService {

    private final Classification classification;

    private final PneumoniaDetection pneumoniaDetection;

    @Override
    public String imageRecognition(String path) {
        String result = null;
        try {
            result = classification.predict(path);
        }catch (Exception e){
            log.error("图像识别推理异常：{}", e);
            return result;
        }
        return result;
    }

    @Override
    public String pneumoniaDetection(String path) {
        String result = null;
        try {
            result = pneumoniaDetection.predict(path);
        }catch (Exception e){
            log.error("肺炎检测推理异常：{}", e);
            return result;
        }
        return result;
    }
}
