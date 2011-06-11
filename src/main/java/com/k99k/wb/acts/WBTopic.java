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
 * 话题,可用于Task中调用
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
	
	

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ActionMsg act(ActionMsg msg) {
		
		ArrayList<String> topics = (ArrayList<String>) msg.getData("topics");
		long msgId = (Long)msg.getData("msgId");
		if (topics != null) {
			for (Iterator<String> it = topics.iterator(); it.hasNext();) {
				String tp = it.next();
				WBUserDao.addTopic(tp, msgId);
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
