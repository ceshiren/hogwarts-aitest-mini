package com.hogwartstest.aitestmini.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "hogwarts_test_task")
@Data
public class HogwartsTestTask extends BaseEntityNew {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 运行测试的Jenkins服务器id
     */
    @Column(name = "test_jenkins_id")
    private Integer testJenkinsId;

    /**
     * Jenkins的构建url
     */
    @Column(name = "build_url")
    private String buildUrl;

    /**
     * 创建人id
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 用例数量
     */
    @Column(name = "case_conut")
    private Integer caseConut;

    /**
     * 备注
     */
    private String remark;

    /**
     * 任务类型 1 执行测试任务 2 一键执行测试的任务
     */
    @Column(name = "task_type")
    private Integer taskType;

    /**
     * 状态 0 无效 1 新建 2 执行中 3 执行完成
     */
    private Integer status;

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

    /**
     * Jenkins执行测试时的命令脚本
     */
    @Column(name = "test_command")
    private String testCommand;

}