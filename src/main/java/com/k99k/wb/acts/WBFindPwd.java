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
import com.k99k.tools.StringUtil;

/**
 * 找回密码
 * @author keel
 *
 */
public class WBFindPwd extends Action {

	/**
	 * @param name
	 */
	public WBFindPwd(String name) {
		super(name);
	}
	static final Logger log = Logger.getLogger(WBFindPwd.class);
	private static String[] mailContentReStr;
	private static String mailTitle = "找回密码";

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		HttpActionMsg httpmsg = (HttpActionMsg)msg;
		String subact = KFilter.actPath(msg, 2, "findpwd");
		if (subact.equals("find")) {
			String uEmail = httpmsg.getHttpReq().getParameter("uEmail");
			String uName = httpmsg.getHttpReq().getParameter("uName");
			if (StringUtil.isStringWithLen(uEmail, 4) && StringUtil.isStringWithLen(uName, 4)) {
				KObject user = WBUser.findWBUser(uName);
				if (user !=null && user.getProp("email").equals(uEmail)) {
					WBEmail.addTask(uEmail, mailTitle, JOut.templetOut(mailContentReStr,new String[]{user.getName(),user.getProp("pwd").toString(),StringUtil.getFormatDateString("yyyy-MM-dd")}));
					httpmsg.addData("[print]", "ok");
					return super.act(msg);
				}
			}else{
				JOut.err(400, httpmsg);
			}
		}else{
			msg.addData("[jsp]", "/WEB-INF/wb/findpwd.jsp");
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
			Map<String, ?> m = (Map<String, ?>) root.get("wbFindPwd");
			mailContentReStr = m.get("mailContent").toString().split("###");
			mailTitle = m.get("mailTitle").toString();
		} catch (Exception e) {
			log.error("WBSetting init Error!", e);
		}
	}

}
