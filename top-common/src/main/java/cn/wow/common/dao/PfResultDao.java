package cn.wow.common.dao;

import java.util.List;
import java.util.Map;

import cn.wow.common.domain.PfResult;

public interface PfResultDao extends SqlDao {
	
	public void batchAdd(List<PfResult> list);
	
	public int getExpNoByCatagory(Map<String, Object> map);
}