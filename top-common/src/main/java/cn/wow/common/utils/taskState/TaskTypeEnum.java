package cn.wow.common.utils.taskState;

/**
 * 任务类型
 * 
 * @author zhenjunzhuo 2017-10-16
 *
 */
public enum TaskTypeEnum {

	OTS(1, "OTS任务"), PPAP(2, "PPAP任务"), SOP(3, "SOP任务"), GS(4, "材料研究所任务");

	private int state;

	private String msg;

	private TaskTypeEnum(int state, String msg) {
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
