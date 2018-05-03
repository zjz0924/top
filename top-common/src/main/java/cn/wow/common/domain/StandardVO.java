package cn.wow.common.domain;

import java.io.Serializable;

/**
 * 基准vo
 * @author zhenjunzhuo
 * 2017-11-08
 */
public class StandardVO implements Serializable{
	
	private static final long serialVersionUID = -907120399366554878L;

	private Long id;
	
	private String text;
	
	private String taskCode;
	
	private String date;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
}
