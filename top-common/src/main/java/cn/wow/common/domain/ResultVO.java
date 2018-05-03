package cn.wow.common.domain;

/**
 * 任务结果统计
 * 
 * @author zhenjunzhuo 2017-11-15
 */
public class ResultVO {
	// 任务数
	private int taskNum = 0;
	// 合格数
	private int passNum = 0;
	// 一次不合格数
	private int onceNum = 0;
	// 两次不合格数
	private int twiceNum = 0;

	public int getTaskNum() {
		return taskNum;
	}

	public void setTaskNum(int taskNum) {
		this.taskNum = taskNum;
	}

	public int getPassNum() {
		return passNum;
	}

	public void setPassNum(int passNum) {
		this.passNum = passNum;
	}

	public int getOnceNum() {
		return onceNum;
	}

	public void setOnceNum(int onceNum) {
		this.onceNum = onceNum;
	}

	public int getTwiceNum() {
		return twiceNum;
	}

	public void setTwiceNum(int twiceNum) {
		this.twiceNum = twiceNum;
	}

}
