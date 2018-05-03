package cn.wow.support.web;

import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.github.pagehelper.Page;

import cn.wow.common.domain.Material;
import cn.wow.common.service.MaterialService;
import cn.wow.common.utils.AjaxVO;
import cn.wow.common.utils.Contants;
import cn.wow.common.utils.pagination.PageMap;

/**
 * 零部件材料信息
 * 
 * @author zhenjunzhuo 2017-09-28
 */
@Controller
@RequestMapping(value = "material")
public class MaterialController extends AbstractController {

	Logger logger = LoggerFactory.getLogger(MaterialController.class);

	private final static String DEFAULT_PAGE_SIZE = "10";

	@Autowired
	private MaterialService materialService;
	
	@Value("${info.material.url}")
	protected String materialUrl;
	
	@RequestMapping(value = "/list")
	public String list(HttpServletRequest httpServletRequest, Model model) {
		model.addAttribute("defaultPageSize", DEFAULT_PAGE_SIZE);
		model.addAttribute("resUrl", resUrl);
		return "material/material_list";
	}

	/**
	 * 获取数据
	 */
	@ResponseBody
	@RequestMapping(value = "/getList")
	public Map<String, Object> getList(HttpServletRequest request, Model model, String type,
			String proNo, String matName, String matNo, Long orgId) {

		// 设置默认记录数
		String pageSize = request.getParameter("pageSize");
		if (!StringUtils.isNotBlank(pageSize)) {
			request.setAttribute("pageSize", DEFAULT_PAGE_SIZE);
		}

		Map<String, Object> map = new PageMap(request);
		map.put("custom_order_sql", "mat_name asc");
		map.put("state", Contants.FINISH_TYPE);

		if (StringUtils.isNotBlank(type)) {
			map.put("type", type);
		}
		if (StringUtils.isNotBlank(proNo)) {
			map.put("proNo", proNo);
		}
		if (StringUtils.isNotBlank(matName)) {
			map.put("qmatName", matName);
		}
		if (StringUtils.isNotBlank(matNo)) {
			map.put("matNo", matNo);
		}
		if (orgId != null) {
			map.put("orgId", orgId);
		}

		List<Material> dataList = materialService.selectAllList(map);

		// 分页
		Page<Material> pageList = (Page<Material>) dataList;

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
			Material material = materialService.selectOne(Long.parseLong(id));

			model.addAttribute("facadeBean", material);
		}
		model.addAttribute("resUrl", resUrl);
		return "material/material_detail";
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public AjaxVO delete(HttpServletRequest request, String id) {
		AjaxVO vo = new AjaxVO();
		vo.setMsg("删除成功");

		try {
			Material material = materialService.selectOne(Long.parseLong(id));

			if (material != null) {
				materialService.deleteByPrimaryKey(getCurrentUserName(), material);
			} else {
				vo.setMsg("删除失败，记录不存在");
				vo.setSuccess(false);
				return vo;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("材料信息删除失败", ex);

			vo.setMsg("删除失败，系统异常");
			vo.setSuccess(false);
			return vo;
		}
		return vo;
	}

}
