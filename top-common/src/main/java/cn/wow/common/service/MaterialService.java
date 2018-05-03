package cn.wow.common.service;

import java.util.List;
import java.util.Map;
import cn.wow.common.domain.Material;
import cn.wow.common.domain.Parts;

public interface MaterialService {
	
    public Material selectOne(Long id);

    public int save(String userName, Material material);

    public int update(String userName, Material material);

    public int deleteByPrimaryKey(String userName, Material material);

    public List<Material> selectAllList(Map<String, Object> map);
    
    public Material selectByNameAndType(String matName, Integer type);

}
