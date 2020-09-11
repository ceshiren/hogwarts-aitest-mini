package com.hogwartstest.aitestmini.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "hogwarts_test_task_case_rel")
@Data
@ApiModel(value="测试任务用例关联对象")
public class HogwartsTestTaskCaseRel extends BaseEntityNew {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="ID",required=true)
    private Integer id;

    /**
     * 创建人id
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(value="创建人id",required=true)
    private Integer createUserId;

    /**
     * 任务id
     */
    @Column(name = "task_id")
    @ApiModelProperty(value="任务id",required=true)
    private Integer taskId;

    /**
     * 用例id
     */
    @Column(name = "case_id")
    @ApiModelProperty(value="用例id",required=true)
    private Integer caseId;

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
