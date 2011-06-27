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
import com.k99k.khunter.KIoc;
import com.k99k.khunter.dao.WBUserDao;
import com.k99k.tools.JSONTool;
import com.k99k.tools.StringUtil;

/**
 * @author keel
 *
 */
public class WBTopic extends Action {

	/**
	 * @param name
	 */
	public WBTopic(String name) {
		super(name);
	}
	
	private int pageSize = 30;

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		HttpActionMsg httpmsg = (HttpActionMsg)msg;
		String topic = httpmsg.getHttpReq().getParameter("t");
		if (!StringUtil.isStringWithLen(topic, 1)) {
			JOut.err(400, httpmsg);
			return super.act(msg);
		}
		String p_str = httpmsg.getHttpReq().getParameter("p");
		String pz_str = httpmsg.getHttpReq().getParameter("pz");
		int page = StringUtil.isDigits(p_str)?Integer.parseInt(p_str):1;
		int pz = StringUtil.isDigits(pz_str)?Integer.parseInt(pz_str):this.pageSize;
		if (!StringUtil.isDigits(p_str) || !StringUtil.isDigits(pz_str)) {
			JOut.err(400, httpmsg);
			return super.act(msg);
		}
		
		
		String re = WBMsg.writeKObjList(WBUserDao.readOneTopicList(topic, page, pz));
		
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
			Map<String, ?> m = (Map<String, ?>) root.get("wbTopic");
			this.pageSize = (StringUtil.isDigits(m.get("pageSize")))?Integer.parseInt(m.get("pageSize")+""):30;
		} catch (Exception e) {
			log.error("WBTalk init Error!", e);
		}
	}

	static final Logger log = Logger.getLogger(WBTopic.class);

}
