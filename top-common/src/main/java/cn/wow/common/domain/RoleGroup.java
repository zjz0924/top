package cn.wow.common.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 角色组
 * @author samsung
 * 2017-09-21
 */
public class RoleGroup extends JpaEntity {
	
	private static final long serialVersionUID = 5186753282868408599L;

	private Long id;

    private String name;

    private Long parentid;
    
    private String desc;
    
    private RoleGroup parent;
    
    @JsonIgnore
	private List<RoleGroup> subList;
    
    @JsonIgnore
    private List<Role> roleList;
    
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
    
    public RoleGroup getParent() {
		return parent;
	}

	public void setParent(RoleGroup parent) {
		this.parent = parent;
	}

	public List<RoleGroup> getSubList() {
		return subList;
	}

	public void setSubList(List<RoleGroup> subList) {
		this.subList = subList;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@JsonIgnore
	public Serializable getPrimaryKey() {
		return id;
	}
}