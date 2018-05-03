package cn.wow.common.dao;

import cn.wow.common.domain.Dictionary;

public interface DictionaryDao extends SqlDao{
	
	public Dictionary selectByName(String name);
}