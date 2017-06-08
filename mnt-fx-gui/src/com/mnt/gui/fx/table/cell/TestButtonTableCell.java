package com.mnt.gui.fx.table.cell;

import javafx.beans.property.Property;
import javafx.scene.control.Button;

import com.mnt.gui.fx.table.TableViewSupport;
import com.mnt.gui.fx.table.cell.base.BaseTableCell;

/**
 * 
 * <p>
 * 按钮table cell 实现
 * </p>
 * @author    mnt.cico
 * @version  2016年5月15日 上午10:56:26 mnt.cico .
 * @since   FX8.0
 */
public class TestButtonTableCell extends BaseTableCell<Object, Object, Button> {

	@Override
	public Button getGraphic(Object value, int index, Object item, Property<Object> property, TableViewSupport<Object> supportTvw) {
		Button result = new Button(String.valueOf(value));
		return result;
	}
	
	@Override
	public void updateNode(Button node, Object value, int index, Object item, Property<Object> property, TableViewSupport<Object> supportTvw) {

	}

}
