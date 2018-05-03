package cn.wow.common.service;

import java.util.List;
import java.util.Map;
import cn.wow.common.domain.Org;
import cn.wow.common.domain.TreeNode;

public interface OrgService {
    public Org selectOne(Long id);

    public int save(String userName, Org org);

    public int update(String userName, Org org, boolean update);

    public int deleteByPrimaryKey(String userName, Org org);

    public List<Org> selectAllList(Map<String, Object> map);
    
    /**
     * 获取树
     * @param svalue 搜索值
     * @param stype  搜索类型
     */
    public List<TreeNode> getTree(String svalue, String stype);
    
    /**
	 * 获取类型来获取机构树
	 * @param type   1-通用五菱, 2-供应商, 3-实验室, 4-其它
	 */
    public List<TreeNode> getTreeByType(int type);
    
    public int move(Org org, boolean update);
    
    public Org getByCode(String code);
    
    public void delete(String userName, Org org);
    
    public void batchUpdate(Map<String, Object> map);

}
