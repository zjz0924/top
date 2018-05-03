package cn.wow.common.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RolePermission extends JpaEntity{
	
	private static final long serialVersionUID = -2394074790258760100L;

	private Long id;
	
	private Long roleId;
	
	private String permission;
	
	public RolePermission(){
		
	}
	
	public RolePermission(String permission){
		this.permission = permission;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	@JsonIgnore
	public Serializable getPrimaryKey() {
		return id;
	}
	
}
