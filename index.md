## 欢迎使用Fast DJL

您可以通过本项目快速部署一个推理的例子。

Fast DJL 所使用的框架为[DJL](http://djl.ai)和[Spring Boot](https://spring.io/projects/spring-boot)以及接口[Swagger-Ui](https://swagger.io/)

## 上手指南

- clone到本地通过maven
- 运行Application启动类，页面访问[http：// localhost：7798 / swagger-ui / index.html](http://localhost:7798/swagger-ui/index.html)

## 新增内容

- 训练测试图像识别数据，详情见test / resources /目录下
- `swagger-ui` 在线接口文档
- 添加线性回归示例和通过DJL工具实现的简洁线性回归示例，详情见main / java / com / fastdjl / common / staticvoidmainTest /目录下ConciseLinearRegression.java
- 新增通过获取FashionMnist数据集进行服装图像分类示例，详情见main / java / com / fastdjl / common / staticvoidmainTest /目录下ImageClassification.java
- 新增通过swagger-ui进行图像分类接口，详情见main / java / com / fastdjl / controller / ClassificationController.java
- 实现softmax和totensor运行示例，详情见main / java / com / fastdjl / common / staticvoidmainTest /目录下ImplTotenserAndSoftmaxClassifications.java

### 关于作者

- [James Zow](https://github.com/Jzow)
