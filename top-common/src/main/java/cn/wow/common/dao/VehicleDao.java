package cn.wow.common.dao;

import cn.wow.common.domain.Vehicle;

public interface VehicleDao extends SqlDao{
	
	public Vehicle selectByCode(String code);
}