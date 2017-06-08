package com.mnt.gui.fx.controls.dialog.confirm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import com.mnt.gui.fx.base.BaseController;
import com.mnt.gui.fx.controls.dialog.action.OnAction;
import com.mnt.gui.fx.loader.FXMLLoaderUtil;

/**
 * <p>
 * 确认弹框
 * </p>
 * @author    mnt.cico
 * @version  2016年5月18日 下午7:18:17 mnt.cico .
 * @since   FX8.0
 */
public class ConfirmDialog extends BaseController {

	
	
    @FXML
    private Button btnCancel;

    @FXML
    private Label lblInfo;

    @FXML
    private Label lblTitle;

	//点击确认按钮事件
	private OnAction onConfirmAction;
	
	//取消按钮事件
	private OnAction onCancelAction;
	
	//当前stage
	private Stage currStage;
	
	
	public ConfirmDialog(Stage stage, OnAction onConfirmAction, OnAction onCancelAction)
	{
		this.onConfirmAction = onConfirmAction;
		this.onCancelAction = onCancelAction;
		this.currStage = stage;
		FXMLLoaderUtil.load(this);
	}
	
	@Override
	public void init() {
		if(onCancelAction == null)
		{
			btnCancel.setVisible(false);
		}
	}
	
	/**
	 * 设置标题
	 * @param title
	 */
	public void setTitle(String title)
	{
		lblTitle.setText(title);
	}
	
	/**
	 * 设置信息
	 * @param title
	 */
	public void setInfo(String info)
	{
		lblInfo.setText(info);
	}
	
	
   @FXML
    void processCancel(ActionEvent event) {
	   	onCancelAction.action();
	   	hide();
    }

    @FXML
    void processConfirm(ActionEvent event) {
    	onConfirmAction.action();
    	hide();
    }

	
	public void hide()
	{
		currStage.close();
	}


}
