package com.mnt.gui.fx.thread;

import java.lang.Thread.UncaughtExceptionHandler;

import org.apache.log4j.Logger;

/**
 * 
 * <p>
 * 线程异常
 * </p>
 * @author    mnt.cico
 * @version  2014年12月15日 上午7:37:53 mnt.cico .
 * @since   FX8.0
 */
public class ThreadUncaughtExceptionHandler implements UncaughtExceptionHandler
{
	private static final Logger log = Logger.getLogger(ThreadUncaughtExceptionHandler.class);
	
	@Override
	public void uncaughtException(Thread t, Throwable e)
	{
		log.error("严重错误 - 线程: " + t.getName() + " 终止异常 " + e, e);
		if (e instanceof OutOfMemoryError)
		{
			log.error("内存不足! 需要更多的内存!");
		}
	}

}
