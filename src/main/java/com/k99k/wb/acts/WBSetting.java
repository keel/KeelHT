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
import com.k99k.tools.encrypter.Encrypter;

/**
 * @author keel
 *
 */
public class WBSetting extends Action {

	/**
	 * @param name
	 */
	public WBSetting(String name) {
		super(name);
	}
	static final Logger log = Logger.getLogger(WBSetting.class);
//	private static String[] basicReStr;
//	private static String[] changepwdReStr;
//	private static String[] checkmailReStr;
	private static String[] mailContentReStr;
	
	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		String subact = KFilter.actPath(msg, 2, "basic");
		String isSet = KFilter.actPath(msg, 3, "");
		HttpActionMsg httpmsg = (HttpActionMsg)msg;
		//验证用户请求是否合法
		KObject user = WBLogin.cookieAuth(httpmsg);
		if (user == null) {
			msg.addData("[redirect]", "/");
			return super.act(msg);
		}
		
		//基本设置
		if(subact.equals("basic")){
			basic(httpmsg,isSet,user);
			return super.act(msg);
		}
		//头像
		else if (subact.equals("avatar")) {
			msg.addData("[jsp]", "/WEB-INF/wb/avatar.jsp");
			return super.act(msg);
		}
		//修改密码
		else if(subact.equals("changepwd")){
			changepwd(httpmsg,isSet,user);
			return super.act(msg);
		}
		//邮箱修改与验证
		else if(subact.equals("checkmail")){
			checkmail(httpmsg,isSet,user);
			return super.act(msg);
		}
		
		
		
		return super.act(msg);
	}

	/**
	 * 处理基本设置请求
	 * @param httpmsg
	 * @param isSet
	 * @param user
	 */
	static final void basic(HttpActionMsg httpmsg,String isSet,KObject user){
		//是否更新请求
		if (isSet.equals("set")) {
			String uNick = httpmsg.getHttpReq().getParameter("uNick");
			String uSex = httpmsg.getHttpReq().getParameter("uSex");
			String cnLocal1 = httpmsg.getHttpReq().getParameter("cnLocal1");
			String cnLocal2 = httpmsg.getHttpReq().getParameter("cnLocal2");
			String cnYear = httpmsg.getHttpReq().getParameter("cnYear");
			String cnMonth = httpmsg.getHttpReq().getParameter("cnMonth");
			String cnDay = httpmsg.getHttpReq().getParameter("cnDay");
			String uUrl = httpmsg.getHttpReq().getParameter("uUrl");
			String uIntro = httpmsg.getHttpReq().getParameter("uIntro");
			if (StringUtil.isStringWithLen(uNick, 3)&& StringUtil.isDigits(uSex) && StringUtil.isStringWithLen(cnLocal1, 2)
					&& StringUtil.isStringWithLen(cnLocal2, 2)&& StringUtil.isDigits(cnYear)&& StringUtil.isDigits(cnMonth)&& StringUtil.isDigits(cnDay)) {
				user.setProp("screen_name", uNick);
				user.setProp("sex", uSex);
				user.setProp("location", cnLocal1+"-"+cnLocal2);
				user.setProp("birthday",cnYear+"-"+cnMonth+"-"+cnDay);
				user.setProp("user_url", StringUtil.objToStrNotNull(uUrl));
				user.setProp("description",StringUtil.objToStrNotNull(uIntro));
				if(WBUser.saveUserProp(user)){
					httpmsg.addData("[print]", "ok");
				}else{
					JOut.err(502, httpmsg);
				}
			}else{
				JOut.err(400, httpmsg);
			}
		}else{
			httpmsg.addData("[jsp]", "/WEB-INF/wb/basicsetting.jsp");
		}
	}
	
	/**
	 * 处理修改密码请求
	 * @param httpmsg
	 * @param isSet
	 * @param user
	 */
	static final void changepwd(HttpActionMsg httpmsg,String isSet,KObject user){
		//是否更新请求
		if (isSet.equals("set")) {
			String orgPwd = httpmsg.getHttpReq().getParameter("orgPwd");
			String newPwd = httpmsg.getHttpReq().getParameter("newPwd");
			String newPwd2 = httpmsg.getHttpReq().getParameter("newPwd2");
			if (StringUtil.isStringWithLen(orgPwd, 6)&& StringUtil.isStringWithLen(newPwd, 6)
					&& newPwd.equals(newPwd2) && user.getProp("pwd").equals(orgPwd)) {
				user.setProp("pwd", newPwd);
				if(WBUser.saveUserProp(user)){
					httpmsg.addData("[print]", "ok");
					return;
				}else{
					JOut.err(502, httpmsg);
				}
			}
			JOut.err(400, httpmsg);
		}else{
			httpmsg.addData("[jsp]", "/WEB-INF/wb/changepwd.jsp");
		}
		
	}
	
	private static Encrypter enc;// = new Encrypter();
	
	private static String mailTitle = "邮箱验证";
	private static String mailVerifyOK = "邮箱验证成功.";
	private static String mailVerifyFail = "邮箱验证失败.";
	/**
	 * 处理邮箱验证请求
	 * @param httpmsg
	 * @param isSet
	 * @param user
	 */
	static final void checkmail(HttpActionMsg httpmsg,String isSet,KObject user){
		
		if (isSet.equals("show")) {
			httpmsg.addData("[jsp]", "/WEB-INF/wb/checkmail.jsp");
			return;
		}
		//发送验证邮件
		else if (isSet.equals("verify")) {
			try {
				String mail = user.getProp("email").toString();
				String emailcheck = enc.encrypt(user.getName()+"|"+mail+"|"+System.currentTimeMillis());
				user.setProp("emailcheck", emailcheck);
				if(WBUser.saveUserProp(user)){
					WBEmail.addTask(mail, mailTitle, JOut.templetOut(mailContentReStr,new String[]{user.getName(),emailcheck,StringUtil.getFormatDateString("yyyy-MM-dd")}));
					httpmsg.addData("[print]", "ok");
					return;
				}else{
					JOut.err(500, httpmsg);
					return;
				}
			} catch (Exception e) {
				log.error("checkmail encrypt failed.", e);
				JOut.err(500, httpmsg);
				return;
			}
		}
		//修改邮箱
		else if(isSet.equals("modify")){
			String email = httpmsg.getHttpReq().getParameter("newEmail");
			if (StringUtil.isStringWithLen(email, 4)) {
				user.setProp("email", email);
				user.setState(0);
				if (WBUser.saveUserProp(user)) {
					httpmsg.addData("[print]",email);
					return;
				}else{
					JOut.err(500, httpmsg);
					return;
				}
			}else{
				JOut.err(400, httpmsg);
				return;
			}
		}
		//进行验证操作
		else if(isSet.equals("check")){
			String verify = httpmsg.getHttpReq().getParameter("key");
			if (verify.equals(user.getProp("emailcheck"))) {
				user.setState(1);
				if (WBUser.saveUserProp(user)) {
					//验证成功
					httpmsg.addData("[print]",mailVerifyOK);
					return;
				}
				
			}
			httpmsg.addData("[print]",mailVerifyFail);
			return;
		}else{
			httpmsg.addData("[jsp]", "/WEB-INF/wb/checkmail.jsp");
			return;
		}
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
		return "wb.json";
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
			enc = new Encrypter();
			Map<String, ?> m = (Map<String, ?>) root.get("wbSettings");
			mailContentReStr = m.get("mailContent").toString().split("###");
			mailVerifyOK = m.get("mailVerifyOK").toString();
			mailTitle = m.get("mailTitle").toString();
			mailVerifyFail = m.get("mailVerifyFail").toString();
		} catch (Exception e) {
			log.error("WBSetting init Error!", e);
		}
	}

}
