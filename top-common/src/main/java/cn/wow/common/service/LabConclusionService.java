package cn.wow.common.service;

import java.util.List;
import java.util.Map;
import cn.wow.common.domain.LabConclusion;

public interface LabConclusionService {
    public LabConclusion selectOne(Long id);

    public int save(String userName, LabConclusion labConclusion);

    public int update(String userName, LabConclusion labConclusion);

    public int deleteByPrimaryKey(String userName, LabConclusion labConclusion);

    public List<LabConclusion> selectAllList(Map<String, Object> map);
    
    public void batchAdd(List<LabConclusion> conclusionDataList);

    public List<LabConclusion> selectByTaskId(Long taskId);
}
