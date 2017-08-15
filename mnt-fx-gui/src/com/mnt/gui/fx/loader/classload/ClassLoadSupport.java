package com.mnt.gui.fx.loader.classload;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.util.Pair;

/**
 * 
 * <p>
 * 类加载载体
 * </p>
 * @author    mnt.cico
 * @version  2015年5月24日 下午11:28:59 mnt.cico .
 * @since   FX8.0
 * @param <T>
 */
public class ClassLoadSupport<T> {
	
	//当前加载类的文件夹
	private String classDirector;
	
	private Map<String, Class<T>> classesMap; 
	
	/**
	 * 
	 * <p>
	 * 获取一个新实例
	 * </p>
	 * @create mnt.cico
	 * @param name
	 * @return
	 */
	public T newInstance(String name) {
		if(classesMap.containsKey(name))
		{
			try {
				return classesMap.get(name).newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				
			}
		} 
		throw new NullPointerException("未加载当前索引的类 : " + name);
		
	}
	
	/**
	 * 获取所有的脚本
	 * @return
	 */
	public List<T> getScripts() {
		List<T> result = new ArrayList<>(classesMap.size());
		try {
			for (Class<T> t : classesMap.values()) {
				result.add(t.newInstance());
			}
		} catch (InstantiationException | IllegalAccessException e) {
			
		}
		return result;
	}
	
	/**
	 * <p>
	 * 重新加载指定的类
	 * </p>
	 * @create mnt.cico
	 * @param className
	 */
	public void ReloadClass(String className, URLClassLoader appClassLoad)
	{
		classesMap.clear();
		loadClass(classDirector, appClassLoad);
	}
	
	public void unloadClass()
	{
		classesMap.clear();
		classesMap = null;
	}
	
	/**
	 * 
	 * <p>
	 * 价值指定文件夹下面的class文件
	 * </p>
	 * @create mnt.cico
	 * @param classDirector
	 */
	@SuppressWarnings("unchecked")
	public void loadClass(String classDirector, URLClassLoader appClassLoad)
	{
		this.classDirector = classDirector;
		Pair<ClassLoader, List<String>> classLoader = FXClassLoader.loadClasses(classDirector, appClassLoad);
		List<String> classNames = classLoader.getValue();
		classesMap = new HashMap<String, Class<T>>(classNames.size());
		for (String className : classNames) {
			Class<T> loadClass;
			try {
				loadClass = (Class<T>)classLoader.getKey().loadClass(className);
				String name = className;
				for (Class<?> interfaces : loadClass.getInterfaces()) {
					if(interfaces == ClassKey.class)
					{
						name = ((ClassKey)loadClass.newInstance()).getKey();
					}
				}
				classesMap.put(name, loadClass);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	
}
