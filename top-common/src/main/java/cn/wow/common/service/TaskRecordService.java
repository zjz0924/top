package cn.wow.common.service;

import java.util.List;
import java.util.Map;
import cn.wow.common.domain.TaskRecord;

public interface TaskRecordService {
    public TaskRecord selectOne(Long id);

    public int save(String userName, TaskRecord taskRecord);

    public int update(String userName, TaskRecord taskRecord);

    public int deleteByPrimaryKey(String userName, TaskRecord taskRecord);

    public List<TaskRecord> selectAllList(Map<String, Object> map);
    
    public List<Long> selectTaskIdList(Long id);

}
