package com.fastdjl.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "线性神经网络")
@RequestMapping("/LinearRegression")
public class LinearRegressionController {

	/**
	 * 从0实现线性回归
	 * @param trueW 真实权重
	 * @param trueB 真实偏差
	 * @param examples 样本数量
	 * @param inputs 特征数量
	 * @return
	 */
	@PostMapping("/simple")
	@ApiOperation("线性回归实现接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "trueW", value = "真实权重", dataType = "Float", required = false),
			@ApiImplicitParam(name = "trueB", value = "真实偏差", dataType = "Float", required = false),
			@ApiImplicitParam(name = "examples", value = "样本数量", dataType = "Integer", required = false),
			@ApiImplicitParam(name = "inputs", value = "特征数量", dataType = "Integer", required = false)
	})
	public String trainingModel(Float trueW, Float trueB, Integer examples, Integer inputs) {
	//	 LinearRegression.trainingModel(examples,inputs);
		return "test api success";
	}

}