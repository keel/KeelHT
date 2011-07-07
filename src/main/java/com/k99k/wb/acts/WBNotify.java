/**
 * 
 */
package com.k99k.wb.acts;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.HttpActionMsg;
import com.k99k.tools.encrypter.Base64Coder;

/**
 * @author keel
 *
 */
public class WBNotify extends Action {

	/**
	 * @param name
	 */
	public WBNotify(String name) {
		super(name);
	}

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
		msg.addData("[print]", WBUser.hasNew(u_p[0]));
		return super.act(msg);
	}
	
	
	

}
