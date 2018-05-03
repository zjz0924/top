package cn.wow.support.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wow.common.domain.Menu;
import cn.wow.common.domain.MenuNode;
import cn.wow.common.domain.TreeNode;
import cn.wow.common.service.MenuService;
import cn.wow.common.utils.AjaxVO;

/**
 * 菜单管理
 * 
 * @author zhenjunzhuo 
 * 2017-09-12
 */
@Controller
@RequestMapping(value = "menu")
public class MenuController extends AbstractController {

	Logger logger = LoggerFactory.getLogger(MenuController.class);

	@Autowired
	private MenuService menuService;

	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request, Model model) {
		return "sys/menu/menu_list";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/update")
	public AjaxVO update(HttpServletRequest request, Model model, String id, String name, Integer sortNum) {
		AjaxVO vo = new AjaxVO();
		Menu menu = null;

		try {
			if (StringUtils.isNoneBlank(id)) {
				menu = menuService.selectOne(Long.parseLong(id));

				if (menu != null) {
					menu.setName(name);
					menu.setSortNum(sortNum);
					menuService.update(getCurrentUserName(), menu);
					
					vo.setMsg("编辑成功");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();

			vo.setMsg("保存失败，系统异常");
			vo.setSuccess(false);
			logger.error("菜单保存失败：", ex);
			return vo;
		}
		return vo;
	}

	
	/**
	 * treegrid
	 */
	@ResponseBody
	@RequestMapping(value = "/tree")
	public Map<String, Object> tree(HttpServletRequest request, Model model, String svalue, String stype) {
		List<MenuNode> tree = menuService.getMenuTree();
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("total", tree.size());
		dataMap.put("rows", tree);
		
		return dataMap;
	}
	
	
	/**
	 * tree
	 */
	@ResponseBody
	@RequestMapping(value = "/getTreeData")
	public List<TreeNode> getTreeData(HttpServletRequest request, Model model, String svalue, String stype) {
		List<TreeNode> menuTree = menuService.getTreeData();
		return menuTree;
	}
	
}
