/**
 * 
 */
package com.k99k.wb.acts;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.HttpActionMsg;
import com.k99k.khunter.KObject;
import com.k99k.tools.encrypter.Base64Coder;

/**
 * 显示自己发表的消息
 * @author keel
 *
 */
public class WBSent extends Action {

	/**
	 * @param name
	 */
	public WBSent(String name) {
		super(name);
	}
	
	private int pageSize = 20;

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		HttpActionMsg httpmsg = (HttpActionMsg)msg;
		KObject user = WBLogin.cookieAuth(httpmsg);
		if (user != null) {
			//转向自己的sent
			httpmsg.addData("wbUser", user);
			httpmsg.addData("[jsp]", "/WEB-INF/wb/sent.jsp");
			return super.act(msg);
		}
		//401
		JOut.err(401, (HttpActionMsg)msg);
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
