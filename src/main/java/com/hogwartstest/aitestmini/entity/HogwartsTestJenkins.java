package com.hogwartstest.aitestmini.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "hogwarts_test_jenkins")
@Data
@ApiModel(value="测试Jenkins对象")
public class HogwartsTestJenkins extends BaseEntityNew {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="主键",required=true)
    private Integer id;

    /**
     * 名称
     */
    @ApiModelProperty(value="名称",required=true)
    private String name;

    /**
     * 测试命令
     */
    @Column(name = "test_command")
    @ApiModelProperty(value="测试命令",required=true)
    private String testCommand;

    /**
     * Jenkins的baseUrl
     */
    @ApiModelProperty(value="Jenkins的baseUrl",required=true)
    private String url;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    @ApiModelProperty(value="用户名",required=true)
    private String userName;

    /**
     * 密码
     */
    @ApiModelProperty(value="密码",required=true)
    private String password;

    /**
     * 创建人id
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(value="创建人id",required=true)
    private Integer createUserId;

    /**
     * 命令运行的测试用例类型  1 文本 2 文件
     */
    @Column(name = "command_run_case_type")
    @ApiModelProperty(value="命令运行的测试用例类型  1 文本 2 文件",required=true)
    private Integer commandRunCaseType;

    /**
     * 测试用例后缀名 如果case为文件时，此处必填
     */
    @Column(name = "command_run_case_suffix")
    @ApiModelProperty(value="测试用例后缀名",required=true)
    private String commandRunCaseSuffix;

    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(value="创建时间",required=true)
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @ApiModelProperty(value="更新时间",required=true)
    private Date updateTime;

    /**
     *  此字段不存入数据库
     * 是否设置为默认服务器 1 是 0 否
     */
    @Transient
    @ApiModelProperty(value="是否设置为默认服务器 1 是 0 否",required=true)
    private Integer defaultJenkinsFlag = 0;

}
