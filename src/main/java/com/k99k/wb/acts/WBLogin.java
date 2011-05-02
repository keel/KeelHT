/**
 * 
 */
package com.k99k.wb.acts;

import org.apache.log4j.Logger;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.HttpActionMsg;
import com.k99k.khunter.KFilter;
import com.k99k.khunter.KObject;
import com.k99k.tools.encrypter.Base64Coder;

/**
 * 微博登录与注销
 * @author keel
 *
 */
public class WBLogin extends Action {

	/**
	 * @param name
	 */
	public WBLogin(String name) {
		super(name);
	}
	
	static final Logger log = Logger.getLogger(WBLogin.class);
	

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		String subact = KFilter.actPath(msg, 3, "show");
		
		//显示login入口
		if (subact.equals("show")) {
			msg.addData("[jsp]", "/WEB-INF/wb/login.jsp");
			return super.act(msg);
		}
		//login操作
		else if(subact.equals("login")){
			HttpActionMsg httpmsg = (HttpActionMsg)msg;
			String uName = httpmsg.getHttpReq().getParameter("uName");
			String uPwd = httpmsg.getHttpReq().getParameter("uName");
			if (uName != null && uPwd != null && uName.toString().trim().length()>3 && uPwd.toString().trim().length()>6) {
				KObject user = WBUser.findWBUser(uName);
				if (user != null && user.getProp("pwd").toString().equals(uPwd)) {
					//TODO 验证成功
					msg.addData("[print]", "ok");
					//加入cookie
					if (httpmsg.getHttpReq().getParameter("uCookie") != null) {
						WBUrl.setCookie("wbu", Base64Coder.encodeString(uName), httpmsg.getHttpResp());
					}
				}
			}
			msg.addData("[print]", "401");
		}
		//logout操作
		else if(subact.equals("logout")){
			
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
