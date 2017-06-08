package com.mnt.gui.fx.base;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.mnt.gui.fx.style.WindowDefStyle;


/**
 * 
 * <p>
 * 默认的fx application 启动器
 * 1.shutdown() 程序退出的时候会执行<br/>
 * 2.loadSource() 加载场景前执行<br/>
 * 3.loadEnd() 场景加载完毕后执行<br/>
 * </p>
 * @author    mnt.cico
 * @version  2014年12月14日 下午10:09:15 mnt.cico .
 * @since   FX8.0
 */
public abstract class BaseLauncher extends Application{
	//使用默认的窗口
	private static final SimpleObjectProperty<BaseWindowStyle> windowStyle = new SimpleObjectProperty<>(new WindowDefStyle());

	public void start(Stage stage) throws Exception {
		//设置默认的场景
		BaseController.setStage(stage);
		//加载资源
		loadSource();
		
		BaseController baseController = getRoot();
		
		//窗口风格 和style 风格
		windowStyle.getValue().buildStageByStyle(stage, baseController);
		//设置窗口名称
		windowStyle.getValue().setTitle(getTitle());
		//添加关闭请求 直接退出
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				if(shutdown())
				{
					Platform.exit();
					System.exit(1);
				}
				else
				{
					event.consume();
				}
				
			}
		});
		
		//添加监听器样式改变
		windowStyle.addListener(new ChangeListener<BaseWindowStyle>() {
			@Override
			public void changed(ObservableValue<? extends BaseWindowStyle> observable, BaseWindowStyle oldValue, BaseWindowStyle newValue) {
				if(null != oldValue) {
					//清除旧场景
					oldValue.clearStage();
				}
				if(null != newValue) {
					//窗口风格 和style 风格
					newValue.buildStageByStyle(stage, getRoot());
					//设置窗口名称
					newValue.setTitle(getTitle());
				}
			}
		});
		
		//显示
		stage.show();
		//居中
		stage.centerOnScreen();
		
		baseController.afterInit();
		
		//加载完毕之后
		loadEnd();
	}
	
	
	public String getTitle() {
		return "mnt-fx";
	}
	
	public abstract BaseController getRoot(); 
	
	/**
	 * <p>
	 * 实例化scene之前加载资源
	 * </p>
	 * @create mnt.cico
	 */
	public void loadSource() {
		
	}
	
	/**
	 * 
	 * <p>
	 * 实例化scene之后执行
	 * </p>
	 * @create mnt.cico
	 */
	protected void loadEnd() {
		
	}
	
	/**
	 * <p>
	 * 程序退出的时候会调用
	 * </p>
	 * @create mnt.cico
	 */
	public boolean shutdown() {
		return true;
	}
	
	/**
	 * 
	 * <p>
	 * 设置窗口风格
	 * </p>
	 * @create mnt.cico
	 * @param style
	 */
	public static final void setWindowStyle(BaseWindowStyle style) {
		windowStyle.set(style);
	}
	
	/**
	 * 
	 * <p>
	 * 获取第一次加载的主场景
	 * </p>
	 * @create mnt.cico
	 * @return
	 */
	public static BaseController getMainRoot() {
		return windowStyle.getValue().getRoot();
	}
	
	/**
	 * 
	 * <p>
	 * 跳转到指定场景
	 * </p>
	 * @create mnt.cico
	 * @param root
	 */
	public static void setRoot(BaseController root) {
		windowStyle.getValue().setRoot(root);
	}
}
