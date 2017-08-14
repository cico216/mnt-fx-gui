package com.mnt.gui.fx.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 姜彪
 * @author 姜彪
 * @date 2015年10月8日
 */
public class DateConvertUtil {
	
	private DateConvertUtil() {
		
	}
	
	/**
	 * Date转换为 LocalDate
	 * @param date
	 * @return
	 */
	public static final LocalDateTime getLocalDateForDate(Date date) {
		if(null == date) {
			return null;
		}
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
	
	/**
	 * LocalDateTime 转换为 Date
	 * @param date
	 * @return
	 */
	public static final Date getDateByLocalTime(LocalDateTime date) {
		return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	/**
	 * 
	 * LocalDateTime 转换为 毫秒
	 * @param date
	 * @return
	 */
	public static final long getDateLocalTimeMills(LocalDateTime date) {
		return getDateByLocalTime(date).getTime();
	}
	
	
	/**
	 * LocalDate 转换为 Date
	 * @param date
	 * @return
	 */
	public static final Date getDateByLocalDate(LocalDate date) {
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	/**
	 * LocalDate 转换为 毫秒
	 * @return
	 */
	public static final long getDateLocalMills(LocalDate date) {
		return getDateByLocalDate(date).getTime();
	}

}
