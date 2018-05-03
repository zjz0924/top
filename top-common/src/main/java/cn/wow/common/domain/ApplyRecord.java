package cn.wow.common.domain;

import java.util.Date;

/**
 * 申请修改记录
 * @author zhenjunzhuo
 * 2017-11-06
 */
public class ApplyRecord {
    private Long id;
    // 整车信息ID
    private Long vId;
    // 零部件信息ID
    private Long pId;
    // 原材料信息ID
    private Long mId;
    // 任务ID
    private Long tId;
    private Task task;
    
    // 申请人ID
    private Long aId;
    // 状态：0-待审批，1-通过，2-不通过，3-取消
    private Integer state;
    // 性能结果ID
    private String pfResultIds;
    // 图谱结果ID
    private String atlasResult;
    // 类型：1-信息修改，2-试验结果修改
    private Integer type;
    // 备注
    private String remark;
    // 创建时间
    private Date createTime;
    // 确认时间
    private Date confirmTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getvId() {
        return vId;
    }

    public void setvId(Long vId) {
        this.vId = vId;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public Long getmId() {
        return mId;
    }

    public void setmId(Long mId) {
        this.mId = mId;
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

    public String getPfResultIds() {
        return pfResultIds;
    }

    public void setPfResultIds(String pfResultIds) {
        this.pfResultIds = pfResultIds == null ? null : pfResultIds.trim();
    }

    public String getAtlasResult() {
        return atlasResult;
    }

    public void setAtlasResult(String atlasResult) {
        this.atlasResult = atlasResult == null ? null : atlasResult.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
    
    
}