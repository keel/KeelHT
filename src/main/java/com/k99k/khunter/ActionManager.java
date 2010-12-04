/**
 * 
 */
package com.k99k.khunter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.stringtree.json.ExceptionErrorListener;
import org.stringtree.json.JSONReader;
import org.stringtree.json.JSONValidatingReader;

/**
 * Action管理器，负责载入和刷新Action，以及添加新的Action等
 * @author keel
 *
 */
public final class ActionManager {

	private ActionManager() {
	}
	
	static final Logger log = Logger.getLogger(ActionManager.class);
	
	private static final ActionManager me = new ActionManager();
	
	public static final ActionManager getInstance(){
		
		return me;
	}
	
	/**
	 * 用于在json中定位
	 * @return 返回"actions"
	 */
	public static final String getName(){
		return "actions";
	}
	
	private static boolean isInitOK = false;
	
	private static final JSONReader jsonReader = new JSONValidatingReader(new ExceptionErrorListener());
	
	/**
	 * 存储Action的Map,初始化大小为100
	 */
	private static final Map<String, Action> actionMap = new HashMap<String, Action>(100);
	
	private static String iniFilePath;
	
	private static String classFilePath;
	
	/**
	 * 初始化ActionManager
	 * @param iniFile 配置文件路径
	 * @param classPath class文件所在的路径
	 * @return 是否初始化成功
	 */
	public static boolean init(String iniFile,String classPath){
		if (!isInitOK) {
			//读取配置文件刷新注入的Action数据
			try {
				String ini = KIoc.readTxtInUTF8(iniFile);
				Map<String,?> root = (Map<String,?>) jsonReader.read(ini);
				//先定位到json的actions属性
				Map<String, ?> actionsMap = (Map<String, ?>) root.get(ActionManager.getName());
				//循环加入Action
				int i = 0;
				for (Iterator<String> iter = actionsMap.keySet().iterator(); iter.hasNext();) {
					String actionName = iter.next();
					Map<String, ?> m = (Map<String, ?>) actionsMap.get(actionName);
					//读取必要的属性，如果少则报错并继续下一个
					if (m.containsKey("_class")) {
						String _class = (String) m.get("_class");
						
						/*
						//type默认为normal //--直接在属性中加入
						//String _type = (m.containsKey("_type"))?"normal":(String) m.get("_type");
						*/
						
						Object o = KIoc.loadClassInstance("file:/"+classPath, _class, new Object[]{actionName});
						if (o == null) {
							log.error("loadClassInstance error! _class:"+_class+" _name:"+actionName);
							continue;
						}
						Action action = (Action)o;
						
						HTManager.fetchProps(action, m);
						//加入Action,无论是否已存在
						actionMap.put(action.getName(), action);
						log.info("Action added: "+action.getName());
						
					}else{
						log.error("Action init Error! miss one or more key props. Position:"+i);
						continue;
					}
					i++;
				}
				
				
				
			} catch (Exception e) {
				log.error("ActionManager init Error!", e);
				isInitOK = false;
				return false;
			}
			isInitOK = true;
			iniFilePath = iniFile;
			classFilePath = classPath;
			log.info("ActionManager init OK!");
		}
		
		return true;
	}
	
	/**
	 * 重新初始化
	 * @param iniFile 配置文件路径
	 * @param classPath class文件所在的路径
	 * @return 是否初始化成功
	 */
	public static boolean reInit(String iniFile,String classPath){
		isInitOK = false;
		return init(iniFile,classPath);
	}
	
	public static final boolean isInitOK(){
		return isInitOK;
	}

	/**
	 * 获取一个Action
	 * @param actName
	 * @return Action,未找到返回null
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
		if (act == null) {
			return false;
		}
		if (actionMap.containsKey(act.getName())) {
			return false;
		}
		actionMap.put(act.getName(), act);
		log.info("Action added: "+act.getName());
		return true;
	}
	
	/**
	 * 更改某一个key对应的Action实例
	 * @param actName action的name
	 * @param action 新的Action
	 */
	public static final void changeAction(String actName,Action action){
		if (action == null) {
			return;
		}
		actionMap.put(actName, action);
		log.info("Action changed: "+action.getName());
	}
	
	/**
	 * 刷新(重载)一个Action
	 * @param act actionName
	 */
	public static final boolean reLoadAction(String act){
		try {
			String ini = KIoc.readTxtInUTF8(iniFilePath);
			Map<String,?> root = (Map<String,?>) jsonReader.read(ini);
			//先定位到json的actions属性
			Map<String, ?> actionsMap = (Map<String, ?>) root.get(ActionManager.getName());
			Map<String, ?> m = (Map<String, ?>) actionsMap.get(act);
			if (!m.containsKey("_class")) {
				log.error("Action init Error! miss key prop:_class");
				return false;
			}
				
			String _class = (String) m.get("_class");
			Object o = KIoc.loadClassInstance("file:/"+classFilePath, _class, new Object[]{act});
			if (o == null) {
				log.error("loadClassInstance error! _class:"+_class+" _name:"+act);
				return false;
			}
			Action action = (Action)o;
			HTManager.fetchProps(action, m);
			actionMap.put(act, action);
			
		} catch (Exception e) {
			log.error("ActionManager reLoadAction Error:"+act, e);
			return false;
		}
		log.info("Action reLoaded: "+act);
		return true;
	}
	
	public static void main(String[] args) {
		String webRoot = "f:/works/workspace_keel/KHunter/WebContent/WEB-INF/";
		String jsonFilePath = webRoot+"kconfig.json";
		String classPath = webRoot+"classes/";
		ActionManager.init(jsonFilePath, classPath);
		Action a = ActionManager.findAction("login");
		System.out.println(a.getName()+ " id:"+a.getId());
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ActionManager.reLoadAction("login");
		a = ActionManager.findAction("login");
		System.out.println(a.getName()+ " id:"+a.getId());
	}
	
}
