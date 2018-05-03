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
import cn.wow.common.dao.MaterialDao;
import cn.wow.common.domain.Material;
import cn.wow.common.service.MaterialService;

@Service
@Transactional
public class MaterialServiceImpl implements MaterialService{

    private static Logger logger = LoggerFactory.getLogger(MaterialServiceImpl.class);

    @Autowired
    private MaterialDao materialDao;

    public Material selectOne(Long id){
    	return materialDao.selectOne(id);
    }

    public int save(String userName, Material material){
    	return materialDao.insert(material);
    }

    public int update(String userName, Material material){
    	return materialDao.update(material);
    }

    public int deleteByPrimaryKey(String userName, Material material){
    	return materialDao.deleteByPrimaryKey(material.getId());
    }

    public List<Material> selectAllList(Map<String, Object> map){
    	PageHelperExt.startPage(map);
    	return materialDao.selectAllList(map);
    }
    
    public Material selectByNameAndType(String matName, Integer type){
    	Map<String, Object> map = new PageMap(false);
		map.put("matName", matName);
		map.put("type", type);

		List<Material> list = materialDao.selectAllList(map);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
    }

}
