/**
 * 
 */
package com.k99k.wb.acts;

import java.util.Map;

import org.apache.log4j.Logger;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.HTManager;
import com.k99k.khunter.HttpActionMsg;
import com.k99k.khunter.KFilter;
import com.k99k.khunter.KIoc;
import com.k99k.khunter.KObject;
import com.k99k.tools.JSONTool;
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
	
	/**
	 * 模板化处理后的String[]
	 */
	private static String[] logOutStrArr;

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		String subact = KFilter.actPath(msg, 2, "show");
		
		//显示login入口
		if (subact.equals("show")) {
			msg.addData("[jsp]", "/WEB-INF/wb/login.jsp");
			return super.act(msg);
		}
		//login操作
		else if(subact.equals("login")){
			HttpActionMsg httpmsg = (HttpActionMsg)msg;
			login(httpmsg);
		}
		//logout操作
		else if(subact.equals("logout")){
			HttpActionMsg httpmsg = (HttpActionMsg)msg;
			logout(httpmsg);
		}
		
		return super.act(msg);
	}
	

	/**
	 * 登录,需要uName,uPwd,uCookie
	 * @param httpmsg HttpActionMsg
	 */
	public static final void login(HttpActionMsg httpmsg){
		String uName = httpmsg.getHttpReq().getParameter("uName");
		String uPwd = httpmsg.getHttpReq().getParameter("uPwd");
		if (uName != null && uPwd != null && uName.toString().trim().length()>3 && uPwd.toString().trim().length()>=6) {
			KObject user = WBUser.findWBUser(uName);
			if (user != null && user.getProp("pwd").toString().equals(uPwd)) {
				//验证成功,返回uName
				JOut.txtOut(uName, httpmsg);
				//加入cookie
				String cookie = httpmsg.getHttpReq().getParameter("uCookie");
				if (cookie != null && cookie.equals("true")) {
					//保存cookie
					WBUrl.setCookie("wbu", Base64Coder.encodeString(uName+":"+uPwd+":"+System.currentTimeMillis()), httpmsg.getHttpResp());
					WBUrl.setCookie("al", "true", httpmsg.getHttpResp());
				}else{
					//仅持续20分钟
					WBUrl.setCookie("wbu", Base64Coder.encodeString(uName+":"+uPwd+":"+System.currentTimeMillis()),60*20, httpmsg.getHttpResp());
				}
				return;
			}
		}
		JOut.err(401, httpmsg);
	}

	/**
	 * 注销,需要uName
	 * @param httpmsg HttpActionMsg
	 */
	public static final void logout(HttpActionMsg httpmsg){
		String uName = httpmsg.getHttpReq().getParameter("uName");
		if (uName != null) {
			JOut.txtOut(logOutStrArr, new String[]{uName,"Logged out."}, httpmsg);
			WBUrl.removeCookie("wbu", httpmsg.getHttpResp());
			WBUrl.removeCookie("al", httpmsg.getHttpResp());
			return;
		}
		JOut.err(401, httpmsg);
	}
	
	/**
	 * 鉴权,使用basic认证方式在http header实现的鉴权
	 * @param httpmsg HttpActionMsg
	 * @return KObject 成功则返回wbUser对象,为null表示失败
	 */
	public static final KObject basicAuth(HttpActionMsg httpmsg){
		try {
			String authStr = httpmsg.getHttpReq().getHeader("Authorization");
			String enc = authStr.split(" ")[1];
			String[] u_p = Base64Coder.decodeString(enc).split(":");
			KObject user = auth(u_p[0],u_p[1]);
			if (user != null) {
				httpmsg.addData("wbUser", user);
				return user;
			}
		} catch (Exception e) {
			log.error("auth error:", e);
			return null;
		}
		return null;
	}
	
	/**
	 * 鉴权,利用cookie中的用户信息
	 * @param httpmsg HttpActionMsg
	 * @return KObject 成功则返回wbUser对象,为null表示失败
	 */
	public static final KObject cookieAuth(HttpActionMsg httpmsg){
		try {
			String reqUserStr = WBUrl.getCookieValue(httpmsg.getHttpReq().getCookies(),"wbu","");
			String alStr = WBUrl.getCookieValue(httpmsg.getHttpReq().getCookies(),"al","");
			if (reqUserStr.equals("")) {
				return null;
			}
			String[] u_p = Base64Coder.decodeString(reqUserStr).split(":");
			KObject user = auth(u_p[0],u_p[1]);
			if (user != null) {
				httpmsg.addData("wbUser", user);
				//未保存自动登录时,延长cookie时间
				if (alStr.equals("")) {
					WBUrl.setCookie("wbu", Base64Coder.encodeString(u_p[0]+":"+u_p[1]+":"+System.currentTimeMillis()),60*20, httpmsg.getHttpResp());
				}
				return user;
			}
		} catch (Exception e) {
			log.error("auth error:", e);
			return null;
		}
		return null;
		
	}
	
	/**
	 * 鉴权用户名和密码,返回KObject user对象
	 * @param uName
	 * @param uPwd
	 * @return KObject 成功则返回wbUser对象,为null表示失败
	 */
	public static final KObject auth(String uName,String uPwd){
		KObject user = WBUser.findWBUser(uName);
		if (user != null && user.getProp("pwd").toString().equals(uPwd)) {
			return user;
		}
		return null;
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
		return "wbOutTemplet.json";
	}

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#init()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			String ini = KIoc.readTxtInUTF8(HTManager.getIniPath()+getIniPath());
			Map<String,?> root = (Map<String,?>) JSONTool.readJsonString(ini);
			//先定位到json的cookies属性
			Map<String, ?> wbuMap = (Map<String, ?>) root.get("wbLogin");
			logOutStrArr = wbuMap.get("logout").toString().split("###");
		} catch (Exception e) {
			log.error("WBLogin init Error!", e);
		}
	}

}
