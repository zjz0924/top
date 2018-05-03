package cn.wow.common.utils.taskState;

/**
 * 基准任务记录状态（OTS、材料研究所任务）
 * 
 * @author zhenjunzhuo 2017-10-16
 */
public enum StandardTaskRecordEnum {
	
	ENTERING(1, "基准信息录入"), 
	EXAMINE_PASS(2, "审核通过"),
	EXAMINE_NOTPASS(3, "审核不通过"),
	TRANSMIT(4, "任务下达"), 
	APPROVE_AGREE(5, "审批同意"), 
	APPROVE_DISAGREE(6, "审批不同意"),
	UPLOAD(7, "结果上传"),
	SEND(8, "结果发送"), 
	CONFIRM(9, "结果确认"), 
	SAVE(10, "基准保存"),
	NOTICE(11, "收费通知"),
	UPDATE(12, "信息修改"),
	INFO_APPLY(13, "申请信息修改"),
	RESULT_APPLY(14, "申请试验结果修改"),
	INFO_UPDATE(15, "信息修改"),
	RESULT_UPDATE(16, "试验结果修改");

	private int state;

	private String msg;
	
	private StandardTaskRecordEnum(int state, String msg) {
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