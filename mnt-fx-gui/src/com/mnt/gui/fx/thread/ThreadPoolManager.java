package com.mnt.gui.fx.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;


/**
 * 线程池
 * @author 姜彪
 * @date 2016年5月16日
 */
public class ThreadPoolManager implements Executor
{
	private final static Logger log = Logger.getLogger(ThreadPoolManager.class);
	
	/**
	 * 在不发出警告的情况下，最大的运行时长（ms）
	 */
	public final static long MAXIMUM_RUNTIME_IN_MILLISEC_WITHOUT_WARNING = 10000;
	
	
	/**
	 * 最大延迟时间
	 */
	private final static long MAX_DELAY = TimeUnit.NANOSECONDS.toMillis(Long.MAX_VALUE - System.nanoTime()) / 2;
	
	/**
	 * 定时的线程池
	 */
	private final ScheduledThreadPoolExecutor scheduledPool;
	
	/**
	 * 普通的线程池
	 */
	private final ScheduledThreadPoolExecutor generalPool;
	
	/**
	 * 长时间线程池
	 */
	private final ScheduledThreadPoolExecutor longTimePool;
	
	/**
	 * 即时的线程池
	 */
	private final ThreadPoolExecutor instantPool;
	
	
	
	private static final class SingletonHolder
	{
		private static final ThreadPoolManager	INSTANCE	= new ThreadPoolManager();
	}

	public static ThreadPoolManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}

	private ThreadPoolManager()
	{
		ThreadPoolConfig.load();
		/*算出线程池初始化条数*/
		final int instantPoolSize = Math.max(1, ThreadPoolConfig.THREAD_POOL_SIZE / 3);
		
		//final int actionNumber = Math.max(1, ThreadConfig.THREAD_POOL_SIZE / 8);
		
		generalPool = new ScheduledThreadPoolExecutor(instantPoolSize, new PriorityThreadFactory("generalPool", Thread.NORM_PRIORITY));
		generalPool.setRejectedExecutionHandler(new CommonsExecutionHandler());
		//generalPool.prestartAllCoreThreads();
		
		scheduledPool = new ScheduledThreadPoolExecutor(instantPoolSize, new PriorityThreadFactory("scheduledPool", Thread.NORM_PRIORITY));
		scheduledPool.setRejectedExecutionHandler(new CommonsExecutionHandler());
		//scheduledPool.prestartAllCoreThreads();
		
		longTimePool = new ScheduledThreadPoolExecutor(instantPoolSize, new PriorityThreadFactory("longTimePool", Thread.NORM_PRIORITY));
		longTimePool.setRejectedExecutionHandler(new CommonsExecutionHandler());
		//actionPool.prestartAllCoreThreads();
		
		
		instantPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new PriorityThreadFactory("instantPool", Thread.NORM_PRIORITY));
		instantPool.setRejectedExecutionHandler(new CommonsExecutionHandler());
		//instantPool.prestartAllCoreThreads();
		
		
		scheduleAtFixedRate(new Runnable()
		{
			@Override
			public void run()
			{
				purge();
			}
		}, 120000, 120000);
	}
	
	/**
	 * 尝试从工作队列移除所有已取消的 Future 任务。
	 * @create 
	 */
	public void purge()
	{
		generalPool.purge();
		scheduledPool.purge();
		instantPool.purge();
		longTimePool.purge();
	}
	
	/**
	 * 验证时间是否正确
	 * @param delay			时间
	 * @return				返回验证后的时间
	 * @create 
	 */
	private final long validate(long delay)
	{
		return Math.max(0, Math.min(MAX_DELAY, delay));
	}
	
	
	/**
	 * 创建并执行在给定延迟后启用的一次性操作。
	 * @param r					任务
	 * @param delay				延迟的时间
	 * @return					结果
	 * @create 
	 */
	public final ScheduledFuture<?> scheduleGeneral(Runnable r, long delay)
	{
		validate(delay);
		
		return generalPool.schedule(r, delay, TimeUnit.MILLISECONDS);
	}
	
	
	
	/**
	 *  创建并执行一个在给定初始延迟后首次启用的定期操作，后续操作具有给定的周期；
	 *  也就是将在 initialDelay 后开始执行，然后在 initialDelay+period 后执行，
	 *  接着在 initialDelay + 2 * period 后执行，依此类推。
	 * @param r						执行的任务
	 * @param delay					首次执行的延迟时间
	 * @param period				连续执行之间的周期
	 * @return						任务结果
	 * @create 
	 */
	public final ScheduledFuture<?> scheduleAtFixedRateGeneral(Runnable r, long delay, long period)
	{
		delay = validate(delay);
		period = validate(period);

		return generalPool.scheduleAtFixedRate(r, delay, period, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 创建并执行一个在给定初始延迟后首次启用的定期操作，
	 * 随后，在每一次执行终止和下一次执行开始之间都存在给定的延迟。
	 * @param command				需要执行的任务
	 * @param initialDelay			首次执行的延迟时间
	 * @param delay					一次执行终止和下一次执行开始之间的延迟
	 * @return						任务结果
	 * @create 
	 */
	public final ScheduledFuture<?> scheduleWithFixedDelayGeneral(Runnable command, long initialDelay, long delay)
	{
		initialDelay = validate(initialDelay);
		delay = validate(delay);
		return generalPool.scheduleWithFixedDelay(command, initialDelay, delay, TimeUnit.MILLISECONDS);
	}
	
	
	/**
	 * 创建并执行在给定延迟后启用的一次性操作。
	 * @param r					任务
	 * @param delay				延迟的时间
	 * @return					结果
	 * @create 
	 */
	public final ScheduledFuture<?> scheduleAction(Runnable r, long delay)
	{
		validate(delay);
		return longTimePool.schedule(r, delay, TimeUnit.MILLISECONDS);
	}
	
	
	
	/**
	 *  创建并执行一个在给定初始延迟后首次启用的定期操作，后续操作具有给定的周期；
	 *  也就是将在 initialDelay 后开始执行，然后在 initialDelay+period 后执行，
	 *  接着在 initialDelay + 2 * period 后执行，依此类推。
	 * @param r						执行的任务
	 * @param delay					首次执行的延迟时间
	 * @param period				连续执行之间的周期
	 * @return						任务结果
	 * @create 
	 */
	public final ScheduledFuture<?> scheduleAtFixedRateAction(Runnable r, long delay, long period)
	{
		delay = validate(delay);
		period = validate(period);

		return longTimePool.scheduleAtFixedRate(r, delay, period, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 创建并执行一个在给定初始延迟后首次启用的定期操作，
	 * 随后，在每一次执行终止和下一次执行开始之间都存在给定的延迟。
	 * @param command				需要执行的任务
	 * @param initialDelay			首次执行的延迟时间
	 * @param delay					一次执行终止和下一次执行开始之间的延迟
	 * @return						任务结果
	 * @create 
	 */
	public final ScheduledFuture<?> scheduleWithFixedDelayAction(Runnable command, long initialDelay, long delay)
	{
		initialDelay = validate(initialDelay);
		delay = validate(delay);
		return longTimePool.scheduleWithFixedDelay(command, initialDelay, delay, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 创建并执行在给定延迟后启用的一次性操作。
	 * @param r					任务
	 * @param delay				延迟的时间
	 * @return					结果
	 * @create 
	 */
	public final ScheduledFuture<?> schedule(Runnable r, long delay)
	{
		validate(delay);
		
		return scheduledPool.schedule(r, delay, TimeUnit.MILLISECONDS);
	}
	
	
	
	/**
	 *  创建并执行一个在给定初始延迟后首次启用的定期操作，后续操作具有给定的周期；
	 *  也就是将在 initialDelay 后开始执行，然后在 initialDelay+period 后执行，
	 *  接着在 initialDelay + 2 * period 后执行，依此类推。
	 * @param r						执行的任务
	 * @param delay					首次执行的延迟时间
	 * @param period				连续执行之间的周期
	 * @return						任务结果
	 * @create 
	 */
	public final ScheduledFuture<?> scheduleAtFixedRate(Runnable r, long delay, long period)
	{
		delay = validate(delay);
		period = validate(period);

		return scheduledPool.scheduleAtFixedRate(r, delay, period, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 创建并执行一个在给定初始延迟后首次启用的定期操作，
	 * 随后，在每一次执行终止和下一次执行开始之间都存在给定的延迟。
	 * @param command				需要执行的任务
	 * @param initialDelay			首次执行的延迟时间
	 * @param delay					一次执行终止和下一次执行开始之间的延迟
	 * @return						任务结果
	 * @create 
	 */
	public final ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay)
	{
		initialDelay = validate(initialDelay);
		delay = validate(delay);
		return scheduledPool.scheduleWithFixedDelay(command, initialDelay, delay, TimeUnit.MILLISECONDS);
	}

	
	/**
	 * 立即执行短时间的任务
	 * @param r				需要执行任务
	 * @create 
	 */
	@Override
	public final void execute(Runnable r) 
	{
		instantPool.execute(r);
	}
	
	
	/**
	 *  提交一个短时间能执行完成的 Runnable 任务用于执行，并返回一个表示该任务的 Future。
	 * @param r					任务
	 * @return					任务执行的结果
	 * @create 
	 */
	public final Future<?> submit(Runnable r)
	{
		return instantPool.submit(r);
	}
	
	/**
	 * 立即执行短时间的任务
	 * @param r				需要执行任务
	 * @create 
	 */
	public final void executeAction(Runnable r)
	{
		longTimePool.execute(r);
	}
	
	
	/**
	 *  提交一个短时间能执行完成的 Runnable 任务用于执行，并返回一个表示该任务的 Future。
	 * @param r					任务
	 * @return					任务执行的结果
	 * @create 
	 */
	public final Future<?> submitAction(Runnable r)
	{
		return longTimePool.submit(r);
	}
	
	
	public final void executeScheduled(Runnable r)
	{
		scheduledPool.execute(r);
	}
	
	
	public final Future<?> submitScheduled(Runnable r)
	{
		return scheduledPool.submit(r);
	}

	
	
	public void shutdown()
	{
		final long begin = System.currentTimeMillis();

		log.info("ThreadPoolManager: Shutting down.");
		log.info("\t... executing " + getTaskCount(scheduledPool) + " scheduled tasks.");
		log.info("\t... executing " + getTaskCount(instantPool) + " instant tasks.");

		scheduledPool.shutdown();
		instantPool.shutdown();

		boolean success = false;
		try
		{
			success |= awaitTermination(5000);

			scheduledPool.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
			scheduledPool.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);

			success |= awaitTermination(10000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		log.info("\t... success: " + success + " in " + (System.currentTimeMillis() - begin) + " msec.");
		log.info("\t... " + getTaskCount(scheduledPool) + " scheduled tasks left.");
		log.info("\t... " + getTaskCount(instantPool) + " instant tasks left.");
	}
	
	private int getTaskCount(ThreadPoolExecutor tp)
	{
		return tp.getQueue().size() + tp.getActiveCount();
	}

	public List<String> getStats()
	{
		List<String> list = new ArrayList<String>();

		list.add("");
		list.add("Scheduled pool:");
		list.add("=================================================");
		list.add("返回主动执行任务的近似线程数: ...... " + scheduledPool.getActiveCount());
		list.add("返回核心线程数: ..... " + scheduledPool.getCorePoolSize());
		list.add("返回池中的当前线程数: ......... " + scheduledPool.getPoolSize());
		list.add("返回曾经同时位于池中的最大线程数: .. " + scheduledPool.getLargestPoolSize());
		list.add("返回允许的最大线程数: .. " + scheduledPool.getMaximumPoolSize());
		list.add("返回已完成执行的近似任务总数: " + scheduledPool.getCompletedTaskCount());
		list.add("返回此执行程序使用的任务队列: .. " + scheduledPool.getQueue().size());
		list.add("返回曾计划执行的近似任务总数: ........ " + scheduledPool.getTaskCount());
		list.add("");
		list.add("Instant pool:");
		list.add("=================================================");
		list.add("返回主动执行任务的近似线程数: ...... " + instantPool.getActiveCount());
		list.add("返回核心线程数: ..... " + instantPool.getCorePoolSize());
		list.add("返回池中的当前线程数: ......... " + instantPool.getPoolSize());
		list.add("返回曾经同时位于池中的最大线程数: .. " + instantPool.getLargestPoolSize());
		list.add("返回允许的最大线程数: .. " + instantPool.getMaximumPoolSize());
		list.add("返回已完成执行的近似任务总数: " + instantPool.getCompletedTaskCount());
		list.add("返回此执行程序使用的任务队列: .. " + instantPool.getQueue().size());
		list.add("返回曾计划执行的近似任务总数: ........ " + instantPool.getTaskCount());
		list.add("");
		list.add("General pool:");
		list.add("=================================================");
		list.add("返回主动执行任务的近似线程数: ...... " + generalPool.getActiveCount());
		list.add("返回核心线程数: ..... " + generalPool.getCorePoolSize());
		list.add("返回池中的当前线程数: ......... " + generalPool.getPoolSize());
		list.add("返回曾经同时位于池中的最大线程数: .. " + generalPool.getLargestPoolSize());
		list.add("返回允许的最大线程数: .. " + generalPool.getMaximumPoolSize());
		list.add("返回已完成执行的近似任务总数: " + generalPool.getCompletedTaskCount());
		list.add("返回此执行程序使用的任务队列: .. " + generalPool.getQueue().size());
		list.add("返回曾计划执行的近似任务总数: ........ " + generalPool.getTaskCount());
		list.add("");
		list.add("Action pool:");
		list.add("=================================================");
		list.add("返回主动执行任务的近似线程数: ...... " + longTimePool.getActiveCount());
		list.add("返回核心线程数: ..... " + longTimePool.getCorePoolSize());
		list.add("返回池中的当前线程数: ......... " + longTimePool.getPoolSize());
		list.add("返回曾经同时位于池中的最大线程数: .. " + longTimePool.getLargestPoolSize());
		list.add("返回允许的最大线程数: .. " + longTimePool.getMaximumPoolSize());
		list.add("返回已完成执行的近似任务总数: " + longTimePool.getCompletedTaskCount());
		list.add("返回此执行程序使用的任务队列: .. " + longTimePool.getQueue().size());
		list.add("返回曾计划执行的近似任务总数: ........ " + longTimePool.getTaskCount());
		list.add("");
		return list;
	}

	
	/**
	 *  请求关闭、发生超时或者当前线程中断，无论哪一个首先发生之后，都将导致阻塞，直到所有任务完成执行。
	 * @param timeoutInMillisec					阻塞的时长
	 * @return									如果此执行程序终止，则返回 true；如果终止前超时期满，则返回 false 
	 * @throws InterruptedException				当线程在活动之前或活动期间处于正在等待、休眠或占用状态且该线程被中断时，抛出该异常。
	 */
	private boolean awaitTermination(long timeoutInMillisec) throws InterruptedException
	{
		final long begin = System.currentTimeMillis();

		while (System.currentTimeMillis() - begin < timeoutInMillisec)
		{
			if (!scheduledPool.awaitTermination(10, TimeUnit.MILLISECONDS) && scheduledPool.getActiveCount() > 0)
				continue;

			if (!instantPool.awaitTermination(10, TimeUnit.MILLISECONDS) && instantPool.getActiveCount() > 0)
				continue;
			
			if (!generalPool.awaitTermination(10, TimeUnit.MILLISECONDS) && generalPool.getActiveCount() > 0)
				continue;
			
			if (!longTimePool.awaitTermination(10, TimeUnit.MILLISECONDS) && longTimePool.getActiveCount() > 0)
				continue;

			return true;
		}

		return false;
	}

}
