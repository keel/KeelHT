/**
 * 
 */
package com.k99k.khunter.tasks;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

/**
 * 拒绝任务处理方法
 * @author keel
 *
 */
public class RejectedTaskHandler implements RejectedExecutionHandler {

	public RejectedTaskHandler() {
	}
	
	static final Logger log = Logger.getLogger(RejectedTaskHandler.class);

	/* (non-Javadoc)
	 * @see java.util.concurrent.RejectedExecutionHandler#rejectedExecution(java.lang.Runnable, java.util.concurrent.ThreadPoolExecutor)
	 */
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		// 进行日志记录，然后取消任务执行
		log.error("TASK Aborted! task:"+r.toString());
	}

}
