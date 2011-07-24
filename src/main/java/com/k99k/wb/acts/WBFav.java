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
public class WBFav extends Action {

	/**
	 * @param name
	 */
	public WBFav(String name) {
		super(name);
	}
	
	private int pageSize = 20;

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		HttpActionMsg httpmsg = (HttpActionMsg)msg;
		String reqUserStr = WBUrl.getCookieValue(httpmsg.getHttpReq().getCookies(),"wbu","");
		if (reqUserStr.equals("")) {
			JOut.err(401, (HttpActionMsg)msg);
			return super.act(msg);
		}
		String[] u_p = Base64Coder.decodeString(reqUserStr).split(":");
		if (u_p.length >= 2) {
			//验证cookie
			KObject wbUser = WBLogin.auth(u_p[0], u_p[1]);
			if (wbUser != null) {
				//转向自己的inbox
				httpmsg.addData("wbUser", wbUser);
				httpmsg.addData("[jsp]", "/WEB-INF/wb/fav.jsp");
				return super.act(msg);
			}
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
