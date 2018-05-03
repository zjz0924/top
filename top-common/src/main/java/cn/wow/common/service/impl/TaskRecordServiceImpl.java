package cn.wow.common.service.impl;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.wow.common.utils.pagination.PageHelperExt;
import cn.wow.common.dao.TaskRecordDao;
import cn.wow.common.domain.TaskRecord;
import cn.wow.common.service.TaskRecordService;

@Service
@Transactional
public class TaskRecordServiceImpl implements TaskRecordService{

    private static Logger logger = LoggerFactory.getLogger(TaskRecordServiceImpl.class);

    @Autowired
    private TaskRecordDao taskRecordDao;

    public TaskRecord selectOne(Long id){
    	return taskRecordDao.selectOne(id);
    }

    public int save(String userName, TaskRecord taskRecord){
    	return taskRecordDao.insert(taskRecord);
    }

    public int update(String userName, TaskRecord taskRecord){
    	return taskRecordDao.update(taskRecord);
    }

    public int deleteByPrimaryKey(String userName, TaskRecord taskRecord){
    	return taskRecordDao.deleteByPrimaryKey(taskRecord.getaId());
    }

    public List<TaskRecord> selectAllList(Map<String, Object> map){
    	PageHelperExt.startPage(map);
    	return taskRecordDao.selectAllList(map);
    }

    public List<Long> selectTaskIdList(Long id){
    	return taskRecordDao.selectTaskIdList(id);
    }
}
