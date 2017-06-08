package com.mnt.gui.fx.thread;

/**
 * 
 * 
 * <p>
 * 线程池配置
 * </p>
 * @author    mnt.cico
 * @version  2014年12月15日 上午7:22:29 mnt.cico .
 * @since   FX8.0
 */
public class ThreadPoolConfig {
	/**
	 * 线程池每个核对应的初始大小
	 */
	public static int BASE_THREAD_POOL_SIZE = 2;

	/**
	 * 线程池每个核对应的额外线程大小
	 */
	public static int EXTRA_THREAD_PER_CORE = 4;

	/**
	 * 线程池最小连接数
	 */
	public static int THREAD_POOL_SIZE;

	public static void load() {
		final int baseThreadPoolSize = BASE_THREAD_POOL_SIZE;
		final int extraThreadPerCore = EXTRA_THREAD_PER_CORE;

		THREAD_POOL_SIZE = baseThreadPoolSize
				+ Runtime.getRuntime().availableProcessors()
				* extraThreadPerCore;
	}
}
