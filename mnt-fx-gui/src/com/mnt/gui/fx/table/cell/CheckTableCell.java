package com.mnt.gui.fx.table.cell;

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;

import com.mnt.gui.fx.table.TableViewSupport;
import com.mnt.gui.fx.table.cell.base.BaseTableCell;

/**
 * <p>
 * 复选框 table cell
 * </p>
 * @author    mnt.cico
 * @version  2016年5月15日 下午1:49:18 mnt.cico .
 * @since   FX8.0
 */
public class CheckTableCell extends BaseTableCell<Object, Boolean, CheckBox> {

	@Override
	public CheckBox getGraphic(Boolean value, int index, Object item, Property<Boolean> property , TableViewSupport<Object> supportTvw) {
		CheckBox result = new CheckBox();
		result.setSelected(value);
		result.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				property.setValue(newValue);
			}
		});
		return result;
	}
	
	@Override
	public void updateNode(CheckBox node, Boolean value, int index, Object vo, Property<Boolean> property, TableViewSupport<Object> supportTvw) {
		if(node.isSelected() != value)
		{
			node.setSelected(value);
		}
	}

}
