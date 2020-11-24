package com.service;

/**
 * ClassName: Classification
 * Description: 分类接口
 * Author: James Zow
 * Date: 2020/11/6 9:06
 * Version:
 **/
public interface Classification {

    /**
     * 图像分类 interface
     * @param imagePath 图片地址
     * @return
     */
    public String ImageClassification(String imagePath);

}
