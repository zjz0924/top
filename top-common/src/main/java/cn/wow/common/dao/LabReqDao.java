package cn.wow.common.dao;

import java.util.List;
import cn.wow.common.domain.LabReq;

public interface LabReqDao extends SqlDao{
 
	public void batchAdd(List<LabReq> list);
	
	public int deleteByTaskId(Long id);
}