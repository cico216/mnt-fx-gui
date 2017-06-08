package com.mnt.gui.fx.test._3d;

import com.mnt.gui.fx.base.Base3DController;
import com.mnt.gui.fx.base.Base3DLauncher;
import com.mnt.gui.fx.loader.FXMLLoaderUtil;


/**
 * <p>
 * 3d启动
 * </p>
 * @author    mnt.cico
 * @version  2016年5月19日 下午8:06:12 mnt.cico .
 * @since   FX8.0
 */
public class D3Launcher extends Base3DLauncher {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public Base3DController getRoot() {
		return FXMLLoaderUtil.load(Main3dController.class);
	}
	
}
