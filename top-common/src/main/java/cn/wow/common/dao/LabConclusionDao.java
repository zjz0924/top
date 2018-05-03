package cn.wow.common.dao;

import java.util.List;
import cn.wow.common.domain.LabConclusion;

public interface LabConclusionDao extends SqlDao{
	
	public void batchAdd(List<LabConclusion> list);

}