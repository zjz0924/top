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
import cn.wow.common.domain.Org;
import cn.wow.common.domain.TreeNode;
import cn.wow.common.service.OperationLogService;
import cn.wow.common.service.OrgService;
import cn.wow.common.utils.AjaxVO;
import cn.wow.common.utils.operationlog.OperationType;
import cn.wow.common.utils.operationlog.ServiceType;

/**
 * 机构管理控制器
 * 
 * @author zhenjunzhuo 
 * 2017-09-15
 */
@Controller
@RequestMapping(value = "org")
public class OrgController extends AbstractController {

	Logger logger = LoggerFactory.getLogger(OrgController.class);

	@Autowired
	private OrgService orgService;
	@Autowired
	private OperationLogService operationLogService;

	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request, Model model) {
		return "sys/org/org_list";
	}

	/**
	 * 新建/修改页面
	 */
	@RequestMapping(value = "/detail")
	public String detail(HttpServletRequest request, Model model, String id, String parentid) {
		Org org = null;
		boolean canEdit = true;
		
		if (StringUtils.isNotBlank(id)) {
			org = orgService.selectOne(Long.parseLong(id));
			
			if (org.getParent() != null) {
				parentid = org.getParent().getId().toString();
			}
			if(org.getArea() != null){
				model.addAttribute("areaid", org.getArea().getId());
				model.addAttribute("areaname", org.getArea().getName());
			}
			
			model.addAttribute("org", org);
		}

		if(StringUtils.isNotBlank(parentid)){
			Org parentOrg = orgService.selectOne(Long.parseLong(parentid));
			model.addAttribute("parentOrg", parentOrg);
			
			// 如果父节点没有选择类型，那么就显示当前自己的类型
			Integer type = null;
			if(parentOrg.getType() != null){
				type = parentOrg.getType();
				canEdit = false;
			}else{
				if(org != null){
					type = org.getType();
					canEdit = true;
				}
			}
			model.addAttribute("type", type);
			model.addAttribute("canEdit", canEdit);
		}
		
		model.addAttribute("id", id);
		return "sys/org/org_detail";
	}

	/**
	 * 新建/修改保存
	 */
	@ResponseBody
	@RequestMapping(value = "/save")
	public AjaxVO save(HttpServletRequest request, Model model, String id, String code, String parentid, String text, String desc, String areaid, Integer type, String addr) {
		AjaxVO vo = new AjaxVO();
		Org org = null;

		try {
			if (StringUtils.isNoneBlank(id)) {
				org = orgService.selectOne(Long.parseLong(id));

				if (org != null) {
					if (!org.getName().equals(text)) {
						Map<String, Object> rMap = new HashMap<String, Object>();
						rMap.put("name", text);
						rMap.put("parentid", parentid);
						List<Org> areaList = orgService.selectAllList(rMap);

						if (areaList != null && areaList.size() > 0) {
							vo.setMsg("名称已存在");
							vo.setSuccess(false);
							vo.setData("name");
							return vo;
						}
					}

					// 是否需要修改所有子机构的类型
					boolean flag = false;
					if(org.getType() != null && org.getType().intValue() != type.intValue()){
						flag = true;
					}
					
					org.setDesc(desc);
					org.setName(text);
					org.setType(type);
					org.setAddr(addr);
					org.setParentid(Long.parseLong(parentid));
					if(StringUtils.isNotBlank(areaid)){
						org.setAreaid(Long.parseLong(areaid));
					}
					orgService.update(getCurrentUserName(), org, flag);
					
					vo.setMsg("编辑成功");
				}
			} else {
				Org exist = orgService.getByCode(code);
				if(exist != null){
					vo.setMsg("编码已存在");
					vo.setSuccess(false);
					vo.setData("code");
					return vo;
				}
				
				Map<String, Object> rMap = new HashMap<String, Object>();
				rMap.put("name", text);
				rMap.put("parentid", parentid);
				List<Org> orgList = orgService.selectAllList(rMap);

				if (orgList != null && orgList.size() > 0) {
					vo.setMsg("名称已存在");
					vo.setSuccess(false);
					vo.setData("name");
					return vo;
				} else {
					org = new Org();
					org.setDesc(desc);
					org.setName(text);
					org.setCode(code);
					org.setType(type);
					org.setAddr(addr);
					org.setParentid(Long.parseLong(parentid));
					if(StringUtils.isNotBlank(areaid)){
						org.setAreaid(Long.parseLong(areaid));
					}
					Org parent = orgService.selectOne(Long.parseLong(parentid));
					org.setParent(parent);
					
					orgService.save(getCurrentUserName(), org);
					vo.setMsg("新建成功");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();

			vo.setMsg("保存失败，系统异常");
			vo.setSuccess(false);
			logger.error("机构保存失败：", ex);
			return vo;
		}
		vo.setData(org.getId());
		return vo;
	}

	/**
	 * 机构树
	 */
	@ResponseBody
	@RequestMapping(value = "/tree")
	public List<TreeNode> tree(HttpServletRequest request, Model model, String svalue, String stype) {
		List<TreeNode> areaTree = orgService.getTree(svalue, stype);
		return areaTree;
	}
	
	/**
	 * 获取类型获取机构树
	 * @param type   1-通用五菱, 2-供应商, 3-实验室, 4-其它
	 */
	@ResponseBody
	@RequestMapping(value = "/getTreeByType")
	public List<TreeNode> getTreeByType(HttpServletRequest request, Model model, int type) {
		List<TreeNode> areaTree = orgService.getTreeByType(type);
		return areaTree;
	}

	/**
	 * 机构信息
	 */
	@ResponseBody
	@RequestMapping(value = "/info")
	public Org info(HttpServletRequest request, Model model, String id) {
		Org org = orgService.selectOne(Long.parseLong(id));
		return org;
	}

	/**
	 * 机构移动
	 */
	@ResponseBody
	@RequestMapping(value = "/move")
	public AjaxVO move(HttpServletRequest request, Model model, String id, String parentid) {
		AjaxVO vo = new AjaxVO();

		try {
			Org org = orgService.selectOne(Long.parseLong(id));
			
			String oldParentCode = "";
			if (org.getParent() != null) {
				oldParentCode = org.getParent().getCode();
			}
			org.setParentid(Long.parseLong(parentid));
			
			boolean flag = true;
			Org newOrg = orgService.selectOne(Long.parseLong(parentid));
			if(newOrg.getType() != null && org.getParent().getType() != null && newOrg.getType().intValue() == org.getParent().getType().intValue()){
				flag = false;
			}else{
				org.setType(newOrg.getType());
			}
			orgService.move(org, flag);

			// 当前父节点
			Org currentParent = orgService.selectOne(Long.parseLong(id));

			String detail = "{\"name\":\"" + org.getName() + "\", \"from\":\"" + oldParentCode + "\", \"to\":\"" + currentParent.getCode() + "\"}";
			operationLogService.save(getCurrentUserName(), OperationType.MOVE, ServiceType.SYSTEM, detail);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("机构移动失败：", ex);
			
			vo.setSuccess(false);
			vo.setMsg("机构移动失败");
			return vo;
		}

		vo.setMsg("移动成功");
		vo.setSuccess(true);
		return vo;
	}

	/**
	 * 机构删除
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public AjaxVO delete(HttpServletRequest request, Model model, String id) {
		AjaxVO vo = new AjaxVO();

		try {
			Org org = orgService.selectOne(Long.parseLong(id));
			orgService.delete(getCurrentUserName(), org);
		} catch (Exception ex) {
			ex.printStackTrace();

			vo.setMsg("删除失败，系统异常");
			vo.setSuccess(false);
			logger.error("机构删除失败：", ex);
			return vo;
		}

		vo.setMsg("删除成功");
		vo.setSuccess(true);
		return vo;
	}
}
