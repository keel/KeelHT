/**
 * 
 */
package com.k99k.wb.acts;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.HTManager;
import com.k99k.khunter.HttpActionMsg;
import com.k99k.khunter.KIoc;
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
		//如果有此cookie值则用base64解密值
		reqUserStr = (reqUserStr.equals(""))?"":Base64Coder.decodeString(reqUserStr);
		boolean isSelf = reqUserStr.equals(actName);
		//TODO 如果是自己则转向自己的inbox
		if (isSelf) {
			
			return super.act(msg);
		}
		//TODO 显示该用户的sent
		
		
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
		return "wbUrl.json";
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
