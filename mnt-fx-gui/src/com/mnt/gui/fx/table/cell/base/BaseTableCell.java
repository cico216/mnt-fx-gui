package com.mnt.gui.fx.table.cell.base;

import javafx.beans.property.Property;
import javafx.scene.Node;

import com.mnt.gui.fx.table.TableViewSupport;

/**
 * 
 * <p>
 * 基础table cell 
 * </p>
 * @author    mnt.cico
 * @version  2016年5月15日 上午10:43:02 mnt.cico .
 * @since   FX8.0
 */
public abstract class BaseTableCell<S, T, N extends Node> {

	/**
	 * <p>
	 * 获取列title 节点
	 * </p>
	 * @create mnt.cico
	 * @return
	 */
	public N getTitleGraphic()
	{
		return null;
	}
	
	/**
	 * <p>
	 * 获取表格显示
	 * </p>
	 * @create mnt.cico
	 * @param value
	 * @return
	 */
	public String getText(T value)
	{
		return String.valueOf(value);
	}
	
	/**
	 * <p>
	 * 获取节点显示转换
	 * </p>
	 * @create mnt.cico
	 * @param value
	 * @return
	 */
	public abstract N getGraphic(T value, int index, S item, Property<T> property, TableViewSupport<S> supportTvw);
	
	/**
	 * <p>
	 * 更新节点
	 * </p>
	 * @create mnt.cico
	 * @param node
	 * @param value
	 * @param index
	 * @param table
	 */
	public abstract void updateNode(N node, T value, int index, S item, Property<T> property, TableViewSupport<S> supportTvw);
	
}
