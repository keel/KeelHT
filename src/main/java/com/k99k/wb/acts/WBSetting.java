/**
 * 
 */
package com.k99k.wb.acts;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.KFilter;

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
	
	

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		String subact = KFilter.actPath(msg, 2, "avatar");
		
		//显示login入口
		if (subact.equals("avatar")) {
			msg.addData("[jsp]", "/WEB-INF/wb/avatar.jsp");
			return super.act(msg);
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
		return null;
	}

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#init()
	 */
	@Override
	public void init() {

	}

}
