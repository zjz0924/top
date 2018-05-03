package cn.wow.common.domain;

import java.util.Date;

/**
 * 试验结论
 * @author zhen
 * 2018-01-15
 */
public class LabConclusion {
    private Long id;
    // 试验结论（合格、不合格、其它）
    private String conclusion;
    // 报告编号
    private String repNum;
    // 主检
    private String mainInspe;
    // 审核
    private String examine;
    // 签发
    private String issue;
    // 收样时间
    private Date receiveDate;
    // 试验时间
    private Date examineDate;
    // 签发时间
    private Date issueDate;
    // 备注
    private String remark;
    // 任务ID
    private Long taskId;
	// 类型： 1-零部件图谱,2-原材料图谱,3-零部件型式,4-原材料型式
    private Integer type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion == null ? null : conclusion.trim();
    }

    public String getRepNum() {
        return repNum;
    }

    public void setRepNum(String repNum) {
        this.repNum = repNum == null ? null : repNum.trim();
    }

    public String getMainInspe() {
        return mainInspe;
    }

    public void setMainInspe(String mainInspe) {
        this.mainInspe = mainInspe == null ? null : mainInspe.trim();
    }

    public String getExamine() {
        return examine;
    }

    public void setExamine(String examine) {
        this.examine = examine == null ? null : examine.trim();
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue == null ? null : issue.trim();
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Date getExamineDate() {
        return examineDate;
    }

    public void setExamineDate(Date examineDate) {
        this.examineDate = examineDate;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}