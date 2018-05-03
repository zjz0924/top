package cn.wow.common.utils.operationlog;

import java.io.Serializable;

public class FieldValue implements Serializable{
	
	private static final long serialVersionUID = -1118099900938682368L;

	private String name;

	private String oldValue;

	private String newValue;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
}
