package com.mnt.gui.fx.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 线程创建工厂类
 * @author 杜祥
 * @date 2013-1-30
 */
class PriorityThreadFactory implements ThreadFactory
{
	/**
	 * 线程优先级
	 */
	private int prio;
	
	/**
	 * 线程名称
	 */
	private String name;
	
	/**
	 * 线程条数
	 */
	private AtomicInteger	threadNumber	= new AtomicInteger(1);
	
	/**
	 * 线程组
	 */
	private ThreadGroup		group;
	
	
	public PriorityThreadFactory(String name, int prio)
	{
		this.prio = prio;
		this.name = name;
		group = new ThreadGroup(this.name);
	}
	
	
	@Override
	public Thread newThread(Runnable r) 
	{
		Thread t = new Thread(group, r);
		t.setName(name + "-" + threadNumber.getAndIncrement());
		t.setPriority(prio);
		t.setUncaughtExceptionHandler(new ThreadUncaughtExceptionHandler());
		return t;
	}
}
