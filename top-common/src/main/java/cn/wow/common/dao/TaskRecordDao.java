package cn.wow.common.dao;

import java.util.List;

public interface TaskRecordDao extends SqlDao{

	public List<Long> selectTaskIdList(Long id);
}