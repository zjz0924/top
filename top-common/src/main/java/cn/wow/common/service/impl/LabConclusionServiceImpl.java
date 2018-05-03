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
import cn.wow.common.dao.LabConclusionDao;
import cn.wow.common.domain.LabConclusion;
import cn.wow.common.service.LabConclusionService;

@Service
@Transactional
public class LabConclusionServiceImpl implements LabConclusionService{

    private static Logger logger = LoggerFactory.getLogger(LabConclusionServiceImpl.class);

    @Autowired
    private LabConclusionDao labConclusionDao;

    public LabConclusion selectOne(Long id){
    	return labConclusionDao.selectOne(id);
    }

    public int save(String userName, LabConclusion labConclusion){
    	return labConclusionDao.insert(labConclusion);
    }

    public int update(String userName, LabConclusion labConclusion){
    	return labConclusionDao.update(labConclusion);
    }

    public int deleteByPrimaryKey(String userName, LabConclusion labConclusion){
    	return labConclusionDao.deleteByPrimaryKey(labConclusion.getId());
    }

    public List<LabConclusion> selectAllList(Map<String, Object> map){
    	PageHelperExt.startPage(map);
    	return labConclusionDao.selectAllList(map);
    }
    
    public void batchAdd(List<LabConclusion> conclusionDataList) {
    	labConclusionDao.batchAdd(conclusionDataList);
    }
    
	public List<LabConclusion> selectByTaskId(Long taskId) {
		Map<String, Object> map = new PageMap(false);
		map.put("taskId", taskId);
		return labConclusionDao.selectAllList(map);
	}

}
