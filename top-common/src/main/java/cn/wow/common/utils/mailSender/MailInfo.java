package cn.wow.common.utils.mailSender;

import java.security.GeneralSecurityException;
import java.util.Properties;

import com.sun.mail.util.MailSSLSocketFactory;

public class MailInfo {
	/** 发送邮件的服务器的IP */
	private String mailHost;
	/** 发送邮件的服务器的端口 */
	private String mailPort = "465";
	/** 发送邮件的用户名（邮箱全名称） */
	private String username;
	/** 发送邮件的密码 */
	private String password;

	/** 错误信息发送地址（多个邮件地址以";"分隔） */
	private String errorTo;
	/** 错误信息抄送地址（多个邮件地址以";"分隔） */
	private String errorCc;
	/** 警告信息发送地址（多个邮件地址以";"分隔） */
	private String warningTo;
	/** 警告信息抄送地址（多个邮件地址以";"分隔） */
	private String warningCc;
	/** 通知信息发送地址（多个邮件地址以";"分隔） */
	private String notifyTo;
	/** 通知信息抄送地址（多个邮件地址以";"分隔） */
	private String notifyCc;

	/** 邮件主题 */
	private String subject;
	/** 邮件内容 */
	private String content;
	/** 邮件附件的文件名 */
	private String[] attachFileNames;

	/**
	 * 获取邮件参数
	 */
	public Properties getProperties() throws GeneralSecurityException {
		Properties props = new Properties();
		props.put("mail.smtp.host", getMailHost());
		props.put("mail.smtp.port", getMailPort());
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		// 10s 超时
		props.put("mail.smtp.timeout", "8000");

		MailSSLSocketFactory sslSF = new MailSSLSocketFactory();
		sslSF.setTrustAllHosts(true);
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.ssl.socketFactory", sslSF);
		props.put("mail.transport.protocol", "smtp");

		props.put("mail.user", getUsername());
		props.put("mail.password", getPassword());
		return props;
	}

	public String getMailHost() {
		return mailHost;
	}

	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}

	public String getMailPort() {
		return mailPort;
	}

	public void setMailPort(String mailPort) {
		this.mailPort = mailPort;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getErrorTo() {
		return errorTo;
	}

	public void setErrorTo(String errorTo) {
		this.errorTo = errorTo;
	}

	public String getErrorCc() {
		return errorCc;
	}

	public void setErrorCc(String errorCc) {
		this.errorCc = errorCc;
	}

	public String getWarningTo() {
		return warningTo;
	}

	public void setWarningTo(String warningTo) {
		this.warningTo = warningTo;
	}

	public String getWarningCc() {
		return warningCc;
	}

	public void setWarningCc(String warningCc) {
		this.warningCc = warningCc;
	}

	public String getNotifyTo() {
		return notifyTo;
	}

	public void setNotifyTo(String notifyTo) {
		this.notifyTo = notifyTo;
	}

	public String getNotifyCc() {
		return notifyCc;
	}

	public void setNotifyCc(String notifyCc) {
		this.notifyCc = notifyCc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String[] getAttachFileNames() {
		return attachFileNames;
	}

	public void setAttachFileNames(String[] attachFileNames) {
		this.attachFileNames = attachFileNames;
	}
}
