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

import cn.wow.common.dao.AreaDao;
import cn.wow.common.domain.Area;
import cn.wow.common.domain.Org;
import cn.wow.common.domain.TreeNode;
import cn.wow.common.service.AreaService;
import cn.wow.common.utils.operationlog.annotation.OperationLogIgnore;
import cn.wow.common.utils.pagination.PageHelperExt;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {

	private static Logger logger = LoggerFactory.getLogger(AreaServiceImpl.class);

	@Autowired
	private AreaDao areaDao;

	public Area selectOne(Long id) {
		return areaDao.selectOne(id);
	}
	
	public Area getAreaByCode(String code){
		return areaDao.getAreaByCode(code);
	}

	public int save(String userName, Area area) {
		return areaDao.insert(area);
	}

	public int update(String userName, Area area) {
		return areaDao.update(area);
	}

	public int move(Area area) {
		return areaDao.update(area);
	}

	public int deleteByPrimaryKey(String userName, Area area) {
		return areaDao.deleteByPrimaryKey(area.getId());
	}

	public List<Area> selectAllList(Map<String, Object> map) {
		PageHelperExt.startPage(map);
		return areaDao.selectAllList(map);
	}

	/**
	 * 获取区域树
	 */
	public List<TreeNode> getAreaTree(String svalue, String stype) {
		Area rootArea = areaDao.selectOne(1l);
		TreeNode rootNode = new TreeNode();
		
		/**
		 * 搜索思路：
		 * 1. 先搜索有哪些节点匹配
		 * 2. 获取它们的次上级父节点
		 * 3. 再添加子节点的时候过滤掉父节点是当前节点的节点
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
			List<Area> dataList = areaDao.selectAllList(qMap);

			Set<Area> parentSet = new HashSet<Area>();
			if (dataList != null && dataList.size() > 0) {
				for (Area area : dataList) {
					//不处理根节点
					if(area.getParent() != null){
						targetId.add(area.getId());
						legalId.add(area.getId());
						parentId.add(area.getParentid());
					}
					
					// 获取搜索节点的二级父节点
					Area parentArea = getSecondArea(area, legalId, parentId);
					if (!parentSet.contains(parentArea)) {
						parentSet.add(parentArea);
					}
					removeDuplicate(parentSet);
				}
				
				//防止父节点与子节点名称相同
				targetId.removeAll(parentId);
			}
			
			List<Area> parentList = new ArrayList<Area>();
			parentList.addAll(parentSet);
			rootArea.setSubList(parentList);
		}

		List<TreeNode> tree = new ArrayList<TreeNode>();
		if (rootArea != null) {
			rootNode.setId(rootArea.getId().toString());
			rootNode.setText(rootArea.getName());

			// 获取子集合
			Iterator<Area> subList = rootArea.getSubList().iterator();
			if (subList.hasNext()) {
				List<TreeNode> subNodeList = new ArrayList<TreeNode>();

				// 遍历子节点
				while (subList.hasNext()) {
					Area subArea = subList.next();

					if ((isSearch(svalue) && legalId.contains(subArea.getId())) || !isSearch(svalue)) {
						TreeNode subAreaNode = new TreeNode();
						subAreaNode.setId(subArea.getId().toString());
						subAreaNode.setText(subArea.getName());
						
						boolean isSearch = isSearch(svalue);
						if ((isSearch && !targetId.contains(subArea.getId())) || !isSearch) {
							// 遍历子节点
							addSonNode(subAreaNode, subArea, legalId, targetId, isSearch);
						}
						subNodeList.add(subAreaNode);
					}
				}
				rootNode.setChildren(subNodeList);
			}
		}
		tree.add(rootNode);

		return tree;
	}

	private void addSonNode(TreeNode subAreaNode, Area area, Set<Long> legalId, Set<Long> targetId, boolean isSearch) {
		// 获取子集合
		Iterator<Area> subList = area.getSubList().iterator();

		if (subList.hasNext()) {
			List<TreeNode> subNodeList = new ArrayList<TreeNode>();

			// 遍历子节点
			while (subList.hasNext()) {
				Area subArea = subList.next();

				if ((isSearch && legalId.contains(subArea.getId())) || !isSearch) {
					TreeNode sonNode = new TreeNode();
					sonNode.setId(subArea.getId().toString());
					sonNode.setText(subArea.getName());
					// 遍历子节点
					if ((isSearch && !targetId.contains(subArea.getId())) || !isSearch) {
						addSonNode(sonNode, subArea, legalId, targetId, isSearch);
					}
					subNodeList.add(sonNode);
				}
			}
			subAreaNode.setChildren(subNodeList);
		}
	}
	
	// 获取二级节点
	private Area getSecondArea(Area area, Set<Long> legalId, Set<Long> parentId) {
		if (area.getParent() != null && area.getParent().getId() != 1l) {
			legalId.add(area.getParent().getId());
			parentId.add(area.getParent().getId());
			return getSecondArea(area.getParent(), legalId, parentId);
		}
		return area;
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
	private Set<Area> removeDuplicate(Set<Area> set) {
		Map<String, Area> map = new HashMap<String, Area>();
		Set<Area> tempSet = new HashSet<Area>();
		for (Area area : set) {
			if (map.get(area.getCode()) == null) {
				map.put(area.getCode(), area);
			} else {
				tempSet.add(area);
			}
		}
		set.removeAll(tempSet);
		return set;
	}
}
