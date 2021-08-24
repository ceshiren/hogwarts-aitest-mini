package com.hogwartstest.aitestmini.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "hogwarts_test_his")
public class HogwartsTestHis extends BaseEntityNew {
    /**
     * ID
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 报告地址url
     */
    @Column(name = "report_url")
    private String reportUrl;

    /**
     * 报告存储地址
     */
    @Column(name = "report_path")
    private String reportPath;

    /**
     * 创建人id
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 用例id
     */
    @Column(name = "case_id")
    private Integer caseId;

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
     * 获取ID
     *
     * @return id - ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取报告地址url
     *
     * @return report_url - 报告地址url
     */
    public String getReportUrl() {
        return reportUrl;
    }

    /**
     * 设置报告地址url
     *
     * @param reportUrl 报告地址url
     */
    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    /**
     * 获取报告存储地址
     *
     * @return report_path - 报告存储地址
     */
    public String getReportPath() {
        return reportPath;
    }

    /**
     * 设置报告存储地址
     *
     * @param reportPath 报告存储地址
     */
    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    /**
     * 获取创建人id
     *
     * @return create_user_id - 创建人id
     */
    public Integer getCreateUserId() {
        return createUserId;
    }

    /**
     * 设置创建人id
     *
     * @param createUserId 创建人id
     */
    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * 获取用例id
     *
     * @return case_id - 用例id
     */
    public Integer getCaseId() {
        return caseId;
    }

    /**
     * 设置用例id
     *
     * @param caseId 用例id
     */
    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    /**
     * 获取状态 0 无效 1 新建 2 执行中 3 执行完成
     *
     * @return status - 状态 0 无效 1 新建 2 执行中 3 执行完成
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态 0 无效 1 新建 2 执行中 3 执行完成
     *
     * @param status 状态 0 无效 1 新建 2 执行中 3 执行完成
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}