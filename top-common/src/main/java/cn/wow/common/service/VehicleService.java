package cn.wow.common.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import cn.wow.common.domain.Vehicle;

public interface VehicleService {
    public Vehicle selectOne(Long id);

    public int save(String userName, Vehicle vehicle);

    public int update(String userName, Vehicle vehicle);

    public int deleteByPrimaryKey(String userName, Vehicle vehicle);

    public List<Vehicle> selectAllList(Map<String, Object> map);
    
    public Vehicle selectByCode(String code);
    
    /**
     * 检查整车信息是否存在
     */
    public boolean isExist(Long id, String code, String type, Date proTime, String proAddr, String remark);
}
