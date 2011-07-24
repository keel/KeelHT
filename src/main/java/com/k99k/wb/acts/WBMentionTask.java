/**
 * 
 */
package com.k99k.wb.acts;

import java.util.ArrayList;
import java.util.Iterator;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.dao.WBUserDao;
import com.k99k.tools.StringUtil;

/**
 * 提到的用户(@someone),用于Task处理
 * @author keel
 *
 */
public class WBMentionTask extends Action {

	/**
	 * @param name
	 */
	public WBMentionTask(String name) {
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
		Object ort = msg.getData("rt_id");
		long rt_id = (StringUtil.isDigits(ort))?(Long)ort:0;
		if (mentions != null) {
			for (Iterator<String> it = mentions.iterator(); it.hasNext();) {
				String mt = it.next();
				WBUserDao.addMention(mt, msgId,rt_id);
			}
		}
		
		
		return super.act(msg);
	}



}
