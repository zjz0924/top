package cn.wow.common.service.impl;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.wow.common.utils.pagination.PageHelperExt;
import cn.wow.common.dao.EmailRecordDao;
import cn.wow.common.domain.EmailRecord;
import cn.wow.common.service.EmailRecordService;

@Service
@Transactional
public class EmailRecordServiceImpl implements EmailRecordService{

    private static Logger logger = LoggerFactory.getLogger(EmailRecordServiceImpl.class);

    @Autowired
    private EmailRecordDao emailRecordDao;

    public EmailRecord selectOne(Long id){
    	return emailRecordDao.selectOne(id);
    }

    public int save(String userName, EmailRecord emailRecord){
    	return emailRecordDao.insert(emailRecord);
    }

    public int update(String userName, EmailRecord emailRecord){
    	return emailRecordDao.update(emailRecord);
    }

    public int deleteByPrimaryKey(String userName, EmailRecord emailRecord){
    	return emailRecordDao.deleteByPrimaryKey(emailRecord.getId());
    }

    public List<EmailRecord> selectAllList(Map<String, Object> map){
    	PageHelperExt.startPage(map);
    	return emailRecordDao.selectAllList(map);
    }

}
