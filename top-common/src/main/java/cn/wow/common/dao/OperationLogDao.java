package cn.wow.common.dao;

import java.util.List;
import java.util.Map;
import cn.wow.common.domain.OperationLog;

public interface OperationLogDao {

	public OperationLog selectOne(Long id);
	
	public Long insert(OperationLog operationLog);

	public List<OperationLog> selectAllList(Map<String, Object> map);

}