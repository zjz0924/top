package cn.wow.common.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.wow.common.utils.pagination.PageHelperExt;
import cn.wow.common.utils.pagination.PageMap;
import cn.wow.common.dao.PartsDao;
import cn.wow.common.domain.Parts;
import cn.wow.common.domain.Vehicle;
import cn.wow.common.service.PartsService;

@Service
@Transactional
public class PartsServiceImpl implements PartsService{

    private static Logger logger = LoggerFactory.getLogger(PartsServiceImpl.class);

    @Autowired
    private PartsDao partsDao;

    public Parts selectOne(Long id){
    	return partsDao.selectOne(id);
    }
    
    public Parts selectByCode(String code){
    	return partsDao.selectByCode(code);
    }

    public int save(String userName, Parts parts){
    	return partsDao.insert(parts);
    }

    public int update(String userName, Parts parts){
    	return partsDao.update(parts);
    }

    public int deleteByPrimaryKey(String userName, Parts parts){
    	return partsDao.deleteByPrimaryKey(parts.getId());
    }

    public List<Parts> selectAllList(Map<String, Object> map){
    	PageHelperExt.startPage(map);
    	return partsDao.selectAllList(map);
    }

    
	public Parts selectByCodeAndType(String code, Integer type) {
		Map<String, Object> map = new PageMap(false);
		map.put("code", code);
		map.put("type", type);

		List<Parts> list = partsDao.selectAllList(map);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	/**
     * 检查零部件信息是否存在
     */
	public boolean isExist(Long id, String p_code, String p_name, Date p_proTime, String p_place, String p_proNo,
			String p_keyCode, Integer p_isKey, Long p_orgId, String p_remark, String p_contacts,
			String p_phone) {
		Map<String, Object> map = new PageMap(false);
		if(id != null) {
			map.put("eid", id);
		}
		map.put("code", p_code);
		map.put("ename", p_name);
		map.put("orgId", p_orgId);
		map.put("eproNo", p_proNo);
		map.put("eProTime", p_proTime);
		map.put("place", p_place);
		map.put("isKey", p_isKey);
		map.put("ekeyCode", p_keyCode);
		map.put("econtacts", p_contacts);
		map.put("ephone", p_phone);
		map.put("remark", p_remark);

		List<Parts> dataList = partsDao.selectAllList(map);
		if (dataList != null && dataList.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
}
