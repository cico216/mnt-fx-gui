package com.mnt.gui.fx.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * 1.auto scan all properties with "conf" file 
 * 2.parse properties file use getInstance()
 * @author 2014-6-12 mnt.cico
 *
 */
public class PropertiesUtil
{
	private final Properties properties = new Properties();
	private final File file;
	private static final Map<String, String> allPropertiesMap = new HashMap<String, String>();
	private static final Map<String, Properties> allPropertiesKeyAndFileMap = new HashMap<String, Properties>();
	private static final Map<Properties, File> propertiesFileMap = new HashMap<Properties, File>();
	
	static {
		loadAllPropertiesFile();
	}
	
	private PropertiesUtil(File file) {
		this.file = file;
		if(!file.exists()) {
			try
			{
				file.createNewFile();
			} catch (IOException e)
			{
//				MestarLogger.error(e);
			}
		} 
		try
		{
			properties.load(new FileInputStream(file));
		} catch (IOException e)
		{
//			MestarLogger.error(e);
		} 
	}
	
	/**
	 * 
	 * <p>
	 * auto scan all properties with 'conf' file 
	 * </p>
	 * @since 2014年5月29日 下午4:48:15 Cico.Jiang
	 */
	private static void loadAllPropertiesFile() {
		File file = new File(getCurrConfPath());
		parserConfFile(file);
	}
	
	public static final String getValueByKey(String key) {
		return getValueByKey(key, null);
	}
	
	public static final String getValueByKey(String key, String defaultValue) {
		if(allPropertiesMap.containsKey(key)) {
			return allPropertiesMap.get(key);
		} else {
			return defaultValue;
		}
	}
	
	private static void parserConfFile(File propertiesFile) {
		if(propertiesFile.isFile() && propertiesFile.getName().endsWith(".properties")) {
			Properties properties = new Properties();
			FileInputStream fis = null;
			try
			{
				fis = new FileInputStream(propertiesFile);
				properties.load(fis);
				propertiesFileMap.put(properties, propertiesFile);
				Enumeration<?> enu = properties.propertyNames();
				while(enu.hasMoreElements()){
				    String key = (String)enu.nextElement();
				    if(allPropertiesMap.containsKey(key)) {
//				    	MestarLogger.error("发现重复配置文件key : key = " + key +", file = " + propertiesFile.getName());
				    } else {
				    	 allPropertiesMap.put(key, properties.getProperty(key));
						 allPropertiesKeyAndFileMap.put(key, properties);
				    }
				} 
			} catch (IOException e)
			{
//				MestarLogger.error(e);
			} finally {
				if(null != fis) {
					try
					{
						fis.close();
					} catch (IOException e)
					{
//						MestarLogger.error(e);
					}
				}
			}
			
		} else if(propertiesFile.isDirectory()) {
			File[] listFile = propertiesFile.listFiles();
			for (File file : listFile)
			{
				parserConfFile(file);
			}
		}
	}
	
	/**
	 * 在conf路径下面创建Properties文件
	 * <p>
	 * <pre>
	 * createPropertiesFile("biz/PageConfiguration.properties") 
	 * 在conf/biz路径下创建PageConfiguration.properties文件
	 * </pre>
	 * 
	 * </p>
	 * @since 2014年5月28日 下午4:50:03 Cico.Jiang
	 * @param fileName
	 */
	public static final void createPropertiesFile(String fileName) {
		String filePath = getCurrConfPath() + fileName;
		File file = new File(filePath);
		if(!file.exists()) {
			try
			{
				file.createNewFile();
			} catch (IOException e)
			{
//				MestarLogger.error(filePath + "文件创建失败！");
			}
		}
	}
	
	/**
	 * get properties context with properties file 
	 * 
	 * <p>
	 * <pre>
	 * getInstance("biz/PageConfiguration.properties") 
	 * get "conf/biz" path "PageConfiguration.properties" file properties value
	 * </pre>
	 * </p>
	 * @since 2014年5月30日 上午10:51:38 Cico.Jiang
	 * @param path
	 * @return
	 */
	public static final PropertiesUtil getInstance(String fileName) {
		return new PropertiesUtil(new File(getCurrConfPath() + fileName));
	}
	
	/**
	 * 获取配置文件夹的路径
	 * <p>
	 * 重写后获取指定的文件夹路径
	 * </p>
	 * @since 2014年5月30日 上午10:54:30 Cico.Jiang
	 * @return
	 */
	protected static String getCurrConfPath() {
		return System.getProperty("user.dir") + "/conf/";
	}
	
	private synchronized void save() {
		FileOutputStream outputStream = null;
		try
		{
			outputStream = new FileOutputStream(file);
			properties.store(outputStream, null);
		} catch (IOException e)
		{
//			MestarLogger.error(e);
		} finally {
			if(null != outputStream) {
				try
				{
					outputStream.close();
				} catch (IOException e)
				{
//					MestarLogger.error(e);
				}
			}
		}
		
	}
	
	
	public final String getByKey(String key, String defaultValue) {
		if(properties.containsKey(key)) {
			return properties.getProperty(key);
		} else {
			return defaultValue;
		}
	}
	
	public final void setValue(String key, String value) {
		properties.put(key, value);
		save();
	}
	
}
