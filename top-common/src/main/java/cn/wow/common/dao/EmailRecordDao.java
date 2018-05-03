package cn.wow.common.dao;

import java.util.List;

import cn.wow.common.domain.EmailRecord;

public interface EmailRecordDao extends SqlDao{

	public void batchAdd(List<EmailRecord> list);
}