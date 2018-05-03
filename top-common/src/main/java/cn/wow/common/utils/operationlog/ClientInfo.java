package cn.wow.common.utils.operationlog;

/**
 * 
 * These class represents the client information of the Job, such as client IP, user agent.
 * 
 */
public class ClientInfo
{
	private String clientIp;

	private String userAgent;

	public ClientInfo()
	{

	}

	public ClientInfo(String clientIp, String userAgent)
	{
		setClientIp(clientIp);
		setUserAgent(userAgent);
	}

	public String getClientIp()
	{
		return clientIp;
	}

	public void setClientIp(String clientIp)
	{
		this.clientIp = clientIp;
	}

	public String getUserAgent()
	{
		return userAgent;
	}

	public void setUserAgent(String userAgent)
	{
		this.userAgent = userAgent;
	}

}
