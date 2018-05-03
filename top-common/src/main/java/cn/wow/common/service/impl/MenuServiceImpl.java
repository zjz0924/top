package cn.wow.common.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.wow.common.dao.MenuDao;
import cn.wow.common.domain.Menu;
import cn.wow.common.domain.MenuNode;
import cn.wow.common.domain.TreeNode;
import cn.wow.common.service.MenuService;
import cn.wow.common.utils.pagination.PageMap;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {

	private static Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

	@Autowired
	private MenuDao menuDao;

	public List<Menu> getMenuList() {
		return menuDao.getMenuList();
	}

	public Menu selectByAlias(String alias) {
		return menuDao.selectByAlias(alias);
	}

	public Menu selectOne(Long id) {
		return menuDao.selectOne(id);
	}

	public int update(String userName, Menu menu) {
		return menuDao.update(menu);
	}

	public List<Menu> selectAllList(Map<String, Object> map) {
		return menuDao.selectAllList(map);
	}

	/**
	 * treegrid 
	 */
	public List<MenuNode> getMenuTree() {
		Map<String, Object> rMap = new PageMap(false);
		rMap.put("custom_order_sql", "p_id asc, sort_num asc");
		List<Menu> menuList = selectAllList(rMap);
		List<MenuNode> nodeList = new ArrayList<MenuNode>();
		
		for(Menu menu : menuList){
			MenuNode node = new MenuNode(menu.getId(), menu.getName(), menu.getUrl(), menu.getSortNum(), menu.getpId());
			nodeList.add(node);
		}
		
		return nodeList;
	}
	
	/**
	 * tree
	 */
	public List<TreeNode> getTreeData(){
		List<Menu> sonList = getMenuList();
		
		Menu rootMenu = new Menu();
		rootMenu.setName("菜单");
		rootMenu.setId(0l);
		rootMenu.setSubList(sonList);
		
		TreeNode rootNode = new TreeNode();
		
		List<TreeNode> tree = new ArrayList<TreeNode>();
		if (rootMenu != null) {
			rootNode.setId(rootMenu.getId().toString());
			rootNode.setText(rootMenu.getName());

			// 获取子集合
			Iterator<Menu> subList = rootMenu.getSubList().iterator();
			if (subList.hasNext()) {
				List<TreeNode> subNodeList = new ArrayList<TreeNode>();

				// 遍历子节点
				while (subList.hasNext()) {
					Menu subArea = subList.next();

					TreeNode subAreaNode = new TreeNode();
					subAreaNode.setId(subArea.getId().toString());
					subAreaNode.setText(subArea.getName());
					
					// 遍历子节点
					addSonNode(subAreaNode, subArea);
					subNodeList.add(subAreaNode);
				}
				rootNode.setChildren(subNodeList);
			}
		}
		tree.add(rootNode);

		return tree;
	}
	
	private void addSonNode(TreeNode subAreaNode, Menu menu) {
		// 获取子集合
		Iterator<Menu> subList = menu.getSubList().iterator();

		if (subList.hasNext()) {
			List<TreeNode> subNodeList = new ArrayList<TreeNode>();

			// 遍历子节点
			while (subList.hasNext()) {
				Menu subArea = subList.next();

				TreeNode sonNode = new TreeNode();
				sonNode.setId(subArea.getId().toString());
				sonNode.setText(subArea.getName());
				// 遍历子节点
				addSonNode(sonNode, subArea);
				subNodeList.add(sonNode);
			}
			subAreaNode.setChildren(subNodeList);
		}
	}

	
}
