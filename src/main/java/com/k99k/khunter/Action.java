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
	 */
	public Action(String name,Action pre,Action next) {
		this.name = name;
		this.next = next;
		this.pre = pre;
	}
	
	private String name;
	
	private int id;
	
	private Action pre;
	private Action next;
	
	
	
	public String act(String jsonInput,IOInterface ioInput){
		String re = "";
		
		return re;
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


	/**
	 * @return the pre
	 */
	public final Action getPre() {
		return pre;
	}


	/**
	 * @param pre the pre to set
	 */
	public final void setPre(Action pre) {
		this.pre = pre;
	}


	/**
	 * @return the next
	 */
	public final Action getNext() {
		return next;
	}


	/**
	 * @param next the next to set
	 */
	public final void setNext(Action next) {
		this.next = next;
	}
	
	
	
}
