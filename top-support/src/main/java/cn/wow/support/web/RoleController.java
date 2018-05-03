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

import cn.wow.common.domain.Role;
import cn.wow.common.domain.RoleGroup;
import cn.wow.common.domain.RolePermission;
import cn.wow.common.domain.TreeNode;
import cn.wow.common.service.OperationLogService;
import cn.wow.common.service.RoleGroupService;
import cn.wow.common.service.RolePermissionService;
import cn.wow.common.service.RoleService;
import cn.wow.common.utils.AjaxVO;
import cn.wow.common.utils.Contants;
import cn.wow.common.utils.operationlog.OperationType;
import cn.wow.common.utils.operationlog.ServiceType;

@Controller
@RequestMapping(value = "role")
public class RoleController extends AbstractController {

	private static Logger logger = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private RoleService roleService;
	@Autowired
	private RoleGroupService roleGroupService;
	@Autowired
	private OperationLogService operationLogService;
	@Autowired
	private RolePermissionService rolePermissionService;

	@RequestMapping(value = "/list")
	public String list(HttpServletRequest httpServletRequest, Model model) {
		model.addAttribute("superRoleCode", Contants.SUPER_ROLE_CODE);
		model.addAttribute("superRoleId", "r_" + Contants.SUPER_ROLE_ID);
		return "sys/role/role_list";
	}

	/**
	 * 获取树
	 */
	@ResponseBody
	@RequestMapping(value = "/tree")
	public List<TreeNode> tree(HttpServletRequest request, Model model, String svalue, String stype) {
		List<TreeNode> areaTree = roleGroupService.getTree(svalue, stype);
		return areaTree;
	}

	/**
	 * 新建/修改页面
	 * 
	 * @param type:
	 *            类型- 1. 角色 2. 角色组
	 */
	@RequestMapping(value = "/detail")
	public String detail(HttpServletRequest request, Model model, String id, String parentid, Integer type) {
		RoleGroup parentGroup = null;
		String title = "";

		if (type == 1) {
			if (StringUtils.isNotBlank(id)) {
				Role role = roleService.selectOne(plainId(id));
				if (role.getGroup() != null) {
					parentGroup = role.getGroup();
				}
				model.addAttribute("facade", role);
			} else {
				parentGroup = roleGroupService.selectOne(plainId(parentid));
			}
			title = "角色";
		} else {
			if (StringUtils.isNotBlank(id)) {
				RoleGroup roleGroup = roleGroupService.selectOne(plainId(id));
				if (roleGroup.getParent() != null) {
					parentGroup = roleGroup.getParent();
				}
				model.addAttribute("facade", roleGroup);
			} else {
				parentGroup = roleGroupService.selectOne(plainId(parentid));
			}
			title = "角色组";
		}

		model.addAttribute("parentGroup", parentGroup);
		model.addAttribute("type", type);
		model.addAttribute("title", title);
		return "sys/role/role_detail";
	}

	/**
	 * 新建/修改保存
	 */
	@ResponseBody
	@RequestMapping(value = "/save")
	public AjaxVO save(HttpServletRequest request, Model model, String id, String code, String parentid, String name,
			String desc, Integer type) {
		AjaxVO vo = null;

		if (type == 1) {
			vo = createUpdateRole(id, code, parentid, name, desc);
		} else {
			vo = createUpdateRoleGroup(id, parentid, name, desc);
		}
		return vo;
	}

	public AjaxVO createUpdateRole(String id, String code, String parentid, String name, String desc) {
		AjaxVO vo = new AjaxVO();
		Role role = null;

		try {
			if (StringUtils.isNoneBlank(id)) {
				if (Contants.SUPER_ROLE_CODE.equals(code)) {
					vo.setMsg("禁止编辑超级管理员");
					vo.setSuccess(false);
					return vo;
				}
				role = roleService.selectOne(Long.parseLong(id));

				if (role != null) {
					if (!role.getName().equals(name)) {
						Map<String, Object> rMap = new HashMap<String, Object>();
						rMap.put("name", name);
						rMap.put("grid", parentid);
						List<Role> roleList = roleService.selectAllList(rMap);

						if (roleList != null && roleList.size() > 0) {
							vo.setMsg("角色名已存在");
							vo.setSuccess(false);
							vo.setData("name");
							return vo;
						}
					}

					role.setDesc(desc);
					role.setName(name);
					roleService.update(getCurrentUserName(), role);

					vo.setMsg("编辑成功");
				}
			} else {
				Role exist = roleService.selectByCode(code);
				if (exist != null) {
					vo.setMsg("编码已存在");
					vo.setSuccess(false);
					vo.setData("code");
					return vo;
				}

				Map<String, Object> rMap = new HashMap<String, Object>();
				rMap.put("name", name);
				rMap.put("grid", parentid);
				List<Role> roleList = roleService.selectAllList(rMap);

				if (roleList != null && roleList.size() > 0) {
					vo.setMsg("角色名已存在");
					vo.setSuccess(false);
					vo.setData("name");
					return vo;
				} else {
					role = new Role();
					role.setDesc(desc);
					role.setName(name);
					role.setCode(code);
					role.setGrid(Long.parseLong(parentid));
					RoleGroup parentGroup = roleGroupService.selectOne(Long.parseLong(parentid));
					role.setGroup(parentGroup);

					roleService.save(getCurrentUserName(), role);
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
		vo.setData("r_" + role.getId());
		return vo;
	}

	public AjaxVO createUpdateRoleGroup(String id, String parentid, String name, String desc) {
		AjaxVO vo = new AjaxVO();
		RoleGroup roleGroup = null;

		try {
			if (StringUtils.isNoneBlank(id)) {
				roleGroup = roleGroupService.selectOne(Long.parseLong(id));

				if (roleGroup != null) {
					if (!roleGroup.getName().equals(name)) {
						Map<String, Object> rMap = new HashMap<String, Object>();
						rMap.put("name", name);
						rMap.put("parentid", parentid);
						List<RoleGroup> roleList = roleGroupService.selectAllList(rMap);

						if (roleList != null && roleList.size() > 0) {
							vo.setMsg("角色组名已存在");
							vo.setSuccess(false);
							vo.setData("name");
							return vo;
						}
					}

					roleGroup.setDesc(desc);
					roleGroup.setName(name);
					roleGroupService.update(getCurrentUserName(), roleGroup);

					vo.setMsg("编辑成功");
				}
			} else {
				Map<String, Object> rMap = new HashMap<String, Object>();
				rMap.put("name", name);
				rMap.put("parentid", parentid);
				List<RoleGroup> roleGroupList = roleGroupService.selectAllList(rMap);

				if (roleGroupList != null && roleGroupList.size() > 0) {
					vo.setMsg("角色组名已存在");
					vo.setSuccess(false);
					vo.setData("name");
					return vo;
				} else {
					roleGroup = new RoleGroup();
					roleGroup.setDesc(desc);
					roleGroup.setName(name);
					roleGroup.setParentid(Long.parseLong(parentid));
					RoleGroup parentGroup = roleGroupService.selectOne(Long.parseLong(parentid));
					roleGroup.setParent(parentGroup);

					roleGroupService.save(getCurrentUserName(), roleGroup);
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
		vo.setData("g_" + roleGroup.getId());
		return vo;
	}

	@ResponseBody
	@RequestMapping(value = "/roleInfo")
	public Role roleInfo(HttpServletRequest request, Model model, String id) {
		Role role = roleService.selectOne(plainId(id));
		
		// 角色操作权限
		RolePermission permission = rolePermissionService.selectOne(role.getId());
		if(permission != null){
			role.setPermission(permission.getPermission());
		}
		return role;
	}

	@ResponseBody
	@RequestMapping(value = "/roleGroupInfo")
	public RoleGroup roleGroupInfo(HttpServletRequest request, Model model, String id) {
		RoleGroup roleGroup = roleGroupService.selectOne(plainId(id));
		return roleGroup;
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public AjaxVO delete(HttpServletRequest request, String id) {
		AjaxVO vo = new AjaxVO();
		String type = null;

		try {
			if (isRole(id)) {
				type = "角色";

				Role role = roleService.selectOne(plainId(id));
				if (Contants.SUPER_ROLE_CODE.equals(role.getCode())) {
					vo.setMsg("不能删除超级管理员");
					vo.setSuccess(false);
					return vo;
				}
				roleService.deleteByPrimaryKey(getCurrentUserName(), role);
			} else {
				type = "角色组";
				RoleGroup group = roleGroupService.selectOne(plainId(id));

				roleGroupService.deleteByPrimaryKey(getCurrentUserName(), group);
			}
		} catch (Exception ex) {
			ex.printStackTrace();

			vo.setMsg(type + "删除失败，系统异常");
			vo.setSuccess(false);
			logger.error(type + "删除失败：", ex);
			return vo;
		}

		vo.setMsg("删除成功");
		vo.setSuccess(true);
		return vo;
	}

	

	/**
	 * 移动
	 */
	@ResponseBody
	@RequestMapping(value = "/move")
	public AjaxVO move(HttpServletRequest request, Model model, String id, String parentid) {
		AjaxVO vo = new AjaxVO();
		String type = null;
		try {
			if(isRole(id)){
				type = "角色";
				Role role = roleService.selectOne(plainId(id));
				
				String oldParentGroup = "";
				if (role.getGroup() != null) {
					oldParentGroup = role.getGroup().getName();
				}
				role.setGrid(plainId(parentid));
				roleService.move(role);

				// 当前父节点
				RoleGroup currentParentGroup = roleGroupService.selectOne(plainId(parentid));

				String detail = "{\"name\":\"" + role.getName() + "\", \"from\":\"" + oldParentGroup + "\", \"to\":\"" + currentParentGroup.getName() + "\"}";
				operationLogService.save(getCurrentUserName(), OperationType.MOVE, ServiceType.SYSTEM, detail);
			}else{
				type = "角色组";
				
				RoleGroup roleGroup = roleGroupService.selectOne(plainId(id));
				
				String oldParentGroup = "";
				if (roleGroup.getParent() != null) {
					oldParentGroup = roleGroup.getParent().getName();
				}
				roleGroup.setParentid(plainId(parentid));
				roleGroupService.move(roleGroup);

				// 当前父节点
				RoleGroup currentParentGroup = roleGroupService.selectOne(plainId(parentid));

				String detail = "{\"name\":\"" + roleGroup.getName() + "\", \"from\":\"" + oldParentGroup + "\", \"to\":\"" + currentParentGroup.getName() + "\"}";
				operationLogService.save(getCurrentUserName(), OperationType.MOVE, ServiceType.SYSTEM, detail);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(type + "移动失败：", ex);
			
			vo.setSuccess(false);
			vo.setMsg(type + "移动失败");
			return vo;
		}

		vo.setMsg("移动成功");
		vo.setSuccess(true);
		return vo;
	}

	/**
	 * 保存操作权限
	 */
	@ResponseBody
	@RequestMapping(value = "/saveOperationPermission")
	public AjaxVO saveOperationPermission(HttpServletRequest request, String roleId, String menuIds) {
		AjaxVO vo = new AjaxVO();

		try {
			if (StringUtils.isNotBlank(roleId)) {
				RolePermission permission = new RolePermission();
				permission.setRoleId(Long.parseLong(roleId));
				
				if(StringUtils.isNoneBlank(menuIds) && menuIds.endsWith(",")){
					menuIds = menuIds.substring(0, menuIds.length() - 1);
				}
				permission.setPermission(menuIds);
				rolePermissionService.save(permission);
			} else {
				vo.setMsg("角色操作权限保存失败，请选择角色");
				vo.setSuccess(false);
				return vo;
			}
		} catch (Exception ex) {
			ex.printStackTrace();

			vo.setMsg("角色操作权限保存失败，系统异常");
			vo.setSuccess(false);
			logger.error("保存失败：", ex);
			return vo;
		}

		vo.setMsg("保存成功");
		vo.setSuccess(true);
		return vo;
	}
	
	
	
	/**
	 * 解析获取ID
	 */
	private Long plainId(String id) {
		if (StringUtils.isNotBlank(id)) {
			String[] arry = id.split("_");

			if (arry.length == 2) {
				return Long.parseLong(arry[1]);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	private boolean isRole(String id) {
		if (id.startsWith("r")) {
			return true;
		} else {
			return false;
		}
	}

}