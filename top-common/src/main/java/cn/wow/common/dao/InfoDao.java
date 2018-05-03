package cn.wow.common.dao;

import java.util.List;
import java.util.Map;

public interface InfoDao extends SqlDao {

	public List<Long> selectIdList(Map<String, Object> map);
	
}