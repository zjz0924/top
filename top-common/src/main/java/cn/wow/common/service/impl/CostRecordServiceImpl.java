package cn.wow.common.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wow.common.dao.AccountDao;
import cn.wow.common.dao.CostRecordDao;
import cn.wow.common.dao.ExpItemDao;
import cn.wow.common.domain.Account;
import cn.wow.common.domain.CostRecord;
import cn.wow.common.domain.EmailRecord;
import cn.wow.common.domain.ExpItem;
import cn.wow.common.service.CostRecordService;
import cn.wow.common.service.OperationLogService;
import cn.wow.common.service.TaskService;
import cn.wow.common.utils.operationlog.OperationType;
import cn.wow.common.utils.operationlog.ServiceType;
import cn.wow.common.utils.pagination.PageHelperExt;
import cn.wow.common.utils.pagination.PageMap;

@Service
@Transactional
public class CostRecordServiceImpl implements CostRecordService{

    private static Logger logger = LoggerFactory.getLogger(CostRecordServiceImpl.class);

    @Autowired
    private CostRecordDao costRecordDao;
    @Autowired
    private ExpItemDao expItemDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private TaskService taskService;
    @Autowired
	private OperationLogService operationLogService;

    public CostRecord selectOne(Long id){
    	return costRecordDao.selectOne(id);
    }

    public int save(String userName, CostRecord costRecord){
    	return costRecordDao.insert(costRecord);
    }

    public int update(String userName, CostRecord costRecord){
    	return costRecordDao.update(costRecord);
    }

    public int deleteByPrimaryKey(String userName, CostRecord costRecord){
    	return costRecordDao.deleteByPrimaryKey(costRecord.getId());
    }

    public List<CostRecord> selectAllList(Map<String, Object> map){
    	PageHelperExt.startPage(map);
    	return costRecordDao.selectAllList(map);
    }
    
    
    /**
     * 费用清单发送
     * @param account  
     * @param costId   清单ID
     * @param orgs	        发送机构
     * @param list     试验项目
     */
	public void costSend(Account account, Long costId, String orgs, List<ExpItem> itemList) throws Exception{
		// 添加试验项目
		expItemDao.batchAdd(itemList);
		Date date = new Date();
		
		// 修改清单信息
		CostRecord costRecord = costRecordDao.selectOne(costId);
		costRecord.setaId(account.getId());
		costRecord.setOrgs(orgs);
		costRecord.setSendTime(date);
		costRecord.setState(1);
		costRecordDao.update(costRecord);
		
		// 发送邮件
		taskService.sendCost(account, orgs, costRecord, itemList);
		
		// 操作日志
		String lab = "";
		if(costRecord.getLabType() == 1) {
			lab = "零部件图谱试验费用单";
		}else if(costRecord.getLabType() == 2) {
			lab = "零部件型式试验费用单";
		}else if(costRecord.getLabType() == 3) {
			lab = "原材料图谱试验费用单";
		}else {
			lab = "原材料型式试验费用单";
		}
		String logDetail = "任务：" + costRecord.getTask().getCode() + "," + lab;
		addLog(account.getUserName(), OperationType.SEND_COST, ServiceType.COST, logDetail);
	}

	
	/**
	 *  添加日志
	 */
	void addLog(String userName, OperationType operationType, ServiceType serviceType, String logDetail) {
		operationLogService.save(userName, operationType, serviceType, logDetail);
	}
}
