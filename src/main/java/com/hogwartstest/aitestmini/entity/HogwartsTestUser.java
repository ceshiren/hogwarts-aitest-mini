package com.hogwartstest.aitestmini.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "hogwarts_test_user")
@Data
@ApiModel(value="测试用户对象")
public class HogwartsTestUser extends BaseEntityNew {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="主键",required=true)
    private Integer id;

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
     * 邮箱
     */
    @ApiModelProperty(value="邮箱",required=true)
    private String email;

    /**
     * 自动生成用例job名称 不为空时表示已经创建job
     */
    @Column(name = "auto_create_case_job_name")
    @ApiModelProperty(value="自动生成用例job名称",required=true, hidden = true)
    private String autoCreateCaseJobName;

    /**
     * 执行测试job名称 不为空时表示已经创建job
     */
    @Column(name = "start_test_job_name")
    @ApiModelProperty(value="执行测试job名称 不为空时表示已经创建job")
    private String startTestJobName;

    /**
     * 项目默认的Jenkins服务器id
     */
    @Column(name = "default_jenkins_id")
    @ApiModelProperty(value="项目默认的Jenkins服务器id",required=true)
    private Integer defaultJenkinsId;

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

}
