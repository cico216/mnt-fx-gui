package com.mnt.gui.fx.loader.classload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import jdk.internal.org.objectweb.asm.ClassReader;
import sun.misc.ClassLoaderUtil;

public class ClassLoadUtil {
	
	private static final ReentrantLock lock = new ReentrantLock();

	@SuppressWarnings("rawtypes")
	public static final void loadClass(Class<?> supportClass, URLClassLoader appClassLoad) {
		Field[] declardFields = supportClass.getDeclaredFields();
		for (Field field : declardFields) {
			ClassLoad classLoad = (ClassLoad) field
					.getAnnotation(ClassLoad.class);
			if ((classLoad != null)
					&& (field.getType().equals(ClassLoadSupport.class))) {
				ClassLoadSupport<?> value = new ClassLoadSupport();
				try {
					field.set(field, value);
					value.loadClass(classLoad.srcPath(), appClassLoad);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static final void unloadClass(Object object) {
		
	}

	/**
	 * 
	 * <p>
	 * 加载jar文件或者class文件
	 * </p>
	 * @create mnt.cico
	 * @param consumer
	 * @param urls
	 * @return
	 */
	public static final URLClassLoader loadJarOrClass(Consumer<Class<?>> consumer, URL... urls) {
		URLClassLoader classLoad = null;
		List<String> classList = new ArrayList<>();
		lock.lock();
		try {
			classLoad = new URLClassLoader(parseUrls(urls));
			for (URL url : urls) {
				if (isClass(url)) {
					parseClassFileName(classList, url);
				} else if (isJar(url)) {
					scanJarClass(classList, url);
				} else if (isDir(url)) {
					loadJarOrClass(classLoad, classList, url);
				}
			}
		} finally {
			lock.unlock();
		}
		for (String className : classList) {
			try {
				Class<?> clazz = classLoad.loadClass(className);
				consumer.accept(clazz);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return classLoad;
	}

	/**
	 * 
	 * <p>
	 * 递归加载 jar 或 class
	 * </p>
	 * @create mnt.cico
	 * @param classLoad
	 * @param classList
	 * @param dirUrl
	 */
	private static final void loadJarOrClass(ClassLoader classLoad, List<String> classList, URL dirUrl) {
		File dir = new File(getFilePath(dirUrl));
		URL url = null;
		for (File file : dir.listFiles()) {
			try {
				url = file.toURI().toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			if (isClass(url)) {
				parseClassFileName(classList, url);
			} else if (isJar(url)) {
				scanJarClass(classList, url);
			} else if (isDir(url)) {
				loadJarOrClass(classLoad, classList, url);
			}
		}
	}

	/**
	 * 
	 * <p>
	 * 解析所有的url  jar class
	 * </p>
	 * @create mnt.cico
	 * @param urls
	 * @return
	 */
	private static URL[] parseUrls(URL... urls) {
		List<URL> result = new ArrayList<>();

		for (URL url : urls) {
			
			if (isClass(url)) 
			{
				result.add(url);
			} 
			else if (isJar(url)) 
			{
				result.add(url);
				
			} 
			else if (isDir(url)) 
			{
				parseUrls(result, url);
			}
		}
		return result.toArray(new URL[result.size()]);
	}

	/**
	 * <p>
	 * 递归解析url
	 * </p>
	 * @create mnt.cico
	 * @param result
	 * @param dirUrl
	 */
	private static void parseUrls(List<URL> result, URL dirUrl) {
		File dir = new File(getFilePath(dirUrl));
		URL url = null;
		for (File file : dir.listFiles()) {
			try {
				url = file.toURI().toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			if (isClass(url)) 
			{
				result.add(url);
			} 
			else if (isJar(url)) 
			{
				result.add(url);
			} 
			else if (isDir(url)) 
			{
				parseUrls(result, url);
			}
		}
	}

	/**
	 * 
	 * <p>
	 * 解析jar文件的class类名
	 * </p>
	 * @create mnt.cico
	 * @param classList
	 * @param url
	 */
	private static void scanJarClass(List<String> classList, URL url) {
		String CLASS = ".class";
		File jarFile = new File(getFilePath(url));
		JarFile jarFileTemp = null;
		try {
			jarFileTemp = new JarFile(jarFile);
			Enumeration<JarEntry> en = jarFileTemp.entries();
			while (en.hasMoreElements()) {
				JarEntry je = (JarEntry) en.nextElement();
				String name = je.getName();
				if (name.endsWith(CLASS)) {
					classList.add(classPathToPacckage(name));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * <p>
	 * 解析class文件
	 * </p>
	 * @create mnt.cico
	 * @param classList
	 * @param url
	 */
	private static void parseClassFileName(List<String> classList, URL url) {
		try {
			ClassReader reader = new ClassReader(new FileInputStream(
					getFilePath(url)));
			classList.add(pathToDot(reader.getClassName()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * <p>
	 * 解析路劲为类名
	 * </p>
	 * @create mnt.cico
	 * @param name
	 * @return
	 */
	private static String classPathToPacckage(String name) {
		return pathToDot(name.substring(0, name.length() - 6));
	}

	
	private static String pathToDot(String s) {
		return s.replace('/', '.').replace('\\', '.');
	}

	/**
	 * 
	 * <p>
	 * 卸载类
	 * </p>
	 * @create mnt.cico
	 * @param classLoad
	 */
	public static final void unload(URLClassLoader classLoad) {
		ClassLoaderUtil.releaseLoader(classLoad);
	}
	//是否为jar文件
	private static boolean isJar(URL url) {
		return url.getFile().endsWith(".jar");
	}

	//是否为class文件
	private static boolean isClass(URL url) {
		return url.getFile().endsWith(".class");
	}

	//是否为文件夹
	private static boolean isDir(URL url) {
		return new File(getFilePath(url)).isDirectory();
	}

	/**
	 * <p>
	 * 获取url文件路径
	 * </p>
	 * @create mnt.cico
	 * @param url
	 * @return
	 */
	private static String getFilePath(URL url) {
		if (url.getProtocol().equals("file")) 
		{
			String urlPath = url.toString();
			String result = urlPath.replace("file:///", "").replace("file://", "").replace("file:/", "").replace("file:", "");
			String osName = System.getProperty("os.name").toLowerCase();
			if(osName.contains("mac")) {
				result = "/" + result;
			}

			return result;
		}
		return url.toString();
	}
}
