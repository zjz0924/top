package cn.wow.common.dao;

import java.util.List;
import cn.wow.common.domain.Role;

public interface RoleDao extends SqlDao{

	public List<Role> selectByGrid(Long id);
	
	public List<Role> selectRoles(Long[] ids);
	
	public Role selectByCode(String code);
	
}