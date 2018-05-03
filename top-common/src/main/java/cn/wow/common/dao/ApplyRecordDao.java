package cn.wow.common.dao;

import java.util.List;
import java.util.Map;
import cn.wow.common.domain.ApplyRecord;

public interface ApplyRecordDao extends SqlDao{

	public List<ApplyRecord> getRecordByTaskId(Map<String, Object> map);
}