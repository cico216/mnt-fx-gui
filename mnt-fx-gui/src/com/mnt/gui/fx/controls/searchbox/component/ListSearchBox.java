package com.mnt.gui.fx.controls.searchbox.component;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.mnt.gui.fx.base.BaseController;
import com.mnt.gui.fx.controls.searchbox.action.SelectAction;
import com.mnt.gui.fx.loader.FXMLLoaderUtil;
import com.mnt.gui.fx.util.ApplicationUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Popup;

/**
 * list类型的下拉列表
 *
 * @author jiangbiao
 * @Date 2017年6月13日上午11:21:48
 */
public class ListSearchBox<T> extends BaseController {

    @FXML
    private ListView<T> listItems;
	
	private ObservableList<T> items = FXCollections.observableArrayList();
    
	private final Popup pop;
	private final TextField textField;
	private final SelectAction<T> action;
	private final Predicate<T> predicate;
	private final List<T> allItems;
	private volatile boolean inSelect;
	
	public ListSearchBox(Popup pop, TextField textField, List<T> allItems, SelectAction<T> action, Predicate<T> predicate)
	{
		this.pop = pop;
		this.textField = textField;
		this.allItems = allItems;
		this.action = action;
		this.predicate = predicate;
		FXMLLoaderUtil.load(this);
	}
	
	@Override
	public void init() {
		super.init();
		listItems.setItems(items);
		
		textField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				
				if(!textField.isFocused()) {
					return;
				}
				
				if(null == newValue || "".equals(newValue))
				{
					action.clear();
					hidePop();
				}
				else
				{
					if(isInSelect())
					{
						exitSelect();
						return;
					}
					clear();
					allItems.stream().filter(predicate).forEach(new Consumer<T>() {
						@Override
						public void accept(T t) {
							items.add(t);
						}
					});
					if(!items.isEmpty())
					{
						showPop();
					}
					else
					{
						hidePop();
					}
				}
				
			}
		});
		
		listItems.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			inSelect();
			if(null != newValue)
			{
				action.action(newValue);
				
				hidePop();
			}
			else
			{
				
			}
		});
	}
	
	
	/**
	 * show the pop
	 */
	private void showPop()
	{
		Bounds screenBounds = textField.localToScene(textField.getBoundsInLocal());
		double x = screenBounds.getMinX() + getMainStage().getScene().getWindow().getX() + 3;
		double y = screenBounds.getMinY() + getMainStage().getScene().getWindow().getY() + screenBounds.getHeight() + 30;
		double height = (ApplicationUtil.getInstance().getDesktopHeight() - ApplicationUtil.getInstance().getScrInsets().bottom);
		double windowHeight = height - y;
		setHeight(windowHeight);
		pop.show(textField, x, y);
	}
	
	/**
	 * hide the pop
	 */
	private void hidePop()
	{
		pop.hide();
	}
	
	private void clear()
	{
		items.clear();
	}
	
	private void inSelect()
	{
		inSelect = true;
	}
	
	private void exitSelect()
	{
		inSelect = false;
	}
	
	private boolean isInSelect()
	{
		return inSelect;
	}

}
