package cn.wow.common.service.impl;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.wow.common.utils.pagination.PageHelperExt;
import cn.wow.common.dao.ExpItemDao;
import cn.wow.common.domain.ExpItem;
import cn.wow.common.service.ExpItemService;

@Service
@Transactional
public class ExpItemServiceImpl implements ExpItemService{

    private static Logger logger = LoggerFactory.getLogger(ExpItemServiceImpl.class);

    @Autowired
    private ExpItemDao expItemDao;

    public ExpItem selectOne(Long id){
    	return expItemDao.selectOne(id);
    }

    public int save(String userName, ExpItem expItem){
    	return expItemDao.insert(expItem);
    }

    public int update(String userName, ExpItem expItem){
    	return expItemDao.update(expItem);
    }

    public int deleteByPrimaryKey(String userName, ExpItem expItem){
    	return expItemDao.deleteByPrimaryKey(expItem.getId());
    }

    public List<ExpItem> selectAllList(Map<String, Object> map){
    	PageHelperExt.startPage(map);
    	return expItemDao.selectAllList(map);
    }

}
