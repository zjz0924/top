package cn.wow.common.domain;

import java.util.Date;

/**
 * 审核记录
 * @author zhenjunzhuo
 * 2017-10-28
 */
public class ExamineRecord implements Comparable<ExamineRecord>{
    private Long id;

    // 任务ID
    private Long tId;
    // 操作人ID
    private Long aId;
    // 结果（1-通过，2-不通过）
    private Integer state;
    // 审核意见
    private String remark;
    // 审核时间
    private Date createTime;
    // 类型：1-审核记录，2-审批记录，3-确认记录，4-结果对比，5-二次确认
    private Integer type;
    // 分类：1-零部件图谱，2-原材料图谱，3-零部件型式，4-原材料型式，5-全部（针对审批记录）
    //     1-零部件红外，2-零部件差热，3-零部件热重，4-零部件结论，5-原材料红外，6-原材料差热，7-原材料热重，8-原材料结论，9-零部件样品，10-原材料样品，11-异常，重新上传（针对结果对比）
    private Integer catagory;
    // 任务类型：1-OTS、2-PPAP、3-SOP、4-材料研究所任务
    private Integer taskType;
    
    public ExamineRecord(){
    	
    }
    
	public ExamineRecord(Long tId, Long aId, Integer state, String remark, Integer type, Integer catagory,
			Date createTime, Integer taskType) {
		this.tId = tId;
		this.aId = aId;
		this.state = state;
		this.remark = remark;
		this.type = type;
		this.catagory = catagory;
		this.createTime = createTime;
		this.taskType = taskType;
	}

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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCatagory() {
		return catagory;
	}

	public void setCatagory(Integer catagory) {
		this.catagory = catagory;
	}
	
	public int compareTo(ExamineRecord record) {
		return this.getCatagory().compareTo(record.getCatagory());
	}

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}
	
}