package cn.wow.common.utils.taskState;

/**
 * 基准任务状态（OTS、材料研究所任务）
 * 
 * @author zhenjunzhuo 2017-10-16
 */
public enum StandardTaskEnum {

	EXAMINE(1, "审核中"), 
	EXAMINE_NOTPASS(2, "审核不通过"), 
	TESTING(3, "试验中"), 
	ACCOMPLISH(4, "完成"),
	APPLYING(5, "申请修改"),
	APPLY_NOTPASS(6, "申请不通过"),
	END(7, "中止任务");

	private int state;

	private String msg;
	
	private StandardTaskEnum(int state, String msg) {
		this.state = state;
		this.msg = msg;
	}

	public int getState() {
		return state;
	}

	public String getMsg() {
		return msg;
	}

}