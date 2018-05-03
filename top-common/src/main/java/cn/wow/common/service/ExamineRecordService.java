package cn.wow.common.service;

import java.util.List;
import java.util.Map;
import cn.wow.common.domain.ExamineRecord;

public interface ExamineRecordService {
    public ExamineRecord selectOne(Long id);

    public int save(String userName, ExamineRecord examineRecord);

    public int update(String userName, ExamineRecord examineRecord);

    public int deleteByPrimaryKey(String userName, ExamineRecord examineRecord);

    public List<ExamineRecord> selectAllList(Map<String, Object> map);

    public List<Long> selectTaskIdList(Long aId, int type);
}
