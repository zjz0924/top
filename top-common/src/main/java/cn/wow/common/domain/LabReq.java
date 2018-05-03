package cn.wow.common.domain;

import java.util.Date;

/**
 * 实验要求
 * @author zhenjunzhuo
 * 2018-1-11
 */
public class LabReq {
    private Long id;
    // 任务号（非系统任务号，线下的）
    private String code;
    // 商定完成时间
    private Date time;
    // 测试要求
    private String remark;
    // 任务ID
    private Long taskId;
    // 实验类型： 1- 零部件图谱，2-原材料图谱，3-零部件型式，4-原材料型式
    private Integer type;
    
	public LabReq(String code, Date time, String remark, Long taskId, int type) {
		this.code = code;
		this.time = time;
		this.remark = remark;
		this.taskId = taskId;
		this.type = type;
	}
    
    public LabReq(){
    	
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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