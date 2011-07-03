/**
 * 
 */
package com.k99k.wb.acts;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.DaoManager;
import com.k99k.khunter.HttpActionMsg;
import com.k99k.khunter.KObject;
import com.k99k.tools.StringUtil;

/**
 * 查看单个msg及其评论转播列表
 * @author keel
 *
 */
public class WBOneMsg extends Action {

	/**
	 * @param name
	 */
	public WBOneMsg(String name) {
		super(name);
	}
	private int pageSize = 20;
	/**
	 * @return the pageSize
	 */
	public final int getPageSize() {
		return pageSize;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public final void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		HttpActionMsg httpmsg = (HttpActionMsg)msg;
		String msg_str = httpmsg.getHttpReq().getParameter("mid");
		if (!StringUtil.isDigits(msg_str) ) {
			JOut.err(400, httpmsg);
			return super.act(msg);
		}
		
		
		String p_str = httpmsg.getHttpReq().getParameter("p");
		String pz_str = httpmsg.getHttpReq().getParameter("pz");
		int page = (StringUtil.isDigits(p_str))?Integer.parseInt(p_str):1;
		int pageSize = (StringUtil.isDigits(pz_str))?Integer.parseInt(pz_str):this.pageSize;
		long mid = Long.parseLong(msg_str);
		//TODO 不取comments
		KObject one = DaoManager.findDao("wbMsgDao").findOne(mid);
		if (one ==null) {
			JOut.err(404, httpmsg);
			return super.act(msg);
		}
		msg.addData("mid", mid);
		msg.addData("one", one);
		KObject user = WBUser.findWBUser(Long.parseLong(one.getProp("creatorId").toString()));
		msg.addData("p", page);
		msg.addData("pz", pageSize);
		msg.addData("wbUser", user);
		msg.addData("[jsp]", "/WEB-INF/wb/onemsg.jsp");
		return super.act(msg);
	}

	
	
}
