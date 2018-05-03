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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;

import cn.wow.common.domain.Dictionary;
import cn.wow.common.service.DictionaryService;
import cn.wow.common.utils.AjaxVO;
import cn.wow.common.utils.pagination.PageMap;

/**
 * 字典控制器
 * 
 * @author zhenjunzhuo 2017-09-27
 */
@Controller
@RequestMapping(value = "dictionary")
public class DictionaryController extends AbstractController {

	Logger logger = LoggerFactory.getLogger(DictionaryController.class);

	private final static String DEFAULT_PAGE_SIZE = "20";

	@Autowired
	private DictionaryService dictionaryService;

	@RequestMapping(value = "/list")
	public String list(HttpServletRequest httpServletRequest, Model model) {
		model.addAttribute("defaultPageSize", DEFAULT_PAGE_SIZE);
		return "sys/dictionary/dictionary_list";
	}

	/**
	 * 获取数据
	 */
	@ResponseBody
	@RequestMapping(value = "/getList")
	public Map<String, Object> getList(HttpServletRequest request, Model model, String name) {

		// 设置默认记录数
		String pageSize = request.getParameter("pageSize");
		if (!StringUtils.isNotBlank(pageSize)) {
			request.setAttribute("pageSize", DEFAULT_PAGE_SIZE);
		}

		Map<String, Object> map = new PageMap(request);
		map.put("custom_order_sql", "name asc");

		if (StringUtils.isNotBlank(name)) {
			map.put("name", name);
		}

		List<Dictionary> dataList = dictionaryService.selectAllList(map);

		// 分页
		Page<Dictionary> pageList = (Page<Dictionary>) dataList;

		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("total", pageList.getTotal());
		dataMap.put("rows", pageList.getResult());

		return dataMap;
	}

	@RequestMapping(value = "/detail")
	public String detail(HttpServletRequest request, Model model, String id) {
		if (StringUtils.isNotBlank(id)) {
			Dictionary dictionary = dictionaryService.selectOne(Long.parseLong(id));

			model.addAttribute("facadeBean", dictionary);
		}
		return "sys/dictionary/dictionary_detail";
	}

	@ResponseBody
	@RequestMapping(value = "/save")
	public AjaxVO save(HttpServletRequest request, Model model, String id, String name, String val, String desc) {
		AjaxVO vo = new AjaxVO();
		Dictionary dictionary = null;

		try {
			if (StringUtils.isNotBlank(id)) {
				dictionary = dictionaryService.selectOne(Long.parseLong(id));

				if (dictionary != null) {
					if (!name.equals(dictionary.getName())) {
						Dictionary existDict = dictionaryService.selectByName(name);

						if (existDict != null) {
							vo.setData("name");
							vo.setMsg("类型已经存在");
							vo.setSuccess(false);
							return vo;
						}
					}
					dictionary.setName(name);
					dictionary.setDesc(desc);
					dictionary.setVal(val);
					dictionaryService.update(getCurrentUserName(), dictionary);
				}
				vo.setMsg("编辑成功");
			} else {
				Dictionary existDict = dictionaryService.selectByName(name);

				if (existDict != null) {
					vo.setData("name");
					vo.setMsg("类型已经存在");
					vo.setSuccess(false);
					return vo;
				} else {
					dictionary = new Dictionary();
					dictionary.setName(name);
					dictionary.setDesc(desc);
					dictionary.setVal(val);
					dictionary.setCreateTime(new Date());
					dictionaryService.save(getCurrentUserName(), dictionary);

					vo.setMsg("新增成功");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();

			logger.error("字典保存失败", ex);
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
			Dictionary dictionary = dictionaryService.selectOne(Long.parseLong(id));

			if (dictionary != null) {
				dictionaryService.deleteByPrimaryKey(getCurrentUserName(), dictionary);
			}else {
				vo.setMsg("删除失败，记录不存在");
				vo.setSuccess(false);
				return vo;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("字典删除失败", ex);

			vo.setMsg("删除失败，系统异常");
			vo.setSuccess(false);
			return vo;
		}
		return vo;
	}

}
