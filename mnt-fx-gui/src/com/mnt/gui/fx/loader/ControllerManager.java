package com.mnt.gui.fx.loader;

import com.mnt.gui.fx.base.BaseController;

/**
 * <p>
 * Controller 之间的通讯工具
 *  1.加载的controller 需要添加到缓存才可以通讯 FXMLLoaderUtil.load(BaseContoller.class, true);
 * </p>
 * @author    mnt.cico
 * @version  2014年12月14日 下午11:00:39 mnt.cico .
 * @since   FX8.0
 */
public class ControllerManager {

	/**
	 * <p>
	 * 向指定的控件发送数据
	 * </p>
	 * @create mnt.cico
	 * @param c
	 * @param obj
	 */
	public static final <C extends BaseController> void sendMessage(BaseController fromController, Class<C> toController, Object... obj) {
		if(FXMLLoaderUtil.controllerCacheMap.containsKey(toController.getName())) {
			//向缓存中指定的控件发送数据
			FXMLLoaderUtil.controllerCacheMap.get(toController.getName()).reciveData(fromController.getClass().getName(), obj);
		} else {
			throw new NullPointerException(toController.getName() + "is not cache");
		}
	}
	
	
}
