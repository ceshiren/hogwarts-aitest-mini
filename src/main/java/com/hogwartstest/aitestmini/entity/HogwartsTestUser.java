package com.hogwartstest.aitestmini.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "hogwarts_test_user")
@Data
public class HogwartsTestUser extends BaseEntityNew {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 自动生成用例job名称 不为空时表示已经创建job
     */
    @Column(name = "auto_create_case_job_name")
    private String autoCreateCaseJobName;

    /**
     * 执行测试job名称 不为空时表示已经创建job
     */
    @Column(name = "start_test_job_name")
    private String startTestJobName;

    /**
     * 项目默认的Jenkins服务器id
     */
    @Column(name = "default_jenkins_id")
    private Integer defaultJenkinsId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

}