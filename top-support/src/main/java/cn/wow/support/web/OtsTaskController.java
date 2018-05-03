package cn.wow.support.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;

import cn.wow.common.domain.Account;
import cn.wow.common.domain.ApplyRecord;
import cn.wow.common.domain.AtlasResult;
import cn.wow.common.domain.ExamineRecord;
import cn.wow.common.domain.LabConclusion;
import cn.wow.common.domain.LabReq;
import cn.wow.common.domain.Material;
import cn.wow.common.domain.Menu;
import cn.wow.common.domain.Parts;
import cn.wow.common.domain.PfResult;
import cn.wow.common.domain.Task;
import cn.wow.common.domain.TaskRecord;
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
import cn.wow.common.service.PfResultService;
import cn.wow.common.service.TaskRecordService;
import cn.wow.common.service.TaskService;
import cn.wow.common.service.VehicleService;
import cn.wow.common.utils.AjaxVO;
import cn.wow.common.utils.Contants;
import cn.wow.common.utils.pagination.PageMap;
import cn.wow.common.utils.taskState.StandardTaskEnum;
import cn.wow.common.utils.taskState.TaskTypeEnum;

/**
 * OTS任务
 * 
 * @author zhenjunzhuo 2017-10-30
 */
@Controller
@RequestMapping(value = "ots")
public class OtsTaskController extends AbstractController {

	Logger logger = LoggerFactory.getLogger(OtsTaskController.class);

	// 申请列表
	private final static String REQUIRE_DEFAULT_PAGE_SIZE = "10";
	// 审核列表
	private final static String EXAMINE_DEFAULT_PAGE_SIZE = "10";
	// 下达任务列表
	private final static String TRANSMIT_DEFAULT_PAGE_SIZE = "10";
	// 任务记录列表
	private final static String RECORD_DEFAULT_PAGE_SIZE = "10";
	// 任务审批列表
	private final static String APPROVE_DEFAULT_PAGE_SIZE = "10";

	// 零部件图片上传图片
	@Value("${info.parts.url}")
	protected String partsUrl;

	// 原材料图片上传路径
	@Value("${info.material.url}")
	protected String materialUrl;

	@Autowired
	private MenuService menuService;
	@Autowired
	private InfoService infoService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private TaskRecordService taskRecordService;
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
	private PfResultService pfResultService;
	@Autowired
	private LabReqService labReqService;
	@Autowired
	private LabConclusionService labConclusionService;

	/**
	 * 首页
	 */
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model, String choose,
			int taskType) {
		HttpSession session = request.getSession();
		Menu menu = null;

		if (taskType == TaskTypeEnum.OTS.getState()) {
			menu = menuService.selectByAlias("otsTask");
		} else if (taskType == TaskTypeEnum.GS.getState()) {
			menu = menuService.selectByAlias("gsTask");
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
		return "task/ots/index";
	}

	/** -------------------------------- 任务申请 --------------------------------- */
	/**
	 * 任务申请列表
	 */
	@RequestMapping(value = "/requireList")
	public String requireList(HttpServletRequest request, HttpServletResponse response, Model model, int taskType) {
		model.addAttribute("defaultPageSize", REQUIRE_DEFAULT_PAGE_SIZE);
		model.addAttribute("taskType", taskType);
		return "task/ots/require_list";
	}

	/**
	 * 列表数据
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/requireListData")
	public Map<String, Object> requireListData(HttpServletRequest request, Model model, String startCreateTime,
			String endCreateTime, Integer state, int taskType, String task_code, String parts_code, String parts_name,
			String parts_org, String req_name, String matName, String mat_org, String vehicle_type) {
		Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);

		// 设置默认记录数
		String pageSize = request.getParameter("pageSize");
		if (!StringUtils.isNotBlank(pageSize)) {
			request.setAttribute("pageSize", REQUIRE_DEFAULT_PAGE_SIZE);
		}

		Map<String, Object> map = new PageMap(request);
		map.put("custom_order_sql", "t.create_time desc");

		if (state == null) {
			map.put("examineState", true);
		} else {
			map.put("state", state);
		}
		map.put("type", taskType);

		if (StringUtils.isNotBlank(startCreateTime)) {
			map.put("startCreateTime", startCreateTime + " 00:00:00");
		}
		if (StringUtils.isNotBlank(endCreateTime)) {
			map.put("endCreateTime", endCreateTime + " 23:59:59");
		}
		if (StringUtils.isNotBlank(task_code)) {
			map.put("code", task_code);
		}
		if (StringUtils.isNotBlank(req_name)) {
			map.put("userName", req_name);
		}

		List<Long> iIdList = infoService.selectIds(vehicle_type, parts_code, parts_name, parts_org, matName, mat_org);
		if (iIdList.size() > 0) {
			map.put("iIdList", iIdList);
		}

		// 除了超级管理员，其它用户只能查看自己录入的申请记录
		if (account.getRole() == null || !Contants.SUPER_ROLE_CODE.equals(account.getRole().getCode())) {
			map.put("aId", account.getId());
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
	 * 任务详情
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/requireDetail")
	public String requireDetail(HttpServletRequest request, HttpServletResponse response, Model model, Long id,
			int taskType) {
		if (id != null) {
			Task task = taskService.selectOne(id);

			// 审核记录
			Map<String, Object> rMap = new PageMap(false);
			rMap.put("taskId", id);
			rMap.put("custom_order_sql", "create_time asc");
			rMap.put("type", 1);
			rMap.put("taskType", task.getType());
			List<ExamineRecord> recordList = examineRecordService.selectAllList(rMap);

			model.addAttribute("facadeBean", task);
			model.addAttribute("recordList", recordList);
		}

		model.addAttribute("resUrl", resUrl);
		model.addAttribute("taskType", taskType);
		return "task/ots/require_detail";
	}

	/**
	 * 任务申请保存
	 */
	@ResponseBody
	@RequestMapping(value = "/save")
	public AjaxVO save(HttpServletRequest request, Model model, Long v_id, String v_code, String v_type,
			String v_proTime, String v_proAddr, String v_remark, String p_code, String p_name, String p_proTime,
			String p_place, String p_proNo, Long p_id, String p_keyCode, Integer p_isKey, Long p_orgId, String p_remark,
			Long m_id, String m_matName, String m_matColor, String m_proNo, Long m_orgId, String m_matNo,
			String m_remark, @RequestParam(value = "m_pic", required = false) MultipartFile mfile, Long t_id,
			int taskType, String p_contacts, String p_phone, String m_contacts, String m_phone) {

		AjaxVO vo = new AjaxVO();

		try {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			// 整车信息
			Vehicle vehicle = null;
			if (v_id == null) {
				vehicle = new Vehicle();
				vehicle.setType(v_type);
				vehicle.setCode(v_code);
				vehicle.setProTime(sdf.parse(v_proTime));
				vehicle.setProAddr(v_proAddr);
				vehicle.setRemark(v_remark);
				vehicle.setState(Contants.ONDOING_TYPE);
				vehicle.setCreateTime(date);

				boolean isExist = vehicleService.isExist(null, v_code, v_type, sdf.parse(v_proTime), v_proAddr,
						v_remark);
				if (isExist) {
					vo.setSuccess(false);
					vo.setMsg("整车信息已存在");
					return vo;
				}
			} else {
				vehicle = vehicleService.selectOne(v_id);
				if (vehicle.getState().intValue() == 0) {
					vehicle.setType(v_type);
					vehicle.setProTime(sdf.parse(v_proTime));
					vehicle.setProAddr(v_proAddr);
					vehicle.setRemark(v_remark);
					vehicle.setCode(v_code);

					boolean isExist = vehicleService.isExist(v_id, v_code, v_type, sdf.parse(v_proTime), v_proAddr,
							v_remark);
					if (isExist) {
						vo.setSuccess(false);
						vo.setMsg("整车信息已存在");
						return vo;
					}
				}
			}

			// 零部件信息
			Parts parts = null;
			if (taskType == TaskTypeEnum.OTS.getState()) {
				if (p_id == null) {
					parts = new Parts();
					parts.setType(Contants.STANDARD_TYPE);
					parts.setProTime(sdf.parse(p_proTime));
					parts.setRemark(p_remark);
					parts.setPlace(p_place);
					parts.setProNo(p_proNo);
					parts.setName(p_name);
					parts.setCode(p_code);
					parts.setIsKey(p_isKey);
					parts.setKeyCode(p_keyCode);
					parts.setOrgId(p_orgId);
					parts.setCreateTime(date);
					parts.setContacts(p_contacts);
					parts.setPhone(p_phone);
					parts.setState(Contants.ONDOING_TYPE);

					boolean isExist = partsService.isExist(null, p_code, p_name, sdf.parse(p_proTime), p_place, p_proNo,
							p_keyCode, p_isKey, p_orgId, p_remark, p_contacts, p_phone);
					if (isExist) {
						vo.setSuccess(false);
						vo.setMsg("零部件信息已存在");
						return vo;
					}
				} else {
					parts = partsService.selectOne(p_id);
					if (parts.getState().intValue() == 0) {
						parts.setProTime(sdf.parse(p_proTime));
						parts.setRemark(p_remark);
						parts.setPlace(p_place);
						parts.setProNo(p_proNo);
						parts.setName(p_name);
						parts.setIsKey(p_isKey);
						parts.setKeyCode(p_keyCode);
						parts.setOrgId(p_orgId);
						parts.setContacts(p_contacts);
						parts.setPhone(p_phone);
						parts.setCode(p_code);

						boolean isExist = partsService.isExist(p_id, p_code, p_name, sdf.parse(p_proTime), p_place,
								p_proNo, p_keyCode, p_isKey, p_orgId, p_remark, p_contacts, p_phone);
						if (isExist) {
							vo.setSuccess(false);
							vo.setMsg("零部件信息已存在");
							return vo;
						}
					}
				}
			}

			// 原材料信息
			Material material = null;
			if (m_id == null) {
				material = new Material();
				material.setType(Contants.STANDARD_TYPE);
				material.setRemark(m_remark);
				material.setProNo(m_proNo);
				material.setMatName(m_matName);
				material.setMatNo(m_matNo);
				material.setMatColor(m_matColor);
				material.setOrgId(m_orgId);
				material.setCreateTime(date);
				material.setContacts(m_contacts);
				material.setPhone(m_phone);
				material.setState(Contants.ONDOING_TYPE);

				if (mfile != null && !mfile.isEmpty()) {
					String pic = uploadImg(mfile, materialUrl, false);
					material.setPic(pic);
				}
			} else {
				material = materialService.selectOne(m_id);
				material.setRemark(m_remark);
				material.setProNo(m_proNo);
				material.setMatName(m_matName);
				material.setMatNo(m_matNo);
				material.setMatColor(m_matColor);
				material.setOrgId(m_orgId);
				material.setContacts(m_contacts);
				material.setPhone(m_phone);

				if (mfile != null && !mfile.isEmpty()) {
					String pic = uploadImg(mfile, materialUrl, false);
					material.setPic(pic);
				}
			}

			Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);
			infoService.insert(account, vehicle, parts, material, Contants.STANDARD_TYPE, t_id, taskType);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("任务申请失败", ex);

			vo.setSuccess(false);
			vo.setMsg("保存失败，系统异常");
			return vo;
		}

		vo.setSuccess(true);
		vo.setMsg("保存成功");
		return vo;
	}

	/** -------------------------------- 任务审核 --------------------------------- */
	/**
	 * 审核列表
	 */
	@RequestMapping(value = "/examineList")
	public String examineList(HttpServletRequest request, HttpServletResponse response, Model model, int taskType) {
		model.addAttribute("defaultPageSize", EXAMINE_DEFAULT_PAGE_SIZE);
		model.addAttribute("recordPageSize", RECORD_DEFAULT_PAGE_SIZE);
		model.addAttribute("taskType", taskType);
		return "task/ots/examine_list";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "/examineListData")
	public Map<String, Object> examineListData(HttpServletRequest request, Model model, String code, String orgId,
			String startCreateTime, String endCreateTime, String nickName, int taskType, String parts_code,
			String parts_name, String parts_org, String matName, String mat_org, String vehicle_type) {

		// 设置默认记录数
		String pageSize = request.getParameter("pageSize");
		if (!StringUtils.isNotBlank(pageSize)) {
			request.setAttribute("pageSize", EXAMINE_DEFAULT_PAGE_SIZE);
		}

		Map<String, Object> map = new PageMap(request);
		map.put("custom_order_sql", "t.create_time desc");
		map.put("state", StandardTaskEnum.EXAMINE.getState());
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
	@RequestMapping(value = "/examineDetail")
	public String examineDetail(HttpServletRequest request, HttpServletResponse response, Model model, Long id,
			int taskType) {
		if (id != null) {
			Task task = taskService.selectOne(id);

			model.addAttribute("facadeBean", task);
		}

		model.addAttribute("resUrl", resUrl);
		model.addAttribute("taskType", taskType);
		return "task/ots/examine_detail";
	}

	/**
	 * 批量审核结果
	 * 
	 * @param ids
	 *            任务ID
	 * @param type
	 *            结果： 1-通过， 2-不通过
	 * @param remark
	 *            备注
	 */
	@ResponseBody
	@RequestMapping(value = "/batchExamine")
	public AjaxVO batchExamine(HttpServletRequest request, Model model, @RequestParam(value = "ids[]") Long[] ids,
			int type, String remark) {
		AjaxVO vo = new AjaxVO();
		Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);

		try {
			infoService.examine(account, ids, type, remark);
		} catch (Exception ex) {
			logger.error("任务批量审核失败", ex);

			vo.setSuccess(false);
			vo.setMsg("操作失败，系统异常，请重试");
			return vo;
		}

		vo.setSuccess(true);
		vo.setMsg("操作成功");
		return vo;
	}

	/**
	 * 审核结果
	 * 
	 * @param id
	 *            任务 ID
	 * @param result
	 *            结果： 1-通过， 2-不通过
	 * @param remark
	 *            备注
	 */
	@ResponseBody
	@RequestMapping(value = "/examine")
	public AjaxVO examine(HttpServletRequest request, Model model, Long v_id, String v_code, String v_type,
			String v_proTime, String v_proAddr, String v_remark, String p_code, String p_name, String p_proTime,
			String p_place, String p_proNo, Long p_id, String p_keyCode, Integer p_isKey, Long p_orgId, String p_remark,
			Long m_id, String m_matName, String m_matColor, String m_proNo, Long m_orgId, String m_matNo,
			String m_remark, @RequestParam(value = "m_pic", required = false) MultipartFile mfile, Long t_id,
			int taskType, String p_contacts, String p_phone, String m_contacts, String m_phone, int result,
			String examine_remark, Long id) {

		AjaxVO vo = new AjaxVO();
		Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		try {
			// 整车信息
			Vehicle vehicle = null;
			vehicle = vehicleService.selectOne(v_id);
			if (isUpdateVehicleInfo(vehicle, v_code, v_type, v_proTime, v_proAddr, v_remark)) {
				vehicle.setType(v_type);
				vehicle.setProTime(sdf.parse(v_proTime));
				vehicle.setProAddr(v_proAddr);
				vehicle.setRemark(v_remark);
				vehicle.setCode(v_code);

				boolean isExist = vehicleService.isExist(v_id, v_code, v_type, sdf.parse(v_proTime), v_proAddr,
						v_remark);
				if (isExist) {
					vo.setSuccess(false);
					vo.setMsg("整车信息已存在");
					return vo;
				}
			}

			// 零部件信息
			Parts parts = null;
			if (taskType == TaskTypeEnum.OTS.getState()) {
				parts = partsService.selectOne(p_id);
				if (isUpdatePartsInfo(parts, p_code, p_name, p_proTime, p_place, p_proNo, p_keyCode, p_isKey, p_orgId,
						p_remark, p_phone, p_contacts)) {

					parts.setProTime(sdf.parse(p_proTime));
					parts.setRemark(p_remark);
					parts.setPlace(p_place);
					parts.setProNo(p_proNo);
					parts.setName(p_name);
					parts.setIsKey(p_isKey);
					parts.setKeyCode(p_keyCode);
					parts.setOrgId(p_orgId);
					parts.setContacts(p_contacts);
					parts.setPhone(p_phone);
					parts.setCode(p_code);

					boolean isExist = partsService.isExist(p_id, p_code, p_name, sdf.parse(p_proTime), p_place, p_proNo,
							p_keyCode, p_isKey, p_orgId, p_remark, p_contacts, p_phone);
					if (isExist) {
						vo.setSuccess(false);
						vo.setMsg("零部件信息已存在");
						return vo;
					}
				}
			}

			// 原材料信息
			Material material = null;
			material = materialService.selectOne(m_id);
			material.setRemark(m_remark);
			material.setProNo(m_proNo);
			material.setMatName(m_matName);
			material.setMatNo(m_matNo);
			material.setMatColor(m_matColor);
			material.setOrgId(m_orgId);
			material.setContacts(m_contacts);
			material.setPhone(m_phone);

			if (mfile != null && !mfile.isEmpty()) {
				String pic = uploadImg(mfile, materialUrl, false);
				material.setPic(pic);
			}

			infoService.examine(account, t_id, result, examine_remark, vehicle, parts, material);

		} catch (Exception ex) {
			logger.error("任务审核失败", ex);

			vo.setSuccess(false);
			vo.setMsg("操作失败，系统异常，请重试");
			return vo;
		}

		vo.setSuccess(true);
		vo.setMsg("操作成功");
		return vo;
	}

	/** -------------------------------- 任务下达 --------------------------------- */
	/**
	 * 任务下达列表
	 */
	@RequestMapping(value = "/transmitList")
	public String transmitList(HttpServletRequest request, HttpServletResponse response, Model model, int taskType) {
		model.addAttribute("defaultPageSize", TRANSMIT_DEFAULT_PAGE_SIZE);
		model.addAttribute("recordPageSize", RECORD_DEFAULT_PAGE_SIZE);
		model.addAttribute("taskType", taskType);
		return "task/ots/transmit_list";
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

		if (taskType == TaskTypeEnum.OTS.getState()) {
			map.put("transimtTask_ots", true);
		} else if (taskType == TaskTypeEnum.GS.getState()) {
			map.put("transimtTask_gs", true);
		}

		map.put("state", StandardTaskEnum.TESTING.getState());
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
	@RequestMapping(value = "/transmitDetail")
	public String transmitDetail(HttpServletRequest request, HttpServletResponse response, Model model, Long id,
			int taskType) {
		if (id != null) {
			Task task = taskService.selectOne(id);

			Map<String, Object> rMap = new PageMap(false);
			rMap.put("taskId", id);
			rMap.put("type", 2);
			rMap.put("state", 2);
			rMap.put("custom_order_sql", "create_time asc");
			rMap.put("taskType", taskType);
			List<ExamineRecord> recordList = examineRecordService.selectAllList(rMap);

			model.addAttribute("facadeBean", task);
			model.addAttribute("recordList", recordList);
		}

		model.addAttribute("taskType", taskType);
		model.addAttribute("resUrl", resUrl);
		return "task/ots/transmit_detail";
	}

	/**
	 * 下达任务结果
	 * 
	 * @param id
	 *            任务ID
	 * @param partsAtlId
	 *            零部件图谱实验室ID
	 * @param matAtlId
	 *            原材料图谱实验室ID
	 * @param partsPatId
	 *            零部件型式实验室ID
	 * @param matPatId
	 *            原材料型式实验室ID
	 */
	@ResponseBody
	@RequestMapping(value = "/transmit")
	public AjaxVO transmit(HttpServletRequest request, Model model, Long id, Long partsAtlId, Long matAtlId,
			Long partsPatId, Long matPatId, String partsAtlCode, String partsAtlTime, String partsAtlReq,
			String matAtlCode, String matAtlTime, String matAtlReq, String partsPatCode, String partsPatTime,
			String partsPatReq, String matPatCode, String matPatTime, String matPatReq) {
		AjaxVO vo = new AjaxVO();

		try {
			Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			List<LabReq> labReqList = new ArrayList<LabReq>();
			if (partsAtlId != null) {
				labReqList.add(new LabReq(partsAtlCode,
						StringUtils.isNotBlank(partsAtlTime) ? sdf.parse(partsAtlTime) : null, partsAtlReq, id, 1));
			}
			if (matAtlId != null) {
				labReqList.add(new LabReq(matAtlCode, StringUtils.isNotBlank(matAtlTime) ? sdf.parse(matAtlTime) : null,
						matAtlReq, id, 2));
			}
			if (partsPatId != null) {
				labReqList.add(new LabReq(partsPatCode,
						StringUtils.isNotBlank(partsPatTime) ? sdf.parse(partsPatTime) : null, partsPatReq, id, 3));
			}
			if (matPatId != null) {
				labReqList.add(new LabReq(matPatCode, StringUtils.isNotBlank(matPatTime) ? sdf.parse(matPatTime) : null,
						matPatReq, id, 4));
			}

			infoService.transmit(account, id, partsAtlId, matAtlId, partsPatId, matPatId, labReqList);
		} catch (Exception ex) {
			logger.error("OTS任务下达失败", ex);

			vo.setSuccess(false);
			vo.setMsg("操作失败，系统异常，请重试");
			return vo;
		}

		vo.setSuccess(true);
		vo.setMsg("操作成功");
		return vo;
	}

	/**
	 * 任务记录列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "/taskRecordListData")
	public Map<String, Object> taskRecordListData(HttpServletRequest request, Model model, String code) {

		// 设置默认记录数
		String pageSize = request.getParameter("pageSize");
		if (!StringUtils.isNotBlank(pageSize)) {
			request.setAttribute("pageSize", RECORD_DEFAULT_PAGE_SIZE);
		}

		Map<String, Object> map = new PageMap(request);
		map.put("custom_order_sql", "create_time desc");

		if (StringUtils.isNotBlank(code)) {
			map.put("code", code);

			List<TaskRecord> dataList = taskRecordService.selectAllList(map);

			// 分页
			Page<TaskRecord> pageList = (Page<TaskRecord>) dataList;

			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("total", pageList.getTotal());
			dataMap.put("rows", pageList.getResult());
			return dataMap;

		} else {
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("total", 0);
			dataMap.put("rows", "");
			return dataMap;
		}
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
		return "task/ots/approve_list";
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

		if (taskType == TaskTypeEnum.OTS.getState()) {
			map.put("approveTask_ots", true);
		} else if (taskType == TaskTypeEnum.GS.getState()) {
			map.put("approveTask_gs", true);
		}

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
	@RequestMapping(value = "/approveDetail")
	public String approveDetail(HttpServletRequest request, HttpServletResponse response, Model model, Long id,
			int taskType) {
		int approveType = 3;

		if (id != null) {
			Task task = taskService.selectOne(id);

			if (task.getState() == StandardTaskEnum.APPLYING.getState()) {
				if (task.getInfoApply() == 1) { // 申请修改信息
					approveType = 1;
					ApplyRecord applyRecord = applyRecordService.getRecordByTaskId(task.getId(), 1);

					if (applyRecord != null) {
						// GS任务没有零部件
						if (taskType == TaskTypeEnum.OTS.getState() && applyRecord.getpId() != null) {
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
					if (taskType == TaskTypeEnum.OTS.getState()) {
						// 零部件-性能结果（只取最后一次实验）
						List<PfResult> pPfResult_old = pfResultService.getLastResult(1, task.gettId());

						// 零部件-图谱结果（只取最后一次实验）
						List<AtlasResult> pAtlasResult_old = atlasResultService.getLastResult(1, task.gettId());

						model.addAttribute("pPfResult_old", pPfResult_old);
						model.addAttribute("pAtlasResult_old", pAtlasResult_old);
					}

					// 原材料-性能结果（只取最后一次实验结果）
					List<PfResult> mPfResult_old = pfResultService.getLastResult(2, task.gettId());

					// 原材料-图谱结果（只取最后一次实验）
					List<AtlasResult> mAtlasResult_old = atlasResultService.getLastResult(2, task.gettId());

					// 试验结论
					List<LabConclusion> conclusionList_old = labConclusionService.selectByTaskId(task.gettId());

					/** --------- 修改之后的结果 ----------- */
					if (taskType == TaskTypeEnum.OTS.getState()) {
						// 零部件-性能结果（只取最后一次实验）
						List<PfResult> pPfResult_new = pfResultService.getLastResult(1, task.getId());

						// 零部件-图谱结果（只取最后一次实验）
						List<AtlasResult> pAtlasResult_new = atlasResultService.getLastResult(1, task.getId());

						model.addAttribute("pAtlasResult_new", pAtlasResult_new);
						model.addAttribute("pPfResult_new", pPfResult_new);
					}

					// 原材料-性能结果（只取最后一次实验结果）
					List<PfResult> mPfResult_new = pfResultService.getLastResult(2, task.getId());

					// 原材料-图谱结果（只取最后一次实验）
					List<AtlasResult> mAtlasResult_new = atlasResultService.getLastResult(2, task.getId());

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

					model.addAttribute("mPfResult_old", mPfResult_old);
					model.addAttribute("mAtlasResult_old", mAtlasResult_old);
					model.addAttribute("mPfResult_new", mPfResult_new);
					model.addAttribute("mAtlasResult_new", mAtlasResult_new);
				}
			}

			model.addAttribute("taskType", taskType);
			model.addAttribute("approveType", approveType);
			model.addAttribute("facadeBean", task);
		}

		model.addAttribute("resUrl", resUrl);

		if (approveType == 3) {
			List<LabReq> labReqList = labReqService.getLabReqListByTaskId(id);
			model.addAttribute("labReqList", labReqList);
		}

		if (approveType == 1) {
			return "task/ots/approve_info_detail";
		} else {
			return "task/ots/approve_detail";
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
	 *            分类：1-零部件图谱，2-原材料图谱，3-零部件型式，4-原材料型式，5-全部（试验），6-信息修改申请，7-试验结果修改申请
	 * @param partsAtlId
	 * @param matAtlId
	 * @param partsPatId
	 * @param matPatId
	 */
	@ResponseBody
	@RequestMapping(value = "/approve")
	public AjaxVO approve(HttpServletRequest request, Model model, Long id, int result, int catagory, String remark,
			Long partsAtlId, Long matAtlId, Long partsPatId, Long matPatId) {
		AjaxVO vo = new AjaxVO();

		try {
			Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);
			infoService.approve(account, id, result, remark, catagory, partsAtlId, matAtlId, partsPatId, matPatId);
		} catch (Exception ex) {
			logger.error("OTS任务审批失败", ex);

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
	 * @param ids
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
					int catagory = 5;

					if (task.getInfoApply() == 1) {
						catagory = 6;
					} else if (task.getResultApply() == 1) {
						catagory = 7;
					}
					infoService.approve(account, id, result, remark, catagory, null, null, null, null);
				}
			}
		} catch (Exception ex) {
			logger.error("OTS任务批量审批失败", ex);

			vo.setSuccess(false);
			vo.setMsg("操作失败，系统异常，请重试");
			return vo;
		}

		vo.setSuccess(true);
		vo.setMsg("操作成功");
		return vo;
	}

	/**
	 * 是否更新整车信息
	 */
	boolean isUpdateVehicleInfo(Vehicle vehicle, String v_code, String v_type, String v_proTime, String v_proAddr,
			String v_remark) {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (v_code.equals(vehicle.getCode()) && v_type.equals(vehicle.getType())
				&& v_proTime.equals(sdf.format(vehicle.getProTime())) && v_proAddr.equals(v_proAddr)
				&& v_remark.equals(vehicle.getRemark())) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 是否更新零部件信息
	 */
	boolean isUpdatePartsInfo(Parts parts, String p_code, String p_name, String p_proTime, String p_place,
			String p_proNo, String p_keyCode, Integer p_isKey, Long p_orgId, String p_remark, String p_phone,
			String p_contacts) {

		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (p_code.equals(parts.getCode()) && p_name.equals(parts.getName())
				&& p_proTime.equals(sdf.format(parts.getProTime())) && p_place.equals(parts.getPlace())
				&& p_proNo.equals(parts.getProNo()) && p_orgId.longValue() == parts.getOrgId().longValue()
				&& p_remark.equals(parts.getRemark()) && p_isKey.intValue() == parts.getIsKey().intValue()
				&& p_phone.equals(parts.getPhone()) && p_contacts.equals(parts.getContacts())) {
			return false;
		} else {
			return true;
		}
	}

}
