package cn.wow.support.web;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;

import cn.wow.common.domain.Account;
import cn.wow.common.domain.AtlasResult;
import cn.wow.common.domain.CompareVO;
import cn.wow.common.domain.ExamineRecord;
import cn.wow.common.domain.LabConclusion;
import cn.wow.common.domain.LabReq;
import cn.wow.common.domain.Menu;
import cn.wow.common.domain.PfResult;
import cn.wow.common.domain.Task;
import cn.wow.common.service.AtlasResultService;
import cn.wow.common.service.ExamineRecordService;
import cn.wow.common.service.InfoService;
import cn.wow.common.service.LabConclusionService;
import cn.wow.common.service.LabReqService;
import cn.wow.common.service.MenuService;
import cn.wow.common.service.PfResultService;
import cn.wow.common.service.TaskInfoService;
import cn.wow.common.service.TaskRecordService;
import cn.wow.common.service.TaskService;
import cn.wow.common.utils.Contants;
import cn.wow.common.utils.ImportExcelUtil;
import cn.wow.common.utils.pagination.PageMap;
import cn.wow.common.utils.taskState.SamplingTaskEnum;
import cn.wow.common.utils.taskState.StandardTaskEnum;
import cn.wow.common.utils.taskState.TaskTypeEnum;

@Controller
@RequestMapping(value = "query")
public class QueryController extends AbstractController {

	Logger logger = LoggerFactory.getLogger(QueryController.class);

	private final static String DEFAULT_PAGE_SIZE = "10";

	@Autowired
	private InfoService infoService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private ExamineRecordService examineRecordService;
	@Autowired
	private AtlasResultService atlasResultService;
	@Autowired
	private PfResultService pfResultService;
	@Autowired
	private LabReqService labReqService;
	@Autowired
	private LabConclusionService labConclusionService;
	@Autowired
	private TaskRecordService taskRecordService;
	@Autowired
	private TaskInfoService taskInfoService;

	// 查询的条件，用于导出
	private Map<String, Object> queryMap = new PageMap(false);

	@RequestMapping(value = "/list")
	public String list(HttpServletRequest httpServletRequest, Model model) {

		Menu menu = menuService.selectByAlias("query");

		model.addAttribute("defaultPageSize", DEFAULT_PAGE_SIZE);
		model.addAttribute("resUrl", resUrl);
		model.addAttribute("menuName", menu.getName());
		return "query/task_list";
	}

	/**
	 * 获取数据
	 */
	@ResponseBody
	@RequestMapping(value = "/getListData")
	public Map<String, Object> getListData(HttpServletRequest request, Model model, String code, String startCreateTime,
			String endCreateTime, String taskType, String orgId, String nickName, String startConfirmTime,
			String endConfirmTime, String parts_code, String parts_name, String parts_org, String matName,
			String mat_org, String vehicle_type, String applicant, String department, String reason,
			String provenance) {
		Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);

		// 设置默认记录数
		String pageSize = request.getParameter("pageSize");
		if (!StringUtils.isNotBlank(pageSize)) {
			request.setAttribute("pageSize", DEFAULT_PAGE_SIZE);
		}
		queryMap.clear();

		Map<String, Object> map = new PageMap(request);
		map.put("custom_order_sql", "t.create_time desc");
		queryMap.put("custom_order_sql", "t.create_time desc");

		if (StringUtils.isNotBlank(code)) {
			map.put("code", code);
			queryMap.put("code", code);
		}
		if (StringUtils.isNotBlank(startCreateTime)) {
			map.put("startCreateTime", startCreateTime + " 00:00:00");
			queryMap.put("startCreateTime", startCreateTime + " 00:00:00");
		}
		if (StringUtils.isNotBlank(endCreateTime)) {
			map.put("endCreateTime", endCreateTime + " 23:59:59");
			queryMap.put("endCreateTime", endCreateTime + " 23:59:59");
		}
		if (StringUtils.isNotBlank(startConfirmTime)) {
			map.put("startConfirmTime", startConfirmTime + " 00:00:00");
			queryMap.put("startConfirmTime", startConfirmTime + " 00:00:00");
		}
		if (StringUtils.isNotBlank(endConfirmTime)) {
			map.put("endConfirmTime", endConfirmTime + " 23:59:59");
			queryMap.put("endConfirmTime", endConfirmTime + " 23:59:59");
		}
		if (StringUtils.isNotBlank(nickName)) {
			map.put("nickName", nickName);
			queryMap.put("nickName", nickName);
		}
		if (StringUtils.isNotBlank(orgId)) {
			map.put("orgId", orgId);
			queryMap.put("orgId", orgId);
		}
		if (StringUtils.isNotBlank(taskType)) {
			map.put("type", taskType);
			queryMap.put("type", taskType);
		}

		List<Long> taskInfoIdList = taskInfoService.getTaskIds(applicant, department, reason, provenance);

		/**
		 * 1) SQE审批人全范围 2) 其它审批人只能查看本部门的任务 3) 其它人只能看自己的任务
		 */
		if (!Contants.SUPER_ROLE_CODE.equals(account.getRole().getCode())) {
			// 可查看权限
			String type = judgeAccountRole(request);

			if ("self".equals(type)) {
				List<Long> taskIdList = taskRecordService.selectTaskIdList(account.getId());

				if (taskIdList == null || taskIdList.size() < 1) {
					taskIdList.add(-1l);
				}

				if (taskInfoIdList != null && taskInfoIdList.size() > 0) {
					taskIdList.retainAll(taskInfoIdList);
				}

				map.put("taskIdList", taskIdList);
				queryMap.put("taskIdList", taskIdList);

			} else if ("ots".equals(type)) {
				map.put("type", 1);
				queryMap.put("type", 1);
				
				if (taskInfoIdList != null && taskInfoIdList.size() > 0) {
					map.put("taskIdList", taskInfoIdList);
					queryMap.put("taskIdList", taskInfoIdList);
				}
			} else if ("gs".equals(type)) {
				map.put("type", 4);
				queryMap.put("type", 4);
				
				if (taskInfoIdList != null && taskInfoIdList.size() > 0) {
					map.put("taskIdList", taskInfoIdList);
					queryMap.put("taskIdList", taskInfoIdList);
				}
			}
		} else {
			if (taskInfoIdList != null && taskInfoIdList.size() > 0) {
				map.put("taskIdList", taskInfoIdList);
				queryMap.put("taskIdList", taskInfoIdList);
			}
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
	 * 详情
	 */
	@RequestMapping(value = "/detail")
	public String detail(HttpServletRequest request, Model model, String id) {
		if (StringUtils.isNotBlank(id)) {
			Task task = taskService.selectOne(Long.parseLong(id));
			List<LabConclusion> conclusionList = labConclusionService.selectByTaskId(Long.parseLong(id));

			if (task.getType() == TaskTypeEnum.OTS.getState() || task.getType() == TaskTypeEnum.GS.getState()) { // OTS/GS
																													// 结果确认

				if (task.getMatAtlResult() >= 2 || task.getMatPatResult() >= 2 || task.getPartsAtlResult() >= 2
						|| task.getPartsPatResult() >= 2) {
					// 性能结果
					Map<String, Object> pfMap = new HashMap<String, Object>();
					pfMap.put("tId", id);
					pfMap.put("custom_order_sql", "exp_no asc");
					List<PfResult> pfDataList = pfResultService.selectAllList(pfMap);

					Map<Integer, List<PfResult>> pPfResult = new HashMap<Integer, List<PfResult>>();
					Map<Integer, List<PfResult>> mPfResult = new HashMap<Integer, List<PfResult>>();
					pfResultService.assemblePfResult(pfDataList, pPfResult, mPfResult);

					// 图谱结果
					Map<String, Object> atMap = new HashMap<String, Object>();
					atMap.put("tId", id);
					atMap.put("custom_order_sql", "exp_no asc");
					List<AtlasResult> atDataList = atlasResultService.selectAllList(atMap);

					Map<Integer, List<AtlasResult>> pAtlasResult = new HashMap<Integer, List<AtlasResult>>();
					Map<Integer, List<AtlasResult>> mAtlasResult = new HashMap<Integer, List<AtlasResult>>();
					atlasResultService.assembleAtlasResult(atDataList, pAtlasResult, mAtlasResult);

					// 原材料图谱结果
					model.addAttribute("mAtlasResult", mAtlasResult);
					// 零部件图谱结果
					model.addAttribute("pAtlasResult", pAtlasResult);
					// 原材料型式结果
					model.addAttribute("mPfResult", mPfResult);
					// 零部件型式结果
					model.addAttribute("pPfResult", pPfResult);

					// 试验结论
					if (conclusionList != null && conclusionList.size() > 0) {
						for (LabConclusion conclusion : conclusionList) {
							if (conclusion.getType().intValue() == 1) {
								model.addAttribute("partsAtlConclusion", conclusion);
							} else if (conclusion.getType().intValue() == 2) {
								model.addAttribute("matAtlConclusion", conclusion);
							} else if (conclusion.getType().intValue() == 3) {
								model.addAttribute("partsPatConclusion", conclusion);
							} else {
								model.addAttribute("matPatConclusion", conclusion);
							}
						}
					}
				}
			} else if (task.getType() == TaskTypeEnum.PPAP.getState()
					|| task.getType() == TaskTypeEnum.SOP.getState()) {
				if (task.getState() >= 6) {

					Long iId = task.getiId();

					// 如果是修改生成的记录，基准图谱要取父任务的iID的基准
					if (task.gettId() != null) {
						Task pTask = taskService.getRootTask(task.gettId());
						iId = pTask.getiId();
					}

					// 基准图谱结果
					List<AtlasResult> sd_pAtlasResult = atlasResultService.getStandardAtlResult(iId, 1);
					List<AtlasResult> st_mAtlasResult = atlasResultService.getStandardAtlResult(iId, 2);

					// 抽样图谱结果
					Map<String, Object> atMap = new HashMap<String, Object>();
					atMap.put("tId", id);
					atMap.put("custom_order_sql", "exp_no desc limit 8");
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
					Map<String, List<ExamineRecord>> compareResult = atlasResultService
							.assembleCompareResult(Long.parseLong(id));

					model.addAttribute("mAtlasResult", mAtlasResult);
					model.addAttribute("pAtlasResult", pAtlasResult);
					model.addAttribute("compareResult", compareResult);

					// 试验结论
					if (conclusionList != null && conclusionList.size() > 0) {
						model.addAttribute("conclusionList", conclusionList);
					}
				}
			}

			// 实验说明
			List<LabReq> labReqList = labReqService.getLabReqListByTaskId(Long.parseLong(id));
			model.addAttribute("labReqList", labReqList);

			model.addAttribute("facadeBean", task);
		}
		model.addAttribute("resUrl", resUrl);
		return "query/task_detail";
	}

	/**
	 * 导出任务列表
	 */
	@RequestMapping(value = "/exportTask")
	public void exportTask(HttpServletRequest request, HttpServletResponse response) {
		String filename = "任务清单";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			// 设置头
			ImportExcelUtil.setResponseHeader(response, filename + ".xlsx");

			Workbook wb = new SXSSFWorkbook(100); // 保持100条在内存中，其它保存到磁盘中
			// 工作簿
			Sheet sh = wb.createSheet("任务清单");
			sh.setColumnWidth(0, (short) 6000);
			sh.setColumnWidth(1, (short) 6000);
			sh.setColumnWidth(2, (short) 4000);
			sh.setColumnWidth(3, (short) 4000);
			sh.setColumnWidth(4, (short) 5000);
			sh.setColumnWidth(5, (short) 6000);
			sh.setColumnWidth(6, (short) 6000);
			sh.setColumnWidth(7, (short) 6000);
			sh.setColumnWidth(8, (short) 6000);
			sh.setColumnWidth(9, (short) 6000);
			sh.setColumnWidth(10, (short) 6000);
			sh.setColumnWidth(11, (short) 6000);
			sh.setColumnWidth(12, (short) 6000);

			Map<String, CellStyle> styles = ImportExcelUtil.createStyles(wb);

			String[] titles = { "任务号", "任务类型", "状态", "结果", "车型", "零件号", "零件名称", "生产商", "材料名称", "生产商", "录入用户", "录入时间",
					"完成时间", };
			int r = 0;

			Row titleRow = sh.createRow(0);
			titleRow.setHeight((short) 450);
			for (int k = 0; k < titles.length; k++) {
				Cell cell = titleRow.createCell(k);
				cell.setCellStyle(styles.get("header"));
				cell.setCellValue(titles[k]);
			}

			++r;
			List<Task> dataList = taskService.selectAllList(queryMap);
			for (int j = 0; j < dataList.size(); j++) {// 添加数据
				Row contentRow = sh.createRow(r);
				contentRow.setHeight((short) 400);
				Task task = dataList.get(j);

				Cell cell1 = contentRow.createCell(0);
				cell1.setCellStyle(styles.get("cell"));
				cell1.setCellValue(task.getCode());

				Cell cell2 = contentRow.createCell(1);
				cell2.setCellStyle(styles.get("cell"));
				String taskType = "";
				if (task.getType() == TaskTypeEnum.OTS.getState()) {
					taskType = "车型OTS阶段任务";
				} else if (task.getType() == TaskTypeEnum.PPAP.getState()) {
					taskType = "车型PPAP阶段任务";
				} else if (task.getType() == TaskTypeEnum.SOP.getState()) {
					taskType = "车型SOP阶段任务";
				} else {
					taskType = "非车型材料任务";
				}
				cell2.setCellValue(taskType);

				Cell cell3 = contentRow.createCell(2);
				cell3.setCellStyle(styles.get("cell"));
				String state = "";
				if (task.getType() == TaskTypeEnum.OTS.getState() || task.getType() == TaskTypeEnum.GS.getState()) {
					if (task.getState() == 1) {
						state = "审核中";
					} else if (task.getState() == 2) {
						state = "审核不通过";
					} else if (task.getState() == 3) {
						state = "试验中";
					} else if (task.getState() == 4) {
						state = "完成";
					} else if (task.getState() == 5) {
						state = "申请修改";
					} else {
						state = "申请不通过";
					}
				} else if (task.getType() == TaskTypeEnum.PPAP.getState()
						|| task.getType() == TaskTypeEnum.SOP.getState()) {
					if (task.getState() == 1) {
						state = "审批中";
					} else if (task.getState() == 2) {
						state = "审批不通过";
					} else if (task.getState() == 3) {
						state = "结果上传中";
					} else if (task.getState() == 4) {
						state = "结果比对中";
					} else if (task.getState() == 5) {
						state = "结果发送中";
					} else if (task.getState() == 6) {
						state = "结果确认中";
					} else if (task.getState() == 7) {
						state = "完成";
					} else if (task.getState() == 8) {
						state = "申请修改";
					} else if (task.getState() == 9) {
						state = "申请不通过";
					} else if (task.getState() == 10) {
						state = "等待是否二次抽样";
					} else {
						state = "中止任务";
					}
				}
				cell3.setCellValue(state);

				String result = "";
				Cell cell4 = contentRow.createCell(3);
				cell4.setCellStyle(styles.get("cell"));
				if (task.getType() == TaskTypeEnum.OTS.getState() || task.getType() == TaskTypeEnum.GS.getState()) {
					if (task.getState() == StandardTaskEnum.ACCOMPLISH.getState()) {
						if (task.getFailNum() == 0) {
							result = "合格";
						} else if (task.getFailNum() == 1) {
							result = "一次不合格";
						} else {
							result = "两次不合格";
						}
					}
				} else if (task.getType() == TaskTypeEnum.PPAP.getState()
						|| task.getType() == TaskTypeEnum.SOP.getState()) {
					if (task.getState() == SamplingTaskEnum.ACCOMPLISH.getState()) {
						if (task.getFailNum() == 0) {
							result = "合格";
						} else if (task.getFailNum() == 1) {
							result = "一次不合格";
						} else {
							result = "两次不合格";
						}
					}
				}
				cell4.setCellValue(result);

				Cell cell5 = contentRow.createCell(4);
				cell5.setCellStyle(styles.get("cell"));
				cell5.setCellValue(task.getInfo().getVehicle().getType());

				Cell cell6 = contentRow.createCell(5);
				cell6.setCellStyle(styles.get("cell"));
				if (task.getType() != TaskTypeEnum.GS.getState()) {
					cell6.setCellValue(task.getInfo().getParts().getCode());
				}

				Cell cell7 = contentRow.createCell(6);
				cell7.setCellStyle(styles.get("cell"));
				if (task.getType() != TaskTypeEnum.GS.getState()) {
					cell7.setCellValue(task.getInfo().getParts().getName());
				}

				Cell cell8 = contentRow.createCell(7);
				cell8.setCellStyle(styles.get("cell"));
				if (task.getType() != TaskTypeEnum.GS.getState()) {
					if (task.getInfo().getParts().getOrg() != null) {
						cell8.setCellValue(task.getInfo().getParts().getOrg().getName());
					}
				}

				Cell cell9 = contentRow.createCell(8);
				cell9.setCellStyle(styles.get("cell"));
				cell9.setCellValue(task.getInfo().getMaterial().getMatName());

				Cell cell10 = contentRow.createCell(9);
				cell10.setCellStyle(styles.get("cell"));
				if (task.getInfo().getMaterial().getOrg() != null) {
					cell10.setCellValue(task.getInfo().getMaterial().getOrg().getName());
				}

				Cell cell11 = contentRow.createCell(10);
				cell11.setCellStyle(styles.get("cell"));
				cell11.setCellValue(task.getAccount().getUserName());

				Cell cell12 = contentRow.createCell(11);
				cell12.setCellStyle(styles.get("cell"));
				cell12.setCellValue(sdf.format(task.getCreateTime()));

				Cell cell13 = contentRow.createCell(12);
				cell13.setCellStyle(styles.get("cell"));
				if (task.getConfirmTime() != null) {
					cell13.setCellValue(sdf.format(task.getConfirmTime()));
				}

				r++;
			}

			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			logger.error("任务清单导出失败");

			e.printStackTrace();
		}
	}

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
	 * 判断用户的角色 return type self: 自身任务，ots-ots任务，gs-gs任务，all-所有任务
	 */
	protected String judgeAccountRole(HttpServletRequest request) {
		String type = "self";

		// 审批人（ots任务）
		if (hasPermission(request, "otsApprove")) {
			type = "ots";
		}

		// 材料研究审批人（gs任务）
		if (hasPermission(request, "gsApprove")) {
			type = "gs";
		}

		// SOE审批人（全范围）
		if (hasPermission(request, "ppapApprove") || hasPermission(request, "sopApprove")) {
			type = "all";
		}

		return type;
	}

	/**
	 * 查看角色是否有指定权限
	 */
	protected boolean hasPermission(HttpServletRequest request, String alias) {
		List<Menu> menuList = (List<Menu>) request.getSession().getAttribute(Contants.CURRENT_PERMISSION_MENU);

		// 查看当前是否有审核、审批、上传结果、比对、结果确认权限
		for (Menu menu : menuList) {
			if (menu.getSubList() != null && menu.getSubList().size() > 0) {
				for (Menu subMenu : menu.getSubList()) {
					if (subMenu.getSubList() != null && subMenu.getSubList().size() > 0) {
						for (Menu sMenu : subMenu.getSubList()) {
							if (StringUtils.isNotBlank(sMenu.getAlias())) {
								if (alias.equals(sMenu.getAlias())) {
									return true;
								}
							}
						}
					} else {
						if (StringUtils.isNotBlank(subMenu.getAlias())) {
							if (alias.equals(subMenu.getAlias())) {
								return true;
							}
						}
					}
				}
			} else {
				if (StringUtils.isNotBlank(menu.getAlias())) {
					if (alias.equals(menu.getAlias())) {
						return true;
					}
				}
			}
		}

		return false;
	}

}
