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
 * FIXME Task管理器，负责载入和刷新Task，以及添加新的Task等
 * @author keel
 *
 */
public final class TaskManager {

	private TaskManager() {
	}
	
	static final Logger log = Logger.getLogger(TaskManager.class);
	
	/**
	 * 用于在json中定位
	 * @return 返回"tasks"
	 */
	public static final String getName(){
		return "tasks";
	}
	
	/**
	 * 在json中定位线程配置
	 * @return 返回"taskThreads"
	 */
	public static final String getThreadName(){
		return "taskThreads";
	}
	
	private static boolean isInitOK = false;
	
	private static final JSONReader jsonReader = new JSONValidatingReader(new ExceptionErrorListener());
	
	/**
	 * 存储Task的Map,初始化大小为50
	 */
	private static final Map<String, Task> taskMap = new HashMap<String, Task>(50);
	
	private static String iniFilePath;
	
	private static String classFilePath;
	
	/**
	 * 初始化TaskManager
	 * @param iniFile 配置文件路径
	 * @param classPath class文件所在的路径
	 * @return 是否初始化成功
	 */
	public static boolean init(String iniFile,String classPath){
		if (!isInitOK) {
			//读取配置文件刷新注入的Task数据
			try {
				String ini = KIoc.readTxtInUTF8(iniFile);
				Map<String,?> root = (Map<String,?>) jsonReader.read(ini);
				//先定位到json的actions属性
				Map<String, ?> actionsMap = (Map<String, ?>) root.get(TaskManager.getName());
				//循环加入Task
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
						Task action = (Task)o;
						
						HTManager.fetchProps(action, m);
						//加入Task
						if(!addTask(action)){
							log.error("Task name alread exist! failed load this Task:"+action.getName()+" id:"+action.getId());
						}
						
					}else{
						log.error("Task init Error! miss one or more key props. Position:"+i);
						continue;
					}
					i++;
				}
				
				
				
			} catch (Exception e) {
				log.error("TaskManager init Error!", e);
				isInitOK = false;
				return false;
			}
			isInitOK = true;
			iniFilePath = iniFile;
			classFilePath = classPath;
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
	
	//FIXME 由一个ActionMsg创建一个新任务，并将其放到处理线程的队列中
	
	//创建TaskThread线程集，并管理其状态
	
	//TODO 告警机制：可采用在另一服务器用robot监控游戏各个环节，与服务端分离

	/**
	 * 获取一个Task
	 * @param actName
	 * @return Task,未找到返回null
	 */
	public static final Task findTask(String actName){
		return taskMap.get(actName);
	}
	
	
	/**
	 * 添加一个Task,同时确定它 获取方式(单例或是每次均新建)
	 * @param act
	 * @return
	 */
	public static final boolean addTask(Task act){
		if (act == null) {
			return false;
		}
		if (taskMap.containsKey(act.getName())) {
			return false;
		}
		taskMap.put(act.getName(), act);
		log.info("Task added: "+act.getName());
		return true;
	}
	
	/**
	 * 更改某一个key对应的Task实例
	 * @param actName action的name
	 * @param action 新的Task
	 */
	public static final void changeTask(String actName,Task action){
		if (action == null) {
			return;
		}
		taskMap.put(actName, action);
		log.info("Task changed: "+action.getName());
	}
	
	/**
	 * 刷新(重载)一个Task
	 * @param act actionName
	 */
	public static final boolean reLoadTask(String act){
		try {
			String ini = KIoc.readTxtInUTF8(iniFilePath);
			Map<String,?> root = (Map<String,?>) jsonReader.read(ini);
			//先定位到json的actions属性
			Map<String, ?> actionsMap = (Map<String, ?>) root.get(TaskManager.getName());
			Map<String, ?> m = (Map<String, ?>) actionsMap.get(act);
			if (!m.containsKey("_class")) {
				log.error("Task init Error! miss key prop:_class");
				return false;
			}
				
			String _class = (String) m.get("_class");
			Object o = KIoc.loadClassInstance("file:/"+classFilePath, _class, new Object[]{act});
			if (o == null) {
				log.error("loadClassInstance error! _class:"+_class+" _name:"+act);
				return false;
			}
			Task action = (Task)o;
			HTManager.fetchProps(action, m);
			taskMap.put(act, action);
			
		} catch (Exception e) {
			log.error("TaskManager reLoadTask Error:"+act, e);
			return false;
		}
		log.info("Task reLoaded: "+act);
		return true;
	}
	
	public static void main(String[] args) {
		String webRoot = "f:/works/workspace_keel/KHunter/WebContent/WEB-INF/";
		String jsonFilePath = webRoot+"kconfig.json";
		String classPath = webRoot+"classes/";
		TaskManager.init(jsonFilePath, classPath);
		Task a = TaskManager.findTask("login");
		System.out.println(a.getName()+ " id:"+a.getId());
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		TaskManager.reLoadTask("login");
		a = TaskManager.findTask("login");
		System.out.println(a.getName()+ " id:"+a.getId());
	}
	
}
