package cn.wow.common.service;

import java.util.List;
import java.util.Map;
import cn.wow.common.domain.Dictionary;

public interface DictionaryService {
    public Dictionary selectOne(Long id);

    public int save(String userName, Dictionary dictionary);

    public int update(String userName, Dictionary dictionary);

    public int deleteByPrimaryKey(String userName, Dictionary dictionary);

    public List<Dictionary> selectAllList(Map<String, Object> map);
    
    public Dictionary selectByName(String name);

}
