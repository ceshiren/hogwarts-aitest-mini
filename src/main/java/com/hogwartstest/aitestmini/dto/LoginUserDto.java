package com.hogwartstest.aitestmini.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



@ApiModel(value="用户登录对象",description="用户对象user")
@Data
public class LoginUserDto {

	@ApiModelProperty(value="用户名",name="username",required=true,dataType="String",notes="唯一不可重复",example="tester")
	private String userName;

	@ApiModelProperty(value="用户名",name="password",required=true,dataType="String",notes="字母+数字，6-18位",example="tester123")
	private String password;

}
