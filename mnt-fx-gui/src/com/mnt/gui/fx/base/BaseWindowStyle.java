package com.mnt.gui.fx.base;

import javafx.stage.Stage;

/**
 * 
 * <p>
 * 抽象类样式基类
 * </p>
 * @author    mnt.cico
 * @version  2014年12月16日 上午8:01:53 mnt.cico .
 * @since   FX8.0
 */
public abstract class BaseWindowStyle {

	
	protected Stage stage;
	protected BaseController root;
	
	protected BaseWindowStyle() {
		
	}
	/**
	 * set root node
	 * @param root
	 */
	public abstract void setRoot(BaseController root);
	
	public abstract void buildStageByStyle(Stage stage, BaseController root);
	
	/**
	 * set window title
	 * @param title
	 */
	public abstract void setTitle(String title);
	
	public abstract void clearStage();
	
	public abstract BaseController getRoot();
}
