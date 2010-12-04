/**
 * 
 */
package com.k99k.khunter.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.k99k.khunter.ActionManager;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.Task;
import com.k99k.khunter.TaskManager;

/**
 * @author keel
 *
 */
public class FastTask {

	public FastTask() {
	}
	static final Logger log = Logger.getLogger(FastTask.class);
	private final ScheduledExecutorService scheduler =   Executors.newScheduledThreadPool(2);
    private Map<String,ScheduledFuture<?>> tasks = new HashMap<String,ScheduledFuture<?>>(1000);
    
	
	// 任务
	final Runnable beeper = new Runnable() {
		private int i;
		private String name = "beeper";
		public void run() {
			i++;
			System.out.println(name+ " "+i);
		}
	};
	


	FutureTask<String> beeper2 = new FutureTask<String>(new Callable<String>() {
		private int i;
		public String call() {
			i++;
			System.out.println("beep2 "+i);
			return "beep2 "+i;
		}
	});

	public void addTask(TaskT task) {

		
		//每5秒执行一次
		final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);
		
		tasks.put(task.getName(), beeperHandle);
	}
	

	
	public void cancelBeep(String key){
		ScheduledFuture<?> beeperHandl = this.tasks.get(key);
		beeperHandl.cancel(false);
		System.out.println("beeperHandl canceled.");
	}
	
	
	
	void test1(){
		 this.addTask(new TaskT("t1"));
    	 this.addTask(new TaskT("t2"));
    	 this.addTask(new TaskT("t3"));
    	 
    	 try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
		}
		this.cancelBeep("t1");
    	 try {
 			Thread.sleep(20000);
 		} catch (InterruptedException e) {
 		}
 		this.cancelBeep("t2");
     	 try {
 			Thread.sleep(20000);
 		} catch (InterruptedException e) {
 		}
 		this.cancelBeep("t3");
    	 
	}
	
	//-----------------------------------------------------
	
	private int ratePoolSize = 5;
	private int scheduledPoolSize = 10;
	
	private int taskMapInitSize = 50000;
	private HashMap<String,ScheduledFuture<?>> taskMap = new HashMap<String, ScheduledFuture<?>>(taskMapInitSize);
	
	/**
	 * 定时任务执行的线程池
	 */
	private final ScheduledThreadPoolExecutor scheduledPool =  new ScheduledThreadPoolExecutor(ratePoolSize,new RejectedTaskHandler());
	
	/**
	 * 定时循环执行任务的线程池
	 */
	private final ScheduledThreadPoolExecutor ratePool =  new ScheduledThreadPoolExecutor(scheduledPoolSize,new RejectedTaskHandler());
	
	/**
	 * 单线程立即执行任务的线程池
	 */
	private final ThreadPoolExecutor singleExePool = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());
	
		

	
	private int corePoolSize = 10;
	private int maximumPoolSize = 200;
	private long keepAliveTime = 3000L;
	private int queueSize = 100;
	private ArrayBlockingQueue<Runnable> arrBlockQueue = new ArrayBlockingQueue<Runnable>(queueSize);
	
	/**
	 * 立即执行任务的多线程线程池
	 */
	private final ThreadPoolExecutor exePool = new ThreadPoolExecutor(
			corePoolSize,
			maximumPoolSize,
			keepAliveTime,TimeUnit.MILLISECONDS,
			arrBlockQueue,
			new RejectedTaskHandler()
	);
	
	/**
	 * 添加一个立即执行的任务到立即处理的多线程线程池
	 * @param task
	 */
	public void addExeTask(Task task){
		exePool.execute(task);
	}
	
	/**
	 * 添加一个定时执行的任务
	 * @param task Task
	 * @param delay 延迟
	 * @param unit 时间单位
	 */
	public void addScheduledTask(Task task,long delay,TimeUnit unit){
		if (delay <= 0) {
			log.warn("ScheduledTask with no delay! task:"+task);
		}
		ScheduledFuture<?> sf = scheduledPool.schedule(task, delay, unit);
		if (!task.isCanCanceled()) {
			this.taskMap.put(task.getName(), sf);
		}
	}
	

	/**
	 * 添加一个循环执行的任务,如果没有delay参数则直接返回
	 * @param task Task
	 * @param initDelay 初始延迟
	 * @param delay 循环延迟
	 * @param unit 时间单位
	 */
	public void addRateTask(Task task,long initDelay,long delay,TimeUnit unit){
		if (delay <= 0) {
			log.error("RateTask with no delay! canceled!! task:"+task);
			return;
		}
		ScheduledFuture<?> sf = ratePool.scheduleAtFixedRate(task, initDelay, delay, unit);
		if (!task.isCanCanceled()) {
			this.taskMap.put(task.getName(), sf);
		}
	}
	
	/**
	 * 由任务名来取消任务
	 * @param taskName String 
	 * @return 是否取消成功
	 */
	public boolean cancelTask(String taskName){
		ScheduledFuture<?> sf = this.taskMap.remove(taskName);
		return sf.cancel(false);
	}
	
	/**
	 * 添加一个立即处理的任务到单线程池
	 * @param task Task
	 */
	public void addSingleTask(Task task){
		this.singleExePool.execute(task);
	}
	
	public static final int TASK_TYPE_EXE_POOL = 1;
	public static final int TASK_TYPE_EXE_SINGLE = 2;
	public static final int TASK_TYPE_SCHEDULE_POOL = 3;
	public static final int TASK_TYPE_SCHEDULE_RATE = 4;
	
	public static final String TASK_TYPE = "taskType";
	public static final String TASK_DELAY = "taskDelay";
	public static final String TASK_INIT_DELAY = "taskInitDelay";
	
	
	/**
	 * 创建一个新的任务,ActionMsg必须包括Task相关的参数
	 * @param taskName String
	 * @param msg ActionMsg
	 * @return 当taskName有重名或msg参数有误时返回false
	 */
	public boolean makeNewTask(String taskName,ActionMsg msg){
		if (msg == null || this.taskMap.containsKey(taskName)) {
			return false;
		}
		Object o = msg.getData(TASK_TYPE);
		int type = (o != null && o.toString().matches("[1234]")) ? Integer.parseInt(o.toString()):0;
		switch (type) {
		case TASK_TYPE_EXE_POOL:
			this.addExeTask(new Task(taskName,msg));
			break;
		case TASK_TYPE_EXE_SINGLE:
			this.addSingleTask(new Task(taskName,msg));
			break;
		case TASK_TYPE_SCHEDULE_POOL:
			Object o1 = msg.getData(TASK_DELAY);
			//如果没有delay字段则立即表示执行,delay为0
			long delay = (o1 != null && o1.toString().matches("\\d+")) ? Long.parseLong(o1.toString()):0;
			this.addScheduledTask(new Task(taskName,msg), delay, TimeUnit.MILLISECONDS);
			break;
		case TASK_TYPE_SCHEDULE_RATE:
			Object o2 = msg.getData(TASK_INIT_DELAY);
			//如果没有initDelay字段则立即表示执行,initDelay为0
			long initDelay = (o2 != null && o2.toString().matches("\\d+")) ? Long.parseLong(o2.toString()):0;
			Object o3 = msg.getData(TASK_DELAY);
			//如果没有delay字段则立即表示执行,delay为0
			long delay2 = (o3 != null && o3.toString().matches("\\d+")) ? Long.parseLong(o3.toString()):0;
			this.addRateTask(new Task(taskName,msg), initDelay, delay2, TimeUnit.MILLISECONDS);
			break;
		default:
			log.error("Task type error:"+type);
			return false;
		}
		log.info("TASK added:"+taskName);
		return true;
	}
	
	
	
	void test2(){
		this.addExeTask(new TaskT("exe1"));
		this.addRateTask(new TaskT("rate1"), 5, 5, TimeUnit.SECONDS);
		this.addScheduledTask(new TaskT("sche1"), 10, TimeUnit.SECONDS);
		this.addSingleTask(new TaskT("single1"));
		this.addSingleTask(new TaskT("single2"));
		this.addSingleTask(new TaskT("single3"));
		this.addSingleTask(new TaskT("single4"));
		this.addExeTask(new TaskT("exe2"));
		this.addScheduledTask(new TaskT("sche2"), 20, TimeUnit.SECONDS);
		 try {
	 			Thread.sleep(6000);
	 		} catch (InterruptedException e) {
	 		}
		System.out.println("abort sche2:"+this.cancelTask("sche2"));
		System.out.println("abort rate1:"+this.cancelTask("rate1"));
	}
	
	void test3(){
		System.out.println("test3");
		String webRoot = "f:/works/workspace_keel/KHunter/WebContent/WEB-INF/";
		String jsonFilePath = webRoot+"kconfig.json";
		String classPath = webRoot+"classes/";
		ActionManager.init(jsonFilePath, classPath);
		ActionMsg msg = new ActionMsg("log");
		msg.addData(TASK_TYPE, TASK_TYPE_SCHEDULE_POOL);
		msg.addData(TASK_DELAY, 5000);
		this.makeNewTask("newTaskTest", msg);
		ActionMsg msg2 = new ActionMsg("log");
		msg2.addData(TASK_INIT_DELAY, 2000);
		msg2.addData(TASK_DELAY, 2000);
		msg2.addData(TASK_TYPE, TASK_TYPE_SCHEDULE_RATE);
		try {
 			Thread.sleep(2000);
 		} catch (InterruptedException e) {
 		}
		
		this.makeNewTask("newTaskTest2", msg2);
		try {
 			Thread.sleep(15000);
 		} catch (InterruptedException e) {
 		}
 		System.out.println("cancel newTaskTest2:"+this.cancelTask("newTaskTest2"));
	}
     
     public static void main(String[] args) {
    	 FastTask f = new FastTask();
    	 f.test3();
	}
	
	
}
