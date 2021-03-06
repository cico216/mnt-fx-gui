package com.mnt.gui.fx.launcher;

import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.mnt.gui.fx.base.BaseController;
import com.mnt.gui.fx.base.BaseLauncher;
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
public class MNTFXLauncher extends BaseLauncher {
	private static final Logger log = Logger.getLogger(MNTFXLauncher.class);
	//初始化容器
	protected InitContext initContext;
	//主界面
	protected BaseController mainView;
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
					if (BaseController.class.isAssignableFrom(t) && !Modifier.isAbstract(t.getModifiers())) {
						try {
							mainView = ((BaseController) t.newInstance());
						} catch (Exception e) {
							log.error("load mainView error" + t.getName() , e);
						}
					}
				} else if (InitContext.class.isAssignableFrom(t) && !Modifier.isAbstract(t.getModifiers())) {
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

	public BaseController getRoot() {
		if (this.mainView == null) {
			log.error("not annotation MainView");
			System.exit(1);
		}
		return FXMLLoaderUtil.load(this.mainView);
	}

	public static void main(String[] args) {
//		System.setProperty("java.awt.headless", "false");
//		System.getProperties().put("file.encoding", "UTF-8");
		launch(args);
	}
}
