package com.hogwartstest.aitestmini.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger文档
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket docket() {
		ParameterBuilder builder = new ParameterBuilder();
		builder.parameterType("header").name("token")
				.description("restful方式的header参数")
				.required(false)
				.modelRef(new ModelRef("string")); // 在swagger里显示header

		/*
		* https://blog.csdn.net/u013506207/article/details/102790117
		* SWAGGER_2和SPRING_WEB对应生成的API json格式是有所区别的，
		* SPRING_WEB的json是可以被postman解析的
		* 可以将SPRING_WEB对应生成的API json拷贝到postman下import中的Paste Raw Text 尝试下。
		* */
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("aitest_interface")
				.apiInfo(apiInfo())
				.globalOperationParameters(Lists.newArrayList(builder.build()))
				.select().paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("swagger-demo后台系统")
				.description("swagger后台模块")
				.contact(new Contact("tlibn", "", "1039085737@qq.com"))
				.version("1.0")
				.build();
	}

}
