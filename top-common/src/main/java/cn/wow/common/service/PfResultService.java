package cn.wow.common.service;

import java.util.List;
import java.util.Map;
import cn.wow.common.domain.Account;
import cn.wow.common.domain.LabConclusion;
import cn.wow.common.domain.PfResult;

public interface PfResultService {
    public PfResult selectOne(Long id);

    public int save(String userName, PfResult pfResult);

    public int update(String userName, PfResult pfResult);

    public int deleteByPrimaryKey(String userName, PfResult pfResult);

    public List<PfResult> selectAllList(Map<String, Object> map);
    
    public void batchAdd(List<PfResult> list);
    
    /**
     * 性能结果上传
     * @param account
     * @param dataList   试验结果
     * @param conclusionDataList  结论结果
     */
    public void upload(Account account, List<PfResult> dataList, Long taskId, List<LabConclusion> conclusionDataList);
    
    
    /**
     * 获取试验次数
     * @param taskId    任务ID
     * @param catagory  种类：1-零部件，2-原材料
     */
    public int getExpNoByCatagory(Long taskId, int catagory);
    
    
    /**
	 * 组装型式结果
	 * @param pfDataList  当前任务所有的型式结果记录
	 * @param pPfResult   零部件的型式结果记录
	 * @param mPfResult   原材料的型式结果记录
	 */
	public void assemblePfResult(List<PfResult> pfDataList, Map<Integer, List<PfResult>> pPfResult,
			Map<Integer, List<PfResult>> mPfResult);
	
	
	/**
	 * 获取最后一次试验结果
	 * @param type     类型（1-零部件，2-原材料）
	 * @param taskId   任务ID
	 */
	public List<PfResult> getLastResult(int type, Long taskId);

}
