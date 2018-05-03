package cn.wow.common.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 用户
 * @author zhenjunzhuo 
 * 2016-07-26
 */
public class Account extends JpaEntity{
	
	private static final long serialVersionUID = 9009465862519388181L;
	
	private Long id;
	// 用户名
	private String userName;
	// 昵称
	private String nickName;
	// 登录密码
	@JsonIgnore
	private String password;
	// 手机号码
	private String mobile;
	// 创建时间
	private Date createTime;
	// 是否被封号：Y-是，N-否
	private String lock = "N";
	// 角色ID
	private Long roleId;
	// 角色
	private Role role;
	
	private Long orgId;
	
	private Org org;
	
	private String email;
	// 备注
	private String remark;
	// 签名来源（1 - 登录用户姓名   2 - 使用图片签名）
	private Integer signType;
	// 签名图片
	private String pic;
	// 是否收费
	private Integer isCharge;
	
	public String getLock() {
		return lock;
	}

	public void setLock(String lock) {
		this.lock = lock;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSignType() {
		return signType;
	}

	public void setSignType(Integer signType) {
		this.signType = signType;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Integer getIsCharge() {
		return isCharge;
	}

	public void setIsCharge(Integer isCharge) {
		this.isCharge = isCharge;
	}

	@JsonIgnore
	public Serializable getPrimaryKey() {
		return id;
	}
}