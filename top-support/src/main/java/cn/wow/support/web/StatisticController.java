package cn.wow.support.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wow.common.domain.Menu;
import cn.wow.common.domain.ResultVO;
import cn.wow.common.domain.Task;
import cn.wow.common.service.InfoService;
import cn.wow.common.service.MenuService;
import cn.wow.common.service.TaskInfoService;
import cn.wow.common.service.TaskService;
import cn.wow.common.utils.AjaxVO;
import cn.wow.common.utils.pagination.PageMap;
import cn.wow.common.utils.taskState.TaskTypeEnum;

/**
 * 统计控制器
 * 
 * @author zhenjunzhuo 2017-11-15
 */
@Controller
@RequestMapping(value = "statistic")
public class StatisticController {

	@Autowired
	private MenuService menuService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private InfoService infoService;
	@Autowired
	private TaskInfoService taskInfoService;
	
	@RequestMapping(value = "/result")
	public String result(HttpServletRequest request, HttpServletResponse response, Model model) {
		Menu menu = menuService.selectByAlias("statistic");
		
		model.addAttribute("menuName", menu.getName());
		return "statistic/result";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getResult")
	public AjaxVO getResult(HttpServletRequest request, Model model, String startConfirmTime, String endConfirmTime, String v_code,
			String v_type, String p_code, String taskType, Long parts_org, Long lab_org, String applicant, String department, String reason,
			String provenance) {
		AjaxVO vo = new AjaxVO();

		Map<String, Object> qMap = new PageMap(false);
		
		if (StringUtils.isNotBlank(startConfirmTime)) {
			qMap.put("startConfirmTime", startConfirmTime  + " 00:00:00");
		}
		if (StringUtils.isNotBlank(endConfirmTime)) {
			qMap.put("endConfirmTime", endConfirmTime  + " 23:59:59");
		}
		if (StringUtils.isNotBlank(taskType)) {
			qMap.put("type", taskType);
		}
		if(lab_org != null) {
			qMap.put("labOrgId", lab_org);
		}
		
		List<Long> iIdList = new ArrayList<Long>();
		if (StringUtils.isNotBlank(v_code) || StringUtils.isNotBlank(v_type) || StringUtils.isNotBlank(p_code)
				|| parts_org != null) {
			
			Map<String, Object> iMap = new PageMap(false);
			iMap.put("state", 1);

			if (StringUtils.isNotBlank(v_code)) {
				iMap.put("v_code", v_code);
			}
			if (StringUtils.isNotBlank(v_type)) {
				iMap.put("v_type", v_type);
			}
			if (StringUtils.isNotBlank(p_code)) {
				iMap.put("p_code", p_code);
			}
			if (parts_org != null) {
				iMap.put("parts_org", parts_org);
			}
			iIdList = infoService.selectIdList(iMap);

			if (iIdList.size() < 1) {
				iIdList.add(-1l);
			}
		}

		if (iIdList.size() > 0) {
			qMap.put("iIdList", iIdList);
		}
		
		List<Long> taskInfoIdList = taskInfoService.getTaskIds(applicant, department, reason, provenance);
		if (taskInfoIdList != null && taskInfoIdList.size() > 0) {
			qMap.put("taskIdList", taskInfoIdList);
		}
		
		List<Task> taskList = taskService.selectAllList(qMap);
		
		ResultVO resultVO = new ResultVO();
		if (taskList != null && taskList.size() > 0) {
			resultVO.setTaskNum(taskList.size());

			for (Task task : taskList) {
				if (task.getType() == TaskTypeEnum.PPAP.getState() || task.getType() == TaskTypeEnum.SOP.getState()) {
					if (task.getResult() != null) {
						if (task.getResult() == 1) {
							resultVO.setPassNum(resultVO.getPassNum() + 1);
						} else if (task.getResult() == 2) {
							resultVO.setOnceNum(resultVO.getOnceNum() + 1);
						}
					}
				}
			}
		}
		
		vo.setData(resultVO);
		vo.setSuccess(true);
		return vo;
	}
	
}
