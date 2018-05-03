package cn.wow.common.dao;

import cn.wow.common.domain.Material;

public interface MaterialDao extends SqlDao{
    
	Material selectByCode(String code);
}