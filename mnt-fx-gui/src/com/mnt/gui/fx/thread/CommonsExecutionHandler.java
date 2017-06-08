package com.mnt.gui.fx.thread;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

/**
 * 无法由 ThreadPoolExecutor 执行的任务的处理程序。 
 * @author 杜祥
 * @date 2012-10-22
 */
public class CommonsExecutionHandler implements RejectedExecutionHandler
{
	private final static Logger log = Logger.getLogger(CommonsExecutionHandler.class);
	
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) 
	{
		if(executor.isShutdown())
		{
			return ;
		}
		
		log.warn(r + " from " + executor, new RejectedExecutionException());
		
		if (Thread.currentThread().getPriority() > Thread.NORM_PRIORITY)
			new Thread(r).start();
		else
			r.run();
	}

}
