package com.mnt.gui.fx.concurrent;

import javafx.concurrent.Task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mnt.gui.fx.thread.ThreadPoolManager;

/**
 * 
 * 对于服务端的请求使用此任务类避免界面阻塞
 * <p>
 * 重写 call方法 里面为方法执行体<br/>
 * 添加获取返回值后直接执行更新ui的线程<br/>
 * 如果要获取异常事件 重写 failed() 方法即可<br/>
 * </p>
 * <p>
 * start()<br/>
 * succeeded -> executeAfterSucceeded(V v);<br/>
 * falied -> executeFailed(Throwable throwable);<br/>
 * executeEnd();<br/>
 * </p>
 * 
 * @author Cico.Jiang
 * @version 2014年3月21日 上午11:23:53 Cico.Jiang .
 * @since 3.3
 * @param <V>
 *            为返回值的类型
 */
public abstract class ServiceTask<V> extends Task<V>
{
	protected final Log log = LogFactory.getLog(getClass());;

	/**
	 * 是否立刻执行
	 * @param start
	 */
	public ServiceTask(boolean start) {
		if(start)
		{
			this.start();
		}
	}
	
	public ServiceTask() {
		this(false);
	}
	
	@Override
	protected void succeeded()
	{
		executeAfterSucceeded(getValue());
		executeEnd();
	}

	/**
	 * 
	 * execute successful and execute UI thread
	 * <p>
	 * execute about UI update 
	 * </p>
	 * 
	 * @since 2014年3月21日 上午11:26:16 Cico.Jiang
	 * @param value 为方法体的返回值
	 */
	protected abstract void executeAfterSucceeded(V value);

	/**
	 * 执行失败的时候会执行
	 * 2014-7-1 mnt.cico
	 * @param throwable
	 */
	protected void executeFailed(Throwable throwable) {
		throwable.printStackTrace();
		log.error(throwable);
	}
	
	/**
	 * 
	 * <p>
	 *  不论执行成功还是失败都会执行此方法
	 * </p>
	 * @create mnt.cico
	 */
	protected void executeEnd()
	{
	
	};
	
	@Override
	protected void failed()
	{
		executeFailed(getException());
		executeEnd();
	}

	/**
	 * 异步执行此任务
	 * <p>
	 * 一般用于Controller里面 所以用异步执行<br/>
	 * tip:注意并发事件做好Controller限制
	 * </p>
	 * 
	 * @since 2014年3月21日 上午11:49:22 Cico.Jiang
	 */
	public void start()
	{
		ThreadPoolManager.getInstance().executeAction(this);
//		Thread thread = new Thread(this);
//		thread.setName("service task thread!");
//		thread.setDaemon(true);
//		thread.start();
	}
}
