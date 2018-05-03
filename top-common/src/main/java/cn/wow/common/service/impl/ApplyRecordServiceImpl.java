package cn.wow.common.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wow.common.dao.ApplyRecordDao;
import cn.wow.common.dao.TaskDao;
import cn.wow.common.domain.Account;
import cn.wow.common.domain.ApplyRecord;
import cn.wow.common.domain.Task;
import cn.wow.common.service.ApplyRecordService;
import cn.wow.common.service.OperationLogService;
import cn.wow.common.utils.operationlog.OperationType;
import cn.wow.common.utils.operationlog.ServiceType;
import cn.wow.common.utils.pagination.PageHelperExt;
import cn.wow.common.utils.pagination.PageMap;
import cn.wow.common.utils.taskState.SamplingTaskEnum;
import cn.wow.common.utils.taskState.SamplingTaskRecordEnum;
import cn.wow.common.utils.taskState.StandardTaskEnum;
import cn.wow.common.utils.taskState.StandardTaskRecordEnum;
import cn.wow.common.utils.taskState.TaskTypeEnum;

@Service
@Transactional
public class ApplyRecordServiceImpl implements ApplyRecordService{

    private static Logger logger = LoggerFactory.getLogger(ApplyRecordServiceImpl.class);

    @Autowired
    private ApplyRecordDao applyRecordDao;
    @Autowired
    private TaskDao taskDao;
    @Autowired
	private OperationLogService operationLogService;

    public ApplyRecord selectOne(Long id){
    	return applyRecordDao.selectOne(id);
    }

    public int save(String userName, ApplyRecord applyRecord){
    	return applyRecordDao.insert(applyRecord);
    }

    public int update(String userName, ApplyRecord applyRecord){
    	return applyRecordDao.update(applyRecord);
    }

    public int deleteByPrimaryKey(String userName, ApplyRecord applyRecord){
    	return applyRecordDao.deleteByPrimaryKey(applyRecord.getId());
    }

    public List<ApplyRecord> selectAllList(Map<String, Object> map){
    	PageHelperExt.startPage(map);
    	return applyRecordDao.selectAllList(map);
    }

	public ApplyRecord getRecordByTaskId(Long taskId, int type) {
		Map<String, Object> tMap = new PageMap(false);
		tMap.put("taskId", taskId);
		tMap.put("type", type);

		List<ApplyRecord> list = applyRecordDao.getRecordByTaskId(tMap);

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	 /**
     * 中止申请
     * @param account  
     * @param id	      申请记录ID
	 * @param remark  备注
     */
	public void end(Account account, Long id, String remark) {
		ApplyRecord applyRecord = applyRecordDao.selectOne(id);
		Task task = applyRecord.getTask();
		Date date = new Date();

		
		if(applyRecord.getType() == 1){
			task.setInfoApply(0);
		}else{
			// 父任务
			Task pTask = taskDao.selectOne(task.gettId());
			pTask.setResultApply(0);
			if (pTask.getType() == TaskTypeEnum.OTS.getState() || pTask.getType() == TaskTypeEnum.GS.getState()) {
				pTask.setState(StandardTaskEnum.ACCOMPLISH.getState());
			} else if (pTask.getType() == TaskTypeEnum.PPAP.getState() || pTask.getType() == TaskTypeEnum.SOP.getState()) {
				pTask.setState(SamplingTaskEnum.ACCOMPLISH.getState());
			}
			taskDao.update(pTask);
		}
		
		task.setConfirmTime(date);
		task.setRemark(remark);
		task.setResultApply(0);
		if (task.getType() == TaskTypeEnum.OTS.getState() || task.getType() == TaskTypeEnum.GS.getState()) {
			task.setState(StandardTaskEnum.END.getState());
		} else if (task.getType() == TaskTypeEnum.PPAP.getState() || task.getType() == TaskTypeEnum.SOP.getState()) {
			task.setState(SamplingTaskEnum.END.getState());
		}
		taskDao.update(task);
		
		applyRecord.setConfirmTime(date);
		applyRecord.setState(3);
		applyRecord.setRemark(remark);
		applyRecordDao.update(applyRecord);
		
		// 操作日志
    	String logDetail =  "中止申请，任务号：" + task.getCode();
		addLog(account.getUserName(), OperationType.END, ServiceType.APPLY, logDetail);
	}
	
	
	/**
	 *  添加日志
	 */
	void addLog(String userName, OperationType operationType, ServiceType serviceType, String logDetail) {
		operationLogService.save(userName, operationType, serviceType, logDetail);
	}
	
}
