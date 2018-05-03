package cn.wow.common.service;

import java.util.List;
import java.util.Map;

import cn.wow.common.domain.OperationLog;
import cn.wow.common.utils.operationlog.ClientInfo;
import cn.wow.common.utils.operationlog.OperationType;
import cn.wow.common.utils.operationlog.ServiceType;

public interface OperationLogService {
	
	public OperationLog selectOne(Long id);

	public Long save(String theUserName, OperationType operationType, ServiceType serviceType, String jsonDetail);

	/**
	 * Create or update user client information.If the userName already
	 * exists,then just update the client information record,otherwise create a new one.
	 */
	public void createOrUpdateUserClientInfo(String userName, ClientInfo clientInfo);

	/**
	 * Retrieve the client information by the userName.
	 * 
	 * @param userName
	 * @return
	 */
	public ClientInfo getClientInfoByUserName(String userName);

	/**
	 * Search OperationLog by user name and start time information. If all
	 * parameters are empty or null, then return all OperationLogs.
	 * 
	 * @param userName
	 * @param startTimeFrom
	 * @param startTimeTo
	 * @param detail detail keyword
	 * @return
	 */
	public List<OperationLog> selectAllList(Map<String, Object> map);

}
