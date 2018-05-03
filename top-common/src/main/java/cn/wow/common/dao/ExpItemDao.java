package cn.wow.common.dao;

import java.util.List;

import cn.wow.common.domain.ExpItem;

public interface ExpItemDao extends SqlDao{

	public void batchAdd(List<ExpItem> list);
}