package cn.wow.common.domain;

import java.util.Date;

/**
 * 图谱结果
 */
public class AtlasResult {
    
	private Long id;
    // 任务ID
    private Long tId;
    // 类型 ：1-红外光分析、2-差热分析、3-热重分析、4-样品照片
    private Integer type;
    // 图片
    private String pic;
    // 备注
    private String remark;
    // 创建时间
    private Date createTime;
    // 类型（1-零部件，2-原材料）
    private Integer catagory;
    // 实验序号
    private Integer expNo;
    
    public AtlasResult(){
    	
    }
    
	public AtlasResult(Long tId, Integer type, String pic, String remark, Integer catagory, Integer expNo, Date createTime) {
		this.tId = tId;
		this.type = type;
		this.pic = pic;
		this.remark = remark;
		this.catagory = catagory;
		this.expNo = expNo;
		this.createTime = createTime;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic == null ? null : pic.trim();
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

	public Integer getCatagory() {
		return catagory;
	}

	public void setCatagory(Integer catagory) {
		this.catagory = catagory;
	}

	public Integer getExpNo() {
		return expNo;
	}

	public void setExpNo(Integer expNo) {
		this.expNo = expNo;
	}
}