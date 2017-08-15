package com.mnt.gui.fx.launcher;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.mnt.gui.fx.base.Base3DController;
import com.mnt.gui.fx.base.Base3DLauncher;
import com.mnt.gui.fx.init.InitContext;
import com.mnt.gui.fx.loader.FXMLLoaderUtil;
import com.mnt.gui.fx.loader.classload.ClassLoadUtil;
import com.mnt.gui.fx.table.TabelCellFactory;
import com.mnt.gui.fx.thread.ThreadPoolManager;
import com.mnt.gui.fx.util.DataUtil;
import com.mnt.gui.fx.view.anno.MainView;

/**
 * 
 * <p>
 * mnt fx 初始启动器
 * </p>
 * 
 * @author mnt.cico
 * @version 2016年5月15日 上午12:27:09 mnt.cico .
 * @since FX8.0
 */
public class MNTFX3DLauncher extends Base3DLauncher {
	private static final Logger log = Logger.getLogger(MNTFX3DLauncher.class);
	//初始化容器
	protected InitContext initContext;
	//主界面
	protected Base3DController mainView;
	//主界面注解
	protected MainView mainViewAnno;
	//类加载器
	public URLClassLoader classLoad;

	public void loadSource() {
		PropertyConfigurator.configure(System.getProperty("user.dir") + "/conf/log4j.properties");
		
		ThreadPoolManager.getInstance();
		log.info("start load class or jar");
		URL urlBin = null;
		URL urlApp = null;
		try {
			urlBin = new URL(DataUtil.BIN_PATH);
			urlApp = new URL(DataUtil.APP_PATH);
		} catch (MalformedURLException e) {
			log.error("url path is error [" + DataUtil.BIN_PATH + "] ["+ DataUtil.APP_PATH + "]", e);
		}
		final List<Class<?>> scanClass = new ArrayList<>();

		this.classLoad = ClassLoadUtil.loadJarOrClass(new Consumer<Class<?>>() {
			public void accept(Class<?> t) {
				
				if(t.getName().contains("$"))
				{
					return;
				}
				scanClass.add(t);
				if (t.isAnnotationPresent(MainView.class)) {
					mainViewAnno = ((MainView) t.getAnnotation(MainView.class));
					if (Base3DController.class.isAssignableFrom(t)) {
						try {
							mainView = ((Base3DController) t.newInstance());
						} catch (Exception e) {
							log.error("load mainView error" + t.getName() , e);
						}
					}
				} else if (InitContext.class.isAssignableFrom(t)) {
					try {
						initContext = ((InitContext) t.newInstance());
					} catch (Exception e) {
						log.error("load initContext Error " + t.getName(), e);
					}
				}
			}
		}, urlBin, urlApp);
		if (this.initContext != null) {
			this.initContext.init(scanClass, classLoad);
		}
	}

	protected void loadEnd() 
	{
		log.info("view load succcess");
		if (this.initContext != null) {
			this.initContext.afterInitView(classLoad);
		}
	}

	public boolean shutdown() {
		
		ClassLoadUtil.unload(this.classLoad);
		if (this.initContext != null) {
			this.initContext.shutdown();
		}
		TabelCellFactory.shutdown();
		ThreadPoolManager.getInstance().shutdown();
		return true;
	}

	public String getTitle() {
		if (this.mainViewAnno != null) {
			return this.mainViewAnno.appName();
		}
		return super.getTitle();
	}

	public Base3DController getRoot() {
		if (this.mainView == null) {
			log.error("not annotation MainView");
			System.exit(1);
		}
		return FXMLLoaderUtil.load(this.mainView);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
