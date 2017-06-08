package com.mnt.gui.fx.loader.classload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * <p>
 * 类加载器注解
 * </p>
 * @author    mnt.cico
 * @version  2015年5月24日 下午10:57:21 mnt.cico .
 * @since   FX8.0
 */
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ClassLoad {

	/**
	 * <p>
	 * 类路径
	 * </p>
	 * @create mnt.cico
	 * @return
	 */
	public String srcPath();
	
	/**
	 * 
	 * <p>
	 * 是否单例
	 * </p>
	 * @create mnt.cico
	 * @return
	 */
	public boolean singleton() default false;
	
	
}
