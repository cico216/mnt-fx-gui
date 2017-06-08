package com.mnt.gui.fx.table.cell;

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

import com.mnt.gui.fx.table.TableViewSupport;
import com.mnt.gui.fx.table.cell.base.BaseTableCell;

/**
 * <p>
 * 默认textField cell转换
 * </p>
 * @author    mnt.cico
 * @version  2016年5月15日 下午11:31:06 mnt.cico .
 * @since   FX8.0
 */
public class TextFieldTabelCell extends BaseTableCell<Object, String, TextField>{

	@Override
	public TextField getGraphic(String value, int index, Object item, Property<String> property, TableViewSupport<Object> supportTvw) {
		final TextField result = new TextField(value);
		result.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				property.setValue(newValue);
			}
		});
		return result;
	}

	@Override
	public void updateNode(TextField node, String value, int index, Object item, Property<String> property, TableViewSupport<Object> supportTvw) {
		if(!value.equals(node.getText()))
		{
			node.setText(value);
		}
	}
}
