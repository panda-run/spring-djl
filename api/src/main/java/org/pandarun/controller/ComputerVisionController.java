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
package org.pandarun.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pandarun.service.ComputerVisionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: CV接口层 <br>
 * Date: 2020/11/1
 **/
@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = "计算机视觉（CV）接口")
@RequestMapping("cv")
public class ComputerVisionController {

    private final ComputerVisionService computerVisionService;

    @PostMapping(value = "mxnetResnet18")
    @ApiOperation(value = "[mxnet引擎]-图像识别接口")
    public String animalClassification(String imagePath) {
        log.info("请求分类接口Start...");
        log.info("请求图片地址:" + imagePath);
        return computerVisionService.imageRecognition(imagePath);
    }

    @PostMapping(value = "pneumoniaDetection")
    @ApiOperation(value = "[tensorflow引擎]-肺炎检测接口")
    public String pneumoniaDetection(String imagePath) {
        log.info("请求肺炎检测接口Start...");
        log.info("请求图片地址:" + imagePath);
        return computerVisionService.pneumoniaDetection(imagePath);
    }
}
