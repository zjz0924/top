package cn.wow.support.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;

import cn.wow.common.domain.Account;
import cn.wow.common.domain.ApplyRecord;
import cn.wow.common.domain.AtlasResult;
import cn.wow.common.domain.CompareVO;
import cn.wow.common.domain.ExamineRecord;
import cn.wow.common.domain.Info;
import cn.wow.common.domain.LabConclusion;
import cn.wow.common.domain.LabReq;
import cn.wow.common.domain.Material;
import cn.wow.common.domain.Menu;
import cn.wow.common.domain.Parts;
import cn.wow.common.domain.StandardVO;
import cn.wow.common.domain.Task;
import cn.wow.common.domain.Vehicle;
import cn.wow.common.service.ApplyRecordService;
import cn.wow.common.service.AtlasResultService;
import cn.wow.common.service.ExamineRecordService;
import cn.wow.common.service.InfoService;
import cn.wow.common.service.LabConclusionService;
import cn.wow.common.service.LabReqService;
import cn.wow.common.service.MaterialService;
import cn.wow.common.service.MenuService;
import cn.wow.common.service.PartsService;
import cn.wow.common.service.TaskService;
import cn.wow.common.service.VehicleService;
import cn.wow.common.utils.AjaxVO;
import cn.wow.common.utils.Contants;
import cn.wow.common.utils.pagination.PageMap;
import cn.wow.common.utils.taskState.SamplingTaskEnum;
import cn.wow.common.utils.taskState.TaskTypeEnum;

/**
 * PPAP任务
 * 
 * @author zhenjunzhuo 2017-10-30
 */
@Controller
@RequestMapping(value = "ppap")
public class PpapTaskController extends AbstractController {

	Logger logger = LoggerFactory.getLogger(OtsTaskController.class);

	// 下达任务列表
	private final static String TRANSMIT_DEFAULT_PAGE_SIZE = "10";
	// 任务记录列表
	private final static String RECORD_DEFAULT_PAGE_SIZE = "10";
	// 任务审批列表
	private final static String APPROVE_DEFAULT_PAGE_SIZE = "10";
	// 结果确认列表
	private final static String CONFIRM_DEFAULT_PAGE_SIZE = "10";

	@Autowired
	private MenuService menuService;
	@Autowired
	private InfoService infoService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private VehicleService vehicleService;
	@Autowired
	private PartsService partsService;
	@Autowired
	private MaterialService materialService;
	@Autowired
	private ExamineRecordService examineRecordService;
	@Autowired
	private ApplyRecordService applyRecordService;
	@Autowired
	private AtlasResultService atlasResultService;
	@Autowired
	private LabReqService labReqService;
	@Autowired
	private LabConclusionService labConclusionService;

	/**
	 * 首页
	 * 
	 * @param taskType
	 *            任务类型：２-PPAP，3-SOP
	 */
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model, String choose,
			int taskType) {
		HttpSession session = request.getSession();

		Menu menu = null;
		if (taskType == 2) {
			menu = menuService.selectByAlias("ppapTask");
		} else if (taskType == 3) {
			menu = menuService.selectByAlias("sopTask");
		}

		// 没有权限的菜单
		Set<Long> illegalMenu = (Set<Long>) session.getAttribute(Contants.CURRENT_ILLEGAL_MENU);

		if (menu != null && menu.getSubList() != null && menu.getSubList().size() > 0) {
			Iterator<Menu> it = menu.getSubList().iterator();
			while (it.hasNext()) {
				Menu subMenu = it.next();
				if (illegalMenu.contains(subMenu.getId())) {
					it.remove();
				}
			}
		}

		if (StringUtils.isBlank(choose)) {
			choose = "0";
		}
		model.addAttribute("choose", Integer.parseInt(choose));

		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(menu.getSubList());
			model.addAttribute("menu", json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		model.addAttribute("menuName", menu.getName());
		return "task/ppap/index";
	}

	/** -------------------------------- 任务下达 --------------------------------- */
	/**
	 * 任务下达列表
	 * 
	 * @param taskType
	 *            任务类型：２-PPAP，3-SOP
	 */
	@RequestMapping(value = "/transmitList")
	public String transmitList(HttpServletRequest request, HttpServletResponse response, Model model, int taskType) {
		model.addAttribute("defaultPageSize", TRANSMIT_DEFAULT_PAGE_SIZE);
		model.addAttribute("taskType", taskType);
		return "task/ppap/transmit_list";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "/transmitListData")
	public Map<String, Object> transmitListData(HttpServletRequest request, Model model, String code, String orgId,
			String startCreateTime, String endCreateTime, String nickName, int taskType, String parts_code,
			String parts_name, String parts_org, String matName, String mat_org, String vehicle_type) {

		// 设置默认记录数
		String pageSize = request.getParameter("pageSize");
		if (!StringUtils.isNotBlank(pageSize)) {
			request.setAttribute("pageSize", TRANSMIT_DEFAULT_PAGE_SIZE);
		}

		Map<String, Object> map = new PageMap(request);
		map.put("custom_order_sql", "t.create_time desc");
		map.put("state", SamplingTaskEnum.APPROVE_NOTPASS.getState());
		if (taskType == 2) {
			map.put("type", TaskTypeEnum.PPAP.getState());
		} else if (taskType == 3) {
			map.put("type", TaskTypeEnum.SOP.getState());
		}

		if (StringUtils.isNotBlank(code)) {
			map.put("code", code);
		}
		if (StringUtils.isNotBlank(startCreateTime)) {
			map.put("startCreateTime", startCreateTime + " 00:00:00");
		}
		if (StringUtils.isNotBlank(endCreateTime)) {
			map.put("endCreateTime", endCreateTime + " 23:59:59");
		}
		if (StringUtils.isNotBlank(nickName)) {
			map.put("nickName", nickName);
		}
		if (StringUtils.isNotBlank(orgId)) {
			map.put("orgId", orgId);
		}

		List<Long> iIdList = infoService.selectIds(vehicle_type, parts_code, parts_name, parts_org, matName, mat_org);
		if (iIdList.size() > 0) {
			map.put("iIdList", iIdList);
		}

		List<Task> dataList = taskService.selectAllList(map);

		// 分页
		Page<Task> pageList = (Page<Task>) dataList;

		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("total", pageList.getTotal());
		dataMap.put("rows", pageList.getResult());

		return dataMap;
	}

	/**
	 * 详情列表
	 */
	@RequestMapping(value = "/transmitDetail")
	public String transmitDetail(HttpServletRequest request, HttpServletResponse response, Model model, Long id,
			int taskType) {
		if (id != null) {
			Task task = taskService.selectOne(id);

			// 审批记录
			Map<String, Object> rMap = new PageMap(false);
			rMap.put("taskId", id);
			rMap.put("type", 2);
			rMap.put("state", 2);

			if (taskType == 2) {
				rMap.put("taskType", TaskTypeEnum.PPAP.getState());
			} else if (taskType == 3) {
				rMap.put("taskType", TaskTypeEnum.SOP.getState());
			}

			rMap.put("custom_order_sql", "create_time asc");
			List<ExamineRecord> recordList = examineRecordService.selectAllList(rMap);

			// 试验说明
			List<LabReq> labReqList = labReqService.getLabReqListByTaskId(id);
			if (labReqList != null && labReqList.size() > 0) {
				for (LabReq labReq : labReqList) {
					if (task.getPartsAtlId() != null && labReq.getType() == 1) {
						model.addAttribute("partsAtlCode", labReq.getCode());
						model.addAttribute("partsAtlTime", labReq.getTime());
						model.addAttribute("partsAtlReq", labReq.getRemark());
					} else if (task.getMatAtlId() != null && labReq.getType() == 2) {
						model.addAttribute("matAtlCode", labReq.getCode());
						model.addAttribute("matAtlTime", labReq.getTime());
						model.addAttribute("matAtlReq", labReq.getRemark());
					}
				}
			}

			model.addAttribute("facadeBean", task);
			model.addAttribute("recordList", recordList);
		}

		model.addAttribute("resUrl", resUrl);
		model.addAttribute("taskType", taskType);
		return "task/ppap/transmit_detail";
	}

	/**
	 * 下达任务结果
	 * 
	 * @param t_id
	 *            任务ID
	 * @param i_id
	 *            信息ID
	 * @param taskType
	 *            任务类型
	 * @param partsAtlId
	 *            零部件图谱实验室ID
	 * @param matAtlId
	 *            原材料图谱实验室ID
	 * @param partsPatId
	 *            零部件型式实验室ID
	 * @param matPatId
	 *            原材料型式实验室ID
	 * @param applicant
	 *            申请人
	 * @param department
	 *            科室
	 * @param figure
	 *            零件图号
	 * @param num
	 *            样品数量
	 * @param origin
	 *            样品来源
	 * @param reason
	 *            抽检原因
	 * @param provenance
	 *            实验费用出处
	 */
	@ResponseBody
	@RequestMapping(value = "/transmit")
	public AjaxVO transmit(HttpServletRequest request, Model model, Long t_id, Long i_id, Long partsAtlId,
			Long matAtlId, Long partsPatId, Long matPatId, int taskType, String partsAtlCode, String partsAtlTime,
			String partsAtlReq, String matAtlCode, String matAtlTime, String matAtlReq, String applicant,
			String department, String figure, Integer num, String origin, String reason, String provenance) {
		AjaxVO vo = new AjaxVO();
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		try {
			Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);

			List<LabReq> labReqList = new ArrayList<LabReq>();
			if (partsAtlId != null) {
				labReqList.add(new LabReq(partsAtlCode,
						StringUtils.isNotBlank(partsAtlTime) ? sdf.parse(partsAtlTime) : null, partsAtlReq, t_id, 1));
			}
			if (matAtlId != null) {
				labReqList.add(new LabReq(matAtlCode, StringUtils.isNotBlank(matAtlTime) ? sdf.parse(matAtlTime) : null,
						matAtlReq, t_id, 2));
			}

			boolean flag = infoService.transmit(account, t_id, i_id, partsAtlId, matAtlId, partsPatId, matPatId,
					taskType, labReqList, applicant, department, figure, num, origin, reason, provenance);
			
			if (!flag) {
				vo.setSuccess(true);
				vo.setMsg("操作成功，已有进行中抽样");
				return vo;
			}
		} catch (Exception ex) {
			logger.error("PPAP/SOP任务下达失败", ex);

			vo.setSuccess(false);
			vo.setMsg("操作失败，系统异常，请重试");
			return vo;
		}

		vo.setSuccess(true);
		vo.setMsg("操作成功");
		return vo;
	}

	/** -------------------------------- 任务审批 --------------------------------- */
	/**
	 * 任务审批列表
	 */
	@RequestMapping(value = "/approveList")
	public String approveList(HttpServletRequest request, HttpServletResponse response, Model model, int taskType) {
		model.addAttribute("defaultPageSize", APPROVE_DEFAULT_PAGE_SIZE);
		model.addAttribute("recordPageSize", RECORD_DEFAULT_PAGE_SIZE);
		model.addAttribute("taskType", taskType);
		return "task/ppap/approve_list";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "/approveListData")
	public Map<String, Object> approveListData(HttpServletRequest request, Model model, String code, String orgId,
			String startCreateTime, String endCreateTime, String nickName, int taskType, String parts_code,
			String parts_name, String parts_org, String matName, String mat_org, String vehicle_type) {

		// 设置默认记录数
		String pageSize = request.getParameter("pageSize");
		if (!StringUtils.isNotBlank(pageSize)) {
			request.setAttribute("pageSize", APPROVE_DEFAULT_PAGE_SIZE);
		}

		Map<String, Object> map = new PageMap(request);
		map.put("custom_order_sql", "t.create_time desc");
		map.put("ppap_approveTask", true);
		if (taskType == 2) {
			map.put("type", TaskTypeEnum.PPAP.getState());
		} else if (taskType == 3) {
			map.put("type", TaskTypeEnum.SOP.getState());
		}

		if (StringUtils.isNotBlank(code)) {
			map.put("code", code);
		}
		if (StringUtils.isNotBlank(startCreateTime)) {
			map.put("startCreateTime", startCreateTime + " 00:00:00");
		}
		if (StringUtils.isNotBlank(endCreateTime)) {
			map.put("endCreateTime", endCreateTime + " 23:59:59");
		}
		if (StringUtils.isNotBlank(nickName)) {
			map.put("nickName", nickName);
		}
		if (StringUtils.isNotBlank(orgId)) {
			map.put("orgId", orgId);
		}

		List<Long> iIdList = infoService.selectIds(vehicle_type, parts_code, parts_name, parts_org, matName, mat_org);
		if (iIdList.size() > 0) {
			map.put("iIdList", iIdList);
		}

		List<Task> dataList = taskService.selectAllList(map);

		// 分页
		Page<Task> pageList = (Page<Task>) dataList;

		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("total", pageList.getTotal());
		dataMap.put("rows", pageList.getResult());

		return dataMap;
	}

	/**
	 * 详情列表
	 */
	@RequestMapping(value = "/approveDetail")
	public String approveDetail(HttpServletRequest request, HttpServletResponse response, Model model, Long id) {
		int approveType = 3;

		if (id != null) {
			Task task = taskService.selectOne(id);

			if (task.getState() == SamplingTaskEnum.APPLYING.getState()) {
				if (task.getInfoApply() == 1) { // 申请修改信息
					approveType = 1;
					ApplyRecord applyRecord = applyRecordService.getRecordByTaskId(task.getId(), 1);

					if (applyRecord != null) {
						if (applyRecord.getpId() != null) {
							Parts newParts = partsService.selectOne(applyRecord.getpId());
							model.addAttribute("newParts", newParts);
						}

						if (applyRecord.getvId() != null) {
							Vehicle newVehicle = vehicleService.selectOne(applyRecord.getvId());
							model.addAttribute("newVehicle", newVehicle);
						}

						if (applyRecord.getmId() != null) {
							Material newMaterial = materialService.selectOne(applyRecord.getmId());
							model.addAttribute("newMaterial", newMaterial);
						}
					}

				} else if (task.getResultApply() == 1) { // 申请修改试验结果
					approveType = 2;

					/** --------- 原结果 ----------- */
					// 零部件-图谱结果（只取最后一次实验）
					List<AtlasResult> pAtlasResult_old = atlasResultService.getLastResult(1, task.gettId());

					// 原材料-图谱结果（只取最后一次实验）
					List<AtlasResult> mAtlasResult_old = atlasResultService.getLastResult(2, task.gettId());

					// 对比结果
					Map<String, List<ExamineRecord>> compareResult_old = atlasResultService
							.assembleCompareResult(task.gettId());

					// 试验结论
					List<LabConclusion> conclusionList_old = labConclusionService.selectByTaskId(task.gettId());

					/** --------- 修改之后的结果 ----------- */
					// 零部件-图谱结果（只取最后一次实验）
					List<AtlasResult> pAtlasResult_new = atlasResultService.getLastResult(1, task.getId());

					// 原材料-图谱结果（只取最后一次实验）
					List<AtlasResult> mAtlasResult_new = atlasResultService.getLastResult(2, task.getId());

					// 对比结果
					Map<String, List<ExamineRecord>> compareResult_new = atlasResultService
							.assembleCompareResult(task.getId());

					// 试验结论
					List<LabConclusion> conclusionList_new = labConclusionService.selectByTaskId(task.getId());

					if (conclusionList_old != null && conclusionList_old.size() > 0) {
						for (LabConclusion conclusion : conclusionList_old) {
							if (conclusion.getType().intValue() == 1) {
								model.addAttribute("partsAtlConclusion_old", conclusion);
							} else if (conclusion.getType().intValue() == 2) {
								model.addAttribute("matAtlConclusion_old", conclusion);
							} else if (conclusion.getType().intValue() == 3) {
								model.addAttribute("partsPatConclusion_old", conclusion);
							} else {
								model.addAttribute("matPatConclusion_old", conclusion);
							}
						}
					}

					if (conclusionList_new != null && conclusionList_new.size() > 0) {
						for (LabConclusion conclusion : conclusionList_new) {
							if (conclusion.getType().intValue() == 1) {
								model.addAttribute("partsAtlConclusion_new", conclusion);
							} else if (conclusion.getType().intValue() == 2) {
								model.addAttribute("matAtlConclusion_new", conclusion);
							} else if (conclusion.getType().intValue() == 3) {
								model.addAttribute("partsPatConclusion_new", conclusion);
							} else {
								model.addAttribute("matPatConclusion_new", conclusion);
							}
						}
					}

					model.addAttribute("pAtlasResult_old", pAtlasResult_old);
					model.addAttribute("mAtlasResult_old", mAtlasResult_old);
					model.addAttribute("pAtlasResult_new", pAtlasResult_new);
					model.addAttribute("mAtlasResult_new", mAtlasResult_new);
					model.addAttribute("compareResult_old", compareResult_old);
					model.addAttribute("compareResult_new", compareResult_new);
				}
			}

			model.addAttribute("facadeBean", task);
		}

		model.addAttribute("resUrl", resUrl);
		model.addAttribute("approveType", approveType);

		if (approveType == 3) {
			List<LabReq> labReqList = labReqService.getLabReqListByTaskId(id);
			model.addAttribute("labReqList", labReqList);
		}

		if (approveType == 1) {
			return "task/ppap/approve_info_detail";
		} else {
			return "task/ppap/approve_detail";
		}
	}

	/**
	 * 审批结果
	 * 
	 * @param account
	 *            操作用户
	 * @param id
	 *            任务ID
	 * @param result
	 *            结果：1-通过，2-不通过
	 * @param remark
	 *            备注
	 * @param catagory
	 *            分类：1-信息修改申请，2-试验结果修改申请，3-正常流程
	 */
	@ResponseBody
	@RequestMapping(value = "/approve")
	public AjaxVO approve(HttpServletRequest request, Model model, Long id, int result, String remark, int catagory,
			Long partsAtlId, Long matAtlId) {
		AjaxVO vo = new AjaxVO();

		try {
			Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);

			infoService.approve(account, id, result, catagory, remark, partsAtlId, matAtlId);
		} catch (Exception ex) {
			logger.error("PPAP任务审批失败", ex);

			vo.setSuccess(false);
			vo.setMsg("操作失败，系统异常，请重试");
			return vo;
		}

		vo.setSuccess(true);
		vo.setMsg("操作成功");
		return vo;
	}

	/**
	 * 批量审批结果
	 * 
	 * @param account
	 *            操作用户
	 * @param id
	 *            任务ID
	 * @param result
	 *            结果：1-通过，2-不通过
	 * @param remark
	 *            备注
	 */
	@ResponseBody
	@RequestMapping(value = "/batchApprove")
	public AjaxVO batchApprove(HttpServletRequest request, Model model, @RequestParam(value = "ids[]") Long[] ids,
			int result, String remark) {
		AjaxVO vo = new AjaxVO();

		try {
			Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);

			if (ids != null && ids.length > 0) {
				for (Long id : ids) {
					Task task = taskService.selectOne(id);
					int catagory = 3;

					if (task.getInfoApply() == 1) {
						catagory = 1;
					} else if (task.getResultApply() == 1) {
						catagory = 2;
					}
					infoService.approve(account, id, result, catagory, remark, null, null);
				}
			}
		} catch (Exception ex) {
			logger.error("PPAP任务批量审批失败", ex);

			vo.setSuccess(false);
			vo.setMsg("操作失败，系统异常，请重试");
			return vo;
		}

		vo.setSuccess(true);
		vo.setMsg("操作成功");
		return vo;
	}

	/** -------------------------------- 结果确认 --------------------------------- */

	/**
	 * 结果确认列表
	 */
	@RequestMapping(value = "/confirmList")
	public String confirmList(HttpServletRequest request, HttpServletResponse response, Model model, int taskType) {
		model.addAttribute("defaultPageSize", CONFIRM_DEFAULT_PAGE_SIZE);
		model.addAttribute("recordPageSize", RECORD_DEFAULT_PAGE_SIZE);
		model.addAttribute("taskType", taskType);
		return "task/ppap/confirm_list";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "/confirmListData")
	public Map<String, Object> confirmListData(HttpServletRequest request, Model model, String code, String orgId,
			String startCreateTime, String endCreateTime, String nickName, int taskType, String parts_code,
			String parts_name, String parts_org, String matName, String mat_org, String vehicle_type) {

		// 设置默认记录数
		String pageSize = request.getParameter("pageSize");
		if (!StringUtils.isNotBlank(pageSize)) {
			request.setAttribute("pageSize", CONFIRM_DEFAULT_PAGE_SIZE);
		}

		Map<String, Object> map = new PageMap(request);
		map.put("custom_order_sql", "t.create_time desc");
		map.put("state", SamplingTaskEnum.RECONFIRM.getState());
		map.put("type", taskType);

		if (StringUtils.isNotBlank(code)) {
			map.put("code", code);
		}
		if (StringUtils.isNotBlank(startCreateTime)) {
			map.put("startCreateTime", startCreateTime + " 00:00:00");
		}
		if (StringUtils.isNotBlank(endCreateTime)) {
			map.put("endCreateTime", endCreateTime + " 23:59:59");
		}
		if (StringUtils.isNotBlank(nickName)) {
			map.put("nickName", nickName);
		}
		if (StringUtils.isNotBlank(orgId)) {
			map.put("orgId", orgId);
		}

		List<Long> iIdList = infoService.selectIds(vehicle_type, parts_code, parts_name, parts_org, matName, mat_org);
		if (iIdList.size() > 0) {
			map.put("iIdList", iIdList);
		}

		List<Task> dataList = taskService.selectAllList(map);

		// 分页
		Page<Task> pageList = (Page<Task>) dataList;

		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("total", pageList.getTotal());
		dataMap.put("rows", pageList.getResult());

		return dataMap;
	}

	/**
	 * 详情列表
	 */
	@RequestMapping(value = "/confirmDetail")
	public String confirmDetail(HttpServletRequest request, HttpServletResponse response, Model model, Long id) {

		if (id != null) {
			Task task = taskService.selectOne(id);

			// 基准图谱结果
			List<AtlasResult> sd_pAtlasResult = atlasResultService.getStandardAtlResult(task.getiId(), 1);
			List<AtlasResult> st_mAtlasResult = atlasResultService.getStandardAtlResult(task.getiId(), 2);

			// 抽样图谱结果
			Map<String, Object> atMap = new HashMap<String, Object>();
			atMap.put("tId", id);
			atMap.put("custom_order_sql", "exp_no desc limit 6");
			List<AtlasResult> atDataList = atlasResultService.selectAllList(atMap);

			List<AtlasResult> sl_pAtlasResult = new ArrayList<AtlasResult>();
			List<AtlasResult> sl_mAtlasResult = new ArrayList<AtlasResult>();
			groupAtlasResult(atDataList, sl_pAtlasResult, sl_mAtlasResult);

			// 零部件图谱结果
			Map<Integer, CompareVO> pAtlasResult = atlasResultService.assembleCompareAtlas(sd_pAtlasResult,
					sl_pAtlasResult);
			// 原材料图谱结果
			Map<Integer, CompareVO> mAtlasResult = atlasResultService.assembleCompareAtlas(st_mAtlasResult,
					sl_mAtlasResult);
			// 对比结果
			Map<String, List<ExamineRecord>> compareResult = atlasResultService.assembleCompareResult(id);

			model.addAttribute("mAtlasResult", mAtlasResult);
			model.addAttribute("pAtlasResult", pAtlasResult);
			model.addAttribute("compareResult", compareResult);

			model.addAttribute("facadeBean", task);

			List<LabReq> labReqList = labReqService.getLabReqListByTaskId(id);
			model.addAttribute("labReqList", labReqList);
		}

		model.addAttribute("resUrl", resUrl);

		return "task/ppap/confirm_detail";
	}

	/**
	 * 结果确认
	 * 
	 * @param taskId
	 *            任务ID
	 * @param result
	 *            结果：1-第二次抽样，2-中止任务
	 * @param remark
	 *            中止任务原因
	 */
	@ResponseBody
	@RequestMapping(value = "/confirmResult")
	public AjaxVO confirmResult(HttpServletRequest request, Model model, Long taskId, int result, String remark) {
		AjaxVO vo = new AjaxVO();

		try {
			Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);
			taskService.confirmResult(account, taskId, result, remark);
		} catch (Exception ex) {
			logger.error("结果确认失败", ex);

			vo.setSuccess(false);
			vo.setMsg("操作失败，系统异常，请重试");
			return vo;
		}

		vo.setSuccess(true);
		vo.setMsg("操作成功");
		return vo;
	}

	/** -------------------------------- 其它 --------------------------------- */

	/**
	 * 分组
	 * 
	 * @param atDataList
	 * @param pAtlasResult
	 * @param mAtlasResult
	 */
	public void groupAtlasResult(List<AtlasResult> arDataList, List<AtlasResult> pAtlasResult,
			List<AtlasResult> mAtlasResult) {
		for (AtlasResult ar : arDataList) {
			if (ar.getCatagory() == 1) {
				pAtlasResult.add(ar);
			} else {
				mAtlasResult.add(ar);
			}
		}
	}

	/**
	 * 获取基准数据
	 * 
	 * @param v_id
	 * @param p_id
	 * @param m_id
	 */
	@ResponseBody
	@RequestMapping(value = "/standard")
	public List<StandardVO> standard(HttpServletRequest request, Model model, Long v_id, Long p_id, Long m_id) {
		List<StandardVO> dataList = new ArrayList<StandardVO>();
		if (v_id == null || p_id == null || m_id == null) {
			return dataList;
		}

		Map<String, Object> iMap = new PageMap(false);
		iMap.put("mId", m_id);
		iMap.put("pId", p_id);
		iMap.put("vId", v_id);
		iMap.put("state", 1);
		List<Info> infoList = infoService.selectAllList(iMap);

		if (infoList != null && infoList.size() > 0) {
			List<Long> idList = new ArrayList<Long>();
			for (Info info : infoList) {
				idList.add(info.getId());
			}

			List<Task> taskList = taskService.batchQueryByInfoId(idList);
			if (taskList != null && taskList.size() > 0) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				for (int i = 0; i < taskList.size(); i++) {
					Task task = taskList.get(i);
					StandardVO vo = new StandardVO();
					vo.setDate(sdf.format(task.getCreateTime()));
					vo.setId(task.getiId());
					vo.setTaskCode(task.getCode());
					vo.setText("基准" + (i + 1));
					dataList.add(vo);
				}
			}
		}
		return dataList;
	}

	/**
	 * 查询任务
	 */
	@ResponseBody
	@RequestMapping(value = "/queryTask")
	public AjaxVO queryTask(HttpServletRequest request, Model model, String qCode) {
		AjaxVO vo = new AjaxVO();

		try {
			Task task = taskService.getStandardTask(qCode);
			if (task != null) {
				vo.setSuccess(true);

				ObjectMapper objectMapper = new ObjectMapper();
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("vehicle", objectMapper.writeValueAsString(task.getInfo().getVehicle()));
				data.put("parts", objectMapper.writeValueAsString(task.getInfo().getParts()));
				data.put("material", objectMapper.writeValueAsString(task.getInfo().getMaterial()));

				vo.setData(data);
			} else {
				vo.setSuccess(false);
				vo.setMsg(qCode + "，不存在该基准任务");
			}
		} catch (Exception e) {
			e.printStackTrace();

			vo.setSuccess(false);
			vo.setMsg("查询失败，系统异常");
		}
		return vo;
	}

}
