/**
 * 
 */
package com.k99k.khunter;

import java.util.HashMap;
import java.util.Map;

/**
 * Action管理器，负责载入和刷新Action，以及添加新的Action等
 * @author keel
 *
 */
public class ActionManager {

	private ActionManager() {
	}
	
	private static final ActionManager me = new ActionManager();
	
	public static final ActionManager getInstance(){
		
		return me;
	}
	
	private static boolean isInited = false;
	
	/**
	 * 存储Action的Map,初始化大小为100
	 */
	private static final Map<String, Action> actionMap = new HashMap<String, Action>(100);
	
	public static boolean init(){
		if (!isInited) {
			//FIXME 初始化
			//读取配置文件刷新注入的Action数据
			
			isInited = true;
		}
		return true;
	}

	/**
	 * 获取一个Action
	 * @param actName
	 * @return Action
	 */
	public static final Action findAction(String actName){
		return actionMap.get(actName);
	}
	
	
	/**
	 * 添加一个Action,同时确定它 获取方式(单例或是每次均新建)
	 * @param act
	 * @return
	 */
	public static final boolean addAction(Action act){
		if (actionMap.containsKey(act.getName())) {
			return false;
		}
		actionMap.put(act.getName(), act);
		return true;
	}
	
	/**
	 * 更改某一个key对应的Action实例
	 * @param actName action的name
	 * @param action 新的Action
	 */
	public static final void changeAction(String actName,Action action){
		if (!actionMap.containsKey(actName)) {
			addAction(action);
			return;
		}else{
			actionMap.put(action.getName(), action);
		}
	}
	
	/**
	 * FIXME 刷新(重载)一个Action
	 * @param act
	 */
	public static final void reLoadAction(String act){
		
		
	}
	
	
	
}
