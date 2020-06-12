package com.hogwartstest.aitestmini.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "hogwarts_test_jenkins")
@Data
public class HogwartsTestJenkins extends BaseEntityNew {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 测试命令
     */
    @Column(name = "test_command")
    private String testCommand;

    /**
     * Jenkins的baseUrl
     */
    private String url;

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
     * git地址
     */
    @Column(name = "git_url")
    private String gitUrl;

    /**
     * git分支
     */
    @Column(name = "git_branch")
    private String gitBranch;

    /**
     * 创建人id
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 备注
     */
    private String remark;

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