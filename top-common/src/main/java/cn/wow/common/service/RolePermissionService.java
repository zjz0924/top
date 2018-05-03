package cn.wow.common.service;

import java.util.List;
import java.util.Set;

import cn.wow.common.domain.RolePermission;

public interface RolePermissionService {
    public RolePermission selectOne(Long id);

    public int save(RolePermission rolePermisson);

    public int update(RolePermission rolePermisson);

    public int deleteByPrimaryKey(Long id);

    public List<RolePermission> batchQuery(List<Long> list);
}
