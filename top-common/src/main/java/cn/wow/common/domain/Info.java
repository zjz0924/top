package cn.wow.common.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 信息
 * @author zhenjunzhuo
 * 2017-10-12
 */
public class Info extends JpaEntity{
	
	private static final long serialVersionUID = 1853864103927021620L;

	private Long id;

    // 整车信息ID
    private Long vId;
    
    private Vehicle vehicle;

    // 零部件信息ID
    private Long pId;

    private Parts parts;
    
    // 原材料信息ID
    private Long mId;
    
    private Material material;

    // 类型(1-基准，2-抽样)
    private Integer type;

    // 状态(0-审批中, 1-完成)
    private Integer state;

    private String remark;

    private Date createTime;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Parts getParts() {
		return parts;
	}

	public void setParts(Parts parts) {
		this.parts = parts;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	@JsonIgnore
	public Serializable getPrimaryKey() {
		return id;
	}
    
}