package cn.wow.support.web;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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

import com.github.pagehelper.Page;

import cn.wow.common.domain.Account;
import cn.wow.common.domain.Org;
import cn.wow.common.domain.Role;
import cn.wow.common.service.AccountService;
import cn.wow.common.service.OperationLogService;
import cn.wow.common.service.OrgService;
import cn.wow.common.service.RoleService;
import cn.wow.common.utils.AjaxVO;
import cn.wow.common.utils.Contants;
import cn.wow.common.utils.ImportExcelUtil;
import cn.wow.common.utils.cookie.MD5;
import cn.wow.common.utils.operationlog.OperationType;
import cn.wow.common.utils.operationlog.ServiceType;
import cn.wow.common.utils.pagination.PageMap;

/**
 * 用户控制器
 * 
 * @author zhenjunzhuo 2016-12-20
 */
@Controller
@RequestMapping(value = "account")
public class AccountController extends AbstractController {

	Logger logger = LoggerFactory.getLogger(AccountController.class);

	private final static String DEFAULT_PAGE_SIZE = "10";
	
	private final static String DEFAULT_PWD = "888888";

	@Value("${account.sign.url}")
	protected String signlUrl;
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private OrgService orgService;
	@Autowired
	private OperationLogService operationLogService;

	// 查询的条件，用于导出
	private Map<String, Object> queryMap = new PageMap(false);

	@RequestMapping(value = "/list")
	public String list(HttpServletRequest httpServletRequest, Model model) {
		model.addAttribute("defaultPageSize", DEFAULT_PAGE_SIZE);
		model.addAttribute("resUrl", resUrl);
		return "sys/account/account_list";
	}

	/**
	 * 获取数据
	 */
	@ResponseBody
	@RequestMapping(value = "/getList")
	public Map<String, Object> getList(HttpServletRequest request, Model model, String userName, String nickName,
			String mobile, String startCreateTime, String endCreateTime, String lock, String orgId, String roleId, Integer isCharge) {

		// 设置默认记录数
		String pageSize = request.getParameter("pageSize");
		if (!StringUtils.isNotBlank(pageSize)) {
			request.setAttribute("pageSize", DEFAULT_PAGE_SIZE);
		}

		queryMap.clear();
		Map<String, Object> map = new PageMap(request);
		map.put("custom_order_sql", "is_charge asc, username asc");
		queryMap.put("custom_order_sql", "is_charge asc, username asc");
		
		if (StringUtils.isNotBlank(userName)) {
			map.put("qUserName", userName);
			queryMap.put("qUserName", userName);
		}
		if (StringUtils.isNotBlank(nickName)) {
			map.put("nickName", nickName);
			queryMap.put("nickName", nickName);
		}
		if (isCharge != null) {
			map.put("isCharge", isCharge);
			queryMap.put("isCharge", isCharge);
		}
		if (StringUtils.isNotBlank(lock)) {
			map.put("lock", lock);
			queryMap.put("lock", lock);
		}
		if (StringUtils.isNotBlank(mobile)) {
			map.put("mobile", mobile);
			queryMap.put("mobile", mobile);
		}
		if (StringUtils.isNotBlank(orgId)) {
			map.put("orgId", orgId);
			queryMap.put("orgId", orgId);
		}
		if (StringUtils.isNotBlank(roleId)) {
			map.put("roleId", roleId.substring(roleId.indexOf("_") + 1));
			queryMap.put("roleId", roleId.substring(roleId.indexOf("_") + 1));
		}
		if (StringUtils.isNotBlank(startCreateTime)) {
			map.put("startCreateTime", startCreateTime + " 00:00:00");
			queryMap.put("startCreateTime", startCreateTime + " 00:00:00");
		}
		if (StringUtils.isNotBlank(endCreateTime)) {
			map.put("endCreateTime", endCreateTime + " 23:59:59");
			queryMap.put("endCreateTime", endCreateTime + " 23:59:59");
		}

		List<Account> dataList = accountService.selectAllList(map);

		// 分页
		Page<Account> pageList = (Page<Account>) dataList;

		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("total", pageList.getTotal());
		dataMap.put("rows", pageList.getResult());

		return dataMap;
	}

	/**
	 * 用户列表 详情
	 */
	@RequestMapping(value = "/detail")
	public String detail(HttpServletRequest request, Model model, String id) {
		if (StringUtils.isNotBlank(id)) {
			String roleVal = "";
			Account account = accountService.selectOne(Long.parseLong(id));

			Org org = account.getOrg();
			if (org != null) {
				model.addAttribute("orgId", org.getId());
				model.addAttribute("orgName", org.getName());
			}

			Role role = account.getRole();
			if (role != null) {
				roleVal = "[{ id: 'r_" + role.getId() + "', text: '" + role.getName() + "'}]";
			}

			model.addAttribute("roleVal", roleVal);
			model.addAttribute("facadeBean", account);
		}
		model.addAttribute("resUrl", resUrl);
		return "sys/account/account_detail";
	}
	
	
	/**
	 * 当前用户信息
	 */
	@RequestMapping(value = "/info")
	public String info(HttpServletRequest request, Model model) {
		String roleVal = "";
		Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);

		Org org = account.getOrg();
		if (org != null) {
			model.addAttribute("orgId", org.getId());
			model.addAttribute("orgName", org.getName());
		}

		Role role = account.getRole();
		if (role != null) {
			roleVal = "[{ id: 'r_" + role.getId() + "', text: '" + role.getName() + "'}]";
		}

		model.addAttribute("roleVal", roleVal);
		model.addAttribute("facadeBean", account);
		return "sys/account/account_info";
	}
	

	@ResponseBody
	@RequestMapping(value = "/save")
	public AjaxVO save(HttpServletRequest request, Model model, String id, String userName, String nickName,
			String mobile, String password, Long roleId, Long orgId, String email, String remark, Integer signType,
			@RequestParam(value = "pic", required = false) MultipartFile file, Integer isCharge) {
		AjaxVO vo = new AjaxVO();
		Account account = null;

		try {
			if (StringUtils.isNotBlank(id)) {
				account = accountService.selectOne(Long.parseLong(id));
				Integer oldSignType = account.getSignType();
				
				if (account != null) {
					account.setRoleId(roleId);
					account.setMobile(mobile);
					account.setNickName(nickName);
					account.setEmail(email);
					account.setOrgId(orgId);
					account.setRemark(remark);
					account.setSignType(signType);
					account.setIsCharge(isCharge);
					
					if(signType == 2){
						if (file != null && !file.isEmpty()) {
							String pic = uploadImg(file, signlUrl, true);
							account.setPic(pic);
						}
					}
					accountService.update(getCurrentUserName(), account);
					
					// 原来是图片签名的，现在改成用户名，清空图片
					if(oldSignType != null && oldSignType == 2 && signType == 1){
						accountService.clearPic(account.getId());
					}
				}
				vo.setMsg("编辑成功");
			} else {
				Map<String, Object> rMap = new HashMap<String, Object>();
				rMap.put("userName", userName);
				List<Account> userList = accountService.selectAllList(rMap);

				if (userList != null && userList.size() > 0) {
					vo.setData("userName");
					vo.setMsg("用户名已经存在");
					vo.setSuccess(false);
					return vo;
				} else {
					account = new Account();
					account.setUserName(userName);
					account.setPassword(MD5.getMD5(password, "utf-8").toUpperCase());
					account.setMobile(mobile);
					account.setNickName(nickName);
					account.setRoleId(roleId);
					account.setCreateTime(new Date());
					account.setEmail(email);
					account.setOrgId(orgId);
					account.setRemark(remark);
					account.setSignType(signType);
					account.setIsCharge(isCharge);
					
					if(signType == 2){
						if (file != null && !file.isEmpty()) {
							String pic = uploadImg(file, signlUrl, true);
							account.setPic(pic);
						}
					}
					accountService.save(getCurrentUserName(), account);

					vo.setMsg("新增成功");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();

			logger.error("用户保存失败", ex);
			vo.setMsg("保存失败，系统异常");
			vo.setSuccess(false);
			return vo;
		}
		return vo;
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public AjaxVO delete(HttpServletRequest request, String id) {
		AjaxVO vo = new AjaxVO();
		vo.setMsg("删除成功");

		try {
			Account account = accountService.selectOne(Long.parseLong(id));

			if (account != null) {
				accountService.deleteByPrimaryKey(getCurrentUserName(), account);
			} else {
				vo.setMsg("删除失败，记录不存在");
				vo.setSuccess(false);
				return vo;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("用户删除失败", ex);

			vo.setMsg("删除失败，系统异常");
			vo.setSuccess(false);
			return vo;
		}
		return vo;
	}

	/**
	 * 锁定/解锁用户
	 * 
	 * @param id
	 * @param lock
	 */
	@ResponseBody
	@RequestMapping(value = "/lock")
	public AjaxVO lock(HttpServletRequest request, String id, String lock) {
		AjaxVO vo = new AjaxVO();
		vo.setData("操作成功");
		
		try {
			if (StringUtils.isNotBlank(id)) {
				Account account = accountService.selectOne(Long.parseLong(id));

				account.setLock(lock);
				accountService.update(getCurrentUserName(), account);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("用户锁定/解锁失败", ex);
			
			vo.setSuccess(false);
			vo.setMsg("操作失败，系统异常");
			return vo;
		}
		return vo;
	}

	/**
	 * 重置密码 （默认密码888888）
	 * 
	 * @param id
	 */
	@ResponseBody
	@RequestMapping(value = "/resetPwd")
	public AjaxVO resetPwd(HttpServletRequest request, String id, String lock) {
		Account currentAccount = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);
		AjaxVO vo = new AjaxVO();
		vo.setMsg("密码重置成功，默认密码为：888888");
		
		try {
			if (StringUtils.isNotBlank(id)) {
				Account account = accountService.selectOne(Long.parseLong(id));

				String newPwd = MD5.getMD5(DEFAULT_PWD, "utf-8").toUpperCase();

				account.setPassword(newPwd);
				accountService.update(getCurrentUserName(), account);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("用户重置密码失败", ex);
			
			vo.setSuccess(false);
			vo.setMsg("重置失败，系统异常");
			return vo;
		}
		return vo;
	}


	@ResponseBody
	@RequestMapping(value = "/updatePwd")
	public AjaxVO updatePwd(HttpServletRequest request, String oldPwd, String newPwd) {
		AjaxVO vo = new AjaxVO();
		vo.setMsg("密码修改成功，请重新登录");
		Account currentAccount = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);

		try {
			if (currentAccount != null) {
				oldPwd = MD5.getMD5(oldPwd, "utf-8").toUpperCase();
				if (!oldPwd.equals(currentAccount.getPassword())) {
					vo.setSuccess(false);
					vo.setMsg("原密码不正确");
					vo.setData("oldpwd");
					return vo;
				}

				newPwd = MD5.getMD5(newPwd, "utf-8").toUpperCase();
				currentAccount.setPassword(newPwd);
				accountService.update(getCurrentUserName(), currentAccount);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("用户修改密码失败", ex);
			
			vo.setSuccess(false);
			vo.setMsg("修改失败，系统异常");
			vo.setData("exception");
			return vo;
		}
		return vo;
	}

	/**
	 * 导出用户
	 */
	@RequestMapping(value = "/exportUser")
	public void exportUser(HttpServletRequest request, HttpServletResponse response) {
		Account currentAccount = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);
		String filename = "用户清单";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			// 设置头
			ImportExcelUtil.setResponseHeader(response, filename + ".xlsx");

			Workbook wb = new SXSSFWorkbook(100); // 保持100条在内存中，其它保存到磁盘中
			// 工作簿
			Sheet sh = wb.createSheet("用户清单");
			sh.setColumnWidth(0, (short) 4000);
			sh.setColumnWidth(1, (short) 4000);
			sh.setColumnWidth(2, (short) 4000);
			sh.setColumnWidth(3, (short) 5000);
			sh.setColumnWidth(4, (short) 9000);
			sh.setColumnWidth(5, (short) 6000);
			sh.setColumnWidth(6, (short) 3000);
			sh.setColumnWidth(7, (short) 6000);
			
			Map<String, CellStyle> styles = ImportExcelUtil.createStyles(wb);

			String[] titles = { "用户名", "姓名", "手机号码", "机构名称", "角色", "邮箱", "状态", "创建时间" };
			int r = 0;
			
			Row titleRow = sh.createRow(0);
			titleRow.setHeight((short) 450);
			for(int k = 0; k < titles.length; k++){
				Cell cell = titleRow.createCell(k);
				cell.setCellStyle(styles.get("header"));
				cell.setCellValue(titles[k]);
			}
			
			++r;
			
			List<Account> dataList = accountService.selectAllList(queryMap);
			for (int j = 0; j < dataList.size(); j++) {// 添加数据
				Row contentRow = sh.createRow(r);
				contentRow.setHeight((short) 400);
				Account account = dataList.get(j);

				Cell cell1 = contentRow.createCell(0);
				cell1.setCellStyle(styles.get("cell"));
				cell1.setCellValue(account.getUserName());

				Cell cell2 = contentRow.createCell(1);
				cell2.setCellStyle(styles.get("cell"));
				cell2.setCellValue(account.getNickName());

				Cell cell3 = contentRow.createCell(2);
				cell3.setCellStyle(styles.get("cell"));
				cell3.setCellValue(account.getMobile());

				Cell cell4 = contentRow.createCell(3);
				cell4.setCellStyle(styles.get("cell"));
				if(account.getOrg() != null) {
					cell4.setCellValue(account.getOrg().getName());
				}
				
				String roleStr = "";
				if(account.getRole() != null){
					roleStr = account.getRole().getName();
				}
				Cell cell5 = contentRow.createCell(4);
				cell5.setCellStyle(styles.get("cell"));
				cell5.setCellValue(roleStr);

				Cell cell6 = contentRow.createCell(5);
				cell6.setCellStyle(styles.get("cell"));
				cell6.setCellValue(account.getEmail());

				String lock = "正常";
				if (account.getLock() == "Y") {
					lock = "锁定";
				}
				Cell cell7 = contentRow.createCell(6);
				cell7.setCellStyle(styles.get("cell"));
				cell7.setCellValue(lock);

				Cell cell8 = contentRow.createCell(7);
				cell8.setCellStyle(styles.get("cell"));
				cell8.setCellValue(sdf.format(account.getCreateTime()));
				r++;
			}

			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
			
			String logDetail =  "导出用户列表";
			operationLogService.save(currentAccount.getUserName(), OperationType.EXPORT, ServiceType.ACCOUNT, logDetail);
			
		} catch (Exception e) {
			logger.error("用户清单导出失败");
			
			e.printStackTrace();
		}
	}
	
	
	/**  
     * 用户导入
     */  
	@ResponseBody
	@RequestMapping(value = "/importUser")
	public AjaxVO importUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		AjaxVO vo = new AjaxVO();
		Account currentAccount = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		
		List<String> titleList = new ArrayList<String>();
		titleList.add("用户名");
		titleList.add("姓名");
		titleList.add("手机号码");
		titleList.add("机构编码");
		titleList.add("角色编码");
		titleList.add("邮箱");
		
		MultipartFile file = multipartRequest.getFile("upfile");
		if (file.isEmpty()) {
			vo.setSuccess(false);
			vo.setMsg("文件不存在");
			return vo;
		}

		try {
			InputStream in = file.getInputStream();
			List<List<Object>> execelList = new ImportExcelUtil().getBankListByExcel(in, file.getOriginalFilename());
			List<Account> createList = new ArrayList<Account>();
			List<Account> updateList = new ArrayList<Account>();

			if (execelList != null && execelList.size() > 0) {
				// 检查模板是否正确
				List<Object> titleObj = execelList.get(0);
				if(titleObj == null || titleObj.size() < 6) {
					vo.setSuccess(false);
					vo.setData("导入模板不正确");
					return vo;
				}else {
					boolean flag = true;

					if (!"用户名".equals(titleObj.get(0).toString())) {
						flag = false;
					}
					if (flag && !"姓名".equals(titleObj.get(1).toString())) {
						flag = false;
					}
					if (flag && !"手机号码".equals(titleObj.get(2).toString())) {
						flag = false;
					}
					if (flag && !"机构编码".equals(titleObj.get(3).toString())) {
						flag = false;
					}
					if (flag && !"角色编码".equals(titleObj.get(4).toString())) {
						flag = false;
					}
					if (flag && !"邮箱".equals(titleObj.get(5).toString())) {
						flag = false;
					}

					if (!flag) {
						vo.setSuccess(false);
						vo.setMsg("导入模板不正确");
						return vo;
					}
				}
				
				Map<String, Org> orgMap = new HashMap<String, Org>();
				Map<String, Role> roleMap = new HashMap<String, Role>();
				Map<String, Account> accountMap = new HashMap<String, Account>();
				getMapData(orgMap, roleMap, accountMap);

				for (int i = 1; i < execelList.size(); i++) {
					List<Object> obj = execelList.get(i);
					
					if(obj.size() < 1 || obj.get(0) == null){
						continue;
					}
					
					String userName = obj.get(0).toString();
					String nickName = null;
					if(obj.size() >= 2 && obj.get(1) != null){
						nickName = obj.get(1).toString();
					}
					
					String mobile = null;
					if(obj.size() >= 3 && obj.get(2) != null){
						mobile = obj.get(2).toString();
						
						if(StringUtils.isNoneBlank(mobile)) {
							boolean isMatch = Pattern.matches("^[1][3,4,5,7,8][0-9]{9}$", mobile);
							if(!isMatch) {
								mobile = "";
							}
						}
					}
					
					String orgCode = null;
					if(obj.size() >= 4 && obj.get(3) != null){
						orgCode = obj.get(3).toString();
					}
					
					String roleCodes = null;
					if(obj.size() >= 5 && obj.get(4) != null){
						roleCodes = obj.get(4).toString();
					}
					
					String email = null;
					if(obj.size() >= 6  && obj.get(5) != null){
						email = obj.get(5).toString();
						
						if(StringUtils.isNoneBlank(email)) {
							boolean isMatch = Pattern.matches("^^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", email);
							if(!isMatch) {
								email = "";
							}
						}
					}
					
					if (StringUtils.isBlank(userName)) {
						continue;
					}
					if (StringUtils.isBlank(nickName)) {
						continue;
					}

					// 是否已经存在
					Account dbAccount = accountMap.get(userName);

					Account account = new Account();
					account.setNickName(nickName);
					account.setMobile(mobile);
					account.setEmail(email);
					account.setSignType(1);
					account.setIsCharge(0);

					if (dbAccount == null) { // 修改
						account.setUserName(userName);
						account.setPassword(MD5.getMD5(DEFAULT_PWD, "utf-8").toUpperCase());
						account.setCreateTime(new Date());
					} else {
						account.setId(dbAccount.getId());
					}

					if (StringUtils.isNotBlank(roleCodes)) {
						Role role = roleMap.get(roleCodes);
						if (role != null) {
							account.setRoleId(role.getId());
						}
					}

					Org org = orgMap.get(orgCode);
					if (org != null) {
						account.setOrgId(org.getId());
					}

					if (dbAccount == null) {
						createList.add(account);
						accountMap.put(userName, account);
					} else {
						updateList.add(account);
					}
				}
			}

			if (createList.size() > 0) {
				accountService.batchAdd(createList);
			}
			if (updateList.size() > 0) {
				accountService.batchUpdate(updateList);
			}
			String msg = "新增：" + createList.size() + " 用户，修改：" + updateList.size() + "用户";
			vo.setMsg(msg);
			
			String logDetail =  "导入用户，" + msg;
			operationLogService.save(currentAccount.getUserName(), OperationType.IMPORT, ServiceType.ACCOUNT, logDetail);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("用户导入失败：", ex);

			vo.setMsg("导入失败，系统异常");
			vo.setSuccess(false);
			return vo;
		}
		return vo;
	}
	
	/**
	 * 把list转换成map
	 */
	private void getMapData(Map<String, Org> orgMap, Map<String, Role> roleMap, Map<String, Account> accountMap) {
		List<Org> orgList = orgService.selectAllList(new PageMap(false));
		if (orgList != null && orgList.size() > 0) {
			for (Org org : orgList) {
				orgMap.put(org.getCode(), org);
			}
		}

		List<Role> roleList = roleService.selectAllList(new PageMap(false));
		if (roleList != null && roleList.size() > 0) {
			for (Role role : roleList) {
				roleMap.put(role.getCode(), role);
			}
		}
		
		List<Account> accountList = accountService.selectAllList(new PageMap(false));
		if (accountList != null && accountList.size() > 0) {
			for (Account account : accountList) {
				accountMap.put(account.getUserName(), account);
			}
		}
	}
	

	
	

}
