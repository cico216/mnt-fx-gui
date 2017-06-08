package com.mnt.gui.fx.style;

import javafx.scene.Scene;
import javafx.stage.Stage;

import com.mnt.gui.fx.base.BaseController;
import com.mnt.gui.fx.base.BaseWindowStyle;

/**
 * 
 * <p>
 * os 自带的样式
 * </p>
 * @author    mnt.cico
 * @version  2014年12月16日 上午8:03:37 mnt.cico .
 * @since   FX8.0
 */
public class WindowDefStyle extends BaseWindowStyle {

	@Override
	public void setRoot(BaseController root) {
		stage.getScene().setRoot(root);
	}

	@Override
	public void buildStageByStyle(Stage stage, BaseController root) {
		this.stage = stage;
		this.root = root;
		stage.setScene(new Scene(root));
	}

	@Override
	public void setTitle(String title) {
		stage.setTitle(title);
	}

	@Override
	public void clearStage() {
		stage.setScene(null);
	}

	@Override
	public BaseController getRoot() {
		return root;
	}

}
