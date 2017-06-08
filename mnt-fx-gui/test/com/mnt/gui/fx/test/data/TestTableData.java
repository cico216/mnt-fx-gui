package com.mnt.gui.fx.test.data;

import javafx.beans.property.Property;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

import com.mnt.gui.fx.table.TableViewSupport;
import com.mnt.gui.fx.table.anno.FXColumn;
import com.mnt.gui.fx.table.anno.FXTable;
import com.mnt.gui.fx.table.cell.CheckTableCell;
import com.mnt.gui.fx.table.cell.TextFieldTabelCell;
import com.mnt.gui.fx.table.cell.base.BaseTableCell;

@FXTable
public class TestTableData {
	
	@FXColumn(cellFactory = TextFieldTabelCell.class)
	private String label;
	
	@FXColumn(cellFactory = CheckTableCell.class)
    private boolean choice;
	
	@FXColumn(cellFactory = ButtonTableCell.class)
    private String button;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isChoice() {
		return choice;
	}

	public void setChoice(boolean choice) {
		this.choice = choice;
	}

	public String getButton() {
		return button;
	}

	public void setButton(String button) {
		this.button = button;
	}

	@Override
	public String toString() {
		return "TestTableData [label=" + label + ", choice=" + choice
				+ ", button=" + button + "]";
	}
	
	/**
	 * <p>
	 * 按钮转换器
	 * </p>
	 * @author    mnt.cico
	 * @version  2016年5月16日 上午12:25:21 mnt.cico .
	 * @since   FX8.0
	 */
	public static class ButtonTableCell extends BaseTableCell<TestTableData, String, Button>
	{
		public ButtonTableCell() {
			
		}
		
		@Override
		public Button getGraphic(String value, int index, TestTableData item, Property<String> property, TableViewSupport<TestTableData> supportTvw) {
			Button result = new Button(value);
			result.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					TestTableData s = new TestTableData();
					s.setButton("test" + supportTvw.size());
					s.setLabel("test" + supportTvw.size());
					s.setChoice(false);
					supportTvw.addItem(s);
				}
			});
			return result;
		}
		
		@Override
		public void updateNode(Button node, String value, int index, TestTableData item, Property<String> property, TableViewSupport<TestTableData> supportTvw) {
		}
	}
	
}
