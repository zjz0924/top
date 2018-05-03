package cn.wow.common.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * 零部件材料信息
 * @author zhenjunzhuo
 * 2017-09-30
 */
public class Material extends JpaEntity{
	
	private static final long serialVersionUID = 5960296191241326092L;

	private Long id;
	 // 类型（1-基准， 2-抽样）
    private Integer type;
    // 材料名称
    private String matName;
    // 材料牌号
    private String matNo;
    // 材料颜色
    private String matColor;
    // 材料生产商
    private Long orgId;
    private Org org;
    // 生产批次
    private String proNo;
    // 图片
    private String pic;
    // 备注
    private String remark;
    // 状态(0-审批中, 1-完成)
    private Integer state;

    private Date createTime;
    // 联系人
    private String contacts;
    // 联系电话 
    private String phone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMatName() {
        return matName;
    }

    public void setMatName(String matName) {
        this.matName = matName == null ? null : matName.trim();
    }

    public String getMatNo() {
        return matNo;
    }

    public void setMatNo(String matNo) {
        this.matNo = matNo == null ? null : matNo.trim();
    }

    public String getMatColor() {
        return matColor;
    }

    public void setMatColor(String matColor) {
        this.matColor = matColor == null ? null : matColor.trim();
    }

    public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public String getProNo() {
        return proNo;
    }

    public void setProNo(String proNo) {
        this.proNo = proNo == null ? null : proNo.trim();
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
    
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@JsonIgnore
	public Serializable getPrimaryKey() {
		return id;
	}
}