package cn.wow.common.dao;

import java.util.List;
import java.util.Map;

import cn.wow.common.domain.Account;

public interface AccountDao extends SqlDao{
    int deleteByPrimaryKey(Long id);
    
    Account selectByAccountName(String userName);
    
    public void batchAdd(List<Account> list);
	
	public void batchUpdate(List<Account> list);
	
	public void clearPic(Long id);
	
	public void clearOrg(Long id);
	
	/**
	 * 获取操作的用户（任务阶段）
	 */
	public List<Account> getOperationUser(Map<String, Object> map);
}