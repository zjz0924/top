package cn.wow.common.service;

import java.util.List;
import java.util.Map;
import cn.wow.common.domain.LabReq;

public interface LabReqService {
    public LabReq selectOne(Long id);

    public int save(String userName, LabReq labReq);

    public int update(String userName, LabReq labReq);

    public int deleteByPrimaryKey(String userName, LabReq labReq);

    public List<LabReq> selectAllList(Map<String, Object> map);
    
    public void batchAdd(List<LabReq> list);
    
    public List<LabReq> getLabReqListByTaskId(Long taskId);
}
