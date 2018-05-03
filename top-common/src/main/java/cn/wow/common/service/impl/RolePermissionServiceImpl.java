package cn.wow.common.service.impl;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.wow.common.dao.RolePermissionDao;
import cn.wow.common.domain.RolePermission;
import cn.wow.common.service.RolePermissionService;

@Service
@Transactional
public class RolePermissionServiceImpl implements RolePermissionService{

    private static Logger logger = LoggerFactory.getLogger(RolePermissionServiceImpl.class);

    @Autowired
    private RolePermissionDao rolePermissonDao;

    public RolePermission selectOne(Long id){
    	return rolePermissonDao.selectOne(id);
    }

    public int save(RolePermission rolePermisson){
    	deleteByPrimaryKey(rolePermisson.getRoleId());
    	return rolePermissonDao.insert(rolePermisson);
    }

    public int update(RolePermission rolePermisson){
    	return rolePermissonDao.update(rolePermisson);
    }

    public int deleteByPrimaryKey(Long id){
    	return rolePermissonDao.deleteByPrimaryKey(id);
    }

    public List<RolePermission> batchQuery(List<Long> list){
    	return rolePermissonDao.batchQuery(list);
    }
    
}
