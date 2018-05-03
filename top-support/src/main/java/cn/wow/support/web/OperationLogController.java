package cn.wow.support.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

import cn.wow.common.domain.OperationLog;
import cn.wow.common.service.OperationLogService;
import cn.wow.common.utils.JsonUtil;
import cn.wow.common.utils.operationlog.FieldValue;
import cn.wow.common.utils.operationlog.OperationType;
import cn.wow.common.utils.pagination.PageMap;
import cn.wow.operationlog.OpLogDetailCoder;
import cn.wow.operationlog.manager.EntityServiceTypeMap;

@Controller
@RequestMapping(value = "operationlog")
public class OperationLogController extends AbstractController {

	private static Logger logger = LoggerFactory.getLogger(OperationLogController.class);

	private final static String defaultPageSize = "10";

	@Autowired
	private OperationLogService operationLogService;

	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request, Model model, String userName, String type,
			String startTimeFrom, String startTimeTo, String detail, String operation) {
		
		request.setAttribute("typeList", EntityServiceTypeMap.getAllType());
		request.setAttribute("operationList", OperationType.getAllType());
		request.setAttribute("defaultPageSize", defaultPageSize);
		
		return "sys/operationlog/operationlog_list";
	}

	@RequestMapping(value = "/detail")
	public String detail(HttpServletRequest httpServletRequest, Model model, Long id) {
		List<FieldValue> dataList = new ArrayList<FieldValue>();

		OperationLog operationLog = operationLogService.selectOne(id);
		if (!"任务管理".equals(operationLog.getType()) && !"费用管理".equals(operationLog.getType()) && !"申请管理".equals(operationLog.getType()) && !"实验管理".equals(operationLog.getType()) && !"任务管理".equals(operationLog.getType())) {
			try {
				String detailStr = operationLog.getDetail();
				Map<String, String> strMap = null;

				if (!StringUtils.isEmpty(detailStr)) {
					strMap = JsonUtil.fromJson(detailStr);
				}

				if (strMap != null && strMap.containsKey(OpLogDetailCoder.KEY_ENTITYTYPE)) {
					Map<String, String> oldJsonDetail = null;
					Map<String, String> newJsonDetail = null;

					boolean isCollection = OpLogDetailCoder.isCollection(strMap.get(OpLogDetailCoder.KEY_ENTITYTYPE));
					if (strMap.get(OpLogDetailCoder.KEY_OLDENTITY) != null) {
						if (isCollection) {
							oldJsonDetail = new HashMap<String, String>();
							oldJsonDetail.put("Entities", strMap.get(OpLogDetailCoder.KEY_OLDENTITY));
						} else {
							oldJsonDetail = onlyParseFirstLevel(JsonUtil.fromJson(
									strMap.get(OpLogDetailCoder.KEY_OLDENTITY), Map.class, String.class, Object.class));
						}
					}
					if (strMap.get(OpLogDetailCoder.KEY_ENTITY) != null) {
						if (isCollection) {
							newJsonDetail = new HashMap<String, String>();
							newJsonDetail.put("Entities", strMap.get(OpLogDetailCoder.KEY_ENTITY));
						} else {
							newJsonDetail = onlyParseFirstLevel(JsonUtil.fromJson(
									strMap.get(OpLogDetailCoder.KEY_ENTITY), Map.class, String.class, Object.class));
						}
					}

					dataList = toFacade(newJsonDetail, oldJsonDetail);
					model.addAttribute("operation", strMap.get(OpLogDetailCoder.KEY_OPERATION));
				} else {
					if (strMap != null && strMap.get(OpLogDetailCoder.KEY_FROM) != null) {
						FieldValue val = new FieldValue();
						val.setName(strMap.get(OpLogDetailCoder.KEY_NAME));
						val.setNewValue(strMap.get(OpLogDetailCoder.KEY_TO));
						val.setOldValue(strMap.get(OpLogDetailCoder.KEY_FROM));

						dataList.add(val);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		model.addAttribute("opeartionLog", operationLog);
		model.addAttribute("dataList", dataList);
		return "sys/operationlog/operationlog_detail";
	}
	
	/**
	 * 获取数据
	 */
	@ResponseBody
	@RequestMapping(value = "/getLogList")
	public Map<String, Object> getLogList(HttpServletRequest request, Model model, String userName, String type,
			String startTimeFrom, String startTimeTo, String detail, String operation){
		
		// 设置默认记录数
		String pageSize = request.getParameter("pageSize");
		if (!StringUtils.isNotBlank(pageSize)) {
			request.setAttribute("pageSize", defaultPageSize);
		}
		
		Map<String, Object> map = new PageMap(request);
		map.put("custom_order_sql", "time desc");

		if (StringUtils.isNotBlank(userName)) {
			map.put("userName", userName);
		}
		if (StringUtils.isNotBlank(type)) {
			map.put("type", type);
		}
		if (StringUtils.isNotBlank(operation)) {
			map.put("operation", operation);
		}
		if (StringUtils.isNotBlank(detail)) {
			map.put("detail", detail);
		}
		if (StringUtils.isNotBlank(startTimeFrom)) {
			map.put("startTimeFrom", startTimeFrom + " 00:00:00");
		}
		if (StringUtils.isNotBlank(startTimeTo)) {
			map.put("startTimeTo", startTimeTo + " 23:59:59");
		}
		List<OperationLog> opeartionLogList = operationLogService.selectAllList(map);
		
		if (opeartionLogList != null && opeartionLogList.size() > 0) {
			for (OperationLog operationLog : opeartionLogList) {
				if (StringUtils.isNotBlank(operationLog.getDetail())) {
					operationLog.setDetail(operationLog.getDetail().replaceAll("\\\\r\\\\n", "").replaceAll("\\\\", ""));
				}
			}
		}
		
		// 分页
		Page<OperationLog> pageList = (Page<OperationLog>)opeartionLogList;
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("total", pageList.getTotal());
		dataMap.put("rows", pageList.getResult());
		
		return dataMap;
	}
	

	public List<FieldValue> toFacade(Map<String, String> entityMap, Map<String, String> oldEntityMap) {
		List<FieldValue> dataList = new ArrayList<FieldValue>();

		if (entityMap != null) {
			Iterator<Map.Entry<String, String>> entityEntries = entityMap.entrySet().iterator();
			while (entityEntries.hasNext()) {
				Map.Entry<String, String> entry = entityEntries.next();

				FieldValue val = new FieldValue();
				val.setName(entry.getKey());
				val.setNewValue(entry.getValue());
				dataList.add(val);
			}
		}

		if (oldEntityMap != null) {
			Iterator<Map.Entry<String, String>> oldEntityEntries = oldEntityMap.entrySet().iterator();
			while (oldEntityEntries.hasNext()) {
				Map.Entry<String, String> entry = oldEntityEntries.next();

				boolean hasVal = false;
				for (FieldValue val : dataList) {
					if (val.getName().equals(entry.getKey())) {
						val.setOldValue(entry.getValue());
						hasVal = true;
						break;
					}
				}

				if (!hasVal) {
					FieldValue val = new FieldValue();
					val.setName(entry.getKey());
					val.setOldValue(entry.getValue());
					dataList.add(val);
				}
			}
		}
		return dataList;
	}

	private Map<String, String> onlyParseFirstLevel(Map<String, Object> srcMap) {
		Map<String, String> result = new LinkedHashMap<String, String>();
		if (null == srcMap) {
			return result;
		}
		for (Entry<String, Object> en : srcMap.entrySet()) {
			Object v = en.getValue();
			if (v != null && v instanceof String) {
				result.put(en.getKey(), (String) v);
			} else {
				result.put(en.getKey(), JsonUtil.toJson(v));
			}
		}
		return result;
	}

}