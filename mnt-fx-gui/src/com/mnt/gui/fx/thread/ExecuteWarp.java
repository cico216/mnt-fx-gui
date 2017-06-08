package com.mnt.gui.fx.thread;



import org.apache.log4j.Logger;




/**
 * 
 * @author 姜彪
 * @date 2016年5月16日
 */
public abstract class ExecuteWarp implements Runnable 
{
	protected final Logger	log	= Logger.getLogger(getClass());
	
	@Override
	public void run() 
	{
		try 
		{
			runImpl();
		}
		catch (Throwable e)
		{
			log.warn("线程运行时，出现的异常", e);
		}
	}
	
	protected abstract void runImpl();
}
