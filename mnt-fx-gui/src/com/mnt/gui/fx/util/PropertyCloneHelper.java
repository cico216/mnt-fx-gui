package com.mnt.gui.fx.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * copy pojo to vo  or vo to pojo
 * @author 2014-7-14 mnt.cico
 *
 */
public class PropertyCloneHelper
{
	private PropertyCloneHelper()
	{
		// empty
	}

	/**
	 * 
	 * old object clone to new Object
	 * @author 2014-7-14 mnt.cico
	 */
	public static void cloneProperty(Object oldObj, Object newObj)
	{
		Method[] oldMethods = oldObj.getClass().getDeclaredMethods();
		Method[] newMethods = newObj.getClass().getDeclaredMethods();
		Map<String, Method> getMethodMap = parseAndGetGetterFunction(oldMethods);
		Map<String, Method> setMethodMap = parseAndGetSetterFunction(newMethods);
		if (getMethodMap.size() == setMethodMap.size())
		{
			Method getMethod = null;
			Method setMethod = null;
			for (String fieldName : getMethodMap.keySet())
			{
				getMethod = getMethodMap.get(fieldName);
				setMethod = setMethodMap.get(fieldName);
				try
				{
					setMethod.invoke(newObj, getMethod.invoke(oldObj));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
				{
					// TODO 
				}
			}
			getMethod = null;
			setMethod = null;

		} else
		{
			// TODO MestarLogger.error(" count is not equal");
		}
	}

	/**
	 * 
	 * parse field get function
	 * @author 2014-7-14 mnt.cico
	 */
	private static Map<String, Method> parseAndGetGetterFunction(Method[] methods)
	{
		Map<String, Method> result = new HashMap<String, Method>();
		String methodName = null;
		String get = "get";
		String is = "is";
		for (Method method : methods)
		{
			methodName = method.getName();
			if (methodName.startsWith(get))
			{
				result.put(lowerFieldName(methodName.substring(3)), method);
			} else if (methodName.startsWith(is))
			{
				result.put(lowerFieldName(methodName.substring(2)), method);
			}
		}
		methodName = null;
		get = null;
		return result;
	}

	/**
	 * 
	 * parse field set
	 * @author 2014-7-14 mnt.cico
	 */
	private static Map<String, Method> parseAndGetSetterFunction(Method[] methods)
	{
		Map<String, Method> result = new HashMap<String, Method>();
		String methodName = null;
		String set = "set";
		for (Method method : methods)
		{
			methodName = method.getName();
			if (methodName.startsWith(set))
			{
				result.put(lowerFieldName(methodName.substring(3)), method);
			}
		}
		methodName = null;
		set = null;
		return result;
	}

	/**
	 * 
	 * first char to lower
	 * @author 2014-7-14 mnt.cico
	 */
	private static String lowerFieldName(String name)
	{
		StringBuilder sbString = new StringBuilder();
		char[] filedCharArray = name.toCharArray();
		sbString.append(String.valueOf(filedCharArray[0]).toLowerCase());
		for (int i = 1; i < filedCharArray.length; i++)
		{
			sbString.append(filedCharArray[i]);
		}
		return sbString.toString();
	}
}
