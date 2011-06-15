/**
 * 
 */
package com.k99k.wb.acts;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.dao.WBUserDao;

/**
 * 发表Comm的异步后续操作,作为任务执行.
 * @author keel
 *
 */
public class WBCommAct extends Action {

	/**
	 * @param name
	 */
	public WBCommAct(String name) {
		super(name);
	}
	
	

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		long userId = (Long)msg.getData("userId");
		String txt = (String)msg.getData("txt");
		long msgId = (Long)msg.getData("msgId");
		boolean isRT = (Boolean)msg.getData("isRT");

		//原用户增加一个提到的消息数量
		
		//原消息增加一个评论数量
		
		if (isRT) {
			//更新lastMsg
			WBUserDao.updateLastMsg(userId, txt,msgId);
			//向所有的fans发送消息,处理fans的inbox
			WBUserDao.pushMsgToFans(userId, msgId);
		}
		
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
