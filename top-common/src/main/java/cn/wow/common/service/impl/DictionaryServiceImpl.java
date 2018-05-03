package cn.wow.common.service.impl;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.wow.common.utils.pagination.PageHelperExt;
import cn.wow.common.dao.DictionaryDao;
import cn.wow.common.domain.Dictionary;
import cn.wow.common.service.DictionaryService;

@Service
@Transactional
public class DictionaryServiceImpl implements DictionaryService{

    private static Logger logger = LoggerFactory.getLogger(DictionaryServiceImpl.class);

    @Autowired
    private DictionaryDao dictionaryDao;

    public Dictionary selectOne(Long id){
    	return dictionaryDao.selectOne(id);
    }

    public int save(String userName, Dictionary dictionary){
    	return dictionaryDao.insert(dictionary);
    }

    public int update(String userName, Dictionary dictionary){
    	return dictionaryDao.update(dictionary);
    }

    public int deleteByPrimaryKey(String userName, Dictionary dictionary){
    	return dictionaryDao.deleteByPrimaryKey(dictionary.getId());
    }

    public List<Dictionary> selectAllList(Map<String, Object> map){
    	PageHelperExt.startPage(map);
    	return dictionaryDao.selectAllList(map);
    }

    public Dictionary selectByName(String name){
    	return dictionaryDao.selectByName(name);
    }
}
