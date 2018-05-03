package cn.wow.support.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.github.pagehelper.Page;
import cn.wow.common.domain.Parts;
import cn.wow.common.service.PartsService;
import cn.wow.common.utils.AjaxVO;
import cn.wow.common.utils.Contants;
import cn.wow.common.utils.pagination.PageMap;

/**
 * 零部件信息
 * 
 * @author zhenjunzhuo 2017-09-28
 */
@Controller
@RequestMapping(value = "parts")
public class PartsController extends AbstractController {

	Logger logger = LoggerFactory.getLogger(PartsController.class);

	private final static String DEFAULT_PAGE_SIZE = "10";

	@Autowired
	private PartsService partsService;
	
	@Value("${info.parts.url}")
	protected String partsUrl;
	
	@RequestMapping(value = "/list")
	public String list(HttpServletRequest httpServletRequest, Model model) {
		model.addAttribute("defaultPageSize", DEFAULT_PAGE_SIZE);
		model.addAttribute("resUrl", resUrl);
		return "parts/parts_list";
	}

	/**
	 * 获取数据
	 */
	@ResponseBody
	@RequestMapping(value = "/getList")
	public Map<String, Object> getList(HttpServletRequest request, Model model, String code, String type,
			String startProTime, String endProTime, String name, Long orgId, String proNo, Integer isKey, String keyCode) {

		// 设置默认记录数
		String pageSize = request.getParameter("pageSize");
		if (!StringUtils.isNotBlank(pageSize)) {
			request.setAttribute("pageSize", DEFAULT_PAGE_SIZE);
		}

		Map<String, Object> map = new PageMap(request);
		map.put("custom_order_sql", "code asc");
		map.put("state", Contants.FINISH_TYPE);
		map.put("type", Contants.STANDARD_TYPE);

		if (StringUtils.isNotBlank(code)) {
			map.put("qcode", code);
		}
		if (StringUtils.isNotBlank(startProTime)) {
			map.put("startProTime", startProTime);
		}
		if (StringUtils.isNotBlank(endProTime)) {
			map.put("endProTime", endProTime);
		}
		if (StringUtils.isNotBlank(name)) {
			map.put("name", name);
		}
		if (isKey != null) {
			map.put("isKey", isKey);
		}
		if (StringUtils.isNotBlank(proNo)) {
			map.put("proNo", proNo);
		}
		if (StringUtils.isNotBlank(keyCode)) {
			map.put("keyCode", keyCode);
		}
		if(orgId != null){
			map.put("orgId", orgId);
		}
		
		List<Parts> dataList = partsService.selectAllList(map);

		// 分页
		Page<Parts> pageList = (Page<Parts>) dataList;

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
			Parts parts = partsService.selectOne(Long.parseLong(id));

			model.addAttribute("facadeBean", parts);
		}
		model.addAttribute("resUrl", resUrl);
		return "parts/parts_detail";
	}


	@ResponseBody
	@RequestMapping(value = "/delete")
	public AjaxVO delete(HttpServletRequest request, String id) {
		AjaxVO vo = new AjaxVO();
		vo.setMsg("删除成功");

		try {
			Parts parts = partsService.selectOne(Long.parseLong(id));

			if (parts != null) {
				partsService.deleteByPrimaryKey(getCurrentUserName(), parts);
			} else {
				vo.setMsg("删除失败，记录不存在");
				vo.setSuccess(false);
				return vo;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("零部件信息删除失败", ex);

			vo.setMsg("删除失败，系统异常");
			vo.setSuccess(false);
			return vo;
		}
		return vo;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getByCode")
	public Parts getByCode(HttpServletRequest request, String code) {
		return partsService.selectByCode(code);
	}

}
