package cn.wow.common.dao;

import java.util.List;
import java.util.Map;

import cn.wow.common.domain.AtlasResult;

public interface AtlasResultDao extends SqlDao{
	
	public void batchAdd(List<AtlasResult> list);
	
	public int getExpNoByCatagory(Map<String, Object> map);
	
	/**
     * 获取基准图谱结果
     */
	public List<AtlasResult> getStandardAtlResult(Map<String, Object> map);
	
}