package com.mnt.gui.fx.controls.searchbox;

import java.util.List;
import java.util.function.Predicate;

import com.mnt.gui.fx.controls.searchbox.action.SelectAction;
import com.mnt.gui.fx.controls.searchbox.component.ListSearchBox;

import javafx.scene.control.TextField;
import javafx.stage.Popup;

/**
 * 模糊搜索弹框工厂
 *
 * @author jiangbiao
 * @Date 2017年6月13日上午10:53:28
 */
public class SearchBoxFactory {

	
	private static final class SingletonHolder
	{
		private static final SearchBoxFactory	INSTANCE = new SearchBoxFactory();
	}

	public static SearchBoxFactory getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	
	/**
	 * 构建下拉模糊选择框
	 * @param textField
	 * @param allItems
	 * @param action
	 */
	public <T> void buildTextFiled(TextField textField, List<T> allItems, SelectAction<T> action, Predicate<T> predicate)
	{
		Popup pop = new Popup();
		final ListSearchBox<T> searchBox = new ListSearchBox<>(pop, textField, allItems, action, predicate);
		pop.getScene().setRoot(searchBox);
	}
	
}
