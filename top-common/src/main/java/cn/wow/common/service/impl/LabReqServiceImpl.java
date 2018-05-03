package cn.wow.common.service.impl;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.wow.common.utils.pagination.PageHelperExt;
import cn.wow.common.utils.pagination.PageMap;
import cn.wow.common.dao.LabReqDao;
import cn.wow.common.domain.LabReq;
import cn.wow.common.service.LabReqService;

@Service
@Transactional
public class LabReqServiceImpl implements LabReqService{

    private static Logger logger = LoggerFactory.getLogger(LabReqServiceImpl.class);

    @Autowired
    private LabReqDao labReqDao;

    public LabReq selectOne(Long id){
    	return labReqDao.selectOne(id);
    }

    public int save(String userName, LabReq labReq){
    	return labReqDao.insert(labReq);
    }

    public int update(String userName, LabReq labReq){
    	return labReqDao.update(labReq);
    }

    public int deleteByPrimaryKey(String userName, LabReq labReq){
    	return labReqDao.deleteByPrimaryKey(labReq.getId());
    }

    public List<LabReq> selectAllList(Map<String, Object> map){
    	PageHelperExt.startPage(map);
    	return labReqDao.selectAllList(map);
    }
    
    public void batchAdd(List<LabReq> list) {
    	labReqDao.batchAdd(list);
    }
    
    public List<LabReq> getLabReqListByTaskId(Long taskId){
    	Map<String, Object> lMap = new PageMap(false);
		lMap.put("taskId", taskId);
		lMap.put("custom_order_sql", " type asc");
		
		return this.selectAllList(lMap);
    }

}
