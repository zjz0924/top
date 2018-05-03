package cn.wow.common.dao;

import java.util.List;
import java.util.Map;

import cn.wow.common.domain.Task;

public interface TaskDao extends SqlDao{
   
	public void updateState(Map<String, Object> map);
	
	public List<Task> batchQueryByInfoId(List<Long> list);
	
	/**
	 * 获取任务数
	 */
	public Integer getTaskNum(Map<String, Object> map);
}