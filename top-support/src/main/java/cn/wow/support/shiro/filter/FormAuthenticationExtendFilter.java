package cn.wow.support.shiro.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.wow.common.domain.Account;
import cn.wow.common.domain.Menu;
import cn.wow.common.domain.RolePermission;
import cn.wow.common.service.AccountService;
import cn.wow.common.service.MenuService;
import cn.wow.common.service.OperationLogService;
import cn.wow.common.service.RolePermissionService;
import cn.wow.common.utils.Contants;
import cn.wow.common.utils.operationlog.ClientInfo;
import cn.wow.common.utils.operationlog.OperationType;
import cn.wow.common.utils.operationlog.ServiceType;

public class FormAuthenticationExtendFilter extends FormAuthenticationFilter {
	private static Logger logger = LoggerFactory.getLogger(FormAuthenticationExtendFilter.class);

	@Autowired
	private AccountService accountService;
	@Autowired
	private OperationLogService operationLogService;
	@Autowired
	private RolePermissionService rolePermissionService;
	@Autowired
	private MenuService menuService;

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		String successUrl = "/index";

		/* //成功登录后返回成功跳转页面，指定跳转地址 */
		WebUtils.issueRedirect(request, response, successUrl);

		// 成功登录后返回成功跳转页面
		// issueSuccessRedirect(request, response);

		String username = (String) SecurityUtils.getSubject().getPrincipal();
		if (StringUtils.isNotBlank(username)) {
			HttpSession session = httpServletRequest.getSession();

			Account account = accountService.selectByAccountName(username);
			session.setAttribute(Contants.CURRENT_ACCOUNT, account);

			// 菜单信息
			Set<Long> illegalMenu = new HashSet<Long>();
			session.setAttribute(Contants.CURRENT_PERMISSION_MENU, getPermission(account, illegalMenu));
			// 没有权限的菜单ID
			session.setAttribute(Contants.CURRENT_ILLEGAL_MENU, illegalMenu);

			// 判断用户客户端信息
			createOrUpdateClientInfo(username, request.getRemoteAddr(), httpServletRequest.getHeader("user-agent"));
			// 添加日志
			operationLogService.save(username, OperationType.LOGIN, ServiceType.ACCOUNT, "");
		}

		return false;
	}

	private void createOrUpdateClientInfo(String userName, String clientIp, String userAgent) {
		ClientInfo clientInfo = new ClientInfo();
		clientInfo.setClientIp(clientIp);
		clientInfo.setUserAgent(userAgent);
		operationLogService.createOrUpdateUserClientInfo(userName, clientInfo);
	}

	/**
	 * 获取当前角色的菜单
	 */
	private List<Menu> getPermission(Account account, Set<Long> illegalMenu) {
		List<Menu> emptyData = new ArrayList<Menu>();		
		
		if (account.getRole() != null) {
			// 获取二级父节点
			List<Menu> menuList = menuService.getMenuList();

			if (account.getRole().getId().longValue() != Long.parseLong(Contants.SUPER_ROLE_ID)) { // 非超级管理员
				// 当前用户所有角色的权限
				RolePermission permission = rolePermissionService.selectOne(account.getRoleId());

				if (permission != null) {
					// 有权限的菜单ID
					Set<String> legalMenu = new HashSet<String>();

					if (StringUtils.isNotBlank(permission.getPermission())) {
						legalMenu.addAll(Arrays.asList(permission.getPermission().split(",")));
					}

					Iterator<Menu> it = menuList.iterator();
					while (it.hasNext()) {
						Menu menu = it.next();
						List<Menu> subList = menu.getSubList();

						if (subList != null && subList.size() > 0) {
							Iterator<Menu> subIt = subList.iterator();
							while (subIt.hasNext()) {
								Menu subMenu = subIt.next();
								List<Menu> sList = subMenu.getSubList();

								if (sList != null && sList.size() > 0) {
									Iterator<Menu> sIt = sList.iterator();
									while (sIt.hasNext()) {
										Menu sMenu = sIt.next();

										if (!legalMenu.contains(sMenu.getId().toString())) {
											illegalMenu.add(sMenu.getId());
											sIt.remove();
										}
									}

									if (sList == null || sList.size() < 1) {
										illegalMenu.add(subMenu.getId());
										subIt.remove();
									}
								} else {
									if (!legalMenu.contains(subMenu.getId().toString())) {
										illegalMenu.add(subMenu.getId());
										subIt.remove();
									}
								}
							}

							if (subList == null || subList.size() < 1) {
								illegalMenu.add(menu.getId());
								it.remove();
							}
						} else {
							if (!legalMenu.contains(menu.getId().toString())) {
								illegalMenu.add(menu.getId());
								it.remove();
							}
						}
					}
				} else {
					return emptyData;
				}
			}
			return menuList;
		} else {
			return emptyData;
		}
	}

}
