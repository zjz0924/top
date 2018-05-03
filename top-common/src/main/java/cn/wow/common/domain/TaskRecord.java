package cn.wow.common.domain;

import java.util.Date;

/**
 * 任务操作记录
 * @author zhenjunzhuo
 * 2017-10-13
 */
public class TaskRecord {
    
	private Long id;
    // 任务号
    private String code;
    // 操作人ID
    private Long aId;
    
    private Account account;
    
    /** 
     * 	状态：
     *  OTS、材料研究所任务： 1-基准信息录入，2-审核通过，3-审核不通过，4-任务下达，5-审批同意，6-审批不同意，7-结果上传，8-结果发送，9-结果确认，10-基准保存，11-收费通知，12-信息修改，13-申请信息修改，14-申请试验结果修改，15-试验结果修改
     *  PPAP、SOP： 1-任务下达，2-审批同意，3-审批不同意，4-结果上传，5-结果比对正常，6-结果比对异常，7-结果发送，8-结果确认，9-结果留存，10-重新下任务，11-发送警告书，12-收费通知，13-二次确认，14-申请信息修改，15-申请试验结果修改，16-信息修改，17-试验结果修改
     */
    private Integer state;
     
    private String remark;

    private Date createTime;
    
    // 任务类型
    private Integer taskType;
    
    public TaskRecord(){
    	
    }

    public TaskRecord(String code, Long aId, Integer state, String remark, Date createTime, int taskType){
    	this.code = code;
    	this.aId = aId;
    	this.state = state;
    	this.remark = remark;
    	this.createTime = createTime;
    	this.taskType = taskType;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Long getaId() {
        return aId;
    }

    public void setaId(Long aId) {
        this.aId = aId;
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

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}
	
}