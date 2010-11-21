/**
 * 
 */
package com.k99k.khunter;

import java.util.Date;

/**
 * 同步操作,与Task异步操作相区别,多个Action之间可相互调用
 * @author keel
 *
 */
public class Action {
	
	/**
	 * @param name 动作标记
	 */
	public Action(String name) {
		this.name = name;
	}
	
	private String name;
	
	private int id;
	
	/**
	 * 获取方式,single表示单例,normal表示每次创建新的,默认为normal
	 */
	private String type = "normal";
	
	/**
	 * 执行动作,如果有下一个动作则在本动作完成后直接调用下一个Action继续执行
	 * @param msg ActionMsg
	 * @return 执行后的ActionMsg
	 */
	public ActionMsg act(ActionMsg msg){
		
		//加入本Action处理的完成时间
		msg.addData("action_"+this.name, new Date());
		//如果有下一个Action,则立即执行
		if (msg.getNextAction() != null) {
			msg.getNextAction().act(msg);
		}
		return msg;
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
	 * @return the type
	 */
	public final String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public final void setType(String type) {
		this.type = type;
	}

	
	
}
