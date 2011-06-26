/**
 * 
 */
package com.k99k.wb.acts;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.HTManager;
import com.k99k.khunter.HttpActionMsg;
import com.k99k.khunter.KFilter;
import com.k99k.khunter.KIoc;
import com.k99k.khunter.KObject;
import com.k99k.khunter.dao.WBUserDao;
import com.k99k.tools.JSONTool;
import com.k99k.tools.StringUtil;

/**
 * @author keel
 *
 */
public class WBDMsg extends Action {

	/**
	 * @param name
	 */
	public WBDMsg(String name) {
		super(name);
	}
	
	private int pageSize = 20;
	
	private static String[] mentionReStr;

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		String subact = KFilter.actPath(msg, 2, "list");
		HttpActionMsg httpmsg = (HttpActionMsg)msg;
		//验证用户请求是否合法
		KObject user = WBLogin.cookieAuth(httpmsg);
		if (user == null) {
			JOut.err(401, httpmsg);
			return super.act(msg);
		}
		String re = "";
		if (subact.equals("list")) {
			String p_str = httpmsg.getHttpReq().getParameter("p");
			String pz_str = httpmsg.getHttpReq().getParameter("pz");
			int page = StringUtil.isDigits(p_str)?Integer.parseInt(p_str):1;
			int pz = StringUtil.isDigits(pz_str)?Integer.parseInt(pz_str):this.pageSize;
			re = WBMsg.writeKObjList(WBUserDao.readDMsg(user.getId(), page, pz));
		}else if(subact.equals("add")){
			String dmsg_str = httpmsg.getHttpReq().getParameter("dmsg");
			String targetId_str = httpmsg.getHttpReq().getParameter("targetId");
			if (!StringUtil.isDigits(targetId_str) || !StringUtil.isStringWithLen(dmsg_str, 1)) {
				JOut.err(401, httpmsg);
				return super.act(msg);
			}
			long targetId = Long.parseLong(targetId_str);
			re = String.valueOf(WBUserDao.addDMsg(user.getId(), targetId, dmsg_str, user.getName()));
		}else if(subact.equals("del")){
			String dmsgId_str = httpmsg.getHttpReq().getParameter("dmsgId");
			if (!StringUtil.isDigits(dmsgId_str)) {
				JOut.err(401, httpmsg);
				return super.act(msg);
			}
			long dmsgId = Long.parseLong(dmsgId_str);
			boolean f = WBUserDao.delDMsg(user.getId(), dmsgId);
			re = String.valueOf(f);
		}
		msg.addData("[print]", re);
		return super.act(msg);
	}
	
	/**
	 * 处理提到的用户
	 * @param sb
	 * @param msgId
	 * @return
	 */
	public static final StringBuffer dealMention(StringBuffer sb,long msgId){
		Pattern pattern = Pattern.compile("@((\\S+))");
		Matcher matcher = pattern.matcher(sb);
		StringBuffer buffer = new StringBuffer();
		while(matcher.find()){   
			//TODO 处理@号的链接
			String re = matcher.group(1);
			matcher.appendReplacement(buffer, JOut.templetOut(mentionReStr,new String[]{"?mention="+re,re}));           
		}
		matcher.appendTail(buffer);
		return buffer;
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
			Map<String, ?> m = (Map<String, ?>) root.get("wbDMsg");
			this.pageSize = (StringUtil.isDigits(m.get("pageSize")))?Integer.parseInt(m.get("pageSize")+""):20;
			mentionReStr =  m.get("mentionReplace").toString().split("###");
		} catch (Exception e) {
			log.error("WBTalk init Error!", e);
		}
	}

	static final Logger log = Logger.getLogger(WBDMsg.class);

}
