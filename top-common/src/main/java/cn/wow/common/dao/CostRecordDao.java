package cn.wow.common.dao;

import java.util.List;
import cn.wow.common.domain.CostRecord;


public interface CostRecordDao extends SqlDao{
  
	public void batchAdd(List<CostRecord> list);
	
}