package com.mnt.gui.fx.table;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ScheduledFuture;

import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import com.mnt.gui.fx.table.anno.FXColumn;
import com.mnt.gui.fx.table.cell.base.BaseTableCell;
import com.mnt.gui.fx.table.value.FXTableValueFactory;
import com.mnt.gui.fx.thread.ThreadPoolManager;
import com.mnt.gui.fx.util.FXPair;




/**
 * <p>
 * 表格cell创建工厂
 * </p>
 * @author    mnt.cico
 * @version  2016年5月15日 上午10:27:56 mnt.cico .
 * @since   FX8.0
 */
public abstract class TabelCellFactory {
	// column default name
	public static final String COLUMN_DEFAULT_NAME = "";
	
	/**
	 * 表格刷新延迟队列
	 */
	private static DelayQueue<TableViewSupport<?>> tableUpdateManager = new DelayQueue<>();
	
	/**
	 * 更新表格数据的任务
	 */
	private static ScheduledFuture<?> updateTask;
	
	//初始化自动更新
	static {
		updateTask = ThreadPoolManager.getInstance().scheduleAtFixedRateAction(() -> {
			TableViewSupport<?> task;
			while((task = tableUpdateManager.poll()) != null)
			{
				final TableViewSupport<?> TempTak = task;
				Platform.runLater(() -> {
					TempTak.run();
					tableUpdateManager.add(TempTak);
				});
			}
		}, 30, 30);
	}
	
	/**
	 * 关闭
	 */
	public static void shutdown()
	{
		updateTask.cancel(true);
	}
	
	/**
	 * 添加到自动刷新表格数据
	 * @param tvwSupport
	 */
	public static void addAutoUpdate(TableViewSupport<?> tvwSupport)
	{
		tableUpdateManager.add(tvwSupport);
	}
	
	/**
	 * <p>
	 * 创建表格支持
	 * </p>
	 * @create mnt.cico
	 * @param autoUpdate 是否自动更新表格数据
	 * @param tvw
	 * @param itemTvw
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final <S>TableViewSupport<S> createTableSupport(boolean autoUpdate, TableView<S> tvw, ObservableList<S> itemTvw, Class<S> clazz)
	{
		final TableViewSupport<S> result = new TableViewSupport<>(autoUpdate, tvw, itemTvw, clazz);
		ObservableList<TableColumn<S, ?>> cols = tvw.getColumns(); 
		Field[] fields = clazz.getDeclaredFields();
		//获取对应的属性名
		FXColumn column;
		final Map<String, FXColumn> cellNames = new HashMap<String,FXColumn>(fields.length);
		for (Field field : fields) {
			if(field.isAnnotationPresent(FXColumn.class))
			{
				column = field.getAnnotation(FXColumn.class);
				if(column.idName().equals(COLUMN_DEFAULT_NAME))
				{
					cellNames.put(field.getName(), column);
				}
				else
				{
					cellNames.put(column.idName(), column);
				}
			}
		}
		//给cell 绑定属性
		for (TableColumn<S, ?> col : cols) {
			for (String name : cellNames.keySet()) {
				//绑定 cell value
				if(col.getId().equals(name))
				{
					col.setCellValueFactory(new FXTableValueFactory<>(result.getItemPropertys(), name));
					final FXColumn fxColumn = cellNames.get(name);
					try {
						final BaseTableCell tableCell = fxColumn.cellFactory().newInstance();
						col.setCellFactory(callback -> {
							return new TableCell() {
								protected void updateItem(final Object item, boolean empty) {
									super.updateItem(item, empty);
									if(item == null || empty)
									{
										setGraphic(null);
										setText("");
									}
									else 
									{
//										Node node = getGraphic();
										S s = itemTvw.get(getIndex());
//										result.getCellProperty(s, name).setValue(item);
										setAlignment(fxColumn.alignment());
										FXPair<Property, Node> cellProperty = result.getCellProperty(s, name);
										if(fxColumn.changeGraphic() || null == cellProperty.getValue())
										{
											Node cellGraphic = tableCell.getGraphic(item, getIndex(), s, cellProperty.getKey(), result);
											if(null == cellGraphic)
											{
												setText(tableCell.getText(item));
											}
											else
											{
												cellProperty.setValue(cellGraphic);
												setGraphic(cellGraphic);
											}
										}
										else
										{
											setGraphic(cellProperty.getValue());
											tableCell.updateNode(cellProperty.getValue(), item, getIndex(), s, cellProperty.getKey(), result);
										}
										/*if(node == null)
										{
											
										}
										else
										{
											tableCell.updateNode(node, item, getIndex(), s, result.getCellProperty(s, name), result);
										}*/
										
									}
								}
							};
						});
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		tvw.setItems(itemTvw);
		if(autoUpdate)
		{
			addAutoUpdate(result);
		}
		return result;
	}
	
	/**
	 * <p>
	 * 添加属性转换
	 * </p>
	 * @create mnt.cico
	 * @param fields
	 * @param s
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static <S> Map<String, FXPair<Property, Node>> buildPropertys(Field[] fields, Map<String, String> fieldNamsMap, S s)
	{
		Map<String, FXPair<Property, Node>> result = new HashMap<>(fields.length);
		try {
			Property<? extends Object> property;
			Class<?> type;
			String fieldName;
			for (Field field : fields) {
				fieldName = field.getName();
				type = field.getType();
				field.setAccessible(true);
				if(type.isAssignableFrom(String.class) || type.isAssignableFrom(Character.class) || type.isAssignableFrom(char.class))
				{
					property = new SimpleStringProperty(String.valueOf(field.get(s)));
				}
				else if(type.isAssignableFrom(Integer.class) || type.isAssignableFrom(int.class) ||type.isAssignableFrom(Short.class) || type.isAssignableFrom(short.class) || type.isAssignableFrom(Byte.class) || type.isAssignableFrom(byte.class))
				{
					property = new SimpleIntegerProperty(field.getInt(s));
				}
				else if(type.isAssignableFrom(Boolean.class)|| type.isAssignableFrom(boolean.class))
				{
					property = new SimpleBooleanProperty(field.getBoolean(s));
				}
				else if(type.isAssignableFrom(Float.class)|| type.isAssignableFrom(float.class))
				{
					property = new SimpleFloatProperty(field.getFloat(s));
				}
				else if(type.isAssignableFrom(Long.class)|| type.isAssignableFrom(long.class))
				{
					property = new SimpleLongProperty(field.getLong(s));
				}
				else if(type.isAssignableFrom(Double.class)|| type.isAssignableFrom(double.class))
				{
					property = new SimpleDoubleProperty(field.getDouble(s));
				}
				else 
				{
					property = new SimpleObjectProperty<>(field.get(s));
				}
				property.addListener(new ChangeListener<Object>() {
					@Override
					public void changed(ObservableValue<? extends Object> observable,
							Object oldValue, Object newValue) {
						try {
							field.set(s, newValue);
						} catch (IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				});
				
				if(fieldNamsMap.containsKey(fieldName))
				{
					result.put(fieldNamsMap.get(fieldName), new FXPair<>(property, null));
				}
				else
				{
					result.put(fieldName, new FXPair<>(property, null));
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 
	 * <p>
	 * 创建表格支持 不自动更新item数据显示
	 * </p>
	 * @create mnt.cico
	 * @param tvw
	 * @param item
	 * @return
	 */
	public static final <S>TableViewSupport<S> createTableSupport(TableView<S> tvw, ObservableList<S> item, Class<S> clazz)
	{
		return createTableSupport(false, tvw, item, clazz);
	}
	
	
	/**
	 * 
	 * <p>
	 * 创建 表格支持 不自动更新item属性  创建item容器
	 * </p>
	 * @create mnt.cico
	 * @param tvw
	 * @return
	 */
	public static final <S>TableViewSupport<S> createTableSupport(TableView<S> tvw, Class<S> clazz)
	{
		final ObservableList<S> item = FXCollections.observableArrayList();
		return createTableSupport(false, tvw, item, clazz);
	}
	
	/**
	 * 
	 * <p>
	 * 创建 表格支持   创建item容器
	 * </p>
	 * @create mnt.cico
	 * @param autoUpdate 是否自动更新表格数据
	 * @param tvw
	 * @return
	 */
	public static final <S>TableViewSupport<S> createTableSupport(boolean autoUpdate, TableView<S> tvw, Class<S> clazz)
	{
		final ObservableList<S> item = FXCollections.observableArrayList();
		return createTableSupport(autoUpdate, tvw, item, clazz);
	}
	
}
