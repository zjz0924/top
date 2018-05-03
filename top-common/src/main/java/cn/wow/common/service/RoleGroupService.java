package cn.wow.common.service;

import java.util.List;
import java.util.Map;

import cn.wow.common.domain.Role;
import cn.wow.common.domain.RoleGroup;
import cn.wow.common.domain.TreeNode;

public interface RoleGroupService {
    public RoleGroup selectOne(Long id);

    public int save(String userName, RoleGroup roleGroup);

    public int update(String userName, RoleGroup roleGroup);

    public int deleteByPrimaryKey(String userName, RoleGroup roleGroup);

    public List<RoleGroup> selectAllList(Map<String, Object> map);

    public List<TreeNode> getTree(String svalue, String stype);
    
    public int move(RoleGroup roleGroup);
}
