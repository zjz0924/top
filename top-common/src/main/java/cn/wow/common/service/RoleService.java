package cn.wow.common.service;

import java.util.List;
import java.util.Map;
import cn.wow.common.domain.Role;
import cn.wow.common.domain.RolePermission;

public interface RoleService {
    public Role selectOne(Long id);

    public int save(String userName, Role role);

    public int update(String userName, Role role);

    public int deleteByPrimaryKey(String userName, Role role);

    public List<Role> selectAllList(Map<String, Object> map);
    
    public void createRole(String userName, Role role, RolePermission rolePermission);
    
    public void updateRole(String userName, Role role, RolePermission rolePermission);
    
    public void deleteRole(String userName, Role role);
    
    public Role selectByCode(String code);
    
    public int move(Role role);
    
    public List<Role> selectRoles(Long[] ids);
}
