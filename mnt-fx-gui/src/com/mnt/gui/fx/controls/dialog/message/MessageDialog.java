package com.mnt.gui.fx.controls.dialog.message;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import javafx.util.Duration;

import com.mnt.gui.fx.base.BaseController;
import com.mnt.gui.fx.controls.dialog.DialogFactory;
import com.mnt.gui.fx.controls.dialog.action.OnAction;
import com.mnt.gui.fx.loader.FXMLLoaderUtil;
import com.mnt.gui.fx.util.ApplicationUtil;

/**
 * <p>
 * 消息通知框
 * </p>
 * @author    mnt.cico
 * @version  2016年5月15日 上午12:09:24 mnt.cico .
 * @since   FX8.0
 */
public class MessageDialog extends BaseController {

	
	@FXML
    private Label lblInfo;

    @FXML
    private Label lblTitle;
    
    //是否已经显示
    private boolean isShow;
    
    //动画时间
    private Timeline timeline = new Timeline();
    
    //动画开始x
    private double startX;
    //动画开始y
    private double startY;
    //动画结束x
    private double endX;
    //动画结束y
    private double endY;
    //点击触发事件
    private OnAction onAction;
    
    //显示属性
    private SimpleBooleanProperty showProperty = new SimpleBooleanProperty();
    
    /**
     * 弹出窗口
     */
    private Popup pop;
    
    public MessageDialog(Popup pop) {
    	FXMLLoaderUtil.load(this);
    	this.pop = pop;
	}
    
    @Override
    public void init() {
    	super.init();
    	showProperty.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!newValue)
				{
					hide();
				}
			}
		});
    }
    
    @FXML
    void processClicked(MouseEvent event) {
    	if(event.getButton() == MouseButton.PRIMARY)
    	{
    		if(null != onAction)
    		{
    			onAction.action();
    		}
    		hide();
    	}
    }
    
    /**
     * <p>
     * 设置消息类型
     * </p>
     * @create mnt.cico
     * @param type
     */
    public void setType(MessageType type)
    {
    	switch (type) {
		case FAILED:
			
			break;
		case INDO:
			
			break;
		case SUCCESS:
			
			break;
		case WARNING:
			
			break;
		default:
			break;
		}
    }
    
    /**
     * 设置信息标题
     * @create mnt.cico
     * @param title
     */
    public void setTitle(String title)
    {
    	lblTitle.setText(title);
    }
    
    /**
     * 设置信息内容
     * @create mnt.cico
     * @param title
     */
    public void setInfo(String info)
    {
    	lblInfo.setText(info);
    }
    
    
    
    public OnAction getOnAction() {
		return onAction;
	}

	public void setOnAction(OnAction onAction) {
		this.onAction = onAction;
	}

	/**
     * <p>
     * 显示
     * </p>
     * @create mnt.cico
     */
    public void show()
    {
    	isShow = true;
    	showProperty.set(isShow);
    	pop.setAnchorX(ApplicationUtil.getInstance().getDesktopWidth() - 20 - this.getPrefWidth());
		startX = this.getPrefWidth() - 10;
		endX = 0;
		endY = - this.getPrefHeight();
		this.setLayoutX(startX);
		this.setLayoutY(0);
		pop.show(getMainStage());
		timeline.getKeyFrames().addAll(new KeyFrame(Duration.millis(300), new KeyValue(layoutXProperty(), endX))
		,new KeyFrame(Duration.millis(2250), new KeyValue(showProperty, false))
		,new KeyFrame(Duration.millis(2000), new KeyValue(layoutYProperty(), 0))
		,new KeyFrame(Duration.millis(2250), new KeyValue(layoutYProperty(), endY)));
		timeline.play();
    }
    
    /**
     * 重新信息设置位置索引
     * @param index
     */
    public void resetIndex(int index)
    {
    	startY = 20 + index * (this.getPrefHeight() + 8);
    	pop.setAnchorY(startY);
    }
    
//    /**
//     * <p>
//     * 淡出消失
//     * </p>
//     * @create mnt.cico
//     */
//    private void outHide()
//    {
//    	timeline.getKeyFrames().clear();
//    	endY = -this.getPrefHeight();
//    	this.setLayoutY(0);
//    	timeline.stop();
//    	timeline.getKeyFrames().addAll(new KeyFrame(Duration.millis(200), new KeyValue(layoutYProperty(), endY)));
//    	timeline.setOnFinished((event) -> {
//    		hide();
//    	});
//    	timeline.play();
//    }
    
    /**
     * <p>
     * 暂停显示
     * </p>
     * @create mnt.cico
     */
    public void hide()
    {
    	timeline.setOnFinished(null);
    	onAction = null;
    	timeline.stop();
    	timeline.getKeyFrames().clear();
    	isShow = false;
    	DialogFactory.getInstance().recoverMessage(this);
    	pop.hide();
    }
    
}
