package com.mnt.gui.fx.table.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javafx.geometry.Pos;

import com.mnt.gui.fx.table.TabelCellFactory;
import com.mnt.gui.fx.table.cell.LabelTabelCell;
import com.mnt.gui.fx.table.cell.base.BaseTableCell;

/**
 * 
 * <p>
 * fx cell注解
 * </p>
 * @author    mnt.cico
 * @version  2016年5月15日 上午11:23:51 mnt.cico .
 * @since   FX8.0
 */
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FXColumn {

	//字段对应的fxml 属性名
	String idName() default TabelCellFactory.COLUMN_DEFAULT_NAME;
	
	//默认表格cell转换器
	@SuppressWarnings("rawtypes")
	Class<? extends BaseTableCell> cellFactory() default LabelTabelCell.class;
	
	//默认cell内容位置
	Pos alignment() default Pos.CENTER;
	
	//是否需要改变显示节点
	boolean changeGraphic() default false;
	
}
