/**
 * 
 */
package com.k99k.wb.acts;



import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.HttpActionMsg;
import com.k99k.khunter.KFilter;
import com.k99k.khunter.KObject;
import com.k99k.khunter.dao.WBUserDao;
import com.k99k.tools.JSONTool;
import com.k99k.tools.StringUtil;

/**
 * @author keel
 *
 */
public class WBFans extends Action {

	/**
	 * @param name
	 */
	public WBFans(String name) {
		super(name);
	}
	
	
	private int pageSize = 20;
	
	
	
	
	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		String subact = KFilter.actPath(msg, 2, "");
		HttpActionMsg httpmsg = (HttpActionMsg)msg;
		String re = null;
		//验证用户请求是否合法
		KObject user = WBLogin.cookieAuth(httpmsg);
		if (user == null) {
			JOut.err(401, httpmsg);
			return super.act(msg);
		}
		long userId = user.getId();
		if (subact.equals("")) {
			//转到follow页
			msg.addData("wbUser", user);
			msg.addData("[jsp]", "/WEB-INF/wb/fans.jsp");
			return super.act(msg);
		}else if (subact.equals("fans")) {
			String p_str = httpmsg.getHttpReq().getParameter("p");
			String pz_str = httpmsg.getHttpReq().getParameter("pz");
			int page = StringUtil.isDigits(p_str)?Integer.parseInt(p_str):1;
			int pz = StringUtil.isDigits(pz_str)?Integer.parseInt(pz_str):this.pageSize;
			re = JSONTool.writeJsonString(WBUserDao.getFans(userId, page, pz));
			if (re == null || re.equals("null")) {
				re = "[]";
			}
		}
		msg.addData("[print]", re);
		return super.act(msg);
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


}
