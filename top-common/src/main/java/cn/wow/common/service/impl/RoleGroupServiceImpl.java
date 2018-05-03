package cn.wow.common.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wow.common.dao.RoleDao;
import cn.wow.common.dao.RoleGroupDao;
import cn.wow.common.domain.Role;
import cn.wow.common.domain.RoleGroup;
import cn.wow.common.domain.TreeNode;
import cn.wow.common.service.RoleGroupService;
import cn.wow.common.utils.operationlog.annotation.OperationLogIgnore;
import cn.wow.common.utils.pagination.PageHelperExt;

@Service
@Transactional
public class RoleGroupServiceImpl implements RoleGroupService {

	private static Logger logger = LoggerFactory.getLogger(RoleGroupServiceImpl.class);

	@Autowired
	private RoleGroupDao roleGroupDao;
	@Autowired
	private RoleDao roleDao;

	public RoleGroup selectOne(Long id) {
		return roleGroupDao.selectOne(id);
	}

	public int save(String userName, RoleGroup roleGroup) {
		return roleGroupDao.insert(roleGroup);
	}

	public int update(String userName, RoleGroup roleGroup) {
		return roleGroupDao.update(roleGroup);
	}

	public int move(RoleGroup roleGroup){
		return roleGroupDao.update(roleGroup);
	}
	
	public int deleteByPrimaryKey(String userName, RoleGroup roleGroup) {
		return roleGroupDao.deleteByPrimaryKey(roleGroup.getId());
	}

	public List<RoleGroup> selectAllList(Map<String, Object> map) {
		PageHelperExt.startPage(map);
		return roleGroupDao.selectAllList(map);
	}

	/**
	 * 获取树
	 */
	public List<TreeNode> getTree(String svalue, String stype) {
		RoleGroup rootGroup = roleGroupDao.selectOne(1l);
		
		TreeNode rootNode = new TreeNode();
		/**
		 * 搜索思路： 1. 先搜索有哪些节点匹配 2. 获取它们的次上级父节点 3. 再添加子节点的时候过滤掉父节点是当前节点的节点
		 */
		// 搜索的节点ID
		Set<Long> targetId = new HashSet<Long>();
		// 所有符合要求的节点ID
		Set<Long> legalId = new HashSet<Long>();
		// 搜索的节点的父ID
		Set<Long> parentId = new HashSet<Long>();

		if (isSearch(svalue)) {
			Map<String, Object> qMap = new HashMap<String, Object>();
			qMap.put(stype, svalue);
			List<Role> dataList = roleDao.selectAllList(qMap);

			Set<RoleGroup> parentSet = new HashSet<RoleGroup>();
			if (dataList != null && dataList.size() > 0) {
				for (Role role : dataList) {
					// 不处理根节点
					if (role.getGroup() != null) {
						targetId.add(role.getId());
						legalId.add(role.getId());
						parentId.add(role.getGrid());
					}

					// 获取搜索节点的二级父节点
					RoleGroup parentArea = getSecondNode(role, legalId);
					if (!parentSet.contains(parentArea)) {
						parentSet.add(parentArea);
					}
					removeDuplicate(parentSet);
				}

				// 防止父节点与子节点名称相同
				targetId.removeAll(parentId);
			}

			List<RoleGroup> parentList = new ArrayList<RoleGroup>();
			parentList.addAll(parentSet);
			rootGroup.setSubList(parentList);
		}

		List<TreeNode> tree = new ArrayList<TreeNode>();
		if (rootGroup != null) {
			rootNode.setId("g_" + rootGroup.getId());
			rootNode.setText(rootGroup.getName());
			rootNode.setIconCls("icon-sitemap");

			// 获取子集合
			Iterator<RoleGroup> subList = rootGroup.getSubList().iterator();
			if (subList.hasNext()) {
				List<TreeNode> subNodeList = new ArrayList<TreeNode>();

				// 遍历子节点
				while (subList.hasNext()) {
					RoleGroup subGroup = subList.next();

					if ((isSearch(svalue) && legalId.contains(subGroup.getId())) || !isSearch(svalue)) {
						TreeNode subNode = new TreeNode();
						subNode.setId("g_" + subGroup.getId());
						subNode.setText(subGroup.getName());
						subNode.setIconCls("icon-sitemap");

						boolean isSearch = isSearch(svalue);
						if ((isSearch && !targetId.contains(subGroup.getId())) || !isSearch) {
							// 遍历子节点
							addSonNode(subNode, subGroup, legalId, targetId, isSearch);
						}
						subNodeList.add(subNode);
					}
				}
				rootNode.setChildren(subNodeList);
			}
			
			if(rootGroup.getRoleList() != null && rootGroup.getRoleList().size() > 0){
				List<TreeNode> children = new ArrayList<TreeNode>();
				if(rootNode.getChildren() != null){
					children = rootNode.getChildren();
				}
				
				for(Role role: rootGroup.getRoleList()){
					TreeNode roleNode = new TreeNode();
					roleNode.setId("r_" + role.getId());
					roleNode.setText(role.getName());
					roleNode.setIconCls("icon-group");
					children.add(roleNode);
				}
				rootNode.setChildren(children);
			}
		}
		tree.add(rootNode);

		return tree;
	}

	private void addSonNode(TreeNode subAreaNode, RoleGroup group, Set<Long> legalId, Set<Long> targetId, boolean isSearch) {
		// 获取子集合
		Iterator<RoleGroup> subList = group.getSubList().iterator();

		if (subList.hasNext()) {
			List<TreeNode> subNodeList = new ArrayList<TreeNode>();

			// 遍历子节点
			while (subList.hasNext()) {
				RoleGroup subGroup = subList.next();

				if ((isSearch && legalId.contains(subGroup.getId())) || !isSearch) {
					TreeNode sonNode = new TreeNode();
					sonNode.setId("g_" + subGroup.getId());
					sonNode.setText(subGroup.getName());
					sonNode.setIconCls("icon-sitemap");
					// 遍历子节点
					if ((isSearch && !targetId.contains(subGroup.getId())) || !isSearch) {
						addSonNode(sonNode, subGroup, legalId, targetId, isSearch);
					}
					subNodeList.add(sonNode);
				}
			}
			
			if(group.getRoleList() != null && group.getRoleList().size() > 0){
				for(Role role: group.getRoleList()){
					TreeNode roleNode = new TreeNode();
					roleNode.setId("r_" + role.getId());
					roleNode.setText(role.getName());
					roleNode.setIconCls("icon-group");
					subNodeList.add(roleNode);
				}
			}
			subAreaNode.setChildren(subNodeList);
		}else{
			List<TreeNode> nodeList = new ArrayList<TreeNode>();
			if (group.getRoleList() != null && group.getRoleList().size() > 0) {
				for (Role role : group.getRoleList()) {
					TreeNode roleNode = new TreeNode();
					roleNode.setId("r_" + role.getId());
					roleNode.setText(role.getName());
					roleNode.setIconCls("icon-group");
					nodeList.add(roleNode);
				}
			}
			subAreaNode.setChildren(nodeList);
		}
	}

	// 获取二级节点
	private RoleGroup getSecondNode(Role role, Set<Long> legalId) {
		if (role.getGroup() != null ) {
			legalId.add(role.getGroup().getId());
			return getSecondNode(role, legalId);
		}
		return role.getGroup();
	}

	private boolean isSearch(String svalue) {
		if (StringUtils.isNotBlank(svalue)) {
			return true;
		} else {
			return false;
		}
	}

	// 去除set中重复数据的方法
	@OperationLogIgnore
	private Set<RoleGroup> removeDuplicate(Set<RoleGroup> set) {
		Map<String, RoleGroup> map = new HashMap<String, RoleGroup>();
		Set<RoleGroup> tempSet = new HashSet<RoleGroup>();
		for (RoleGroup group : set) {
			if (map.get(group.getName()) == null) {
				map.put(group.getName(), group);
			} else {
				tempSet.add(group);
			}
		}
		set.removeAll(tempSet);
		return set;
	}

}
