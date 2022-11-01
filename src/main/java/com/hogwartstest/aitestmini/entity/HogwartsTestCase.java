package com.hogwartstest.aitestmini.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "hogwarts_test_case")
@Data
@ApiModel(value="测试用例对象")
public class HogwartsTestCase extends BaseEntityNew {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="主键",required=true)
    private Integer id;

    /**
     * 测试数据
     */
    @Column(name = "case_data")
    @ApiModelProperty(value="测试数据",required=true)
    private String caseData;

    /**
     * 测试命令
     */
    @Column(name = "test_command")
    @ApiModelProperty(value="测试命令",required=true)
    private String testCommand;

    /**
     * 用例名称
     */
    @Column(name = "case_name")
    @ApiModelProperty(value="用例名称",required=true)
    private String caseName;

    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String remark;

    /**
     * 状态 0 无效 1 新建 2 执行中 3 执行完成
     */
    @ApiModelProperty(value="状态 0 无效 1 新建 2 执行中 3 执行完成",required=true)
    private Integer status;

    /**
     * 删除标志 1 未删除 0 已删除
     */
    @Column(name = "del_flag")
    @ApiModelProperty(value="删除标志 1 未删除 0 已删除",required=true)
    private Integer delFlag;

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
