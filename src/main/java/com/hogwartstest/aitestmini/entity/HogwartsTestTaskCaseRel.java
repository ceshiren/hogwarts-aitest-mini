package com.hogwartstest.aitestmini.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "hogwarts_test_task_case_rel")
@Data
public class HogwartsTestTaskCaseRel extends BaseEntityNew {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 创建人id
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 任务id
     */
    @Column(name = "task_id")
    private Integer taskId;

    /**
     * 用例id
     */
    @Column(name = "case_id")
    private Integer caseId;

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