package cn.wow.common.dao;

import java.util.List;
import java.util.Map;

import cn.wow.common.domain.ExamineRecord;

public interface ExamineRecordDao extends SqlDao {

	public void batchAdd(List<ExamineRecord> list);
	
	public List<Long> selectTaskIdList(Map<String, Object> map);
}