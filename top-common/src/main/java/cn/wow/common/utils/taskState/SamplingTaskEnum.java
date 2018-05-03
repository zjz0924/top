package cn.wow.common.utils.taskState;

/**
 * 抽样任务状态（PPAP、SOP）
 * 
 * @author zhenjunzhuo 2017-10-16
 */
public enum SamplingTaskEnum {

	APPROVE(1, "审批中"), 
	APPROVE_NOTPASS(2, "审批不通过"), 
	UPLOAD(3, "结果上传中"), 
	COMPARE(4, "对比中"),
	SENDING(5, "结果发送中"),
	COMFIRM(6, "结果确认中"),
	ACCOMPLISH(7, "完成"),
	APPLYING(8, "申请修改"),
	APPLY_NOTPASS(9, "申请不通过"),
	RECONFIRM(10, "确认是否二次抽样"),
	END(11, "中止任务");

	private int state;

	private String msg;
	
	private SamplingTaskEnum(int state, String msg) {
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