package com.fastdjl.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "首页相关接口")
@RequestMapping("/home")
public class HomeController {

	@GetMapping("/index")
	@ApiOperation("根据id查询用户的接口")
	@ApiImplicitParam(name = "hello", value = "字符串", defaultValue = "test", required = true)
	public String getUserById(@PathVariable String hello) {
		System.out.println("redirect to home page!");
		return "redirect to home page";
	}

	public void home(){
		System.out.println("redirect to home page!");
	}

}