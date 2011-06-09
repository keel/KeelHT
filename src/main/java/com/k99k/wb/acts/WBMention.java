/**
 * 
 */
package com.k99k.wb.acts;

import com.k99k.khunter.Action;

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
