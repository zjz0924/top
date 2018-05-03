package cn.wow.support.web;

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

import cn.wow.common.domain.Area;
import cn.wow.common.domain.TreeNode;
import cn.wow.common.service.AreaService;
import cn.wow.common.service.OperationLogService;
import cn.wow.common.utils.AjaxVO;
import cn.wow.common.utils.operationlog.OperationType;
import cn.wow.common.utils.operationlog.ServiceType;

/**
 * 区域管理控制器
 * 
 * @author zhenjunzhuo 2017-09-12
 */
@Controller
@RequestMapping(value = "area")
public class AreaController extends AbstractController {

	Logger logger = LoggerFactory.getLogger(AreaController.class);

	@Autowired
	private AreaService areaService;
	@Autowired
	private OperationLogService operationLogService;

	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request, Model model) {
		return "sys/area/area_list";
	}

	/**
	 * 新建/修改页面
	 */
	@RequestMapping(value = "/detail")
	public String detail(HttpServletRequest request, Model model, String id, String parentid) {
		if (StringUtils.isNotBlank(id)) {
			Area area = areaService.selectOne(Long.parseLong(id));
			if(area.getParent() != null){
				parentid = area.getParent().getId().toString();
			}
			model.addAttribute("area", area);
		}
		
		if(StringUtils.isNotBlank(parentid)){
			Area parentArea = areaService.selectOne(Long.parseLong(parentid));
			model.addAttribute("parentArea", parentArea);
		}
		
		model.addAttribute("id", id);
		return "sys/area/area_detail";
	}

	/**
	 * 新建/修改保存
	 */
	@ResponseBody
	@RequestMapping(value = "/save")
	public AjaxVO save(HttpServletRequest request, Model model, String id, String code, String parentid, String text, String desc) {
		AjaxVO vo = new AjaxVO();
		Area area = null;

		try {
			if (StringUtils.isNoneBlank(id)) {
				area = areaService.selectOne(Long.parseLong(id));

				if (area != null) {
					if (!area.getName().equals(text)) {
						Map<String, Object> rMap = new HashMap<String, Object>();
						rMap.put("name", text);
						rMap.put("parentid", parentid);
						List<Area> areaList = areaService.selectAllList(rMap);

						if (areaList != null && areaList.size() > 0) {
							vo.setMsg("区域名已存在");
							vo.setSuccess(false);
							vo.setData("name");
							return vo;
						}
					}

					area.setDesc(desc);
					area.setName(text);
					areaService.update(getCurrentUserName(), area);
					
					vo.setMsg("编辑成功");
				}
			} else {
				Area exist = areaService.getAreaByCode(code);
				if(exist != null){
					vo.setMsg("编码已存在");
					vo.setSuccess(false);
					vo.setData("code");
					return vo;
				}
				
				Map<String, Object> rMap = new HashMap<String, Object>();
				rMap.put("name", text);
				rMap.put("parentid", parentid);
				List<Area> areaList = areaService.selectAllList(rMap);

				if (areaList != null && areaList.size() > 0) {
					vo.setMsg("区域名已存在");
					vo.setSuccess(false);
					vo.setData("name");
					return vo;
				} else {
					area = new Area();
					area.setDesc(desc);
					area.setName(text);
					area.setCode(code);
					area.setParentid(Long.parseLong(parentid));
					Area parentArea = areaService.selectOne(Long.parseLong(parentid));
					area.setParent(parentArea);
					
					areaService.save(getCurrentUserName(), area);
					vo.setMsg("新建成功");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();

			vo.setMsg("保存失败，系统异常");
			vo.setSuccess(false);
			logger.error("区域保存失败：", ex);
			return vo;
		}
		vo.setData(area.getId());
		return vo;
	}

	/**
	 * 区域树
	 */
	@ResponseBody
	@RequestMapping(value = "/tree")
	public List<TreeNode> tree(HttpServletRequest request, Model model, String svalue, String stype) {
		List<TreeNode> areaTree = areaService.getAreaTree(svalue, stype);
		return areaTree;
	}

	/**
	 * 区域信息
	 */
	@ResponseBody
	@RequestMapping(value = "/info")
	public Area info(HttpServletRequest request, Model model, String id) {
		Area area = areaService.selectOne(Long.parseLong(id));
		return area;
	}

	/**
	 * 区域移动
	 */
	@ResponseBody
	@RequestMapping(value = "/move")
	public AjaxVO move(HttpServletRequest request, Model model, String id, String parentid) {
		AjaxVO vo = new AjaxVO();

		try {
			Area area = areaService.selectOne(Long.parseLong(id));
			
			String oldParentCode = "";
			if (area.getParent() != null) {
				oldParentCode = area.getParent().getCode();
			}
			area.setParentid(Long.parseLong(parentid));
			areaService.move(area);

			// 当前父节点
			Area currentParentArea = areaService.selectOne(Long.parseLong(id));

			String detail = "{\"name\":\"" + area.getName() + "\", \"from\":\"" + oldParentCode + "\", \"to\":\"" + currentParentArea.getCode() + "\"}";
			operationLogService.save(getCurrentUserName(), OperationType.MOVE, ServiceType.SYSTEM, detail);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("区域移动失败：", ex);
			
			vo.setSuccess(false);
			vo.setMsg("区域移动失败");
			return vo;
		}

		vo.setMsg("移动成功");
		vo.setSuccess(true);
		return vo;
	}

	/**
	 * 区域删除
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public AjaxVO delete(HttpServletRequest request, Model model, String id) {
		AjaxVO vo = new AjaxVO();

		try {
			Area area = areaService.selectOne(Long.parseLong(id));
			areaService.deleteByPrimaryKey(getCurrentUserName(), area);
		} catch (Exception ex) {
			ex.printStackTrace();

			vo.setMsg("删除失败，系统异常");
			vo.setSuccess(false);
			logger.error("区域删除失败：", ex);
			return vo;
		}

		vo.setMsg("删除成功");
		vo.setSuccess(true);
		return vo;
	}

}
