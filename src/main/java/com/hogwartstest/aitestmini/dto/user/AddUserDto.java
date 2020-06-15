package com.hogwartstest.aitestmini.dto.user;

import com.hogwartstest.aitestmini.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author tlibn
 * @Date 2020/6/12 16:49
 **/

@ApiModel(value="注册用户对象",description="用户对象user")
@Data
public class AddUserDto extends BaseDto {

    @ApiModelProperty(value="用户名",required=true)
    private String userName;
    @ApiModelProperty(value="密码",required=true)
    private String password;
    @ApiModelProperty(value="邮箱",required=true)
    private String email;

}
