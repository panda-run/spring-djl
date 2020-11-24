package com.controller;

import com.service.Classification;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: ClassificationController
 * Description: 分类控制层
 * Author: James Zow
 * Date: 2020/11/6 12:55
 * Version:
 **/
@RestController
@Api(tags = "分类推理接口")
@RequestMapping("/classification")
@CrossOrigin
public class ClassificationController {

    public Logger log = LoggerFactory.getLogger(ClassificationController.class);

    @Autowired
    private Classification classification;

    @RequestMapping(value = "/v1/simple", method = RequestMethod.POST)
    @ApiOperation(value = "图像识别分类" ,notes = "图像识别分类接口（支持本地图片文件地址和网络图片URL地址）")
    @ApiImplicitParam(name = "imagePath", value = "图片地址", required = true)
    public String animalClassification(String imagePath) {
        log.info("请求分类接口Start...");
        log.info("请求图片地址:" + imagePath);
        String result = classification.ImageClassification(imagePath);
        if(result != null){
            return result;
        } else {
            return "请求接口没有数据";
        }
    }
}
