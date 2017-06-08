package com.mnt.gui.fx.init;

import java.util.List;

/**
 * <p>
 * 初始化内容
 * </p>
 * @author    mnt.cico
 * @version  2016年5月15日 上午12:25:00 mnt.cico .
 * @since   FX8.0
 */
public abstract class InitContext
{
	//初始化加载  扫描的class
	public abstract void init(List<Class<?>> paramList);
  
	//界面显示后执行
	public abstract void afterInitView();
  
	//关闭时执行
	public abstract void shutdown();
}

