package cn.wow.common.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 区域树节点
 */
public class TreeNode implements Serializable {

	private static final long serialVersionUID = -3120773122177211682L;

	public String id;

	public String text;
	
	@JsonInclude(Include.NON_NULL) 
	public List<TreeNode> children;

	@JsonInclude(Include.NON_NULL) 
	public String iconCls;
	
	@JsonInclude(Include.NON_NULL) 
	public String state;
	
	@JsonInclude(Include.NON_NULL) 
	public boolean checked;
	
	@JsonInclude(Include.NON_NULL) 
	public Object attributes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}


	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Object getAttributes() {
		return attributes;
	}

	public void setAttributes(Object attributes) {
		this.attributes = attributes;
	}
}
