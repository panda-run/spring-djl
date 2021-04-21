# fastdjl
Fast Deep Java Library
本项目是一个纯Java编写的的DeepLearning Project,整合了AWS的DJL深度学习框架，与DJL不同的是本项目采用Maven依赖，提供给大家参考学习。

(目前Mxnet tensorflow在Windows10 环境下已跑通)

## 所用技术框架

* `AWS DJL` 亚马逊开源深度学习框架
* `Spring Boot` Spring官方框架
* `swagger-ui` 在线接口文档

## 上手指南
* clone到本地通过maven引入该项目<br>
* 在Mxnet下,运行Application启动类,页面访问http://localhost:7798/swagger-ui/index.html <br>

## 新增内容 
* `swagger-ui` 在线接口文档

* 新增线性回归例子和通过DJL工具实现的简洁线性回归例子，详情见[ConciseLinearRegression.java](https://github.com/Jzow/FastDJL/blob/master/mxnet/src/main/java/com/example/ConciseLinearRegression.java)

* 新增通过获取FashionMnist数据集进行服装图像分类例子，详情见[ImageClassification.java](https://github.com/Jzow/FastDJL/blob/master/mxnet/src/main/java/com/example/ImageClassification.java)

* 新增通过swagger-ui进行图像分类接口，详情见[ClassificationController.java](https://github.com/Jzow/FastDJL/blob/master/mxnet/src/main/java/com/controller/ClassificationController.java)

* 实现softmax和totensor运行例子，详情见[ImplTotenserAndSoftmaxClassifications.java](https://github.com/Jzow/FastDJL/blob/master/mxnet/src/main/java/com/example/ImplTotenserAndSoftmaxClassifications.java)
