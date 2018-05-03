package cn.wow.common.service;


import java.util.List;
import java.util.Map;
import cn.wow.common.domain.Account;

public interface AccountService {
	
	public Account selectOne(Long id);
	
	public int save(String userName, Account account);
	
	public int update(String userName, Account account);
	
	public int deleteByPrimaryKey(String userName, Account account);
	
	public Account selectByAccountName(String userName);
	
	public List<Account> selectAllList(Map<String, Object> map);
	
	public void batchAdd(List<Account> list);
	
	public void batchUpdate(List<Account> list);
	
	public void clearPic(Long id);
	
	/**
	 * 获取操作的用户（任务阶段）
	 * @param taskId  任务ID
	 * @param state   任务记录状态
	 */
	public List<Account> getOperationUser(Long taskId, Integer state);
}
