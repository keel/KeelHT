/**
 * 
 */
package com.k99k.wb.acts;

import java.util.ArrayList;
import java.util.Iterator;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.dao.WBUserDao;

/**
 * 提到的用户(@someone),用于Task处理
 * @author keel
 *
 */
public class WBMention extends Action {

	/**
	 * @param name
	 */
	public WBMention(String name) {
		super(name);
	}

	
	
	
	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ActionMsg act(ActionMsg msg) {
		ArrayList<String> mentions = (ArrayList<String>) msg.getData("mentions");
		long msgId = (Long)msg.getData("msgId");
		if (mentions != null) {
			for (Iterator<String> it = mentions.iterator(); it.hasNext();) {
				String mt = it.next();
				WBUserDao.addMention(mt, msgId);
			}
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
