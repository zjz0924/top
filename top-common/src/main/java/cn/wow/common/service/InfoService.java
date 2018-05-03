package cn.wow.common.service;

import java.util.List;
import java.util.Map;

import cn.wow.common.domain.Account;
import cn.wow.common.domain.AtlasResult;
import cn.wow.common.domain.ExamineRecord;
import cn.wow.common.domain.Info;
import cn.wow.common.domain.LabConclusion;
import cn.wow.common.domain.LabReq;
import cn.wow.common.domain.Material;
import cn.wow.common.domain.Parts;
import cn.wow.common.domain.PfResult;
import cn.wow.common.domain.Task;
import cn.wow.common.domain.Vehicle;

public interface InfoService {
	public Info selectOne(Long id);

	public int save(String userName, Info info);

	public int update(String userName, Info info);

	public int deleteByPrimaryKey(String userName, Info info);

	public List<Info> selectAllList(Map<String, Object> map);

	public void insert(Account account, Vehicle vehicle, Parts parts, Material material, int type, Long taskId,
			int taskType);

	/**
	 * 审批审核
	 * 
	 * @param account
	 *            操作用户
	 * @param ids
	 *            任务ID
	 * @param type
	 *            结果：1-通过，2-不通过
	 * @param remark
	 *            备注
	 */
	public void examine(Account account, Long[] ids, int type, String remark);
	
	/**
	 * 审核
	 * @param result 结果：1-通过 2-不通过
	 * @param remark 备注
	 */
	public void examine(Account account, Long id, int result, String remark, Vehicle vehicle, Parts parts, Material material);

	/**
	 * 更新任务状态
	 * 
	 * @param id
	 *            任务ID
	 * @param state
	 *            任务状态
	 */
	public void updateState(Long id, Integer state);

	/**
	 * 下达任务（OTS）
	 * 
	 * @param account
	 *            操作用户
	 * @param id
	 *            任务ID
	 * @param partsAtlId
	 *            零部件图谱实验室ID
	 * @param matAtlId
	 *            原材料图谱实验室ID
	 * @param partsPatId
	 *            零部件型式实验室ID
	 * @param matPatId
	 *            原材料型式实验室ID
	 * @param labReqList
	 *            试验说明
	 */
	public void transmit(Account account, Long id, Long partsAtlId, Long matAtlId, Long partsPatId, Long matPatId,
			List<LabReq> labReqList);

	/**
	 * 下达任务（PPAP）
	 * 
	 * @param t_id
	 *            任务ID
	 * @param i_id
	 *            信息ID
	 * @param taskType
	 *            任务类型
	 * @param partsAtlId
	 *            零部件图谱实验室ID
	 * @param matAtlId
	 *            原材料图谱实验室ID
	 * @param partsPatId
	 *            零部件型式实验室ID
	 * @param matPatId
	 *            原材料型式实验室ID
	 */
	public boolean transmit(Account account, Long t_id, Long i_id, Long partsAtlId, Long matAtlId, Long partsPatId,
			Long matPatId, int taskType, List<LabReq> labReqList, String applicant, String department, String figure,
			Integer num, String origin, String reason, String provenance);

	/**
	 * 审批（OTS）
	 * 
	 * @param account
	 *            操作用户
	 * @param id
	 *            任务ID
	 * @param result
	 *            结果：1-通过，2-不通过
	 * @param remark
	 *            备注
	 * @param catagory
	 *            分类：1-零部件图谱，2-原材料图谱，3-零部件型式，4-原材料型式，5-全部（试验），6-信息修改申请，7-试验结果修改申请
	 */
	public void approve(Account account, Long id, int result, String remark, int catagory, Long partsAtlId,
			Long matAtlId, Long partsPatId, Long matPatId);

	/**
	 * 审批（PPAP）
	 * 
	 * @param account
	 *            操作用户
	 * @param id
	 *            任务ID
	 * @param result
	 *            结果：1-通过，2-不通过
	 * @param remark
	 *            备注
	 * @param catagory
	 *            分类：1-信息修改申请，2-试验结果修改申请，3-正常流程
	 */
	public void approve(Account account, Long id, int result, int catagory, String remark, Long partsAtlId,
			Long matAtlId);

	/**
	 * 申请信息修改
	 * 
	 * @param account
	 * @param vehicle
	 *            整车信息
	 * @param parts
	 *            零部件信息
	 * @param material
	 *            原材料信息
	 * @param task
	 *            任务
	 */
	public void applyInfo(Account account, Task task, Vehicle vehicle, Parts parts, Material material);

	/**
	 * 申请结果修改
	 * 
	 * @param account
	 * @param taskId
	 *            任务ID
	 * @param pfResultList
	 *            性能结果
	 * @param atlResultList
	 *            图谱结果
	 * @param compareList
	 *            对比结果
	 * @param conclusionDataList
	 *            试验结论
	 * @param isPass
	 *            是否合格（1-合格，2-不合格）
	 */
	public void applyResult(Account account, Long taskId, List<PfResult> pfResultList, List<AtlasResult> atlResultList,
			List<ExamineRecord> compareList, List<LabConclusion> conclusionDataList, int isPass);

	/**
	 * 获取信息ID列表
	 */
	public List<Long> selectIdList(Map<String, Object> map);

	/**
	 * 获取信息ID列表
	 * 
	 * @param vehicle_type
	 *            车型
	 * @param parts_code
	 *            零件号
	 * @param parts_name
	 *            零件名称
	 * @param parts_org
	 *            零件生产商
	 * @param matName
	 *            材料名称
	 * @param mat_org
	 *            材料生产商
	 */
	public List<Long> selectIds(String vehicle_type, String parts_code, String parts_name, String parts_org,
			String matName, String mat_org);

}
