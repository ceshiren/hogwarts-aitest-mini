package com.hogwartstest.aitestmini.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "hogwarts_test_case")
@Data
public class HogwartsTestCase extends BaseEntityNew {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 测试数据
     */
    @Column(name = "case_data")
    private String caseData;

    /**
     * 用例名称
     */
    @Column(name = "case_name")
    private String caseName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标志 1 未删除 0 已删除
     */
    @Column(name = "del_flag")
    private Integer delFlag;

    /**
     * 创建人id
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

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