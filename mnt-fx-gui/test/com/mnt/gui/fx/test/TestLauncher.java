package com.mnt.gui.fx.test;

import com.mnt.gui.fx.base.BaseController;
import com.mnt.gui.fx.base.BaseLauncher;
import com.mnt.gui.fx.loader.FXMLLoaderUtil;

public class TestLauncher extends BaseLauncher {

	@Override
	public BaseController getRoot() {
		
		return FXMLLoaderUtil.load(MainController.class);
	}
	
	@Override
	public String getTitle() {
		return "测试";
	}

	public static void main(String[] args) {
		launch(args);
	}
}
