package com.mnt.gui.fx.table.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * <p>
 * fx table注解
 * </p>
 * @author    mnt.cico
 * @version  2016年5月15日 上午10:02:44 mnt.cico .
 * @since   FX8.0
 */
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FXTable {
	

}
