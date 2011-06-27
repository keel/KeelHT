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
import com.k99k.khunter.dao.WBUserDao;
import com.k99k.tools.JSONTool;
import com.k99k.tools.StringUtil;

/**
 * @author keel
 *
 */
public class WBFavor extends Action {

	/**
	 * @param name
	 */
	public WBFavor(String name) {
		super(name);
	}
	
	private int pageSize;

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
			re = WBMsg.writeKObjList(WBUserDao.readFavList(user.getId(), page, pz));
		}else if(subact.equals("add")){
			String msg_str = httpmsg.getHttpReq().getParameter("msgid");
			if (!StringUtil.isDigits(msg_str)) {
				JOut.err(400, httpmsg);
				return super.act(msg);
			}
			long msgId = Long.parseLong(msg_str);
			boolean f = WBUserDao.addFavor(msgId, user.getId());
			re = String.valueOf(f);
		}else if(subact.equals("del")){
			String fav_str = httpmsg.getHttpReq().getParameter("favid");
			if (!StringUtil.isDigits(fav_str)) {
				JOut.err(400, httpmsg);
				return super.act(msg);
			}
			long favId = Long.parseLong(fav_str);
			boolean f = WBUserDao.delFavor(favId, user.getId());
			re = String.valueOf(f);
		}
		msg.addData("[print]", re);
		return super.act(msg);
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
			Map<String, ?> m = (Map<String, ?>) root.get("wbFavor");
			this.pageSize = (StringUtil.isDigits(m.get("pageSize")))?Integer.parseInt(m.get("pageSize")+""):20;
		} catch (Exception e) {
			log.error("WBTalk init Error!", e);
		}
	}

	static final Logger log = Logger.getLogger(WBFavor.class);
	
	
}
