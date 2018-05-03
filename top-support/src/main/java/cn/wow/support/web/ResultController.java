package cn.wow.support.web;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import cn.wow.common.service.AccountService;
import cn.wow.common.service.AtlasResultService;
import cn.wow.common.service.InfoService;
import cn.wow.common.service.LabConclusionService;
import cn.wow.common.service.LabReqService;
import cn.wow.common.service.MenuService;
import cn.wow.common.service.PfResultService;
import cn.wow.common.service.TaskService;
import cn.wow.common.utils.AjaxVO;
import cn.wow.common.utils.Contants;
import cn.wow.common.utils.ImportExcelUtil;
import cn.wow.common.utils.pagination.PageMap;
import cn.wow.common.utils.taskState.SamplingTaskEnum;
import cn.wow.common.utils.taskState.StandardTaskEnum;
import cn.wow.common.utils.taskState.TaskTypeEnum;

/**
 * 结果控制器
 * 
 * @author zhenjunzhuo 2017-10-17
 */
@Controller
@RequestMapping(value = "result")
public class ResultController extends AbstractController {

	Logger logger = LoggerFactory.getLogger(ResultController.class);

	// 结果上传列表
	private final static String UPLOAD_DEFAULT_PAGE_SIZE = "10";
	// 结果发送列表
	private final static String SEND_DEFAULT_PAGE_SIZE = "10";
	// 任务记录列表
	private final static String RECORD_DEFAULT_PAGE_SIZE = "10";
	// 结果确认列表
	private final static String CONFIRM_DEFAULT_PAGE_SIZE = "10";
	// 结果对比列表
	private final static String COMPARE_DEFAULT_PAGE_SIZE = "10";
	
	// 图谱图片上传路径
	@Value("${result.atlas.url}")
	protected String atlasUrl;
	
	@Autowired
	private MenuService menuService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private AtlasResultService atlasResultService;
	@Autowired
	private PfResultService pfResultService;
	@Autowired
	private InfoService infoService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private LabReqService labReqService;
	@Autowired
	private LabConclusionService labConclusionService;
	
	
	//-----------------------------------    结果上传        ---------------------------------------------------------------
	
	/**
	 * 结果上传列表
	 * @param type  类型：1-型式结果上传 2-图谱结果上传
	 */
	@RequestMapping(value = "/uploadList")
	public String uploadList(HttpServletRequest request, HttpServletResponse response, Model model, int type) {
		Menu menu = null;
		
		if(type == 1){
			menu = menuService.selectByAlias("patternUpload");
		}else{
			menu = menuService.selectByAlias("atlasUpload");
		}
		
		model.addAttribute("defaultPageSize", UPLOAD_DEFAULT_PAGE_SIZE);
		model.addAttribute("recordPageSize", RECORD_DEFAULT_PAGE_SIZE);
		model.addAttribute("menuName", menu.getName());
		model.addAttribute("type", type);
		return "result/upload_list";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "/uploadListData")
	public Map<String, Object> uploadListData(HttpServletRequest request, Model model, String code, String orgId,
			String startCreateTime, String endCreateTime, String nickName, int type, String parts_code,
			String parts_name, String parts_org, String matName, String mat_org, String vehicle_type) {
		Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);
		
		// 设置默认记录数
		String pageSize = request.getParameter("pageSize");
		if (!StringUtils.isNotBlank(pageSize)) {
			request.setAttribute("pageSize", UPLOAD_DEFAULT_PAGE_SIZE);
		}

		Map<String, Object> map = new PageMap(request);
		map.put("custom_order_sql", "t.create_time desc");
		map.put("state", StandardTaskEnum.TESTING.getState());
		
		// 超级管理员具有所有的权限
		if(account.getRole() == null || !Contants.SUPER_ROLE_CODE.equals(account.getRole().getCode())){
			if(type == 1){  //型式结果
				map.put("patternTask", account.getOrgId() == null ? -1: account.getOrgId());
			}else{   // 图谱结果
				map.put("atlasTask", account.getOrgId() == null ? -1: account.getOrgId());
			}
		}else{
			if(type == 1){
				map.put("patternTask_admin", true);
			}else{
				map.put("atlasTask_admin", true);
			}
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
		if(iIdList.size() > 0 ) {
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
	 * @param type  类型：1-型式结果上传 2-图谱结果上传
	 */
	@RequestMapping(value = "/uploadDetail")
	public String uploadDetail(HttpServletRequest request, HttpServletResponse response, Model model, Long id, int type) {
		if (id != null) {
			Task task = taskService.selectOne(id);

			model.addAttribute("facadeBean", task);
		}
		
		if(type == 1){
			// 零部件试验次数
			int pNum = pfResultService.getExpNoByCatagory(id, 1);
			// 原材料试验次数
			int mNum = pfResultService.getExpNoByCatagory(id, 2);
			
			model.addAttribute("pNum", pNum + 1);
			model.addAttribute("mNum", mNum + 1);
		}
		
		List<LabReq> labReqList =  labReqService.getLabReqListByTaskId(id);
		model.addAttribute("labReqList", labReqList);

		model.addAttribute("resUrl", resUrl);
		model.addAttribute("type", type);
		model.addAttribute("superRoleCole", Contants.SUPER_ROLE_CODE);
		return "result/upload_detail";
	}

	/**
	 * 图谱结果上传
	 * 
	 * @param taskId  任务ID
	 * @param p_tgLab   零部件热重分析描述
	 * @param p_infLab  零部件红外光分析描述
	 * @param p_dtLab   零部件差热扫描描述
	 * @param m_tgLab   原材料热重分析描述
	 * @param m_infLab  原材料红外光分析描述
	 * @param m_dtLab   原材料差热扫描描述
	 */
	@ResponseBody
	@RequestMapping(value = "/atlasUpload")
	public AjaxVO atlasUpload(HttpServletRequest request, Model model, Long taskId, 
			String p_tgLab, String p_infLab, String p_dtLab, String m_tgLab, String m_infLab, String m_dtLab,  String m_tempLab, String p_tempLab,
			@RequestParam(value = "p_tgLab_pic", required = false) MultipartFile p_tgfile,
			@RequestParam(value = "p_infLab_pic", required = false) MultipartFile p_infile,
			@RequestParam(value = "p_dtLab_pic", required = false) MultipartFile p_dtfile,
			@RequestParam(value = "m_tgLab_pic", required = false) MultipartFile m_tgfile,
			@RequestParam(value = "m_infLab_pic", required = false) MultipartFile m_infile,
			@RequestParam(value = "m_dtLab_pic", required = false) MultipartFile m_dtfile,
			@RequestParam(value = "m_tempLab_pic", required = false) MultipartFile m_tempfile,
			@RequestParam(value = "p_tempLab_pic", required = false) MultipartFile p_tempfile, String conclusionResult) {
		AjaxVO vo = new AjaxVO();
		String pic = null;
		
		try {
			Date date = new Date();
			List<AtlasResult> dataList = new ArrayList<AtlasResult>();
			
			/**  零部件结果  **/
			if(p_tgfile != null){
				// 零部件试验次数
				int pNum = atlasResultService.getExpNoByCatagory(taskId, 1);
				
				// 样品照片
				if (p_tempfile != null && !p_tempfile.isEmpty()) {
					pic = uploadImg(p_tempfile, atlasUrl + taskId + "/parts/temp/", false);
				}
				AtlasResult p_temp = new AtlasResult(taskId, 4, pic, p_tempLab, 1, pNum + 1, date);
				
				// 热重分析
				if (p_tgfile != null && !p_tgfile.isEmpty()) {
					pic = uploadImg(p_tgfile, atlasUrl + taskId + "/parts/tg/", false);
				}
				AtlasResult p_tg = new AtlasResult(taskId, 3, pic, p_tgLab, 1, pNum + 1, date);
				
				// 红外光分析
				if (p_infile != null && !p_infile.isEmpty()) {
					pic = uploadImg(p_infile, atlasUrl + taskId + "/parts/inf/", false);
				}
				AtlasResult p_inf = new AtlasResult(taskId, 1, pic, p_infLab, 1, pNum + 1, date);
				
				// 差热扫描
				if (p_dtfile != null && !p_dtfile.isEmpty()) {
					pic = uploadImg(p_dtfile, atlasUrl + taskId + "/parts/dt/", false);
				}
				AtlasResult p_dt = new AtlasResult(taskId, 2, pic, p_dtLab, 1, pNum + 1, date);
				
				dataList.add(p_tg);
				dataList.add(p_inf);
				dataList.add(p_dt);
				dataList.add(p_temp);
			}
			
			/** 原材料结果  **/
			if(m_tgfile != null){
				// 原材料试验次数
				int mNum = atlasResultService.getExpNoByCatagory(taskId, 2);
				
				// 样品照片
				if (m_tempfile != null && !m_tempfile.isEmpty()) {
					pic = uploadImg(m_tempfile, atlasUrl + taskId + "/material/temp/", false);
				}
				AtlasResult m_temp = new AtlasResult(taskId, 4, pic, m_tempLab, 2, mNum + 1, date);
				
				// 热重分析
				if (m_tgfile != null && !m_tgfile.isEmpty()) {
					pic = uploadImg(m_tgfile, atlasUrl + taskId + "/material/tg/", false);
				}
				AtlasResult m_tg = new AtlasResult(taskId, 3, pic, m_tgLab, 2, mNum + 1, date);
				
				// 红外光分析
				if (m_infile != null && !m_infile.isEmpty()) {
					pic = uploadImg(m_infile, atlasUrl + taskId + "/material/inf/", false);
				}
				AtlasResult m_inf = new AtlasResult(taskId, 1, pic, m_infLab, 2, mNum + 1, date);
				
				// 差热扫描
				if (m_dtfile != null && !m_dtfile.isEmpty()) {
					pic = uploadImg(m_dtfile, atlasUrl + taskId + "/material/dt/", false);
				}
				AtlasResult m_dt = new AtlasResult(taskId, 2, pic, m_dtLab, 2, mNum + 1, date);
				
				dataList.add(m_tg);
				dataList.add(m_inf);
				dataList.add(m_dt);
				dataList.add(m_temp);
			}
			
			ObjectMapper mapper = new ObjectMapper();
			List<LabConclusion> conclusionDataList = mapper.readValue(conclusionResult, new TypeReference<List<LabConclusion>>() { });
			
			Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);
			atlasResultService.upload(account, dataList, taskId, date, conclusionDataList);
		} catch (Exception ex) {
			logger.error("图谱上传失败", ex);

			vo.setSuccess(false);
			vo.setMsg("操作失败，系统异常，请重试");
			return vo;
		}

		vo.setSuccess(true);
		vo.setMsg("操作成功");
		return vo;
	}
	
	
	/**
	 * 型式结果上传
	 * 
	 * @param taskId  任务ID
	 * @param result  试验结果
	 * @param conclusionResult 结论结果
	 */
	@ResponseBody
	@RequestMapping(value = "/patternUpload")
	public AjaxVO patternUpload(HttpServletRequest request, Model model, Long taskId, String result, String conclusionResult){
		AjaxVO vo = new AjaxVO();
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			List<PfResult> dataList = mapper.readValue(result, new TypeReference<List<PfResult>>() {});
			List<LabConclusion> conclusionDataList = mapper.readValue(conclusionResult, new TypeReference<List<LabConclusion>>() { });
	        
	        Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);
	        pfResultService.upload(account, dataList, taskId, conclusionDataList);
	        
		}catch(Exception ex){
			logger.error("型式结果上传失败", ex);

			vo.setSuccess(false);
			vo.setMsg("操作失败，系统异常，请重试");
			return vo;
		}
		
		vo.setSuccess(true);
		vo.setMsg("操作成功");
		return vo;
	}

	
	//-----------------------------------    结果发送        ---------------------------------------------------------------
	
	/**
	 * 结果发送列表
	 */
	@RequestMapping(value = "/sendList")
	public String sendList(HttpServletRequest request, HttpServletResponse response, Model model) {
		Menu menu = menuService.selectByAlias("send");
		
		model.addAttribute("defaultPageSize", SEND_DEFAULT_PAGE_SIZE);
		model.addAttribute("recordPageSize", RECORD_DEFAULT_PAGE_SIZE);
		model.addAttribute("menuName", menu.getName());
		return "result/send_list";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "/sendListData")
	public Map<String, Object> sendListData(HttpServletRequest request, Model model, String code, String orgId,
			String startCreateTime, String endCreateTime, String nickName, String parts_code, String parts_name,
			String parts_org, String req_name, String matName, String mat_org, String vehicle_type) {
		
		Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);
		
		// 设置默认记录数
		String pageSize = request.getParameter("pageSize");
		if (!StringUtils.isNotBlank(pageSize)) {
			request.setAttribute("pageSize", SEND_DEFAULT_PAGE_SIZE);
		}

		Map<String, Object> map = new PageMap(request);
		map.put("custom_order_sql", "t.create_time desc");
		
		// 超级管理员具有所有的权限
		if(account.getRole() == null || !Contants.SUPER_ROLE_CODE.equals(account.getRole().getCode())){
			map.put("sendTask", account.getOrgId());
		}else{
			map.put("sendTask_admin", true);
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
		if(iIdList.size() > 0 ) {
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
	@RequestMapping(value = "/sendDetail")
	public String sendDetail(HttpServletRequest request, HttpServletResponse response, Model model, Long id) {
		if (id != null) {
			Task task = taskService.selectOne(id);

			// 性能结果
			if (task.getMatPatResult() == 2 || task.getPartsPatResult() == 2) {
				Map<String, Object> pfMap = new HashMap<String, Object>();
				pfMap.put("tId", id);
				pfMap.put("custom_order_sql", "exp_no asc");
				List<PfResult> pfDataList = pfResultService.selectAllList(pfMap);

				Map<Integer, List<PfResult>> pPfResult = new HashMap<Integer, List<PfResult>>();
				Map<Integer, List<PfResult>> mPfResult = new HashMap<Integer, List<PfResult>>();
				pfResultService.assemblePfResult(pfDataList, pPfResult, mPfResult);

				// 原材料型式结果
				model.addAttribute("mPfResult", mPfResult);
				// 零部件型式结果
				model.addAttribute("pPfResult", pPfResult);
			}

			// 图谱结果
			if (task.getMatAtlResult() == 2 || task.getPartsAtlResult() == 2) {
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
			}

			if(task.getType() == TaskTypeEnum.PPAP.getState() || task.getType() == TaskTypeEnum.SOP.getState() ){
				// 对比结果
				Map<String, List<ExamineRecord>> compareResult = atlasResultService.assembleCompareResult(id);
				model.addAttribute("compareResult", compareResult);
			}
			
			// 获取操作的用户（任务阶段）
			Integer state = null;
			if(task.getType() == TaskTypeEnum.OTS.getState() || task.getType() == TaskTypeEnum.GS.getState()) {
				state = 4;
			}else {
				state = 2;
			}
			
			List<Account> accountList = null;
			if(task.gettId() == null) {
				accountList = accountService.getOperationUser(task.getId(), state);
			}else {
				accountList = accountService.getOperationUser(task.gettId(), state);
			}
			
			if (accountList != null && accountList.size() > 0) {
				StringBuffer emails = new StringBuffer();
				StringBuffer names = new StringBuffer();

				for (int i = 0; i < accountList.size(); i++) {
					Account account = accountList.get(i);

					if (StringUtils.isNotBlank(account.getEmail())) {
						if (i != accountList.size() - 1) {
							emails.append(account.getEmail() + ";");
							names.append(account.getUserName() + ",");
						} else {
							emails.append(account.getEmail());
							names.append(account.getUserName());
						}
					}
				}
				model.addAttribute("sendEmails", emails.toString());
				model.addAttribute("sendNames", names.toString());
			}
			
			model.addAttribute("facadeBean", task);
			
			// 试验要求
			List<LabReq> labReqList =  labReqService.getLabReqListByTaskId(id);
			model.addAttribute("labReqList", labReqList);
			
			
			// 试验结论
			List<LabConclusion> conclusionList = labConclusionService.selectByTaskId(id);
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

		model.addAttribute("resUrl", resUrl);
		model.addAttribute("superRoleCole", Contants.SUPER_ROLE_CODE);
		return "result/send_detail";
	}
	
	
	/**
	 * 结果发送
	 * @param taskId  任务ID
	 * @param pAtlVal    零部件图谱
	 * @param pPatVal    零部件型式
	 * @param mAtlVal    原材料图谱
	 * @param mPatVal    原材料型式   
	 * @param type       类型：1-发送结果， 2-不发送，直接跳过 
	 */
	@ResponseBody 
	@RequestMapping(value = "/sendResult")
	public AjaxVO sendResult(HttpServletRequest request, Model model, Long taskId, String pAtlVal, String pPatVal, String mAtlVal, String mPatVal, Integer type){
		AjaxVO vo = new AjaxVO();
		
		try {
			Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);
			taskService.sendResult(account, taskId, pAtlVal, pPatVal, mAtlVal, mPatVal, type);
		}catch(Exception ex){
			logger.error("结果发送失败", ex);

			vo.setSuccess(false);
			vo.setMsg("操作失败，系统异常，请重试");
			return vo;
		}
		
		vo.setSuccess(true);
		vo.setMsg("操作成功");
		return vo;
	}
	
	
	/**
	 * 用户选择
	 * @param model
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/userList")
	public String userList(HttpServletRequest httpServletRequest, Model model, String type) {
		model.addAttribute("defaultPageSize", SEND_DEFAULT_PAGE_SIZE);
		model.addAttribute("resUrl", resUrl);
		model.addAttribute("type", type);
		return "result/account_list";
	} 
	
	
	//-----------------------------------    结果确认        ---------------------------------------------------------------
	
	/**
	 * 结果确认列表
	 * @param type  类型：1-待上传结果，2-已上传结果
	 */
	@RequestMapping(value = "/confirmList")
	public String confirmList(HttpServletRequest request, HttpServletResponse response, Model model, int type) {
		Menu menu = null;
		
		if(type == 1){
			menu = menuService.selectByAlias("waitConfirm");
		}else{
			menu = menuService.selectByAlias("finishConfirm");
		}
		
		model.addAttribute("defaultPageSize", CONFIRM_DEFAULT_PAGE_SIZE);
		model.addAttribute("recordPageSize", RECORD_DEFAULT_PAGE_SIZE);
		model.addAttribute("menuName", menu.getName());
		model.addAttribute("type", type);
		return "result/confirm_list";
	}

	/**
	 * 列表数据
	 * @param type  类型：1-待上传结果，2-已上传结果
	 */
	@ResponseBody
	@RequestMapping(value = "/confirmListData")
	public Map<String, Object> confirmListData(HttpServletRequest request, Model model, String code, String orgId,
			String startCreateTime, String endCreateTime, String nickName, int type, String parts_code, String parts_name,
			String parts_org, String req_name, String matName, String mat_org, String vehicle_type) {
		
		// 设置默认记录数
		String pageSize = request.getParameter("pageSize");
		if (!StringUtils.isNotBlank(pageSize)) {
			request.setAttribute("pageSize", CONFIRM_DEFAULT_PAGE_SIZE);
		}

		Map<String, Object> map = new PageMap(request);
		map.put("custom_order_sql", "t.create_time desc");
		
		if(type == 1){
			map.put("confirmTask_wait", true);
		}else{
			map.put("confirmTask_finish", true);
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
		if(iIdList.size() > 0 ) {
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
	public String confirmDetail(HttpServletRequest request, HttpServletResponse response, Model model, Long id, int type) {
		if (id != null) {
			Task task = taskService.selectOne(id);

			// 试验结论
			List<LabConclusion> conclusionList = labConclusionService.selectByTaskId(id);
			
			if(task.getType() == TaskTypeEnum.OTS.getState() || task.getType() == TaskTypeEnum.GS.getState()){   // OTS/GS 结果确认
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
				
			}else if(task.getType() == TaskTypeEnum.PPAP.getState() || task.getType() == TaskTypeEnum.SOP.getState() ){
				
				// 基准图谱结果
				List<AtlasResult> sd_pAtlasResult = atlasResultService.getStandardAtlResult(task.getiId(), 1);
				List<AtlasResult> st_mAtlasResult = atlasResultService.getStandardAtlResult(task.getiId(), 2);
				
				// 抽样图谱结果
				Map<String, Object> atMap = new HashMap<String, Object>();
				atMap.put("tId", id);
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
				Map<String, List<ExamineRecord>> compareResult = atlasResultService.assembleCompareResult(id);
				
				model.addAttribute("mAtlasResult", mAtlasResult);
				model.addAttribute("pAtlasResult", pAtlasResult);
				model.addAttribute("compareResult", compareResult);
				
				if (conclusionList != null && conclusionList.size() > 0) {
					model.addAttribute("conclusionList", conclusionList);
				}
			}
						
			model.addAttribute("facadeBean", task);
			
			List<LabReq> labReqList = labReqService.getLabReqListByTaskId(id);
			model.addAttribute("labReqList", labReqList);
		}
		
		model.addAttribute("resUrl", resUrl);
		model.addAttribute("type", type);
		return "result/confirm_detail";
	}
	
	
	/**
	 * 结果确认
	 * @param taskId  任务ID
	 * @param result  结果：1-接收，2-不接收
	 * @param type    类型：1-零部件图谱试验，2-零部件型式试验，3-原材料图谱试验，4-原材料型式试验，5-全部  （针对OTS任务类型）
	 * @param remark  不接受的理由
	 * @param orgs    发送警告书的机构
	 */
	@ResponseBody
	@RequestMapping(value = "/confirmResult")
	public AjaxVO confirmResult(HttpServletRequest request, Model model, Long taskId, int result, int type, String remark, String orgs){
		AjaxVO vo = new AjaxVO();
		
		try {
			Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);
			taskService.confirmResult(account, taskId, result, type, remark, orgs);
		}catch(Exception ex){
			logger.error("结果接收失败", ex);

			vo.setSuccess(false);
			vo.setMsg("操作失败，系统异常，请重试");
			return vo;
		}
		
		vo.setSuccess(true);
		vo.setMsg("操作成功");
		return vo;
	}
	
	
	//-----------------------------------    结果对比       ---------------------------------------------------------------
	
	/**
	 * 结果对比列表
	 */
	@RequestMapping(value = "/compareList")
	public String compareList(HttpServletRequest request, HttpServletResponse response, Model model) {
		Menu menu = menuService.selectByAlias("compare");
		
		model.addAttribute("defaultPageSize", COMPARE_DEFAULT_PAGE_SIZE);
		model.addAttribute("recordPageSize", RECORD_DEFAULT_PAGE_SIZE);
		model.addAttribute("menuName", menu.getName());
		return "result/compare_list";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "/compareListData")
	public Map<String, Object> compareListData(HttpServletRequest request, Model model, String code, String orgId,
			String startCreateTime, String endCreateTime, String nickName, String parts_code, String parts_name,
			String parts_org, String req_name, String matName, String mat_org, String vehicle_type) {
		
		// 设置默认记录数
		String pageSize = request.getParameter("pageSize");
		if (!StringUtils.isNotBlank(pageSize)) {
			request.setAttribute("pageSize", COMPARE_DEFAULT_PAGE_SIZE);
		}

		Map<String, Object> map = new PageMap(request);
		map.put("custom_order_sql", "t.create_time desc");
		map.put("state", SamplingTaskEnum.COMPARE.getState());
		map.put("compareTask", true);

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
		if(iIdList.size() > 0 ) {
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
	@RequestMapping(value = "/compareDetail")
	public String compareDetail(HttpServletRequest request, HttpServletResponse response, Model model, Long id) {
		if (id != null) {
			Task task = taskService.selectOne(id);

			// 基准图谱结果
			List<AtlasResult> sd_pAtlasResult = atlasResultService.getStandardAtlResult(task.getiId(), 1);
			List<AtlasResult> st_mAtlasResult = atlasResultService.getStandardAtlResult(task.getiId(), 2);
			
			// 抽样图谱结果
			Map<String, Object> atMap = new HashMap<String, Object>();
			atMap.put("tId", id);
			atMap.put("custom_order_sql", "exp_no desc limit 8");
			List<AtlasResult> atDataList = atlasResultService.selectAllList(atMap);

			List<AtlasResult> sl_pAtlasResult = new ArrayList<AtlasResult>();
			List<AtlasResult> sl_mAtlasResult = new ArrayList<AtlasResult>();
			groupAtlasResult(atDataList, sl_pAtlasResult, sl_mAtlasResult);
			
			// 零部件图谱结果
			Map<Integer, CompareVO> pAtlasResult = atlasResultService.assembleCompareAtlas(sd_pAtlasResult, sl_pAtlasResult);
			// 原材料图谱结果
			Map<Integer, CompareVO> mAtlasResult = atlasResultService.assembleCompareAtlas(st_mAtlasResult, sl_mAtlasResult);

			// 原材料图谱结果
			model.addAttribute("mAtlasResult", mAtlasResult);
			// 零部件图谱结果
			model.addAttribute("pAtlasResult", pAtlasResult);
						
			model.addAttribute("facadeBean", task);
			
			List<LabReq> labReqList =  labReqService.getLabReqListByTaskId(id);
			model.addAttribute("labReqList", labReqList);
		}
		
		model.addAttribute("resUrl", resUrl);
		return "result/compare_detail";
	}
	
	
	/**
	 * 结果对比
	 * @param taskId  任务ID
	 * @param p_temp            零部件样品照片一致性
	 * @param p_temp_remark     零部件样品照片结论
	 * @param p_inf             零部件红外光一致性
	 * @param p_inf_remark      零部件红外光结论
	 * @param p_dt 				零部件差热一致性
	 * @param p_dt_remark       零部件差热结论
	 * @param p_tg              零部件热重一致性
	 * @param p_tg_remark       零部件热重光结论
	 * @param p_result          零部件结论一致性
	 * @param p_result_remark   零部件结论
	 * @param m_temp            原材料样品照片一致性
	 * @param m_temp_remark     原材料样品照片结论
	 * @param m_inf             原材料红外光一致性
	 * @param m_inf_remark      原材料红外光结论
	 * @param m_dt              原材料差热一致性
	 * @param m_dt_remark       原材料差热结论
	 * @param m_tg              原材料热重一致性
	 * @param m_tg_remark       原材料热重光结论
	 * @param m_result          原材料结论一致性
	 * @param m_result_remark   原材料结论
	 * @param state             状态：1-正常，2-异常
	 */
	@ResponseBody
	@RequestMapping(value = "/compareResult")
	public AjaxVO compareResult(HttpServletRequest request, Model model, Long taskId, Integer p_inf, String p_inf_remark,
			Integer p_dt, String p_dt_remark, Integer p_tg, String p_tg_remark, Integer p_result, String p_result_remark, Integer m_inf,
			String m_inf_remark, Integer m_dt, String m_dt_remark, Integer m_tg, String m_tg_remark, Integer m_result,
			String m_result_remark, Integer state, Integer p_temp, String p_temp_remark, Integer m_temp, String m_temp_remark) {

		AjaxVO vo = new AjaxVO();

		try {
			Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);
			Date date = new Date();

			// 根据一致性结论来判断任务是否合格
			int result = 1;
			if((p_result != null && p_result == 2) || (m_result != null && m_result == 2)) {
				result = 2;
			}
			
			// 对比结果
			List<ExamineRecord> resultList = new ArrayList<ExamineRecord>();

			if (state == 1) {
				if(p_inf != null) {
					ExamineRecord record1 = new ExamineRecord(taskId, account.getId(), p_inf, p_inf_remark, 4, 1, date, TaskTypeEnum.PPAP.getState());
					ExamineRecord record2 = new ExamineRecord(taskId, account.getId(), p_dt, p_dt_remark, 4, 2, date, TaskTypeEnum.PPAP.getState());
					ExamineRecord record3 = new ExamineRecord(taskId, account.getId(), p_tg, p_tg_remark, 4, 3, date, TaskTypeEnum.PPAP.getState());
					ExamineRecord record4 = new ExamineRecord(taskId, account.getId(), p_result, p_result_remark, 4, 4, date, TaskTypeEnum.PPAP.getState());
					ExamineRecord record9 = new ExamineRecord(taskId, account.getId(), p_temp, p_temp_remark, 4, 9, date, TaskTypeEnum.PPAP.getState());
					
					resultList.add(record1);
					resultList.add(record2);
					resultList.add(record3);
					resultList.add(record4);
					resultList.add(record9);
				}
				
				if(m_inf != null) {
					ExamineRecord record5 = new ExamineRecord(taskId, account.getId(), m_inf, m_inf_remark, 4, 5, date, TaskTypeEnum.PPAP.getState());
					ExamineRecord record6 = new ExamineRecord(taskId, account.getId(), m_dt, m_dt_remark, 4, 6, date, TaskTypeEnum.PPAP.getState());
					ExamineRecord record7 = new ExamineRecord(taskId, account.getId(), m_tg, m_tg_remark, 4, 7, date, TaskTypeEnum.PPAP.getState());
					ExamineRecord record8 = new ExamineRecord(taskId, account.getId(), m_result, m_result_remark, 4, 8, date, TaskTypeEnum.PPAP.getState());
					ExamineRecord record10 = new ExamineRecord(taskId, account.getId(), m_temp, m_temp_remark, 4, 10, date, TaskTypeEnum.PPAP.getState());
					
					resultList.add(record5);
					resultList.add(record6);
					resultList.add(record7);
					resultList.add(record8);
					resultList.add(record10);
				}
			}

			taskService.compareResult(account, taskId, resultList, state, result);
		} catch (Exception ex) {
			logger.error("结果对比失败", ex);

			vo.setSuccess(false);
			vo.setMsg("操作失败，系统异常，请重试");
			return vo;
		}

		vo.setSuccess(true);
		vo.setMsg("操作成功");
		return vo;
	}
	
	
	//-----------------------------------    结果导入\导出      ---------------------------------------------------------------
	@ResponseBody
	@RequestMapping(value = "/importResult")
	public AjaxVO importResult(HttpServletRequest request, HttpServletResponse response) throws Exception {
		AjaxVO vo = new AjaxVO();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		
		List<String> titleList = new ArrayList<String>();
		titleList.add("类型(零部件/原材料)");
		titleList.add("试验项目");
		titleList.add("参考标准");
		titleList.add("试验要求");
		titleList.add("试验结果");
		titleList.add("结果评价");
		titleList.add("备注");
		
		MultipartFile file = multipartRequest.getFile("upfile");
		if (file.isEmpty()) {
			vo.setSuccess(false);
			vo.setMsg("文件不存在");
			return vo;
		}
		
		try {
			InputStream in = file.getInputStream();
			List<List<Object>> execelList = new ImportExcelUtil().getBankListByExcel(in, file.getOriginalFilename());
			List<PfResult> createList = new ArrayList<PfResult>();

			if (execelList != null && execelList.size() > 0) {
				// 检查模板是否正确
				List<Object> titleObj = execelList.get(0);
				if(titleObj == null || titleObj.size() < 7) {
					vo.setSuccess(false);
					vo.setData("导入模板不正确");
					return vo;
				}else {
					boolean flag = true;

					if (!"类型(零部件/原材料)".equals(titleObj.get(0).toString())) {
						flag = false;
					}
					if (flag && !"试验项目".equals(titleObj.get(1).toString())) {
						flag = false;
					}
					if (flag && !"参考标准".equals(titleObj.get(2).toString())) {
						flag = false;
					}
					if (flag && !"试验要求".equals(titleObj.get(3).toString())) {
						flag = false;
					}
					if (flag && !"试验结果".equals(titleObj.get(4).toString())) {
						flag = false;
					}
					if (flag && !"结果评价".equals(titleObj.get(5).toString())) {
						flag = false;
					}
					if (flag && !"备注".equals(titleObj.get(6).toString())) {
						flag = false;
					}
					if (!flag) {
						vo.setSuccess(false);
						vo.setMsg("导入模板不正确");
						return vo;
					}
				}

				for (int i = 1; i < execelList.size(); i++) {
					PfResult pf = new PfResult();
					
					List<Object> obj = execelList.get(i);
					
					if(obj.size() < 1 || obj.get(0) == null){
						continue;
					}
					
					String type = obj.get(0).toString();
					if (StringUtils.isBlank(type) || (!"零部件".equals(type) && !"原材料".equals(type))) {
						continue;
					}
					pf.setCatagory("零部件".equals(type) ? 1 : 2);
					
					if(obj.size() >= 2 && obj.get(1) != null){
						pf.setProject(obj.get(1).toString());
					}
					
					if(obj.size() >= 3 && obj.get(2) != null){
						pf.setStandard(obj.get(2).toString());
					}
					
					if(obj.size() >= 4 && obj.get(3) != null){
						pf.setRequire(obj.get(3).toString());
					}
					
					if(obj.size() >= 5 && obj.get(4) != null){
						pf.setResult(obj.get(4).toString());
					}
					
					if(obj.size() >= 6  && obj.get(5) != null){
						pf.setEvaluate(obj.get(5).toString());
					}
					
					if(obj.size() >= 7  && obj.get(6) != null){
						pf.setRemark(obj.get(6).toString());
					}
					createList.add(pf);
				}
			}

			String msg = "导入：" + createList.size() + " 条记录";
			vo.setMsg(msg);
			
			ObjectMapper mapper = new ObjectMapper(); 
			String jsonResult = mapper.writeValueAsString(createList);  
			vo.setData(jsonResult);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("试验结果导入失败：", ex);

			vo.setMsg("导入失败，系统异常");
			vo.setSuccess(false);
			return vo;
		}
		return vo;
	}
	
	
	/**
	 * 导出结果
	 */
	@RequestMapping(value = "/exportResult")
	public void exportResult(HttpServletRequest request, HttpServletResponse response, String mResult, String pResult) {
		String filename = "试验结果清单";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			List<PfResult> dataList = new ArrayList<PfResult>();
			ObjectMapper mapper = new ObjectMapper();
	        List<PfResult> mResultList = mapper.readValue(mResult, new TypeReference<List<PfResult>>() { });
	        List<PfResult> pResultList = mapper.readValue(pResult, new TypeReference<List<PfResult>>() { });
	        
	        if(mResultList != null && mResultList.size() > 0) {
	        	dataList.addAll(mResultList);
	        }
	        
	        if(pResultList != null && pResultList.size() > 0) {
	        	dataList.addAll(pResultList);
	        }
	        
			// 设置头
			ImportExcelUtil.setResponseHeader(response, sdf.format(new Date()) + "_" + filename + ".xlsx");

			Workbook wb = new SXSSFWorkbook(100); // 保持100条在内存中，其它保存到磁盘中
			// 工作簿
			Sheet sh = wb.createSheet("结果清单");
			sh.setColumnWidth(0, (short) 4000);
			sh.setColumnWidth(1, (short) 4000);
			sh.setColumnWidth(2, (short) 4000);
			sh.setColumnWidth(3, (short) 5000);
			sh.setColumnWidth(4, (short) 9000);
			sh.setColumnWidth(5, (short) 6000);
			sh.setColumnWidth(6, (short) 3000);
			sh.setColumnWidth(7, (short) 6000);
			sh.setColumnWidth(8, (short) 6000);
			sh.setColumnWidth(9, (short) 6000);
			
			Map<String, CellStyle> styles = ImportExcelUtil.createStyles(wb);

			String[] titles = { "类型(零部件/原材料)", "试验项目", "参考标准", "试验要求", "试验结果", "结果评价", "备注"};
			int r = 0;
			
			Row titleRow = sh.createRow(0);
			titleRow.setHeight((short) 450);
			for(int k = 0; k < titles.length; k++){
				Cell cell = titleRow.createCell(k);
				cell.setCellStyle(styles.get("header"));
				cell.setCellValue(titles[k]);
			}
			
			++r;
			
			for (int j = 0; j < dataList.size(); j++) {// 添加数据
				Row contentRow = sh.createRow(r);
				contentRow.setHeight((short) 400);
				PfResult pfResult = dataList.get(j);

				Cell cell1 = contentRow.createCell(0);
				cell1.setCellStyle(styles.get("cell"));
				cell1.setCellValue(pfResult.getCatagory().intValue() == 1 ? "零部件": "原材料");

				Cell cell2 = contentRow.createCell(1);
				cell2.setCellStyle(styles.get("cell"));
				if(StringUtils.isNotBlank(pfResult.getProject())) {
					cell2.setCellValue(pfResult.getProject());
				}

				Cell cell3 = contentRow.createCell(2);
				cell3.setCellStyle(styles.get("cell"));
				if(StringUtils.isNotBlank(pfResult.getStandard())) {
					cell3.setCellValue(pfResult.getStandard());
				}
				
				Cell cell4 = contentRow.createCell(3);
				cell4.setCellStyle(styles.get("cell"));
				if(StringUtils.isNotBlank(pfResult.getRequire())) {
					cell4.setCellValue(pfResult.getRequire());
				}
				
				Cell cell5 = contentRow.createCell(4);
				cell5.setCellStyle(styles.get("cell"));
				if(StringUtils.isNotBlank(pfResult.getResult())) {
					cell5.setCellValue(pfResult.getResult());
				}

				Cell cell6 = contentRow.createCell(5);
				cell6.setCellStyle(styles.get("cell"));
				if(StringUtils.isNotBlank(pfResult.getEvaluate())) {
					cell6.setCellValue(pfResult.getEvaluate());
				}
				
				Cell cell7 = contentRow.createCell(6);
				cell7.setCellStyle(styles.get("cell"));
				if(StringUtils.isNotBlank(pfResult.getRemark())) {
					cell7.setCellValue(pfResult.getRemark());
				}
				r++;
			}

			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			logger.error("试验结果清单导出失败");
			
			e.printStackTrace();
		}
	}
	
	
	//-----------------------------------    其它       ---------------------------------------------------------------
	
	
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
