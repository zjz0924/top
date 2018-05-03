package cn.wow.common.dao;

import java.util.Map;

import cn.wow.common.domain.Org;

public interface OrgDao extends SqlDao{
    
	public Org getByCode(String code);
	
	public void batchUpdate(Map<String, Object> map);
	
}