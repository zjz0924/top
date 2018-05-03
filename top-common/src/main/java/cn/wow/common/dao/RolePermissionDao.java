package cn.wow.common.dao;

import java.util.List;
import java.util.Set;

import cn.wow.common.domain.RolePermission;

public interface RolePermissionDao extends SqlDao{

	public List<RolePermission> batchQuery(List<Long> list);
}