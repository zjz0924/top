package cn.wow.support.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wow.common.domain.Account;
import cn.wow.common.domain.EmailRecord;
import cn.wow.common.domain.Menu;
import cn.wow.common.service.EmailRecordService;
import cn.wow.common.service.TaskService;
import cn.wow.common.utils.AjaxVO;
import cn.wow.common.utils.Contants;
import cn.wow.common.utils.pagination.PageMap;
import cn.wow.common.utils.taskState.SamplingTaskEnum;
import cn.wow.common.utils.taskState.StandardTaskEnum;
import cn.wow.common.utils.taskState.TaskTypeEnum;

/**
 * 首页控制器
 * 
 * @author zhenjunzhuo
 */
@Controller
@RequestMapping(value = "")
public class IndexController {

	private static Logger logger = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	private EmailRecordService emailRecordService;
	@Autowired
	private TaskService taskService;

	// 审核权限
	private boolean examinePermission = false;
	// PPAP 任务审批权限
	private boolean ppapApprovePermission = false;
	// OTS 任务审批权限
	private boolean otsApprovePermission = false;
	// SOP 任务审批权限
	private boolean sopApprovePermission = false;
	// GS 任务审批权限
	private boolean gsApprovePermission = false;
	// 型式结果上传权限
	private boolean patternUploadPermission = false;
	// 图谱上传权限
	private boolean atlasUploadPermission = false;
	// 对比权限
	private boolean comparePermission = false;
	// 待上传结果确认权限
	private boolean waitConfirmPermission = false;
	// 已上传结果确认权限
	private boolean finishConfirmPermission = false;

	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request, Model model) {

		Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);

		if (account != null) {
			// 未读消息
			Map<String, Object> map = new PageMap(false);
			map.put("addr", account.getEmail());
			map.put("state", 1);
			List<EmailRecord> dataList = emailRecordService.selectAllList(map);

			int unread = 0;
			if (dataList != null && dataList.size() > 0) {
				unread = dataList.size();
			}
			model.addAttribute("unread", unread);
		}
		return "index/index";
	}

	@ResponseBody
	@RequestMapping(value = "/getTaskNum")
	public AjaxVO getTaskNum(HttpServletRequest request, Model model) {
		examinePermission = false;
		ppapApprovePermission = false;
		otsApprovePermission = false;
		sopApprovePermission = false;
		gsApprovePermission = false;
		patternUploadPermission = false;
		atlasUploadPermission = false;
		comparePermission = false;
		waitConfirmPermission = false;
		finishConfirmPermission = false;
		
		Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);
		List<Menu> menuList = (List<Menu>) request.getSession().getAttribute(Contants.CURRENT_PERMISSION_MENU);
		Map<String, Object> qMap = new PageMap(false);
		
		AjaxVO vo = new AjaxVO();
		
		// 任务数
		int examineNum = 0;
		int approveNum = 0;
		int patternUploadNum = 0;
		int atlasUploadNum = 0;
		int compareNum = 0;
		int waitConfirmNum = 0;
		int finishConfirmNum = 0;

		try {
			// 查看当前是否有审核、审批、上传结果、比对、结果确认权限
			for (Menu menu : menuList) {
				if (menu.getSubList() != null && menu.getSubList().size() > 0) {
					for (Menu subMenu : menu.getSubList()) {
						if (subMenu.getSubList() != null && subMenu.getSubList().size() > 0) {
							for (Menu sMenu : subMenu.getSubList()) {
								if (StringUtils.isNotBlank(sMenu.getAlias())) {
									isHasPermission(sMenu.getAlias());
								}
							}
						} else {
							if (StringUtils.isNotBlank(subMenu.getAlias())) {
								isHasPermission(subMenu.getAlias());
							}
						}
					}
				} else {
					if (StringUtils.isNotBlank(menu.getAlias())) {
						isHasPermission(menu.getAlias());
					}
				}
			}

			// 审核数
			if (examinePermission) {
				qMap.clear();
				qMap.put("state", StandardTaskEnum.EXAMINE.getState());
				qMap.put("type", 1);
				// 除了超级管理员，其它用户只能查看自己录入的申请记录
				if (account.getRole() == null || !Contants.SUPER_ROLE_CODE.equals(account.getRole().getCode())) {
					qMap.put("aId", account.getId());
				}
				examineNum = taskService.getTaskNum(qMap);

				qMap.put("type", 4);
				examineNum += taskService.getTaskNum(qMap);
			}

			// OTS 审批数
			if (otsApprovePermission) {
				qMap.clear();
				qMap.put("approveTask_ots", true);
				qMap.put("type", 1);
				approveNum += taskService.getTaskNum(qMap);
			}

			// GS审批数
			if (gsApprovePermission) {
				qMap.clear();
				qMap.put("approveTask_gs", true);
				qMap.put("type", 4);
				approveNum += taskService.getTaskNum(qMap);
			}

			// SOP 审批数
			if (sopApprovePermission) {
				qMap.clear();
				qMap.put("ppap_approveTask", true);
				qMap.put("type", TaskTypeEnum.SOP.getState());
				approveNum += taskService.getTaskNum(qMap);
			}

			// PPAP 审批数
			if (ppapApprovePermission) {
				qMap.clear();
				qMap.put("ppap_approveTask", true);
				qMap.put("type", TaskTypeEnum.PPAP.getState());
				approveNum += taskService.getTaskNum(qMap);
			}

			// 型式结果上传数
			if (patternUploadPermission) {
				qMap.clear();
				qMap.put("state", StandardTaskEnum.TESTING.getState());
				// 超级管理员具有所有的权限
				if (account.getRole() == null || !Contants.SUPER_ROLE_CODE.equals(account.getRole().getCode())) {
					qMap.put("patternTask", account.getOrgId() == null ? -1 : account.getOrgId());
				} else {
					qMap.put("patternTask_admin", true);
				}
				patternUploadNum = taskService.getTaskNum(qMap);
			}

			// 图谱结果上传数
			if (atlasUploadPermission) {
				qMap.clear();
				qMap.put("state", StandardTaskEnum.TESTING.getState());
				// 超级管理员具有所有的权限
				if (account.getRole() == null || !Contants.SUPER_ROLE_CODE.equals(account.getRole().getCode())) {
					qMap.put("atlasTask", account.getOrgId() == null ? -1 : account.getOrgId());
				} else {
					qMap.put("atlasTask_admin", true);
				}
				atlasUploadNum = taskService.getTaskNum(qMap);
			}

			if (comparePermission) {
				qMap.clear();
				qMap.put("state", SamplingTaskEnum.COMPARE.getState());
				qMap.put("compareTask", true);
				compareNum = taskService.getTaskNum(qMap);
			}

			if (waitConfirmPermission) {
				qMap.clear();
				qMap.put("confirmTask_wait", true);
				waitConfirmNum = taskService.getTaskNum(qMap);
			}

			if (finishConfirmPermission) {
				qMap.clear();
				qMap.put("confirmTask_finish", true);

				finishConfirmNum = taskService.getTaskNum(qMap);
			}
		} catch (Exception ex) {
			logger.error("获取任务数异常", ex);
		} finally {
			Integer[] data = new Integer[] { examineNum, approveNum, patternUploadNum, atlasUploadNum, compareNum,
					waitConfirmNum, finishConfirmNum };
			vo.setData(data);
		}
		return vo;
	}

	public void isHasPermission(String alias) {
		if ("otsExamine".equals(alias)) {
			examinePermission = true;
		} else if ("ppapApprove".equals(alias)) {
			ppapApprovePermission = true;
		} else if ("otsApprove".equals(alias)) {
			otsApprovePermission = true;
		} else if ("sopApprove".equals(alias)) {
			sopApprovePermission = true;
		} else if ("gsApprove".equals(alias)) {
			gsApprovePermission = true;
		} else if ("patternUpload".equals(alias)) {
			patternUploadPermission = true;
		} else if ("atlasUpload".equals(alias)) {
			atlasUploadPermission = true;
		} else if ("compare".equals(alias)) {
			comparePermission = true;
		} else if ("waitConfirm".equals(alias)) {
			waitConfirmPermission = true;
		} else if ("finishConfirm".equals(alias)) {
			finishConfirmPermission = true;
		}
	}

}
