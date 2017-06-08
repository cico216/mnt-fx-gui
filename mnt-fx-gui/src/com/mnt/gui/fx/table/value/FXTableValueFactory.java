package com.mnt.gui.fx.table.value;

import java.util.Map;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

import com.mnt.gui.fx.util.FXPair;

/**
 * 
 * <p>
 * table cell value Factory
 * </p>
 * @author    mnt.cico
 * @version  2016年5月15日 下午9:06:36 mnt.cico .
 * @since   FX8.0
 * @param <S>
 * @param <T>
 */
@SuppressWarnings("rawtypes")
public class FXTableValueFactory<S,T> implements Callback<CellDataFeatures<S,T>, ObservableValue<T>> {

	/**
	 * table绑定的属性
	 */
	private Map<S, Map<String, FXPair<Property, Node>>> itemPropertys;
	
	/**
	 * 字段名
	 */
	private String fileName;
	
	
	public FXTableValueFactory(Map<S, Map<String, FXPair<Property, Node>>> itemPropertys, String fileName) {
		this.itemPropertys = itemPropertys;
		this.fileName = fileName;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ObservableValue<T> call(CellDataFeatures<S, T> param) {
		return itemPropertys.get(param.getValue()).get(fileName).getKey();
	}
	
}