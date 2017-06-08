package com.mnt.gui.fx.base;

import java.net.URL;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * javafx 基础界面
 * 对应的fxml文件root需要为VBox
 * @author mnt.cico
 *
 */
public abstract class BaseController extends VBox {

	//启动时的stage
	protected static Stage stage; 
	
	//fxml加载地址
	private URL locationUrl;
	
	protected BaseController() {
		
	}
	
	/**
	 * fxml文件加载完毕后执行 加载只会执行一次
	 */
	public void init() {
		
	};
	
	/**
	 * set stage (stage from BaseLauncher)
	 * @param stage
	 */
	static void setStage(Stage stage) {
		BaseController.stage = stage;
	}
	
	/**
	 * 
	 * @return main view stage
	 */
	public static Stage getMainStage() {
		return stage;
	}
	
	/**
	 * 
	 * <p>
	 * 接受其他界面发送的数据
	 * </p>
	 * @create mnt.cico
	 * @param obj
	 */
	public void reciveData(String from, Object... obj) {
		
	}

	
	/**
	 * 其他地方调用执行这个方法
	 * 2014-6-30 mnt.cico
	 * @param object
	 */
	public void outExecute(Object object) {
		
	}
	
	/**
	 * 
	 * <p>
	 * 每次显示的时候都会执行 init只会初始化的时候执行一次
	 * </p>
	 * @create mnt.cico
	 */
	public void showAndExecute() {
		
	}
	
	/**
	 * <p>
	 * 每次隐藏时候都会执行
	 * </p>
	 * @create mnt.cico
	 */
	public void hideAndExecute()
	{
		
	}
	
	/**
	 * <p>
	 * 卸载时执行
	 * </p>
	 * @create mnt.cico
	 */
	public void shutdown()
	{
		
	}
	
	/**
	 * 初始化之后执行
	 */
	public void afterInit()
	{
		
	}
	

	public URL getLocationUrl() {
		return locationUrl;
	}

	public void setLocationUrl(URL locationUrl) {
		this.locationUrl = locationUrl;
	}
	
}
