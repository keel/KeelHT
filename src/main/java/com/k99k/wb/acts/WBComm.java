/**
 * 
 */
package com.k99k.wb.acts;

import java.util.ArrayList;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.HttpActionMsg;
import com.k99k.khunter.KObject;
import com.k99k.khunter.TaskManager;
import com.k99k.khunter.dao.WBUserDao;

/**
 * 废弃掉,使用WBTalk实现
 * 评论,同步操作
 * @author keel
 *
 */
public class WBComm extends Action {

	/**
	 * @param name
	 */
	public WBComm(String name) {
		super(name);
	}
	
	

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		HttpActionMsg httpmsg = (HttpActionMsg)msg;
		//验证用户请求是否合法
		KObject user = WBLogin.cookieAuth(httpmsg);
		if (user == null) {
			msg.addData("[redirect]", "/");
			return super.act(msg);
		}
		
		String talk = httpmsg.getHttpReq().getParameter("comm");
		long msgId = Long.parseLong(httpmsg.getHttpReq().getParameter("msgId"));
		long re_user_id = Long.parseLong(httpmsg.getHttpReq().getParameter("re_user_id"));
		boolean isRT = httpmsg.getHttpReq().getParameter("isRT")!=null && httpmsg.getHttpReq().getParameter("isRT").equals("true");
		String source = "web";
		String place = "";
		StringBuffer sb = new StringBuffer(talk);
		KObject newComm = WBUserDao.newComm();
		long commId = newComm.getId();
		//关键词过滤
		sb = WBTalk.dealKeyWords(sb);
		//url处理
		ArrayList<String> us = WBTalk.dealUrl(sb);
		//topic
		ArrayList<String> ts = WBTalk.dealTopic(sb,commId);
		//mention
		ArrayList<String> ms = WBTalk.dealMention(sb,commId);
		String txt = sb.toString();
		WBUserDao.addComm(newComm,msgId,re_user_id, user.getId(), txt,source, place,ts,ms,false);
		//生成异步任务
		ActionMsg task = new ActionMsg("commact");
		task.addData(TaskManager.TASK_TYPE, TaskManager.TASK_TYPE_EXE_POOL);
		task.addData("userId", user.getId());
		task.addData("txt", txt);
		task.addData("msgId", msgId);
		task.addData("isRT", isRT);
		TaskManager.makeNewTask("WBCommAct:"+newComm.getId(), task);
		
		
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
