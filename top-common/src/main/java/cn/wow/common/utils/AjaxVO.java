package cn.wow.common.utils;

import java.io.Serializable;


/**
 * 
 * @author junzzh
 * 2016-06-29
 */
public class AjaxVO implements Serializable{

	private static final long serialVersionUID = -6779629759636426487L;

	private boolean success = true;
	
	private String msg;
	
	private Object data;
	
	public AjaxVO(){
		
	}
	
	public AjaxVO(String msg, boolean success){
		this.msg = msg;
		this.success = success;
	}
	
	public AjaxVO(String msg, boolean success, Object data){
		this.msg = msg;
		this.success = success;
		this.data = data;
	}

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	
	
	
}
