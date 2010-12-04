/**
 * 
 */
package com.k99k.khunter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

	private static boolean isInitOK = false;
	
	private static final JSONReader jsonReader = new JSONValidatingReader(new ExceptionErrorListener());
	
	private static String iniFilePath;
	
	private static String classFilePath;
	
	
	
	private static int ratePoolSize = 5;
	private static int scheduledPoolSize = 10;
	
	private static int taskMapInitSize = 50000;
	private static HashMap<String,ScheduledFuture<?>> taskMap = new HashMap<String, ScheduledFuture<?>>(taskMapInitSize);
	
	/**
	 * 定时任务执行的线程池
	 */
	private final static ScheduledThreadPoolExecutor scheduledPool =  new ScheduledThreadPoolExecutor(ratePoolSize,new RejectedTaskHandler());
	
	/**
	 * 定时循环执行任务的线程池
	 */
	private final static ScheduledThreadPoolExecutor ratePool =  new ScheduledThreadPoolExecutor(scheduledPoolSize,new RejectedTaskHandler());
	
	/**
	 * 单线程立即执行任务的线程池
	 */
	private final static ThreadPoolExecutor singleExePool = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());
	
		

	//exePool的相关参数如下
	private static int corePoolSize = 10;
	private static int maximumPoolSize = 200;
	private static long keepAliveTime = 3000L;
	private static int queueSize = 100;
	private static ArrayBlockingQueue<Runnable> arrBlockQueue = new ArrayBlockingQueue<Runnable>(queueSize);
	
	/**
	 * 立即执行任务的多线程线程池
	 */
	private static ThreadPoolExecutor exePool = new ThreadPoolExecutor(
			corePoolSize,
			maximumPoolSize,
			keepAliveTime,TimeUnit.MILLISECONDS,
			arrBlockQueue,
			new RejectedTaskHandler()
	);
	
	
	public static final int TASK_TYPE_EXE_POOL = 1;
	public static final int TASK_TYPE_EXE_SINGLE = 2;
	public static final int TASK_TYPE_SCHEDULE_POOL = 3;
	public static final int TASK_TYPE_SCHEDULE_RATE = 4;
	
	public static final String TASK_TYPE = "taskType";
	public static final String TASK_DELAY = "taskDelay";
	public static final String TASK_INIT_DELAY = "taskInitDelay";
	
	
	
	/**
	 * 添加一个立即执行的任务到立即处理的多线程线程池
	 * @param task
	 */
	public static void addExeTask(Task task){
		exePool.execute(task);
	}
	
	/**
	 * 添加一个定时执行的任务
	 * @param task Task
	 * @param delay 延迟
	 * @param unit 时间单位
	 */
	public static void addScheduledTask(Task task,long delay,TimeUnit unit){
		if (delay <= 0) {
			log.warn("ScheduledTask with no delay! Excuting now. task:"+task);
		}
		ScheduledFuture<?> sf = scheduledPool.schedule(task, delay, unit);
		if (!task.isCanCanceled()) {
			taskMap.put(task.getName(), sf);
		}
	}
	

	/**
	 * 添加一个循环执行的任务,如果没有delay参数则直接返回
	 * @param task Task
	 * @param initDelay 初始延迟
	 * @param delay 循环延迟
	 * @param unit 时间单位
	 */
	public static void addRateTask(Task task,long initDelay,long delay,TimeUnit unit){
		if (delay <= 0) {
			log.error("RateTask with no delay! Task canceled!! task:"+task);
			return;
		}
		ScheduledFuture<?> sf = ratePool.scheduleAtFixedRate(task, initDelay, delay, unit);
		if (!task.isCanCanceled()) {
			taskMap.put(task.getName(), sf);
		}
	}
	
	/**
	 * 由任务名来取消任务
	 * @param taskName String 
	 * @return 是否取消成功
	 */
	public static boolean cancelTask(String taskName){
		ScheduledFuture<?> sf = taskMap.remove(taskName);
		return sf.cancel(false);
	}
	
	/**
	 * 添加一个立即处理的任务到单线程池
	 * @param task Task
	 */
	public static void addSingleTask(Task task){
		singleExePool.execute(task);
	}
	

	/**
	 * 创建一个新的任务,ActionMsg必须包括Task相关的参数
	 * @param taskName String
	 * @param msg ActionMsg
	 * @return 当taskName有重名或msg参数有误时返回false
	 */
	public static boolean makeNewTask(String taskName,ActionMsg msg){
		if (msg == null || taskMap.containsKey(taskName)) {
			return false;
		}
		Object o = msg.getData(TASK_TYPE);
		int type = (o != null && o.toString().matches("[1234]")) ? Integer.parseInt(o.toString()):0;
		switch (type) {
		case TASK_TYPE_EXE_POOL:
			addExeTask(new Task(taskName,msg));
			break;
		case TASK_TYPE_EXE_SINGLE:
			addSingleTask(new Task(taskName,msg));
			break;
		case TASK_TYPE_SCHEDULE_POOL:
			Object o1 = msg.getData(TASK_DELAY);
			//如果没有delay字段则立即表示执行,delay为0
			long delay = (o1 != null && o1.toString().matches("\\d+")) ? Long.parseLong(o1.toString()):0;
			addScheduledTask(new Task(taskName,msg), delay, TimeUnit.MILLISECONDS);
			break;
		case TASK_TYPE_SCHEDULE_RATE:
			Object o2 = msg.getData(TASK_INIT_DELAY);
			//如果没有initDelay字段则立即表示执行,initDelay为0
			long initDelay = (o2 != null && o2.toString().matches("\\d+")) ? Long.parseLong(o2.toString()):0;
			Object o3 = msg.getData(TASK_DELAY);
			//如果没有delay字段则立即表示执行,delay为0
			long delay2 = (o3 != null && o3.toString().matches("\\d+")) ? Long.parseLong(o3.toString()):0;
			addRateTask(new Task(taskName,msg), initDelay, delay2, TimeUnit.MILLISECONDS);
			break;
		default:
			log.error("Task type error:"+type);
			return false;
		}
		log.info("TASK added:"+taskName);
		return true;
	}
	
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
				//先定位到json的tasks属性
				Map<String, ?> m = (Map<String, ?>) root.get(TaskManager.getName());
				Object o = m.get("taskMapInitSize");
				if (o != null && o.toString().matches("\\d+")) {
					int val = Integer.parseInt(o.toString());
					if (val != taskMapInitSize) {
						taskMapInitSize = val;
						HashMap<String, ScheduledFuture<?>> tm = new HashMap<String, ScheduledFuture<?>>(taskMapInitSize);
						synchronized (taskMap) {
							tm.putAll(taskMap);
							taskMap = tm;
						}
					}
				}
				o = m.get("ratePoolSize");
				if (o != null && o.toString().matches("\\d+")) {
					int val = Integer.parseInt(o.toString());
					if (val != ratePoolSize) {
						ratePoolSize = val;
						ratePool.setCorePoolSize(ratePoolSize);
					}
					
				}
				o = m.get("scheduledPoolSize");
				if (o != null && o.toString().matches("\\d+")) {
					int val = Integer.parseInt(o.toString());
					if (val != scheduledPoolSize) {
						scheduledPoolSize = val;
						scheduledPool.setCorePoolSize(scheduledPoolSize);
					}
					
				}
				o =  m.get("exePool");
				if (o != null && o instanceof Map) {
					Map<String,?> ep = (Map<String, ?>)o;
					o = ep.get("corePoolSize");
					if (o != null && o.toString().matches("\\d+")) {
						int val = Integer.parseInt(o.toString());
						if (val != corePoolSize) {
							corePoolSize = val;
							exePool.setCorePoolSize(corePoolSize);
						}
						
					}
					o = ep.get("maximumPoolSize");
					if (o != null && o.toString().matches("\\d+")) {
						int val = Integer.parseInt(o.toString());
						if (val != maximumPoolSize) {
							maximumPoolSize = val;
							exePool.setMaximumPoolSize(maximumPoolSize);
						}
					}
					o = ep.get("keepAliveTime");
					if (o != null && o.toString().matches("\\d+")) {
						long val = Long.parseLong(o.toString());
						if (val != keepAliveTime) {
							keepAliveTime = val;
							exePool.setKeepAliveTime(keepAliveTime, TimeUnit.MILLISECONDS);
						}
						
					}
					o = ep.get("queueSize");
					if (o != null && o.toString().matches("\\d+")) {
						int val = Integer.parseInt(o.toString());
						if (val != queueSize) {
							queueSize = val;
							ArrayBlockingQueue<Runnable> aq = new ArrayBlockingQueue<Runnable>(queueSize);
							exePool.shutdown();
							exePool = new ThreadPoolExecutor(
									corePoolSize,
									maximumPoolSize,
									keepAliveTime,TimeUnit.MILLISECONDS,
									arrBlockQueue,
									new RejectedTaskHandler()
							);
						}
					}
				}
			} catch (Exception e) {
				log.error("TaskManager init Error!", e);
				isInitOK = false;
				return false;
			}
			isInitOK = true;
			iniFilePath = iniFile;
			classFilePath = classPath;
			log.info("TaskManager init OK!");
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
	
	
	//TODO 告警机制：可采用在另一服务器用robot监控游戏各个环节，与服务端分离

	
	/**
	 * 刷新(重载)一个Task
	 * @param act actionName
	 */
	public static final boolean reLoadTask(String act){
		try {
			String ini = KIoc.readTxtInUTF8(iniFilePath);
			Map<String,?> root = (Map<String,?>) jsonReader.read(ini);
			//先定位到json的actions属性
			Map<String, ?> tasksMap = (Map<String, ?>) root.get(TaskManager.getName());
			Map<String, ?> m = (Map<String, ?>) tasksMap.get(act);
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
		ActionManager.init(jsonFilePath, classPath);
		TaskManager.init(jsonFilePath, classPath);
		
		ActionMsg msg = new ActionMsg("log");
		msg.addData(TASK_TYPE, TASK_TYPE_SCHEDULE_POOL);
		msg.addData(TASK_DELAY, 5000);
		TaskManager.makeNewTask("newTaskTest", msg);
		ActionMsg msg2 = new ActionMsg("log");
		msg2.addData(TASK_INIT_DELAY, 2000);
		msg2.addData(TASK_DELAY, 2000);
		msg2.addData(TASK_TYPE, TASK_TYPE_SCHEDULE_RATE);
		try {
 			Thread.sleep(2000);
 		} catch (InterruptedException e) {
 		}
		
 		TaskManager.makeNewTask("newTaskTest2", msg2);
		try {
 			Thread.sleep(15000);
 		} catch (InterruptedException e) {
 		}
 		System.out.println("cancel newTaskTest2:"+TaskManager.cancelTask("newTaskTest2"));
	}
	
}
