/**
 * 
 */
package com.k99k.wb.acts;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.HttpActionMsg;
import com.k99k.khunter.KFilter;
import com.k99k.khunter.KObject;
import com.k99k.khunter.dao.WBUserDao;
import com.k99k.tools.StringUtil;

/**
 * @author keel
 *
 */
public class WBTopic extends Action {

	/**
	 * @param name
	 */
	public WBTopic(String name) {
		super(name);
	}
	
	private int pageSize = 30;

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		String subact = KFilter.actPath(msg, 2, "");
		HttpActionMsg httpmsg = (HttpActionMsg)msg;
		if (subact.equals("list")) {
			//处理list请求
			String re = null;
			String topic = httpmsg.getHttpReq().getParameter("t");
			if (!StringUtil.isStringWithLen(topic, 1)) {
				JOut.err(400, httpmsg);
				return super.act(msg);
			}
			String p_str = httpmsg.getHttpReq().getParameter("p");
			String pz_str = httpmsg.getHttpReq().getParameter("pz");
			int page = StringUtil.isDigits(p_str)?Integer.parseInt(p_str):1;
			int pz = StringUtil.isDigits(pz_str)?Integer.parseInt(pz_str):this.pageSize;
			if (!StringUtil.isDigits(p_str) || !StringUtil.isDigits(pz_str)) {
				JOut.err(400, httpmsg);
				return super.act(msg);
			}
			re = WBMsg.writeKObjList(WBUserDao.readOneTopicList(topic, page, pz));
			if (re == null || re.equals("null")) {
				re = "[]";
			}
			msg.addData("[print]", re);
		}else {
			//显示页面,将subact作为topic
			KObject user = WBLogin.cookieAuth(httpmsg);
			httpmsg.addData("wbUser", user);
			msg.addData("topic", subact);
			int sum = WBUserDao.getTopicSum(subact);
			msg.addData("sum", sum);
			msg.addData("[jsp]", "/WEB-INF/wb/topic.jsp");
			return super.act(msg);
		}
		
		
		return super.act(msg);
	}

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
	

}
