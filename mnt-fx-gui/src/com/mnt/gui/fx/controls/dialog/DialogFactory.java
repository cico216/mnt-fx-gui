package com.mnt.gui.fx.controls.dialog;

import java.util.LinkedList;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.mnt.gui.fx.base.BaseController;
import com.mnt.gui.fx.controls.dialog.action.OnAction;
import com.mnt.gui.fx.controls.dialog.confirm.ConfirmDialog;
import com.mnt.gui.fx.controls.dialog.message.MessageDialog;
import com.mnt.gui.fx.controls.dialog.message.MessageType;
import com.mnt.gui.fx.controls.dialog.window.WindowDialog;

/**
 * <p>
 * 弹出工厂
 * </p>
 * @author    mnt.cico
 * @version  2016年5月16日 下午9:29:35 mnt.cico .
 * @since   FX8.0
 */
public class DialogFactory {
	
	private static DialogFactory INSTANCE;
	
	//最大显示的消息数
	private static int MAX_MESSAGE_COUNT = 6;
	
	//消息显示的位置
	public static Pos msgPos = Pos.TOP_RIGHT;
	
	//消息队列
	private LinkedList<MessageDialog> messageDeque = new LinkedList<>();
	
	//已经显示的消息队列
	private LinkedList<MessageDialog> showMessageDeque = new LinkedList<>();

	public static DialogFactory getInstance()
	{
		if(null == INSTANCE)
		{
			INSTANCE = new DialogFactory();
		}
		return INSTANCE;
	}
	
	private DialogFactory()
	{
		for (int i = 0; i < MAX_MESSAGE_COUNT; i++)
		{
			messageDeque.addLast(preCreateMessageDialog());
		}
	}
	
	/**
	 * <p>
	 * 显示消息
	 * </p>
	 * @create mnt.cico
	 * @param type
	 * @param title
	 * @param info
	 */
	public void showMessage(MessageType type, String title, String info, OnAction action)
	{
		MessageDialog dialog;
		if((dialog = messageDeque.poll()) == null)
		{
			dialog = showMessageDeque.removeFirst();
			dialog.hide();
		}
		
		showMessageDeque.addLast(dialog);
		
		dialog.setType(type);
		dialog.setTitle(title);
		dialog.setInfo(info);
		dialog.setOnAction(action);
		dialog.resetIndex(showMessageDeque.size() - 1);
		dialog.show();
	}
	
	/**
	 * 回收消息
	 * @param dialog
	 */
	public void recoverMessage(MessageDialog dialog)
	{
		if(showMessageDeque.contains(dialog))
		{
			showMessageDeque.remove(dialog);
			messageDeque.addLast(dialog);
		}
		for (int i = 0; i < showMessageDeque.size(); i++)
		{
			showMessageDeque.get(i).resetIndex(i);
		}
		
	}
	
	
	/**
	 * 预创建信息控件
	 * @return
	 */
	private MessageDialog preCreateMessageDialog()
	{
		Popup pop = new Popup();
		final MessageDialog dialog = new MessageDialog(pop);
		pop.getScene().setRoot(dialog);
		return dialog;
	}
	
	/**
	 * <p>
	 * 显示成功消息
	 * </p>
	 * @create mnt.cico
	 * @param type
	 * @param title
	 * @param info
	 */
	public void showSuccessMsg(String title, String info, OnAction action)
	{
		showMessage(MessageType.SUCCESS, title, info, action);
	}
	
	/**
	 * <p>
	 * 显示成功消息
	 * </p>
	 * @create mnt.cico
	 * @param type
	 * @param title
	 * @param info
	 */
	public void showInfoMsg(String title, String info, OnAction action)
	{
		showMessage(MessageType.INDO, title, info, action);
	}
	
	/**
	 * <p>
	 * 显示成功消息
	 * </p>
	 * @create mnt.cico
	 * @param type
	 * @param title
	 * @param info
	 */
	public void showFaildMsg(String title, String info, OnAction action)
	{
		showMessage(MessageType.FAILED, title, info, action);
	}
	
	/**
	 * <p>
	 * 显示成功消息
	 * </p>
	 * @create mnt.cico
	 * @param type
	 * @param title
	 * @param info
	 */
	public void showWarningMsg(String title, String info, OnAction action)
	{
		showMessage(MessageType.WARNING, title, info, action);
	}
	
	/**
	 * 展示窗口
	 * @param title
	 * @param info
	 * @param action
	 */
	public void showWindow(String title, String info, OnAction action)
	{
		Popup pop = new Popup();
		final WindowDialog dialog = new WindowDialog(pop);
		pop.getScene().setRoot(dialog);
		dialog.setTitle(title);
		dialog.setInfo(info);
		dialog.setOnAction(action);
		dialog.show();
	}
	
	
	/**
	 * 指定的stage显示一个有确认和取消的阻塞窗口
	 * @param title
	 * @param info
	 * @param confirm
	 * @param cancel
	 */
	public void showConfirm(Stage stage, String title, String info, OnAction confirm, OnAction cancel)
	{
		final Stage innerStage = new Stage();
		innerStage.setTitle(title);
		innerStage.initModality(Modality.APPLICATION_MODAL);
		innerStage.initStyle(StageStyle.DECORATED);
		final ConfirmDialog dialog = new ConfirmDialog(innerStage, confirm , cancel);
		innerStage.setScene(new Scene(dialog));
		innerStage.initOwner(stage);
		dialog.setTitle(title);
		dialog.setInfo(info);
		innerStage.showAndWait();
		
	}
	
	/**
	 * 
	 * <p>
	 * 显示阻塞窗口
	 * </p>
	 * @create mnt.cico
	 * @param stage
	 * @param parent
	 */
	public void showBlockWindow(Stage stage, Parent parent)
	{
		final Stage innerStage = new Stage();
		innerStage.initModality(Modality.APPLICATION_MODAL);
		innerStage.initStyle(StageStyle.DECORATED);
		innerStage.setScene(new Scene(parent));
		innerStage.initOwner(stage);
		innerStage.showAndWait();
	}
	
	/**
	 * 在主场景显示一个有确认和取消的阻塞窗口
	 * @param title
	 * @param info
	 * @param confirm
	 */
	public void showConfirm(String title, String info, OnAction confirm, OnAction cancel)
	{
		showConfirm(BaseController.getMainStage(), title, info, confirm, cancel);
	}
	
	/**
	 * 在主场景显示一个只有确认的阻塞窗口
	 * @param title
	 * @param info
	 * @param confirm
	 */
	public void showConfirm(String title, String info, OnAction confirm)
	{
		showConfirm(BaseController.getMainStage(), title, info, confirm, null);
	}
	
	//关闭方法
	public void shutdown()
	{
		messageDeque.clear();
	}
	
}
