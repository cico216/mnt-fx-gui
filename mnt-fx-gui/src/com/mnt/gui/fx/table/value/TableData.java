package com.mnt.gui.fx.table.value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.util.Pair;


/**
 * 
 * 
 * <p>
 * table all data 
 * </p>
 * @author    mnt.cico
 * @version  2014-9-22 9:01:32 mnt.cico .
 * @since   FX8.0
 */
public class TableData {

	/**
	 * name -> width
	 */
	private List<Pair<String, Double>> titleList;
	
	private List<String[]> contentList;
	
	private Map<String, String> otherValue = new HashMap<>();
	
	private List<?> models;

	public List<Pair<String, Double>> getTitleList() {
		return titleList;
	}

	public void setTitleList(List<Pair<String, Double>> titleList) {
		this.titleList = titleList;
	}

	public List<String[]> getContentList() {
		return contentList;
	}

	public void setContentList(List<String[]> contentList) {
		this.contentList = contentList;
	}
	

	public String getOtherValue(String key) {
		return otherValue.get(key);
	}

	public String putOtherValue(String key, String value) {
		return otherValue.put(key, value);
	}

	public Map<String, String> getOtherValue() {
		return otherValue;
	}

	public List<?> getModels() {
		return models;
	}

	public void setModels(List<?> models) {
		this.models = models;
	}

	@Override
	public String toString() {
		return "TableData [titleList=" + titleList + ", contentList="
				+ contentList + "]";
	}
	
}
