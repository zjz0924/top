package cn.wow.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wow.common.dao.ExamineRecordDao;
import cn.wow.common.domain.ExamineRecord;
import cn.wow.common.service.ExamineRecordService;
import cn.wow.common.utils.pagination.PageHelperExt;

@Service
@Transactional
public class ExamineRecordServiceImpl implements ExamineRecordService{

    private static Logger logger = LoggerFactory.getLogger(ExamineRecordServiceImpl.class);

    @Autowired
    private ExamineRecordDao examineRecordDao;

    public ExamineRecord selectOne(Long id){
    	return examineRecordDao.selectOne(id);
    }

    public int save(String userName, ExamineRecord examineRecord){
    	return examineRecordDao.insert(examineRecord);
    }

    public int update(String userName, ExamineRecord examineRecord){
    	return examineRecordDao.update(examineRecord);
    }

    public int deleteByPrimaryKey(String userName, ExamineRecord examineRecord){
    	return examineRecordDao.deleteByPrimaryKey(examineRecord.getId());
    }

    public List<ExamineRecord> selectAllList(Map<String, Object> map){
    	PageHelperExt.startPage(map);
    	return examineRecordDao.selectAllList(map);
    }
    
	public List<Long> selectTaskIdList(Long aId, int type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("aId", aId);
		map.put("type", type);

		return examineRecordDao.selectTaskIdList(map);
	}

}
