package cn.wow.common.service;

import java.util.List;
import java.util.Map;
import cn.wow.common.domain.TaskInfo;

public interface TaskInfoService {
	public TaskInfo selectOne(Long id);

	public int save(String userName, TaskInfo taskInfo);

	public int update(String userName, TaskInfo taskInfo);

	public int deleteByPrimaryKey(String userName, TaskInfo taskInfo);

	public List<TaskInfo> selectAllList(Map<String, Object> map);

	public List<Long> getTaskIds(String applicant, String department, String reason, String provenance);

}
