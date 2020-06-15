package com.hogwartstest.aitestmini.dto.testcase;

import com.hogwartstest.aitestmini.entity.BaseEntityNew;
import lombok.Data;

import java.util.Date;

@Data
public class HogwartsTestTaskCaseRelDetailDto extends BaseEntityNew {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 任务主键
     */
    private Integer taskId;

    /**
     * 测试用例主键
     */
    private Integer caseId;

    /**
     * 包名
     */
    private String packageName;

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 用例标识
     */
    private String caseSign;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标志 1 未删除 0 已删除
     */
    private Integer delFlag;

    /**
     * 创建人id
     */
    private Integer createUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}