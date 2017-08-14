package com.mnt.gui.fx.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Consumer;

import com.mnt.gui.fx.loader.classload.ClassLoadUtil;

public class TestLoad {

	
	public static void main(String[] args) {
		
		ClassLoadUtil.loadClass(TestClassLoad.class);
		System.out.println(TestClassLoad.TEST_CLASS_LOADER.newInstance("key2").getValue());
//		URL url = null;
//		try {
//			url = new URL("file://" + System.getProperty("user.dir") + "/lib/mnt-gui.jar");
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
//		ClassLoadUtil.loadJarOrClass(new Consumer<Class<?>>() {
//			@Override
//			public void accept(Class<?> t) {
//				
//			}
//		}, url);
		
	}
}
