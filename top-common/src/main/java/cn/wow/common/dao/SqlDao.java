package cn.wow.common.dao;

import java.util.List;

import cn.wow.common.domain.Role;

/**
 * 功能：公用SqlMapper接口<br/>
 * 注意：继承此接口子类的*.Mapper.xml文件里定义的SQL["id"] 要与此接口方法名一致
 * 
 */
public interface SqlDao {
	
	<T> int insert (T bean);
	
	<T> int update(T bean);
	
	<T> int delete(T bean);
	
	<T> T selectOne(Object params);
	
	<T> List<T> selectAllList(Object params);
	
	int deleteByPrimaryKey(Long id);
	
}

