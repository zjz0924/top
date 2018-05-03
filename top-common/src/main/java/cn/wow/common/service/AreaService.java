package cn.wow.common.service;

import java.util.List;
import java.util.Map;
import cn.wow.common.domain.Area;
import cn.wow.common.domain.TreeNode;

public interface AreaService {
    public Area selectOne(Long id);

    public int save(String userName, Area area);

    public int update(String userName, Area area);

    public int deleteByPrimaryKey(String userName, Area area);

    public List<Area> selectAllList(Map<String, Object> map);
    
    public List<TreeNode> getAreaTree(String svalue, String stype);
    
    public int move(Area area);
    
    public Area getAreaByCode(String code);
    
}
