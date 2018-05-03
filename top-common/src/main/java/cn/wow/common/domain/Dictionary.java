package cn.wow.common.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 字典
 * @author zhenjunzhuo
 * 2017-09-27
 */
public class Dictionary extends JpaEntity{
	
	private static final long serialVersionUID = -4284577071542828775L;

	private Long id;

    private String name;

    private String val;

    private String desc;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val == null ? null : val.trim();
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	@Override
	@JsonIgnore
	public Serializable getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}
}