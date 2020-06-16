package com.hogwartstest.aitestmini.dto.jenkins;

import com.hogwartstest.aitestmini.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="添加Jenkins对象")
@Data
public class AddHogwartsTestJenkinsDto extends BaseEntityNew {

    /**
     * 名称
     */
    @ApiModelProperty(value="Jenkins名称",required=true)
    private String name;

    /**
     * 测试命令
     */
    @ApiModelProperty(value="测试命令前缀",required=true)
    private String testCommand;

    /**
     * Jenkins的baseUrl
     */
    @ApiModelProperty(value="Jenkins的baseUrl",required=true)
    private String url;

    /**
     * 用户名
     */
    @ApiModelProperty(value="Jenkins用户名称",required=true)
    private String userName;

    /**
     * 密码
     */
    @ApiModelProperty(value="Jenkins用户密码",required=true)
    private String password;

    /**
     * git地址
     */
    @ApiModelProperty(value="被测项目git地址",required=true)
    private String gitUrl;

    /**
     * git分支
     */
    @ApiModelProperty(value="被测项目git分支",required=true)
    private String gitBranch;

    /**
     * 备注
     */
    @ApiModelProperty(value="Jenkins备注",required=true)
    private String remark;

    /**
     *
     */
    @ApiModelProperty(value="是否设置为默认服务器 1 是 0 否",required=true)
    private Integer defaultJenkinsFlag = 0;

}