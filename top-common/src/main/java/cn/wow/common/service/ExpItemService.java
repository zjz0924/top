package cn.wow.common.service;

import java.util.List;
import java.util.Map;
import cn.wow.common.domain.ExpItem;

public interface ExpItemService {
    public ExpItem selectOne(Long id);

    public int save(String userName, ExpItem expItem);

    public int update(String userName, ExpItem expItem);

    public int deleteByPrimaryKey(String userName, ExpItem expItem);

    public List<ExpItem> selectAllList(Map<String, Object> map);

}
