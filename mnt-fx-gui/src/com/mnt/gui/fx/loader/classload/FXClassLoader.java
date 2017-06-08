package com.mnt.gui.fx.loader.classload;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * 
 * <p>
 * fx类加载器
 * </p>
 * 
 * @author mnt.cico
 * @version 2015年5月24日 下午7:00:13 mnt.cico .
 * @since FX8.0
 */
public class FXClassLoader {

	/**
	 * 编译器
	 */
	private static JavaCompiler javac = ToolProvider.getSystemJavaCompiler();

	/**
	 * 
	 * <p>
	 * 编译指定文件夹下面的java文件
	 * </p>
	 * @create mnt.cico
	 * @param fileDirector
	 * @return
	 */
	private final static List<String> compilerJavaFile(String... fileDirector)
	{
		final List<String> result = new ArrayList<>();
		StandardJavaFileManager manager = javac.getStandardFileManager(null, null, null);  
		
		 Iterable<? extends JavaFileObject> it = manager.getJavaFileObjects(fileDirector);
		 it.forEach(javaFileObject -> {
			 try {
				 result.add(parsePackageClassName(javaFileObject.getName(), javaFileObject.getCharContent(true).toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		 });
		 StringWriter sw = new StringWriter(); 
		 CompilationTask compilationTask = javac.getTask(sw, manager, null, null, null, it);  
		 
		 compilationTask.call();
		 try {
			manager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		return result;
	}
	
	/**
	 * 
	 * <p>
	 * 获取类加载器
	 * </p>
	 * @create mnt.cico
	 * @param classPath
	 * @return
	 */
	private static URLClassLoader getClassLoader(String classPath)
	{
		URL[] urls = null;
		try {
			urls = new URL[] {new URL("file:/"+ classPath)};
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return new URLClassLoader(urls);
	}
	
	/**
	 * <p>
	 * 加载指定src路径下的class文件
	 * </p>
	 * 
	 * @create mnt.cico
	 * @param srcPath
	 */
	public final static Pair<URLClassLoader, List<String>> loadClasses(String srcPath) {
		File [] javaFiles = new File(srcPath).listFiles((name)->{return name.getName().endsWith(".java");});
		String [] filePaths = new String[javaFiles.length];
		for (int i = 0; i < javaFiles.length; i++) {
			filePaths[i] = javaFiles[i].getAbsolutePath();
		}
		final List<String> classNames = compilerJavaFile(filePaths);
		final URLClassLoader classLoad = getClassLoader(srcPath);
		return new Pair<URLClassLoader, List<String>>(classLoad, classNames);
	}
	
	/**
	 * 分隔符
	 */
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	/**
	 * 
	 * <p>
	 * 解析java包名
	 * </p>
	 * @create mnt.cico
	 * @param filePath
	 * @param javaContent
	 * @return
	 */
	private static String parsePackageClassName(String filePath, String javaContent)
	{
		String javaName = filePath.substring(filePath.lastIndexOf(FILE_SEPARATOR) + 1);
		
		return javaContent.substring(0, javaContent.indexOf(";")).trim().substring(8) + "." + javaName.substring(0, javaName.length() - 5);
	}


}
