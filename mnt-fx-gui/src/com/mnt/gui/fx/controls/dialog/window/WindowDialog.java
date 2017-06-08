package com.mnt.gui.fx.controls.dialog.window;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.util.Duration;

import com.mnt.gui.fx.base.BaseController;
import com.mnt.gui.fx.controls.dialog.action.OnAction;
import com.mnt.gui.fx.loader.FXMLLoaderUtil;
import com.mnt.gui.fx.util.ApplicationUtil;

/**
 * 窗口弹窗
 * @author 姜彪
 * @date 2016年5月18日
 */
public class WindowDialog extends BaseController
{

    @FXML
    private Hyperlink hlinkInfo;

    @FXML
    private Label lblTitle;
    
    //动画时间
    private Timeline timeline = new Timeline();
    
    //点击触发事件
    private OnAction onAction;
    
    /**
     * 弹出窗口
     */
    private Popup pop;
    
    public WindowDialog(Popup pop)
	{
    	this.pop = pop;
    	FXMLLoaderUtil.load(this);
	}

	@Override
	public void init()
	{
		pop.setAnchorX(ApplicationUtil.getInstance().getDesktopWidth() - getPrefWidth());
		pop.setAnchorY(ApplicationUtil.getInstance().getDesktopHeight() - getPrefHeight() - 5);
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
		hlinkInfo.setText(info);
	}

	public void setOnAction(OnAction onAction)
	{
		this.onAction = onAction;
	}
	
	public OnAction getOnAction()
	{
		return onAction;
	}

	/**
	 * 显示窗口
	 */
	public void show()
	{
		setLayoutY(getPrefHeight());
		timeline.getKeyFrames().addAll(new KeyFrame(Duration.millis(300), new KeyValue(layoutYProperty(), 0)));
		timeline.play();
		pop.show(getMainStage());
	}

    //关闭
    @FXML
    void processClose(ActionEvent event) {
    	hide();
    }

    //查看详情
    @FXML
    void processView(ActionEvent event) {
    	if(onAction != null)
    	{
    		onAction.action();
    	}
    	hide();
    }
    
    //隐藏窗口
    private void hide()
    {
    	onAction = null;
    	timeline.stop();
    	timeline.getKeyFrames().clear();
    	pop.hide();
    }
    
}
