package com.hogwartstest.aitestmini.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "hogwarts_test_task")
@Data
@ApiModel(value="测试任务对象")
public class HogwartsTestTask extends BaseEntityNew {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="id",required=true)
    private Integer id;

    /**
     * 名称
     */
    @ApiModelProperty(value="名称",required=true)
    private String name;

    /**
     * 运行测试的Jenkins服务器id
     */
    @Column(name = "test_jenkins_id")
    @ApiModelProperty(value="运行测试的Jenkins服务器id",required=true)
    private Integer testJenkinsId;

    /**
     * Jenkins的构建url
     */
    @Column(name = "build_url")
    @ApiModelProperty(value="Jenkins的构建url",required=true)
    private String buildUrl;

    /**
     * 创建人id
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(value="创建人id",required=true)
    private Integer createUserId;

    /**
     * 用例数量
     */
    @Column(name = "case_count")
    @ApiModelProperty(value="用例数量",required=true)
    private Integer caseCount;

    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String remark;

    /**
     * 任务类型 1 执行测试任务 2 一键执行测试的任务
     */
    @Column(name = "task_type")
    @ApiModelProperty(value="任务类型 1 执行测试任务 2 一键执行测试的任务",required=true)
    private Integer taskType;

    /**
     * 状态 0 无效 1 新建 2 执行中 3 执行完成
     */
    @ApiModelProperty(value="状态 0 无效 1 新建 2 执行中 3 执行完成",required=true)
    private Integer status;

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
     * Jenkins执行测试时的命令脚本
     */
    @Column(name = "test_command")
    @ApiModelProperty(value="Jenkins执行测试时的命令脚本",required=true)
    private String testCommand;

}
