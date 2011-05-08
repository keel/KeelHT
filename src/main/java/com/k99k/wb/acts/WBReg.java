/**
 * 
 */
package com.k99k.wb.acts;

import java.util.HashMap;
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
 * 用户注册
 * @author keel
 *
 */
public class WBReg extends Action {

	/**
	 * @param name
	 */
	public WBReg(String name) {
		super(name);
	}
	static final Logger log = Logger.getLogger(WBReg.class);
	
	static String[] regJsonStr;
	
	private static final String[] regParas = new String[]{"uName","uPwd","uPwd2","email"};
	private static final String[] realParas = new String[]{"name","pwd","pwd","email"};

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		String subact = KFilter.actPath(msg, 2, "show");
		if (subact.equals("show")) {
			msg.addData("[jsp]", "/WEB-INF/wb/reg.jsp");
			return super.act(msg);
		}
		//reg操作
		else if(subact.equals("reg")){
			HttpActionMsg httpmsg = (HttpActionMsg)msg;
			HashMap<String,Object> paras = WBUrl.parseParas(httpmsg, regParas,realParas);
			if (paras != null && paras.size() > 0) {
				KObject user = WBUser.regUser(paras);
				if (user != null) {
					//注册成功,加入登录信息
					WBUrl.setCookie("wbu", Base64Coder.encodeString(user.getName()+":"+user.getProp("pwd")+":"+System.currentTimeMillis()),60*20, httpmsg.getHttpResp());
					JOut.txtOut(regJsonStr, new String[]{user.getName()}, httpmsg);
					return super.act(msg);
				}
			}
			JOut.err(400, httpmsg);
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
			Map<String, ?> wbuMap = (Map<String, ?>) root.get("wbReg");
			regJsonStr = wbuMap.get("reg").toString().split("###");
		} catch (Exception e) {
			log.error("WBLogin init Error!", e);
		}
	}

}
