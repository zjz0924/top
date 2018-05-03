package cn.wow.common.domain;

import java.util.Date;

/**
 * 邮件记录
 */
public class EmailRecord {
    private Long id;
    // 主题
    private String subject;
    // 内容
    private String content;
    // 发送地址
    private String addr;
    // 任务ID
    private Long taskId;
    
    private Task task;
    
    // 操作人ID
    private Long aId;
    
    private Account account;
    
    // 状态：1-未查看，2-已查看
    private Integer state;
    // 类型：1-结果发送，2-收费通知，3-警告书
    private Integer type;
    // 发送邮箱
    private String orginEmail;
    // 创建时间
    private Date createTime;

    
    public EmailRecord(){
    	
    }
    
    public EmailRecord(String subject, String content, String addr, Long taskId, Long aId, Integer state, Integer type, String orginEmail, Date createTime){
    	this.subject = subject;
    	this.content = content;
    	this.addr = addr;
    	this.taskId = taskId;
    	this.aId = aId;
    	this.state = state;
    	this.type = type;
    	this.orginEmail = orginEmail;
    	this.createTime = createTime;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject == null ? null : subject.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr == null ? null : addr.trim();
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getaId() {
        return aId;
    }

    public void setaId(Long aId) {
        this.aId = aId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOrginEmail() {
        return orginEmail;
    }

    public void setOrginEmail(String orginEmail) {
        this.orginEmail = orginEmail == null ? null : orginEmail.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
}