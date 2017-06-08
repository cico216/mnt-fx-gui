package com.mnt.gui.fx.convert;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javafx.scene.control.DatePicker;

/**
 * 转换工厂
 * 1.java.util.Date to LocalDate</p>
 * 2.LocalDate to java.util.Date</p>
 * @author 2014-8-17 mnt.cico
 *
 */
public abstract class DateConvertFactory {

	private DateConvertFactory() {
		//empty
	}
	
	/**
	 * get date
	 * @param datePicker
	 * @return
	 */
	public static final Date getDateByDatePicker(DatePicker datePicker) {
		return getDateLocalTime(datePicker.getValue());
	}
	
	/**
	 * get localDate
	 * @param date
	 * @return
	 */
	public static final LocalDate getLocalDateForDate(Date date) {
		if(null == date) {
			return null;
		}
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	/**
	 * localDate to date
	 * @param date
	 * @return
	 */
	public static final Date getDateLocalTime(LocalDate date) {
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
}
