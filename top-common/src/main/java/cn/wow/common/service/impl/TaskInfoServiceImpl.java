package cn.wow.common.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.wow.common.utils.pagination.PageHelperExt;
import cn.wow.common.utils.pagination.PageMap;
import cn.wow.common.dao.TaskInfoDao;
import cn.wow.common.domain.TaskInfo;
import cn.wow.common.service.TaskInfoService;

@Service
@Transactional
public class TaskInfoServiceImpl implements TaskInfoService {

	private static Logger logger = LoggerFactory.getLogger(TaskInfoServiceImpl.class);

	@Autowired
	private TaskInfoDao taskInfoDao;

	public TaskInfo selectOne(Long id) {
		return taskInfoDao.selectOne(id);
	}

	public int save(String userName, TaskInfo taskInfo) {
		return taskInfoDao.insert(taskInfo);
	}

	public int update(String userName, TaskInfo taskInfo) {
		return taskInfoDao.update(taskInfo);
	}

	public int deleteByPrimaryKey(String userName, TaskInfo taskInfo) {
		return taskInfoDao.deleteByPrimaryKey(taskInfo.getId());
	}

	public List<TaskInfo> selectAllList(Map<String, Object> map) {
		PageHelperExt.startPage(map);
		return taskInfoDao.selectAllList(map);
	}

	public List<Long> getTaskIds(String applicant, String department, String reason, String provenance) {
		List<Long> idList = new ArrayList<Long>();

		if (StringUtils.isNotBlank(applicant) || StringUtils.isNotBlank(department) || StringUtils.isNotBlank(reason)
				|| StringUtils.isNotBlank(provenance)) {

			Map<String, Object> queryMap = new PageMap(false);
			if (StringUtils.isNotBlank(applicant)) {
				queryMap.put("applicant", applicant);
			}

			if (StringUtils.isNotBlank(department)) {
				queryMap.put("department", department);
			}

			if (StringUtils.isNotBlank(reason)) {
				queryMap.put("reason", reason);
			}

			if (StringUtils.isNotBlank(provenance)) {
				queryMap.put("provenance", provenance);
			}

			List<TaskInfo> dataList = selectAllList(queryMap);
			if (dataList != null && dataList.size() > 0) {
				for (TaskInfo info : dataList) {
					idList.add(info.getTaskId());
				}
			} else {
				idList.add(-1l);
			}
		}

		return idList;
	}

}
