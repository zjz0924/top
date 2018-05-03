package cn.wow.rest;

import java.io.IOException;

import javax.inject.Named;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.NamedThreadLocal;
import cn.wow.rest.mapper.ApiExceptionMapper;
import cn.wow.rest.model.User;
import cn.wow.rest.utils.Base64;

@Named
public class APIRequestAuthFilter implements Filter {
	enum StatusCode {

		OK(200, "OK"), UNAUTHORIZED(401, ""), FORBIDDEN(403, "Forbidden"), NOT_FOUND(404,
				"Not found"), INTERNAL_ERROR(500, "Internal Error");

		private int code = 0;

		private String status;

		StatusCode(int code, String status) {
			this.code = code;
			this.status = status;
		}

		public int getCode() {
			return code;
		}

		public String getStatus() {
			return status;
		}

	}

	private static final Log LOGGER = LogFactory.getLog(ApiExceptionMapper.class);

	private static final String SESSION_LAST_ACCESS_API_TIME = "SESSION_LAST_ACCESS_API_TIME";

	public static final String SESSION_CURRENT_USER = "currentUser";

	public static final String SELECTED_CLUSTER = "SELECTED_CLUSTER";

	/**
	 * for local debug only.
	 */
	private static User localUser;

	/**
	 * store authorized user.
	 */
	private static final ThreadLocal<User> users = new NamedThreadLocal<User>("Users");

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		// Clean responseStatus.
		StatusCode responseStatus = StatusCode.UNAUTHORIZED;
		try {
			// check login
			StatusCode statusCode = authorizeUserLogin(httpRequest);
			if (statusCode != StatusCode.OK) {
				LOGGER.debug("authorizeUserLogin status: " + statusCode + ", Request URI: "
						+ httpRequest.getRequestURI() + ", method: " + httpRequest.getMethod());
				httpResponse.setStatus(statusCode.getCode());
				return;
			} else {
				User currentUser = (User) httpRequest.getSession().getAttribute(SESSION_CURRENT_USER);
				users.set(currentUser);
			}

			// authorize user.
			// Whether request user own the authorization.
			statusCode = authorizeUser(httpRequest);
			LOGGER.debug("AuthorizeUser result status = " + statusCode);
			if (statusCode == StatusCode.OK) {
				HttpSession se = httpRequest.getSession(false);
				if (se != null) {
					se.setAttribute(SESSION_LAST_ACCESS_API_TIME, System.currentTimeMillis());
				}
				chain.doFilter(request, response);
				return;
			} else {
				responseStatus = statusCode;
			}
		} catch (Exception e)// NOSONAR
		{
			LOGGER.error("authorize failed.", e);
			responseStatus = StatusCode.INTERNAL_ERROR;
		}

		if (responseStatus == StatusCode.UNAUTHORIZED) {
			httpResponse.addHeader("WWW-Authenticate", "Basic realm=\"TOP API\"");
		}
		LOGGER.debug("Response status: " + responseStatus + ", Request URI: " + httpRequest.getRequestURI()
				+ ", method: " + httpRequest.getMethod());
		httpResponse.setStatus(responseStatus.getCode());

	}

	private StatusCode authorizeUserLogin(HttpServletRequest request) throws Exception {
		// this code block is for UISDK which is login via CAS first
		// IMPORTANT: and we have a back door for UISDK dev which we always have a cross
		// domain access between UISDK server and MSA Tomcat
		User currentUser = this.getLoginUser(request);
		if (currentUser != null) {
			// already login
			return StatusCode.OK;
		}

		// Analysis username and password in request header.
		String info = request.getHeader("Authorization");
		if (info == null || "".equals(info)) {
			LOGGER.debug("no Authorization header found in request header.");
			return StatusCode.UNAUTHORIZED;
		}
		// only support HTTP Basic Authorization.
		if (!info.startsWith("Basic ")) {
			LOGGER.debug("Invalid authorization header found in request header.");
			return StatusCode.UNAUTHORIZED;
		}
		info = info.replace("Basic ", "");
		byte[] decoded;
		String decodeString = null;
		try {
			decoded = Base64.decode(info);
			decodeString = new String(decoded);
		} catch (Exception e)// NOSONAR
		{
			LOGGER.error("decode password failed.", e);
			return StatusCode.UNAUTHORIZED;
		}

		if (decodeString != null) {
			String[] strArray = decodeString.split(":");
			String username = strArray[0];
			String password = strArray[1];
			// Call DB to validate.
			LOGGER.debug("loading user info from database with name '" + username + "'");

			User user = "admin".equals(username) ? new User() : null; // 从数据库读取用户
			user.setUserName(username);
			user.setPassword(password);

			if (user != null) {
				String passwordMD5 = password; // md5加密码
				if (user.getPassword().equals(passwordMD5)) {
					request.getSession().setAttribute(SESSION_CURRENT_USER, user);// NOSONAR

					LOGGER.info("user '" + username + "' and its permissions was loaded and stored in session.");
					return StatusCode.OK;
				} else {
					LOGGER.debug("Password is incorret for user: " + username);
				}
			} else {
				LOGGER.debug("msa account not found for: " + username);
			}
		}
		// Invalid User Name or Password.
		return StatusCode.UNAUTHORIZED;
	}

	// 查看是否有权限
	public StatusCode authorizeUser(HttpServletRequest request) {
		return StatusCode.OK;
	}

	User getLoginUser(HttpServletRequest request) {
		User currentUser = (User) request.getSession().getAttribute(SESSION_CURRENT_USER);
		if (currentUser == null) {
			// for local debug only.
			currentUser = localUser;
			request.getSession(false).setAttribute(SESSION_CURRENT_USER, currentUser);
		}
		return currentUser;
	}

	public static User getCurrentUser() {
		return users.get();
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void destroy() {

	}

}
