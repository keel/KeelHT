/**
 * 
 */
package com.k99k.khunter;

import java.util.Date;

/**
 * Task 异步操作,与Action同步操作相区别
 * @author keel
 *
 */
public class Task {

	
	public Task() {
	}
	
	/**
	 * @param name
	 */
	public Task(String name) {
		super();
		this.name = name;
	}

	/**
	 * @param id
	 * @param name
	 */
	public Task(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	
	/**
	 * Task处理Action
	 * @param msg ActionMsg
	 * @return 执行后的ActionMsg
	 */
	public ActionMsg exe(ActionMsg msg){
		//加入本Task处理的完成时间
		msg.addData("task_"+this.name, new Date());
		//如果有下一个Action,则立即执行
		if (msg.getNextAction() != null) {
			msg.getNextAction().act(msg);
		}
		return msg;
	}

	private int id;
	
	private String name;

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
	 * @param name the name to set
	 */
	public final void setName(String name) {
		this.name = name;
	}
	
	

}
