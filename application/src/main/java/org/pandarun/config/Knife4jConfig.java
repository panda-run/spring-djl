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
package org.pandarun.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 描述: 在线接口文档 首页基本配置
 *
 * @author James Zow
 * @create 2021/10/25
 */
@Configuration
@EnableSwagger2
public class Knife4jConfig {

    @Bean
    public Docket RestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(
                        new ApiInfoBuilder()
                                .title("Spring DJL AI服务API接口文档")
                                .description("有任何使用问题请在github issues 提问")
                                .termsOfServiceUrl("http://localhost:8081/")
                                .version("1.0")
                                .build()
                )
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.pandarun.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
