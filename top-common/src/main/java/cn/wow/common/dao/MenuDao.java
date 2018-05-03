package cn.wow.common.dao;

import java.util.List;

import cn.wow.common.domain.Menu;
import cn.wow.common.domain.TreeNode;

public interface MenuDao extends SqlDao{

	public List<Menu> getMenuList();
	
	public Menu selectByAlias(String alias);
}