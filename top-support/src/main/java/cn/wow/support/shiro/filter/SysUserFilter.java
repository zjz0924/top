package cn.wow.support.shiro.filter;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.web.filter.PathMatchingFilter;

/**
 * 可用于更新数据
 * @author zhenjunzhuo
 */
public class SysUserFilter extends PathMatchingFilter {

	// 白名单
	private static List<String> whiteList = new ArrayList<String>();
	static {
		whiteList.add("/account/info");
		whiteList.add("/account/updatePwd");
	}
	
    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
    	HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    	HttpServletResponse httpServletResponse = (HttpServletResponse) response;
    	HttpSession session = httpServletRequest.getSession();
    	
    	// 当前访问路径
	/*	String uri = httpServletRequest.getRequestURI();

		boolean isWhite = false;
		for (String str : whiteList) {
			if (uri.contains(str)) {
				isWhite = true;
				break;
			}
		}

		if (!isWhite) {
			// 没有权限的菜单别名
			Set<String> illegalMenu = (Set<String>) session.getAttribute(Contants.CURRENT_ILLEGAL_MENU);
			if (illegalMenu != null && illegalMenu.size() > 0) {
				for (String alias : illegalMenu) {
					if (uri.contains(alias)) {
						httpServletResponse.sendError(403);
						break;
					}
				}
			}
		}
		*/
        return true;
    }
}
