package cn.wow.common.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 区域
 */
public class Area extends JpaEntity {

	private static final long serialVersionUID = -5450900564575959225L;

	private Long id;
	// 编码
	private String code;

	private String name;
	// 父区域
	private Long parentid;

	private Area parent;

	private String desc;

	// 子区域
	@JsonIgnore
	private List<Area> subList;

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

	public Long getParentid() {
		return parentid;
	}

	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc == null ? null : desc.trim();
	}

	public List<Area> getSubList() {
		return subList;
	}

	public void setSubList(List<Area> subList) {
		this.subList = subList;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Area getParent() {
		return parent;
	}

	public void setParent(Area parent) {
		this.parent = parent;
	}

	@JsonIgnore
	public Serializable getPrimaryKey() {
		return id;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof Area) {
			Area area = (Area) obj;
			return code.equals(area.getCode().trim());
		}
		return false;
	}
	
	public int hashCode() {
		return code.hashCode();
	}
}