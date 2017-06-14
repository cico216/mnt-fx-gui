package com.mnt.gui.fx.table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

import com.mnt.gui.fx.table.anno.FXColumn;
import com.mnt.gui.fx.table.value.TableData;
import com.mnt.gui.fx.util.FXPair;

import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Pair;

/**
 * 
 * <p>
 * tableView 附带支持
 * </p>
 * @author    mnt.cico
 * @version  2016年5月15日 下午12:31:43 mnt.cico .
 * @since   FX8.0
 */
@SuppressWarnings("rawtypes")
public class TableViewSupport<S> implements Runnable, Delayed {
	
	/**
	 * item 绑定的 属性
	 */
	private final Map<S, Map<String, FXPair<Property, Node>>> itemPropertys = new HashMap<>();
//	private final Map<S, Map<String, FXPair<Property, Node>>> searchItemPropertys = new HashMap<>();
	/**
	 * 表格item
	 */
	private final ObservableList<S> item;
	
	private final TableView<S> tvw;
	//注解类
	private final Class<S> clazz;
	//table泛型的类字段
	private Field[] fields;
	
	//字段名转换后的名称
	private Map<String, String> fieldNamsMap;
	
	//自动更新时间间隔
	private int autoUpdateTime = 30;
	//下次刷新数据时间
	private long nextUpdateTime;
	
	private ObservableList<S> searchItem = FXCollections.observableArrayList();
	
	private ReentrantLock lock = new ReentrantLock();
	
	TableViewSupport(boolean autoUpdate, TableView<S> tvw, ObservableList<S> item, Class<S> clazz)
	{
		this.item = item;
		this.tvw = tvw;
		this.clazz = clazz;
		fields = this.clazz.getDeclaredFields();
		fieldNamsMap = new HashMap<>();
		List<Field> fieldsTemp = new ArrayList<>();
		for (Field field : fields) {
			if(field.isAnnotationPresent(FXColumn.class))
			{
				fieldsTemp.add(field);
				FXColumn column = field.getAnnotation(FXColumn.class);
				if(!column.idName().equals(TabelCellFactory.COLUMN_DEFAULT_NAME))
				{
					fieldNamsMap.put(field.getName(), column.idName());
				}
			}
		}
		fields = new Field[fieldsTemp.size()];
		fields = fieldsTemp.toArray(fields);
		
		if(autoUpdate)
		{
			nextUpdateTime = getCurrTime() + autoUpdateTime;
		}
	}
	
	/**
	 * 获取当前系统时间
	 * @return
	 */
	private long getCurrTime()
	{
		return System.currentTimeMillis();
	}
	
	/**
	 * 数据过滤
	 * @param predicate
	 */
	public void filter(Predicate<S> predicate)
	{
		searchItem.clear();
		searchItem.addAll(item.filtered(predicate));
		tvw.setItems(searchItem);
	}
	
	/**
	 * 清除过滤
	 */
	public void clearFilter()
	{
		searchItem.clear();
		tvw.setItems(item);
	}

	
	/**
	 * 
	 * <p>
	 * 添加多个item
	 * </p>
	 * @create mnt.cico
	 * @param list
	 */
	public void addItems(List<S> list)
	{
		try {
			lock.lock();
			for (S s : list) {
				item.add(s);
				itemPropertys.put(s, TabelCellFactory.buildPropertys(fields, fieldNamsMap, s));
			}
		} finally {
			lock.unlock();
		}
		
	}
	
	/**
	 * 
	 * <p>
	 * 添加item
	 * </p>
	 * @create mnt.cico
	 * @param s
	 */
	public void addItem(S s)
	{
		try {
			lock.lock();
			item.add(s);
			itemPropertys.put(s, TabelCellFactory.buildPropertys(fields, fieldNamsMap, s));
			
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * <p>
	 * 移除item
	 * </p>
	 * @create mnt.cico
	 * @param s
	 */
	public void removeItem(S s)
	{
		try {
			lock.lock();
			item.remove(s);
			itemPropertys.remove(s);
			
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * <p>
	 * 移除item 根据index
	 * </p>
	 * @create mnt.cico
	 * @param s
	 */
	public void removeItem(int index)
	{
		try {
			lock.lock();
			S s = item.remove(index);
			itemPropertys.remove(s);
			
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * <p>
	 * 清除 表格数据
	 * </p>
	 * @create mnt.cico
	 */
	public void clear()
	{
		try {
			lock.lock();
			item.clear();
			itemPropertys.clear();
			
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * <p>
	 * 获取选择索引
	 * </p>
	 * @create mnt.cico
	 * @return
	 */
	public int getSelectIndex()
	{
		return tvw.getSelectionModel().getSelectedIndex();
	}
	
	/**
	 * <p>
	 * 获取选择item
	 * </p>
	 * @create mnt.cico
	 * @return
	 */
	public S getSelectItem()
	{
		return tvw.getSelectionModel().getSelectedItem();
	}
	
	/**
	 * <p>
	 * 获取属性map 
	 * </p>
	 * @create mnt.cico
	 * @return
	 */
	public Map<S, Map<String, FXPair<Property, Node>>> getItemPropertys() {
		return itemPropertys;
	}
	
	/**
	 * <p>
	 * 获取cell的属性
	 * </p>
	 * @create mnt.cico
	 * @param s
	 * @param name
	 * @return
	 */
	public FXPair<Property, Node> getCellProperty(S s, String name)
	{
		return itemPropertys.get(s).get(name);
	}

	/**
	 * <p>
	 * 更新表格数据
	 * </p>
	 * @create mnt.cico
	 */
	@SuppressWarnings("unchecked")
	public void updateData()
	{
		try {
			List<S> tempItems = new ArrayList<>(item);
			for (S s : tempItems) {
				for (Field field : fields) {
					String fieldName = field.getName();
					if(fieldNamsMap.containsKey(fieldName))
					{
						fieldName = fieldNamsMap.get(fieldName);
					}
					Property perperty = itemPropertys.get(s).get(fieldName).getKey();
					if(null != perperty)
					{
						final Object value = field.get(s);
						if(perperty.getValue() == null)
						{
							if(null != value)
							{
								Platform.runLater(() -> {
									perperty.setValue(value);
								});
							}
						}
						else
						{
							if(!perperty.getValue().equals(value))
							{
								Platform.runLater(() -> {
									perperty.setValue(value);
								});
							}
						}
					}
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <p>
	 * 关闭方法
	 * </p>
	 * @create mnt.cico
	 */
	public void shutdown()
	{
		
	}

	public ObservableList<S> getItem() {
		return item;
	}

	public TableView<S> getTvw() {
		return tvw;
	}
	
	/**
	 * <p>
	 * item size
	 * </p>
	 * @create mnt.cico
	 * @return
	 */
	public int size()
	{
		return item.size();
	}

	@Override
	public void run() {
		
		try {
			lock.lock();
			
			updateData();
			nextUpdateTime = getCurrTime() + autoUpdateTime;
			
		} finally {
			lock.unlock();
		}
		
	}
	
	/**
	 * <p>
	 * 获取table所有数据
	 * </p>
	 * @create mnt.cico
	 * @return
	 */
	public final TableData getTableData() {
		final TableData result = new TableData();
		final ObservableList<S> items = item;
		ObservableList<TableColumn<S, ?>> columns = tvw.getColumns();
		ObservableList<TableColumn<S, ?>> visibleColumns = FXCollections.observableArrayList();
		
		for (TableColumn<S, ?> tableColumn : columns) {
			if(tableColumn.isVisible()) {
				visibleColumns.add(tableColumn);
			}
		}
		
		List<Pair<String, Double>> titleList = new ArrayList<>();
		List<String[]> contentList = new ArrayList<>();
		Pair<String, Double> title;
		
		for (TableColumn<S, ?> tableColumn : visibleColumns) {
			if(tableColumn.isVisible()) {
				title = new Pair<String, Double>(tableColumn.getText(), tableColumn.getWidth());
				titleList.add(title);
			}
		}
		
		String[] columnText;
		Object context;
		for (S model : items) {
			columnText = new String[visibleColumns.size()];
			for (int i = 0; i < visibleColumns.size(); i++) {
				context = visibleColumns.get(i).getCellData(model);
				if(context != null) {
					columnText[i] = context.toString();
				} else {
					columnText[i] = "";
				}
			}
			contentList.add(columnText);
		}
		result.setContentList(contentList);
		result.setTitleList(titleList);
		return result;
	}

	@Override
	public int compareTo(Delayed o)
	{
		if(o == this)
			return 0;
		long a = getDelay(TimeUnit.MILLISECONDS);
		long b = o.getDelay(TimeUnit.MILLISECONDS);
        return (a < b) ? -1 : (a == b) ? 0 : 1;
	}

	@Override
	public long getDelay(TimeUnit unit)
	{
		return unit.convert(nextUpdateTime - getCurrTime(), TimeUnit.MILLISECONDS);
	}
	
	public int getAutoUpdateTime()
	{
		return autoUpdateTime;
	}

	public void setAutoUpdateTime(int autoUpdateTime)
	{
		this.autoUpdateTime = autoUpdateTime;
	}
	
}
