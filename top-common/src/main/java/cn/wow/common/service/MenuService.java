package cn.wow.common.service;

import java.util.List;
import java.util.Map;

import cn.wow.common.domain.Menu;
import cn.wow.common.domain.MenuNode;
import cn.wow.common.domain.TreeNode;

public interface MenuService {

    public List<Menu> getMenuList();
    
    public Menu selectByAlias(String alias);

    public Menu selectOne(Long id);
    
    public int update(String userName, Menu menu);
    
    public List<Menu> selectAllList(Map<String, Object> map);
    
    public List<MenuNode> getMenuTree();
    
    public List<TreeNode> getTreeData();
}
