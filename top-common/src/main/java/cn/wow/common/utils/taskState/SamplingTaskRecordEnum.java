package cn.wow.common.utils.taskState;

/**
 * 抽样任务记录状态（PPAP、SOP）
 * 
 * @author zhenjunzhuo 2017-10-16
 */
public enum SamplingTaskRecordEnum {
	
	TRANSMIT(1, "任务下达"), 
	APPROVE_AGREE(2, "审批同意"), 
	APPROVE_DISAGREE(3, "审批不同意"), 
	UPLOAD(4, "结果上传"), 
	COMPARISON_NORMAL(5, "结果比对正常"),
	COMPARISON_ABNORMAL(6, "结果比对异常"),
	SEND(7, "结果发送"), 
	CONFIRM(8, "结果确认"), 
	SAVE(9, "结果留存"),
	REORDER(10, "重新下任务"),
	ALARM(11, "发送警告书"),
	NOTICE(12, "收费通知"),
	RECONFIRM(13, "二次确认"),
	INFO_APPLY(14, "申请信息修改"),
	RESULT_APPLY(15, "申请试验结果修改"),
	INFO_UPDATE(16, "基础信息修改"),
	RESULT_UPDATE(17, "试验结果修改");

	private int state;

	private String msg;
	
	private SamplingTaskRecordEnum(int state, String msg) {
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