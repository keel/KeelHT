/**
 * 
 */
package com.k99k.wb.acts;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.TaskManager;

/**
 * 发表msg的异步后续操作,作为任务执行.<br />
 * 依次执行以下操作：<br />
 * 1.处理话题;<br />
 * 2.处理提到的用户;<br />
 * 3.处理fans的消息推送;<br />
 * @author keel
 *
 */
public class WBTalkAct extends Action {

	/**
	 * @param name
	 */
	public WBTalkAct(String name) {
		super(name);
	}

	
	
	
	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		
		
		
		ActionMsg task = new ActionMsg("talkact");
		task.addData(TaskManager.TASK_TYPE, TaskManager.TASK_TYPE_EXE_POOL);
		TaskManager.makeNewTask("WBTalkAct", task);
		
		return super.act(msg);
	}




	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#exit()
	 */
	@Override
	public void exit() {

	}

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#getIniPath()
	 */
	@Override
	public String getIniPath() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#init()
	 */
	@Override
	public void init() {
	}

}
