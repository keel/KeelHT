/**
 * 
 */
package com.k99k.khunter;

import org.apache.log4j.Logger;

/**
 * 一个动作单元
 * @author keel
 *
 */
public class Action {
	
	static final Logger log = Logger.getLogger(Action.class);

	/**
	 * @param name 动作标记
	 * @param pre 前一个Action,若无则为null
	 * @param next 后一个Action,若无则为null
	 * @param actRes ActionResource
	 */
	public Action(String name,Action pre,Action next,ActionResource actRes) {
		this.name = name;
		this.next = next;
		this.pre = pre;
		this.actRes = actRes;
	}
	
	private String name;
	
	private int id;
	
	private Action pre;
	private Action next;
	private ActionResource actRes;
	
	
	
	public String act(){
		String re = "";
		
		return re;
	}
	
	
	public Action getNextAction(){
		return this.next;
	}

	public Action getPreAction(){
		return this.pre;
	}


	/**
	 * @return the id
	 */
	public final int getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public final void setId(int id) {
		this.id = id;
	}


	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}
	
	
	
}
