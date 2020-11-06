package com.fastdjl.controller;

import com.fastdjl.common.conversion.FastJsonUtils;
import com.fastdjl.service.Classification;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "图像识别分类" ,notes = "图像识别分类接口")
  //  @ApiImplicitParam(name = "imagePath", value = "图片地址（支持网络url图片和本地图片地址）", required = true, dataType = "String", paramType = "body")
    public String animalClassification(@RequestBody String imagePath) {
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
