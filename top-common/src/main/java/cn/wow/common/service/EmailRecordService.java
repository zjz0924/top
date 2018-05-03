package cn.wow.common.service;

import java.util.List;
import java.util.Map;
import cn.wow.common.domain.EmailRecord;

public interface EmailRecordService {
    public EmailRecord selectOne(Long id);

    public int save(String userName, EmailRecord emailRecord);

    public int update(String userName, EmailRecord emailRecord);

    public int deleteByPrimaryKey(String userName, EmailRecord emailRecord);

    public List<EmailRecord> selectAllList(Map<String, Object> map);

}
