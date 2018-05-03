package cn.wow.common.domain;

import java.io.Serializable;

/**
 * 图谱对比
 * @author zhenjunzhuo
 * 2017-10-31
 */
public class CompareVO implements Serializable{

	private static final long serialVersionUID = 882552753882932265L;
	
	// 基准图谱
	private String standard_pic;
	// 抽样图谱
	private String sampling_pic;
	
	public String getStandard_pic() {
		return standard_pic;
	}
	public void setStandard_pic(String standard_pic) {
		this.standard_pic = standard_pic;
	}
	public String getSampling_pic() {
		return sampling_pic;
	}
	public void setSampling_pic(String sampling_pic) {
		this.sampling_pic = sampling_pic;
	}
	
	
}
