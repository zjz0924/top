package cn.wow.common.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.wow.common.domain.Account;
import cn.wow.common.domain.AtlasResult;
import cn.wow.common.domain.CompareVO;
import cn.wow.common.domain.ExamineRecord;
import cn.wow.common.domain.LabConclusion;

public interface AtlasResultService {
    public AtlasResult selectOne(Long id);

    public int save(String userName, AtlasResult atlasResult);

    public int update(String userName, AtlasResult atlasResult);

    public int deleteByPrimaryKey(String userName, AtlasResult atlasResult);

    public List<AtlasResult> selectAllList(Map<String, Object> map);
    
    public void batchAdd(List<AtlasResult> list);
    
    /**
     * 图谱结果上传
     * @param account
     * @param atlasResult  实验结果
     * @param conclusionDataList 结论结果
     */
    public void upload(Account account, List<AtlasResult> atlasResult, Long taskId, Date time, List<LabConclusion> conclusionDataList);
    
    /**
     * 获取试验次数
     * @param taskId    任务ID
     * @param catagory  种类：1-零部件，2-原材料
     */
    public int getExpNoByCatagory(Long taskId, int catagory);
    
    /**
     * 获取基准图谱结果
     * @param iId        信息ID
     * @param catagory  种类：1-零部件，2-原材料
     */
    public List<AtlasResult> getStandardAtlResult(Long iId, int catagory);
	
	/**
	 * 组装图谱对比
	 * @param sd_atlasResult  基准图谱
	 * @param sl_atlasResult  抽样图谱
	 */
	public Map<Integer, CompareVO> assembleCompareAtlas(List<AtlasResult> sd_atlasResult, List<AtlasResult> sl_atlasResult);
	
	/**
	 * 组装图谱结果
	 * @param pfDataList  当前任务所有的图谱结果记录
	 * @param pPfResult   零部件的图谱结果记录
	 * @param mPfResult   原材料的图谱结果记录
	 */
	public void assembleAtlasResult(List<AtlasResult> atDataList, Map<Integer, List<AtlasResult>> pAtlasResult, Map<Integer, List<AtlasResult>> mAtlasResult);

	/**
	 * 组装对比结果
	 */
	public Map<String, List<ExamineRecord>> assembleCompareResult(Long taskId);
	
	
	/**
	 * 获取最后一次试验结果
	 * @param type    类型（1-零部件，2-原材料）
	 * @param taskId  任务ID
	 */
	public List<AtlasResult> getLastResult(int type, Long taskId);
}
