package cn.wow.common.domain;

import java.util.Date;

/**
 * 性能结果
 */
public class PfResult {
    private Long id;
    // 任务ID
    private Long tId;
    // 试验项目
    private String project;
    // 参考标准
    private String standard;
    // 试验要求
    private String require;
    // 试验结果
    private String result;
    // 结果评价
    private String evaluate;
    // 备注
    private String remark;
    // 创建时间
    private Date createTime;
    // 类型（1-零部件，2-原材料）
    private Integer catagory;
    // 实验序号
    private Integer expNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long gettId() {
        return tId;
    }

    public void settId(Long tId) {
        this.tId = tId;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project == null ? null : project.trim();
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard == null ? null : standard.trim();
    }

    public String getRequire() {
        return require;
    }

    public void setRequire(String require) {
        this.require = require == null ? null : require.trim();
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result == null ? null : result.trim();
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate == null ? null : evaluate.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public Integer getCatagory() {
		return catagory;
	}

	public void setCatagory(Integer catagory) {
		this.catagory = catagory;
	}

	public Integer getExpNo() {
		return expNo;
	}

	public void setExpNo(Integer expNo) {
		this.expNo = expNo;
	}

}