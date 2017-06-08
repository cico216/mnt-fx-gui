package com.mnt.gui.fx.loader;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;

import com.mnt.gui.fx.base.BaseController;

/**
 * FXML loader tool<br/>
 * controller load<br/>
 * fxml file load<br/>
 * i18n file load<br/>
 * 
 * @author mnt.cico
 *
 */
public class FXMLLoaderUtil<C extends BaseController> {

	private FXMLLoaderUtil(){
		//empty
	}
	
	private static final String RES_DEFALUR_PATH = "res/";
	
	/**
	 * <p>
	 * 获取fxml文件路径
	 * </p>
	 * @create mnt.cico
	 * @param path
	 * @param name
	 * @return
	 */
	public static final String getFxmlUrl(String path, String name) {
		
		return path + name + ".fxml";
	}
	
	/**
	 * 界面缓存map
	 */
	static final Map<String, BaseController> controllerCacheMap = new HashMap<>();
	
	/**
	 * init the class conver to BaseController object
	 * @author mnt.cico
	 * @param c
	 * @return
	 */
	public static final <C extends BaseController> C load(C c, URL fxmlFileUrl,  ResourceBundle i18nBUNDLE, boolean isCache){
		C controller = c;
		FXMLLoader loader = new FXMLLoader(fxmlFileUrl);
		controller.setLocationUrl(fxmlFileUrl);
		loader.setController(controller);
		loader.setRoot(controller);
		loader.setResources(i18nBUNDLE);
		try {
			loader.load();
			c.init();
			c.showAndExecute();
			//如果需要添加到缓存 则添加到缓存
			if(isCache) {
				controllerCacheMap.put(c.getClass().getName(), c);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return controller;
	}
	
	/**
	 * builder class to controller
	 * isCache 是否缓存 下次加载的时候直接拿内存
	 * 2014-3-29 mnt.cico
	 */
	public static final <C extends BaseController>C load(Class<C> c, boolean isCache){
		//判断缓存中是否存在
		final C cTemp = loadCache(c.getName());
		if(null != cTemp) {
			cTemp.showAndExecute();
			return cTemp;
		}
		
		C controller = null;
		try {
			controller = c.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return load(controller, controller.getClass().getResource(RES_DEFALUR_PATH + controller.getClass().getSimpleName() + ".fxml"), null, isCache);
	}
	
	/**
	 * load fxml file to object 
	 * 2014-6-30 mnt.cico
	 * @param baseController
	 * @return
	 */
	public static final <C extends BaseController>C load(C baseController, boolean isCache) {
		//判断缓存中是否存在
		final C cTemp = loadCache(baseController.getClass().getName());
		if(null != cTemp) {
			cTemp.showAndExecute();
			return cTemp;
		}
		
		return load(baseController, baseController.getClass().getResource(RES_DEFALUR_PATH + baseController.getClass().getSimpleName() + ".fxml"), null, isCache);
	}
	
	/**
	 * 
	 * 
	 * <p>
	 * 加载baseController对应的fxml文件  默认不添加到缓存
	 * </p>
	 * @create mnt.cico
	 * @param baseController
	 * @return
	 */
	public static final <C extends BaseController>C load(C baseController) {
		return load(baseController, false);
	}
	
	/**
	 * <p>
	 * 直接将class 加载对应的fxml 默认不添加的缓冲
	 * </p>
	 * @create mnt.cico
	 * @param c
	 * @return
	 */
	public static final <C extends BaseController>C load(Class<C> c){
		return load(c, false);
	}
	
	/**
	 * 
	 * <p>
	 * 从缓存中加载controller
	 * </p>
	 * @create mnt.cico
	 * @param className
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <C extends BaseController>C loadCache(String className) {
		return (C) controllerCacheMap.get(className);
	}
	
}
