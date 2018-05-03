package cn.wow.common.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wow.common.dao.PfResultDao;
import cn.wow.common.dao.TaskDao;
import cn.wow.common.dao.TaskRecordDao;
import cn.wow.common.domain.Account;
import cn.wow.common.domain.LabConclusion;
import cn.wow.common.domain.PfResult;
import cn.wow.common.domain.Task;
import cn.wow.common.domain.TaskRecord;
import cn.wow.common.service.LabConclusionService;
import cn.wow.common.service.OperationLogService;
import cn.wow.common.service.PfResultService;
import cn.wow.common.utils.operationlog.OperationType;
import cn.wow.common.utils.operationlog.ServiceType;
import cn.wow.common.utils.pagination.PageHelperExt;
import cn.wow.common.utils.taskState.SamplingTaskRecordEnum;
import cn.wow.common.utils.taskState.StandardTaskRecordEnum;
import cn.wow.common.utils.taskState.TaskTypeEnum;

@Service
@Transactional
public class PfResultServiceImpl implements PfResultService{

    private static Logger logger = LoggerFactory.getLogger(PfResultServiceImpl.class);

    @Autowired
    private PfResultDao pfResultDao;
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private TaskRecordDao taskRecordDao;
    @Autowired
	private OperationLogService operationLogService;
    @Autowired
    private LabConclusionService labConclusionService;

    public PfResult selectOne(Long id){
    	return pfResultDao.selectOne(id);
    }

    public int save(String userName, PfResult pfResult){
    	return pfResultDao.insert(pfResult);
    }

    public int update(String userName, PfResult pfResult){
    	return pfResultDao.update(pfResult);
    }

    public int deleteByPrimaryKey(String userName, PfResult pfResult){
    	return pfResultDao.deleteByPrimaryKey(pfResult.getId());
    }

    public List<PfResult> selectAllList(Map<String, Object> map){
    	PageHelperExt.startPage(map);
    	return pfResultDao.selectAllList(map);
    }
    
    // 获取试验次数
    public int getExpNoByCatagory(Long taskId, int catagory){
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("taskId", taskId);
    	map.put("catagory", catagory);
    	return pfResultDao.getExpNoByCatagory(map);
    }
    
    public void batchAdd(List<PfResult> list){
    	pfResultDao.batchAdd(list);
    }
    
    
    /**
     * 性能结果上传
     * @param account
     * @param dataList   试验结果
     * @param conclusionDataList  结论结果
     */
    public void upload(Account account, List<PfResult> dataList, Long taskId, List<LabConclusion> conclusionDataList){
    	Task task = taskDao.selectOne(taskId);
    	String remark = "上传零部件型式试验和原材料型式试验结果";
    	
		// 批量添加
    	batchAdd(dataList);
    	
    	if(conclusionDataList != null && conclusionDataList.size() > 0) {
    		labConclusionService.batchAdd(conclusionDataList);
    	}
    	
    	List<Integer> catagory = getCatagory(dataList);
    	if(catagory.size() == 2){
    		// 结果
    		task.setPartsPatResult(2);
    		task.setMatPatResult(2);
    		
    		// 实验次数
    		task.setPartsPatTimes(task.getPartsPatTimes() + 1);
			task.setMatPatTimes(task.getMatPatTimes() + 1);
    	}else{
    		if(catagory.get(0) == 1){
				task.setPartsPatResult(2);
				task.setPartsPatTimes(task.getPartsPatTimes() + 1);
				remark = "上传零部件型式试验结果";
			}else{
				task.setMatPatResult(2);
				task.setMatPatTimes(task.getMatPatTimes() + 1);
				remark = "上传原材料型式试验结果";
			}
    	}

		// 操作记录
		TaskRecord record = new TaskRecord();
		record.setCreateTime(dataList.get(0).getCreateTime());
		record.setCode(task.getCode());
		if(task.getType() == TaskTypeEnum.OTS.getState() || task.getType() == TaskTypeEnum.GS.getState()) {
			record.setState(StandardTaskRecordEnum.UPLOAD.getState());
		}else if(task.getType() == TaskTypeEnum.PPAP.getState() || task.getType() == TaskTypeEnum.SOP.getState()) {
			record.setState(SamplingTaskRecordEnum.UPLOAD.getState());
		}
		record.setaId(account.getId());
		record.setRemark(remark);
		record.setTaskType(task.getType());
		taskRecordDao.insert(record);

		taskDao.update(task);
		
		String logDetail =  remark + "，任务号：" + task.getCode();
		addLog(account.getUserName(), OperationType.UPLOAD_PF, ServiceType.LAB, logDetail);
    }
    
	// 获取性能结果是哪种结果（零部件、原材料、全部）
	List<Integer> getCatagory(List<PfResult> dataList) {
		Set<Integer> catagory = new HashSet<Integer>();
		for (PfResult pfResult : dataList) {
			catagory.add(pfResult.getCatagory());
		}

		List<Integer> list = new ArrayList<Integer>();
		list.addAll(catagory);

		return list;
	}

	
	/**
	 * 组装型式结果
	 * @param pfDataList  当前任务所有的型式结果记录
	 * @param pPfResult   零部件的型式结果记录
	 * @param mPfResult   原材料的型式结果记录
	 */
	public void assemblePfResult(List<PfResult> pfDataList, Map<Integer, List<PfResult>> pPfResult,
			Map<Integer, List<PfResult>> mPfResult) {

		if (pfDataList != null && pfDataList.size() > 0) {
			for (PfResult pf : pfDataList) {
				if (pf.getCatagory() == 1) { // 零部件
					List<PfResult> list = pPfResult.get(pf.getExpNo());
					if (list != null) {
						list.add(pf);
					} else {
						list = new ArrayList<PfResult>();
						list.add(pf);
					}
					pPfResult.put(pf.getExpNo(), list);
				} else { // 原材料
					List<PfResult> list = mPfResult.get(pf.getExpNo());
					if (list != null) {
						list.add(pf);
					} else {
						list = new ArrayList<PfResult>();
						list.add(pf);
					}
					mPfResult.put(pf.getExpNo(), list);
				}
			}
		}
	}
	
	
	/**
	 * 获取最后一次试验结果
	 * @param type     类型（1-零部件，2-原材料）
	 * @param taskId   任务ID
	 */
	public List<PfResult> getLastResult(int type, Long taskId) {
		Map<String, Object> pfMap = new HashMap<String, Object>();
		pfMap.put("tId", taskId);
		pfMap.put("expNo", this.getExpNoByCatagory(taskId, type));
		pfMap.put("catagory", type);

		return this.selectAllList(pfMap);
	}
	
	
	/**
	 *  添加日志
	 */
	void addLog(String userName, OperationType operationType, ServiceType serviceType, String logDetail) {
		operationLogService.save(userName, operationType, serviceType, logDetail);
	}
}
