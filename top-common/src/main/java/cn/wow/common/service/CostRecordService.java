package cn.wow.common.service;

import java.util.List;
import java.util.Map;

import cn.wow.common.domain.Account;
import cn.wow.common.domain.CostRecord;
import cn.wow.common.domain.ExpItem;

public interface CostRecordService {
    public CostRecord selectOne(Long id);

    public int save(String userName, CostRecord costRecord);

    public int update(String userName, CostRecord costRecord);

    public int deleteByPrimaryKey(String userName, CostRecord costRecord);

    public List<CostRecord> selectAllList(Map<String, Object> map);
    
    /**
     * 费用清单发送
     * @param account  
     * @param costId   清单ID
     * @param orgs	        发送机构
     * @param list     试验项目
     */
    public void costSend(Account account, Long costId, String orgs, List<ExpItem> list) throws Exception;

}
