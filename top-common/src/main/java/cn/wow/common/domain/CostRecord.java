package cn.wow.common.domain;

import java.util.Date;


/**
 * 费用记录
 * @author zhenjunzhuo
 * 2017-11-04
 */
public class CostRecord {
	
    private Long id;
    // 任务ID
    private Long tId;
    private Task task;
    
    // 发送人
    private Long aId;
    // 实验室ID	
    private Long labId;
    // 接收机构
    private String orgs;
    // 结果（0-未发送，1-已发送）
    private Integer state;
    // 备注
    private String remark;
    // 创建时间
    private Date createTime;
    // 发送时间
    private Date sendTime;
    // 实验次数
    private Integer times;
    // 实验类型（1-零部件图谱，2-零部件型式，3-原材料图谱，4-原材料型式）
    private Integer labType;
    // 实验结果（1-合格，2-不合格）
    private Integer labResult;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long gettId() {
        return tId;
    }

    public void settId(Long tId) {
        this.tId = tId;
    }

    public Long getaId() {
        return aId;
    }

    public void setaId(Long aId) {
        this.aId = aId;
    }

    public Long getLabId() {
        return labId;
    }

    public void setLabId(Long labId) {
        this.labId = labId;
    }

    public String getOrgs() {
        return orgs;
    }

    public void setOrgs(String orgs) {
        this.orgs = orgs == null ? null : orgs.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getLabType() {
        return labType;
    }

    public void setLabType(Integer labType) {
        this.labType = labType;
    }

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Integer getLabResult() {
		return labResult;
	}

	public void setLabResult(Integer labResult) {
		this.labResult = labResult;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
    
}