package cn.wow.support.web;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;

import cn.wow.common.domain.Account;
import cn.wow.common.domain.AtlasResult;
import cn.wow.common.domain.CompareVO;
import cn.wow.common.domain.CostRecord;
import cn.wow.common.domain.ExamineRecord;
import cn.wow.common.domain.ExpItem;
import cn.wow.common.domain.LabConclusion;
import cn.wow.common.domain.LabReq;
import cn.wow.common.domain.Menu;
import cn.wow.common.domain.PfResult;
import cn.wow.common.domain.Task;
import cn.wow.common.service.AtlasResultService;
import cn.wow.common.service.CostRecordService;
import cn.wow.common.service.ExpItemService;
import cn.wow.common.service.LabConclusionService;
import cn.wow.common.service.LabReqService;
import cn.wow.common.service.MenuService;
import cn.wow.common.service.PfResultService;
import cn.wow.common.service.TaskService;
import cn.wow.common.utils.AjaxVO;
import cn.wow.common.utils.Contants;
import cn.wow.common.utils.pagination.PageMap;
import cn.wow.common.utils.taskState.TaskTypeEnum;

/**
 * 费用管理控制器
 * 
 * @author zhenjunzhuo 2017-11-04
 */
@Controller
@RequestMapping(value = "cost")
public class CostController extends AbstractController {

	Logger logger = LoggerFactory.getLogger(CostController.class);

	private final static String DEFAULT_PAGE_SIZE = "10";

	@Autowired
	private CostRecordService costRecordService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private AtlasResultService atlasResultService;
	@Autowired
	private PfResultService pfResultService;
	@Autowired
	private ExpItemService expItemService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private LabReqService labReqService;
	@Autowired
	private LabConclusionService labConclusionService;

	@RequestMapping(value = "/list")
	public String list(HttpServletRequest httpServletRequest, Model model, int type) {
		Menu menu = null;
		if(type == 1){
			menu = menuService.selectByAlias("tosend");
		}else{
			menu = menuService.selectByAlias("sent");
		}
		
		model.addAttribute("defaultPageSize", DEFAULT_PAGE_SIZE);
		model.addAttribute("type", type);
		model.addAttribute("menuName", menu.getName());
		return "cost/cost_list";
	}

	/**
	 * 获取数据
	 * @param startCreateTime  创建时间
	 * @param endCreateTime    创建时间
	 * @param code             任务编码
	 * @param type             类型：0-未发送列表 ，1-已发送列表
	 * @param taskType         任务类型
	 * @param labType          实验类型
	 * @param labResult        实验结果
	 */
	@ResponseBody
	@RequestMapping(value = "/getListData")
	public Map<String, Object> getListData(HttpServletRequest request, Model model, String startCreateTime, String endCreateTime, String code, int type, String taskType, String labType, String labResult) {
		
		Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);
		
		// 设置默认记录数
		String pageSize = request.getParameter("pageSize");
		if (!StringUtils.isNotBlank(pageSize)) {
			request.setAttribute("pageSize", DEFAULT_PAGE_SIZE);
		}

		Map<String, Object> map = new PageMap(request);
		map.put("custom_order_sql", "c.create_time desc, t.code asc, c.state asc");

		if (StringUtils.isNotBlank(code)) {
			map.put("code", code);
		}
		if (StringUtils.isNotBlank(startCreateTime)) {
			map.put("startCreateTime", startCreateTime + " 00:00:00");
		}
		if (StringUtils.isNotBlank(endCreateTime)) {
			map.put("endCreateTime", endCreateTime + " 23:59:59");
		}
		if (StringUtils.isNotBlank(taskType)) {
			map.put("taskType", taskType);
		}
		if (StringUtils.isNotBlank(labType)) {
			map.put("labType", labType);
		}
		if (StringUtils.isNotBlank(labResult)) {
			map.put("labResult", labResult);
		}
		
		if (type == 1) {
			map.put("state", 0);

			if (account.getRole() != null && !Contants.SUPER_ROLE_CODE.equals(account.getRole().getCode())) {
				map.put("labId", account.getOrgId());
			}
		} else {
			map.put("state", 1);

			if (account.getRole() != null && !Contants.SUPER_ROLE_CODE.equals(account.getRole().getCode())) {
				map.put("orgs", account.getOrgId());
			}
		}

		List<CostRecord> dataList = costRecordService.selectAllList(map);

		// 分页
		Page<CostRecord> pageList = (Page<CostRecord>) dataList;

		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("total", pageList.getTotal());
		dataMap.put("rows", pageList.getResult());

		return dataMap;
	}

	@RequestMapping(value = "/detail")
	public String detail(HttpServletRequest request, Model model, String id, int type) {
		if (StringUtils.isNotBlank(id)) {
			CostRecord costRecord = costRecordService.selectOne(Long.parseLong(id));
			List<LabConclusion> conclusionList = labConclusionService.selectByTaskId(costRecord.getTask().getId());
			
			if (costRecord.getTask().getType() == TaskTypeEnum.OTS.getState() || costRecord.getTask().getType() == TaskTypeEnum.GS.getState() ) { // OTS 结果确认
				// 性能结果
				Map<String, Object> pfMap = new HashMap<String, Object>();
				pfMap.put("tId", costRecord.getTask().getId());
				pfMap.put("custom_order_sql", "exp_no asc");
				List<PfResult> pfDataList = pfResultService.selectAllList(pfMap);

				Map<Integer, List<PfResult>> pPfResult = new HashMap<Integer, List<PfResult>>();
				Map<Integer, List<PfResult>> mPfResult = new HashMap<Integer, List<PfResult>>();
				pfResultService.assemblePfResult(pfDataList, pPfResult, mPfResult);

				// 图谱结果
				Map<String, Object> atMap = new HashMap<String, Object>();
				atMap.put("tId", costRecord.getTask().getId());
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
				
			} else if (costRecord.getTask().getType() == TaskTypeEnum.PPAP.getState() || costRecord.getTask().getType() == TaskTypeEnum.SOP.getState()) {
				Long iId = costRecord.getTask().getiId();
				
				// 如果是修改生成的记录，基准图谱要取父任务的iID的基准
				if(costRecord.getTask().gettId() != null) {
					Task pTask = taskService.selectOne(costRecord.getTask().gettId());
					iId = pTask.getiId();
				}
				
				// 基准图谱结果
				List<AtlasResult> sd_pAtlasResult = atlasResultService.getStandardAtlResult(iId, 1);
				List<AtlasResult> st_mAtlasResult = atlasResultService.getStandardAtlResult(iId, 2);
				
				// 抽样图谱结果
				Map<String, Object> atMap = new HashMap<String, Object>();
				atMap.put("tId", costRecord.gettId());
				atMap.put("custom_order_sql", "exp_no desc limit 8");
				List<AtlasResult> atDataList = atlasResultService.selectAllList(atMap);

				List<AtlasResult> sl_pAtlasResult = new ArrayList<AtlasResult>();
				List<AtlasResult> sl_mAtlasResult = new ArrayList<AtlasResult>();
				groupAtlasResult(atDataList, sl_pAtlasResult, sl_mAtlasResult);

				// 零部件图谱结果
				Map<Integer, CompareVO> pAtlasResult = atlasResultService.assembleCompareAtlas(sd_pAtlasResult, sl_pAtlasResult);
				// 原材料图谱结果
				Map<Integer, CompareVO> mAtlasResult = atlasResultService.assembleCompareAtlas(st_mAtlasResult, sl_mAtlasResult);
				// 对比结果
				Map<String, List<ExamineRecord>> compareResult = atlasResultService.assembleCompareResult(costRecord.gettId());

				model.addAttribute("mAtlasResult", mAtlasResult);
				model.addAttribute("pAtlasResult", pAtlasResult);
				model.addAttribute("compareResult", compareResult);
				
				// 试验结论
				if (conclusionList != null && conclusionList.size() > 0) {
					model.addAttribute("conclusionList", conclusionList);
				}
			}
			
			// 费用详情
			if (type == 2) {
				Map<String, Object> eMap = new PageMap(false);
				eMap.put("cId", costRecord.getId());
				List<ExpItem> itemList = expItemService.selectAllList(eMap);
				
				model.addAttribute("itemList", itemList);
			}
			
			List<LabReq> labReqList =  labReqService.getLabReqListByTaskId(costRecord.getTask().getId());
			model.addAttribute("labReqList", labReqList);
			
			model.addAttribute("facadeBean", costRecord);
		}
		
		model.addAttribute("resUrl", resUrl);
		model.addAttribute("type", type);
		return "cost/cost_detail";
	}
	
	/**
	 * 费用清单
	 */
	@RequestMapping(value = "/expItemDetail")
	public String expItemDetail(HttpServletRequest request, Model model, String id) {
		if (StringUtils.isNotBlank(id)) {
			CostRecord costRecord = costRecordService.selectOne(Long.parseLong(id));
			model.addAttribute("facadeBean", costRecord);
		}
		return "cost/item_detail";
	}
	
	
	/**
	 * 费用清单发送
	 * @param costId  费用清单ID
	 * @param result  试验项目
	 * @param orgs    发送机构
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/costSend")
	public AjaxVO costSend(HttpServletRequest request, Model model, Long costId, String result, String orgs) {
		AjaxVO vo = new AjaxVO();

		try {
			ObjectMapper mapper = new ObjectMapper();
			List<ExpItem> itemList = mapper.readValue(result, new TypeReference<List<ExpItem>>() {});

			Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);
			costRecordService.costSend(account, costId, orgs, itemList);

		} catch (Exception ex) {
			logger.error("费用清单发送失败", ex);

			vo.setSuccess(false);
			vo.setMsg("操作失败，系统异常，请重试");
			return vo;
		}

		vo.setSuccess(true);
		vo.setMsg("操作成功");
		return vo;
	}

	
	/**
	 * 分组
	 * @param atDataList
	 * @param pAtlasResult
	 * @param mAtlasResult
	 */
	public void groupAtlasResult(List<AtlasResult> arDataList, List<AtlasResult> pAtlasResult,  List<AtlasResult> mAtlasResult) {
		for (AtlasResult ar : arDataList) {
			if (ar.getCatagory() == 1) {
				pAtlasResult.add(ar);
			} else {
				mAtlasResult.add(ar);
			}
		}
	}
}
