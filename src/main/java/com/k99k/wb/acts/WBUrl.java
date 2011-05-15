/**
 * 
 */
package com.k99k.wb.acts;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.HTManager;
import com.k99k.khunter.HttpActionMsg;
import com.k99k.khunter.KIoc;
import com.k99k.khunter.KObject;
import com.k99k.tools.JSONTool;
import com.k99k.tools.encrypter.Base64Coder;

/**
 * 处理WB的URl浏览请求,将url中的二级目录指到对应的用户;
 * 将用户实体加入msg中,同时处理用户web模板,处理cookie,分页等;
 * 不处理鉴权,如果是本人请求(读取cookie判断)则转至本人inbox;
 * @author keel
 *
 */
public class WBUrl extends Action {

	/**
	 * @param name
	 */
	public WBUrl(String name) {
		super(name);
	}
	static final Logger log = Logger.getLogger(WBUrl.class);
	
	/**
	 * cookie保存时间,初始化时由json配置 
	 */
	private static int cookieTime = 60 * 60 * 24;//默认为1天
	

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		HttpActionMsg httpmsg = (HttpActionMsg)msg;
		String actName = httpmsg.getData("[actName]").toString();
		//从cookie获取自己的名称
		String reqUserStr = getCookieValue(httpmsg.getHttpReq().getCookies(),"wbu","");
		//String auto = getCookieValue(httpmsg.getHttpReq().getCookies(),"al","false");
		boolean hasName = (!reqUserStr.equals(""));
		if (actName.equals("")) {
			if (hasName) {
				String[] u_p = Base64Coder.decodeString(reqUserStr).split(":");
				if (u_p.length >= 2) {
					httpmsg.addData("[redirect]", "/"+u_p[0]);
					return super.act(msg);
				}
			}
			httpmsg.addData("[jsp]", "/WEB-INF/wb/home.jsp");
			return super.act(msg);
		}
		if (hasName) {
			String[] u_p = Base64Coder.decodeString(reqUserStr).split(":");
			if (u_p.length >= 2 && u_p[0].equals(actName)) {
				//转向自己的inbox,验证cookie
				KObject wbUser = WBLogin.auth(u_p[0], u_p[1]);
				if (wbUser != null) {
					//转向自己的inbox
					httpmsg.addData("wbUser", wbUser);
					httpmsg.addData("[jsp]", "/WEB-INF/wb/inbox.jsp");
					return super.act(msg);
				}
			}
		}
		//TODO 是否在客户端发起验证请求？
		//如存在此用户则显示该用户的sent
		if (WBUser.checkWBUserName(actName)) {
			httpmsg.addData("wbUser", WBUser.findWBUser(actName));
			httpmsg.addData("[jsp]", "/WEB-INF/wb/sent.jsp");
		}else{
			//404
			JOut.err(404, httpmsg);
		}
		return super.act(msg);
	}

	/**
	 * 遍历cookie[]组,获取匹配值名对的cookie
	 * @param cookies
	 * @param cookieName
	 * @param defaultValue
	 * @return
	 */
	public final static String getCookieValue(Cookie[] cookies, String cookieName,
			String defaultValue) {
		if (cookies == null) {
			return defaultValue;
		}
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookieName.equals(cookie.getName()))
				return (cookie.getValue());
		}
		return (defaultValue);
	}
	
	/**
	 * 设置cookie
	 * @param cookieName
	 * @param cookieVal
	 * @param resp
	 */
	public final static void setCookie(String cookieName,String cookieVal,HttpServletResponse resp){
		setCookie(cookieName,cookieVal,cookieTime,resp);
	}
	
	/**
	 * 设置cookie
	 * @param cookieName
	 * @param cookieVal
	 * @param cookieTime cookie保持时间
	 * @param resp
	 */
	public final static void setCookie(String cookieName,String cookieVal,int cookieTime,HttpServletResponse resp){
		Cookie c = new Cookie(cookieName, cookieVal);// cookie值名对
		c.setMaxAge(cookieTime);// 有效期一年
		c.setPath("/"); // 路径
		//c.setDomain("192.168.0.115");// 域名
		resp.addCookie(c); // 在本地硬盘上产生文件
	}
	
	/**
	 * 删除cookie
	 * @param cookieName
	 * @param resp
	 */
	public final static void removeCookie(String cookieName,HttpServletResponse resp){
		Cookie c = new Cookie(cookieName, "");
		c.setMaxAge(0);// 有效时间为0则系统会自动删除过期的cookie
		c.setPath("/");
		resp.addCookie(c);
	}

	
	/**
	 * 处理req.getParameter得到的参数
	 * @param httpmsg HttpActionMsg
	 * @param paraNames 表单中的参数名数组
	 * @param kobjPropNames 真正用于操作的字段属性名数组
	 * @return 处理后的HashMap
	 */
	public final static HashMap<String,Object> parseParas(HttpActionMsg httpmsg,String[] paraNames,String[] kobjPropNames){
		HashMap<String,Object> paraMap = new HashMap<String, Object>();
		for (int i = 0; i < paraNames.length; i++) {
			String s = httpmsg.getHttpReq().getParameter(paraNames[i]);
			if (s != null) {
				paraMap.put(kobjPropNames[i], s.trim());
			}
		}
		return paraMap;
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
			Map<String, ?> wbuMap = (Map<String, ?>) root.get("cookies");
			int cookTime = Integer.parseInt(wbuMap.get("cookieTime").toString());
			cookieTime = cookTime;
		} catch (Exception e) {
			log.error("WBUrl init Error!", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#getIniPath()
	 */
	@Override
	public String getIniPath() {
		return "wb.json";
	}

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#reLoad()
	 */
	@Override
	public void reLoad() {
		super.reLoad();
	}

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#exit()
	 */
	@Override
	public void exit() {

	}

	
}
