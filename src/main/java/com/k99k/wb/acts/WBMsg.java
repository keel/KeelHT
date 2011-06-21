/**
 * 
 */
package com.k99k.wb.acts;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.HttpActionMsg;
import com.k99k.khunter.KFilter;
import com.k99k.khunter.dao.WBUserDao;
import com.k99k.tools.JSONTool;
import com.k99k.tools.StringUtil;

/**
 * 
 * @author keel
 *
 */
public class WBMsg extends Action {

	/**
	 * @param name
	 */
	public WBMsg(String name) {
		super(name);
	}
	
	
	
	

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		String subact = KFilter.actPath(msg, 2, "inbox");
		HttpActionMsg httpmsg = (HttpActionMsg)msg;
		String p_str = httpmsg.getHttpReq().getParameter("p");
		String pz_str = httpmsg.getHttpReq().getParameter("pz");
		String uid_str = httpmsg.getHttpReq().getParameter("uid");
		if (!StringUtil.isDigits(p_str) || !StringUtil.isDigits(pz_str) || !StringUtil.isDigits(uid_str)) {
			msg.addData("[print]", "err paras");
			return super.act(msg);
		}
		long userId = Long.parseLong(uid_str);
		int page = Integer.parseInt(p_str);
		int pageSize = Integer.parseInt(pz_str);
		
		if (subact.equals("inbox")) {
			String re = JSONTool.writeFormatedJsonString(WBUserDao.readOnePageMsgs(userId, page, pageSize));
			msg.addData("[print]", re);
			return super.act(msg);
		}else if(subact.equals("unread")){
			
		}else if(subact.equals("sent")){
			
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
