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
     * 包名
     */
    @Column(name = "package_name")
    private String packageName;

    /**
     * 类名
     */
    @Column(name = "class_name")
    private String className;

    /**
     * 方法名
     */
    @Column(name = "method_name")
    private String methodName;

    /**
     * 用例标识
     */
    @Column(name = "case_sign")
    private String caseSign;

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