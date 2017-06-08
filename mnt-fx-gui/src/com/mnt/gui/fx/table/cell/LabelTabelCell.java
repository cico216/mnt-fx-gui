package com.mnt.gui.fx.table.cell;

import javafx.beans.property.Property;
import javafx.scene.Node;

import com.mnt.gui.fx.table.TableViewSupport;
import com.mnt.gui.fx.table.cell.base.BaseTableCell;

/**
 * 
 * <p>
 * label cell转换器
 * </p>
 * @author    mnt.cico
 * @version  2016年5月15日 上午11:34:56 mnt.cico .
 * @since   FX8.0
 */
public class LabelTabelCell extends BaseTableCell<Object, Object, Node> {

	@Override
	public void updateNode(Node node, Object value, int index, Object item , Property<Object> property, TableViewSupport<Object> supportTvw) {
		//empty
	}

	@Override
	public Node getGraphic(Object value, int index, Object item, Property<Object> property, TableViewSupport<Object> supportTvw) {
		return null;
	}


}
