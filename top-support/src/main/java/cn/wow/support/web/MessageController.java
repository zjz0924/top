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

import com.github.pagehelper.Page;

import cn.wow.common.domain.Account;
import cn.wow.common.domain.AtlasResult;
import cn.wow.common.domain.CompareVO;
import cn.wow.common.domain.CostRecord;
import cn.wow.common.domain.EmailRecord;
import cn.wow.common.domain.ExamineRecord;
import cn.wow.common.domain.ExpItem;
import cn.wow.common.domain.Menu;
import cn.wow.common.domain.PfResult;
import cn.wow.common.service.EmailRecordService;
import cn.wow.common.service.MenuService;
import cn.wow.common.utils.Contants;
import cn.wow.common.utils.pagination.PageMap;
import cn.wow.common.utils.taskState.TaskTypeEnum;


/**
 * 消息管理控制器
 * 
 * @author zhenjunzhuo 2017-11-16
 */
@Controller
@RequestMapping(value = "message")
public class MessageController {

	Logger logger = LoggerFactory.getLogger(MessageController.class);
	
	private final static String DEFAULT_PAGE_SIZE = "10";
	
	@Autowired
	private EmailRecordService emailRecordService;
	@Autowired
	private MenuService menuService;
	
	
	@RequestMapping(value = "/list")
	public String list(HttpServletRequest httpServletRequest, Model model) {
		Menu menu = menuService.selectByAlias("message");
		
		model.addAttribute("defaultPageSize", DEFAULT_PAGE_SIZE);
		model.addAttribute("menuName", menu.getName());
		return "message/message_list";
	}

	/**
	 * 获取数据
	 * @param startCreateTime  发送时间
	 * @param endCreateTime    发送时间
	 * @param code             任务编码
	 * @param type             类型：1-结果发送，2-收费通知，3-警告书
	 * @param state            状态：1-未查看，2-已查看
	 */
	@ResponseBody
	@RequestMapping(value = "/getListData")
	public Map<String, Object> getListData(HttpServletRequest request, Model model, String startCreateTime, String endCreateTime, String code, Integer type, Integer state) {
		
		Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);
		
		// 设置默认记录数
		String pageSize = request.getParameter("pageSize");
		if (!StringUtils.isNotBlank(pageSize)) {
			request.setAttribute("pageSize", DEFAULT_PAGE_SIZE);
		}

		Map<String, Object> map = new PageMap(request);
		map.put("custom_order_sql", "r.create_time desc");
		
		// 非超级管理员
		if(account.getRole() == null || !Contants.SUPER_ROLE_CODE.equals(account.getRole().getCode())){
			map.put("addr", account.getEmail());
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
		if (state != null) {
			map.put("state", state);
		}
		if (type != null) {
			map.put("type", type);
		}
		
		List<EmailRecord> dataList = emailRecordService.selectAllList(map);

		// 分页
		Page<EmailRecord> pageList = (Page<EmailRecord>) dataList;

		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("total", pageList.getTotal());
		dataMap.put("rows", pageList.getResult());

		return dataMap;
	}

	@RequestMapping(value = "/detail")
	public String detail(HttpServletRequest request, Model model, String id) {
		Account account = (Account) request.getSession().getAttribute(Contants.CURRENT_ACCOUNT);
		
		if (StringUtils.isNotBlank(id)) {
			EmailRecord emailRecord = emailRecordService.selectOne(Long.parseLong(id));

			// 设置为已读
			emailRecord.setState(2);
			emailRecordService.update(account.getUserName(), emailRecord);
			
			model.addAttribute("facadeBean", emailRecord);
		}
		
		return "message/message_detail";
	}
	
}
