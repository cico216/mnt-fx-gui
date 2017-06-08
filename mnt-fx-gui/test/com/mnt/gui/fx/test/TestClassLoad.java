package com.mnt.gui.fx.test;

import com.mnt.gui.fx.loader.classload.ClassLoad;
import com.mnt.gui.fx.loader.classload.ClassLoadSupport;
import com.mnt.gui.fx.test.load.TestClass;

/**
 * 
 * <p>
 * 测试类加载
 * </p>
 * @author    mnt.cico
 * @version  2015年5月24日 下午11:03:58 mnt.cico .
 * @since   FX8.0
 */
public class TestClassLoad {

	@ClassLoad(srcPath="./load/load")
	public static ClassLoadSupport<TestClass> TEST_CLASS_LOADER;
}
