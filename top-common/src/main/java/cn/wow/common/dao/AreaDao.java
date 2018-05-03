package cn.wow.common.dao;

import cn.wow.common.domain.Area;

public interface AreaDao extends SqlDao{

	public Area getAreaByCode(String code);
}