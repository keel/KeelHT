/**
 * 
 */
package com.k99k.wb.acts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.HttpActionMsg;
import com.k99k.khunter.KFilter;
import com.k99k.khunter.KObject;
import com.k99k.khunter.dao.WBUserDao;
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
			String txt = dealMention(dmsg_str).toString();
			re = String.valueOf(WBUserDao.addDMsg(user.getId(), targetId, txt, user.getName()));
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
	 * @return
	 */
	public static final StringBuffer dealMention(String str){
		Pattern pattern = Pattern.compile("@((\\S{3,12}))");
		Matcher matcher = pattern.matcher(str);
		StringBuffer buffer = new StringBuffer();
		while(matcher.find()){   
			//TODO 处理@号的链接
			String re = matcher.group(1);
			matcher.appendReplacement(buffer, JOut.templetOut(mentionReStr,new String[]{"?mention="+re,re}));           
		}
		matcher.appendTail(buffer);
		return buffer;
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

	/**
	 * @param mentionReStr the mentionReStr to set
	 */
	@SuppressWarnings("unchecked")
	public static final void setMentionReStr(Object mReStr) {
		ArrayList<String> marr = (ArrayList<String>)mReStr;
		mentionReStr = new String[marr.size()];
		int i = 0;
		for (Iterator<String> it = marr.iterator(); it.hasNext();) {
			String s = it.next();
			mentionReStr[i] = s;
			i++;
		}
	}
	
	

}
