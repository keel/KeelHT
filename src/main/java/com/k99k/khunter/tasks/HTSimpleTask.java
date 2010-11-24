/**
 * 
 */
package com.k99k.khunter.tasks;

import org.apache.log4j.Logger;

import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.Task;

/**
 * 简单任务,实际上与Task一样
 * @author keel
 *
 */
public class HTSimpleTask extends Task {

	public HTSimpleTask() {
	}
	
	static final Logger log = Logger.getLogger(HTSimpleTask.class);

	/**
	 * @param name
	 */
	public HTSimpleTask(String name) {
		super(name);
	}

	/**
	 * @param id
	 * @param name
	 */
	public HTSimpleTask(int id, String name) {
		super(id, name);
	}

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Task#exe(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg exe(ActionMsg msg) {
		
		log.info("~~~~~Hi task executing...~~~~");
		
		return super.exe(msg);
	}

	
	
	
}
