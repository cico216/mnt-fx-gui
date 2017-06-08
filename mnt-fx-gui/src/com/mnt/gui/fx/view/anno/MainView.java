package com.mnt.gui.fx.view.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 
 * 
 * <p>
 * 主界面标识
 * </p>
 * @author    mnt.cico
 * @version  2016年5月13日 下午7:53:45 mnt.cico .
 * @since   FX8.0
 */
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MainView {

	//应用名称
	public String appName();
	
	//是否为主界面启动
	public String iconName() default "";
	
	//子界面排序
	public String order() default "";
	
	
}
