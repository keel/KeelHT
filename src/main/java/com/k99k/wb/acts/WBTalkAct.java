/**
 * 
 */
package com.k99k.wb.acts;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.dao.WBUserDao;

/**
 * 发表msg的异步后续操作,作为任务执行.(附:此action的初始化中执行WBUserDao.initKCs)<br />
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

	//private static WBUserDao dao = (WBUserDao)DaoManager.findDao("wbUserDao");
	
	
	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		//
		long userId = (Long)msg.getData("userId");
		String txt = (String)msg.getData("txt");
		long msgId = (Long)msg.getData("msgId");
		//更新lastMsg
		WBUserDao.updateLastMsg(userId, txt,msgId);
		
		//向所有的fans发送消息,处理fans的inbox
		WBUserDao.pushMsgToFans(userId, msgId);
		
		
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
		
		//此处初始化WBUserDao
		WBUserDao.initKCs();
	}

}
