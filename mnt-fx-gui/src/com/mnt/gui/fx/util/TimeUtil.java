package com.mnt.gui.fx.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 时间工具类
 * @author 姜彪
 * @date 2015年10月8日
 */
public class TimeUtil
{

	private static TimeUtil timeUtil = new TimeUtil();

	public static TimeUtil newInstance() {
		return timeUtil;
	}

	private TimeUtil() {
	}

	/**
	 * 每秒的毫秒数
	 */
	public static final long SECOND = 1000;
	/**
	 * 每分的毫秒数
	 */
	public static final long MINUTE = SECOND * 60;
	/**
	 * 每小时的毫秒数
	 */
	public static final long HOUR = MINUTE * 60;
	/**
	 * 每天的毫秒数
	 */
	public static final long DAY = HOUR * 24;

	/**
	 * @return 1900年1月1号到现在的天数
	 * @create 2012-11-13 庄宏晓
	 */
	public int currentDays() {
		return (int) ((System.currentTimeMillis()) / DAY);
	}

	/**
	 * @return 凌晨00:00到现在过去的毫秒数(北京时区)
	 * @create 2012-11-13 庄宏晓
	 */
	public long toDayTime() {
		return System.currentTimeMillis() % DAY + HOUR * 8;
	}
	
	/**
	 * @return 凌晨00:00到现在过去的毫秒数(北京时区)
	 * @create 2012-11-13 庄宏晓
	 */
	public long toDayTime(long nowTime) {
		return (nowTime + HOUR * 8) % DAY;
	}
	
	/**
	 * @return 今天时间 剩余的毫秒数（北京时区）
	 * @create 2012-11-13 庄宏晓
	 */
	public long toDayReMain(long nowTime) {
		return DAY - toDayTime(nowTime);
	}

	/**
	 * @return 今天时间 剩余的毫秒数（北京时区）
	 * @create 2012-11-13 庄宏晓
	 */
	public long toDayReMain() {
		return DAY - toDayTime();
	}
	
	/**
	 * 计算相差天数
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 相差天数
	 * @create 2013-09-25 杨小思
	 */
	public int getDifferDays(long startTime, long endTime){
		if(startTime > endTime){
			return 0;
		}
		
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(startTime);
		
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(endTime);
		
        int days = calendar2.get(Calendar.DAY_OF_YEAR) - calendar1.get(Calendar.DAY_OF_YEAR);
        int y2 = calendar2.get(Calendar.YEAR);
        if (calendar1.get(Calendar.YEAR) != y2)
        {
        	calendar1 = (Calendar) calendar1.clone();
            do {
                days += calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);//得到当年的实际天数
                calendar1.add(Calendar.YEAR, 1);
            } while (calendar1.get(Calendar.YEAR) != y2);
       }
       return days;
	}

	/**
	 * 判断是否同一天
	 * 
	 * @param time1
	 *            时间a
	 * @param time2
	 *            时间b
	 * @return true为同一天、 false不是同一天
	 * @create 2012-11-13 庄宏晓
	 */
	public boolean isSameDay(long time1, long time2) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTimeInMillis(time1);
		int year1 = calendar.get(Calendar.YEAR);
		int day1 = calendar.get(Calendar.DAY_OF_YEAR);

		calendar.setTimeInMillis(time2);
		int year2 = calendar.get(Calendar.YEAR);
		int day2 = calendar.get(Calendar.DAY_OF_YEAR);

		if (year1 != year2) {
			return false;
		}

		if (day1 != day2) {
			return false;
		}

		return true;
	}

	/**
	 * 计算时间差
	 * 
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return 时间差（毫秒）
	 * @create 2012-11-14 庄宏晓
	 */
	public long countMS(long beginTime, long endTime) {
		return endTime - beginTime;
	}

	/**
	 * 计算时间差
	 * 
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return 时间差（秒）
	 * @create 2012-11-14 庄宏晓
	 */
	public long countSecond(long beginTime, long endTime) {
		long count = (endTime - beginTime) / SECOND;
		return (endTime - beginTime) % SECOND != 0 ? count + 1 : count;
	}

	/**
	 * 计算时间差
	 * 
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return 时间差（分）
	 * @create 2012-12-11-15 庄宏晓
	 */
	public long countMinute(long beginTime, long endTime) {
		long count = (endTime - beginTime) / MINUTE;
		return (endTime - beginTime) % MINUTE != 0 ? count + 1 : count;
	}

	/**
	 * 根据现在当前时间差，计算目的时间点
	 * 
	 * @param time
	 *            当前时间差
	 * @return 目的时间点
	 * @create 2012-11-14 庄宏晓
	 */
	public long countTime(long time) {
		return System.currentTimeMillis() + time;
	}

	/**
	 * 判断时间是否超时
	 * 
	 * @param time
	 *            判断的时间
	 * @param nowTime
	 *            现在时间
	 * @return true未超时、false超时
	 * @create 2012-11-14 庄宏晓
	 */
	public boolean timeOver(long time, long nowTime) {
		return time <= nowTime;
	}

	/**
	 * 计算第二天凌晨00:00的时间
	 * 
	 * @param nowTime
	 *            某一天
	 * @return 第二天凌晨00:00的时间
	 * @create 2012-11-14 庄宏晓
	 */
	public long dayEnd(long nowTime) {
		return nowTime + (DAY - nowTime % DAY) - 8 * HOUR;
	}

	/**
	 * 计算时间点到下一个小时需要的时间
	 * 
	 * @param time
	 *            时间点
	 * @return 剩余时间
	 * @create 2012-12-12 庄宏晓
	 */
	public long countEndTime(long time, long time2) {
		long result = (time % time2 == 0 ? time2 : time2 - time % time2);
		return result;

	}

	/**
	 * 判断两个点是否为同一周
	 * 
	 * @param time1
	 *            时间a
	 * @param time2
	 *            时间b
	 * @return true是,false否
	 * @create 2012-12-19 杨小思
	 */
	public boolean isSameWeek(long time1, long time2) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTimeInMillis(time1);
		int year1 = calendar.get(Calendar.YEAR);
		int week1 = calendar.get(Calendar.WEEK_OF_YEAR);

		calendar.setTimeInMillis(time2);
		int year2 = calendar.get(Calendar.YEAR);
		int week2 = calendar.get(Calendar.WEEK_OF_YEAR);

		if (year1 != year2) {
			return false;
		}

		return week1 == week2 ? true : false;
	}

	/**
	 * 判断两个点是否为同一月
	 * 
	 * @param time1
	 *            时间a
	 * @param time2
	 *            时间b
	 * @return true是,false否
	 * @create 2012-12-19 杨小思
	 */
	public boolean isSameMonth(long time1, long time2) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTimeInMillis(time1);
		int year1 = calendar.get(Calendar.YEAR);
		int month1 = calendar.get(Calendar.MONTH);

		calendar.setTimeInMillis(time2);
		int year2 = calendar.get(Calendar.YEAR);
		int month2 = calendar.get(Calendar.MONTH);

		if (year1 != year2) {
			return false;
		}

		return month1 == month2 ? true : false;
	}
	
	/**
	 * 获取当前日期为周几
	 * 
	 * @return 1/2/3/4/5/6/7分别代表周几
	 * @create 2012-12-19 杨小思
	 */
	public int getWeek() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获取当前月第几号
	 * 
	 * @return
	 * @create 2013-06-21 杨小思
	 */
	public int getDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 获取当前年号
	 * 
	 * @return 
	 * @create 2012-12-19 杨小思
	 */
	public int getYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}
	
	/**
	 * 获取当前月号
	 * 
	 * @return 
	 * @create 2012-12-19 杨小思
	 */
	public int getMonth() {
		return Calendar.getInstance().get(Calendar.MONTH);
	}
	
	/**
	 * 获取当前月最后一天
	 * 
	 * @return 
	 * @create 2012-12-19 杨小思
	 */
	public int getLastDayOfMonth() {
		return Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 根据指定日期格式的时间
	 * 
	 * @param dateString
	 *            日期
	 * @param format
	 *            日期格式
	 * @return 返回毫秒
	 * @create 2013-01-11 杨小思
	 */
	public long getFormatDateTime(String dateString, String format) {
		long time = 0;
		if (dateString == null || dateString.equals("")) {
			return time;
		}

		SimpleDateFormat formatter = new SimpleDateFormat(format);
		try {
			time = formatter.parse(dateString).getTime();
		} catch (ParseException e) {
			time = -1;
		}
		return time;
	}

	/**
	 * 获取指定格式时间的毫秒数
	 * 
	 * @param timeString
	 *            格式化的时间 格式如： yyyy-MM-dd HH:mm:ss
	 * @return 时间(毫秒)(当为-1时,时间格式无效)
	 * @create 2013-01-11 杨小思
	 */
	public long getDateTime(String dateString) {
		long time = 0;
		if (dateString == null || dateString.equals("")) {
			return time;
		}
		try {
			SimpleDateFormat sdFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			time = sdFormat.parse(dateString).getTime();
		} catch (ParseException e) {
			time = -1;
		}
		return time;
	}

	/**
	 * 获取某天现在的时分秒的时间(毫秒)
	 * 
	 * @param dateTime
	 *            现在的时间点(毫秒)
	 * @return 某天的时分秒(毫秒)
	 * @create 2013-01-22 杨小思
	 */
	public long getTime(long dateTime) {
		return dateTime - getZeroTime(dateTime);
	}

	/**
	 * 获取指定时间的当天零点时间的毫秒数
	 * 
	 * @param dateTime
	 *            指定时间点(毫秒)
	 * @return 毫秒数
	 * @create 2013-01-22 杨小思
	 */
	public long getZeroTime(long dateTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(dateTime);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	/**
	 * 获取格式化时间
	 * 
	 * @param time
	 *            指定时间(时分秒)
	 * @return 格式化时间(格式:HH:mm:ss)
	 * @create 2013-01-22 杨小思
	 */
	public String getFormatTime(long time) {
		SimpleDateFormat sdFormat = new SimpleDateFormat("HH:mm:ss");

		return sdFormat.format(time);
	}

	/**
	 * 获取格式化时间
	 * 
	 * @param time
	 *            指定时间
	 * @param format
	 *            时间格式
	 * @return 格式化时间
	 * @create 2013-01-22 杨小思
	 */
	public String getFormatTime(long time, String format) {
		SimpleDateFormat sdFormat = new SimpleDateFormat(format);

		return sdFormat.format(time);
	}

	/**
	 * 获取指定时间的下个小时开始到指定时间剩余的毫秒数
	 * 
	 * @param time
	 *            指定时间点
	 * @return 剩余的毫秒数
	 * @create 2013-6-9 庄宏晓
	 */
	public long getNextHourSurplus(long time) {
		return HOUR - time % HOUR;
	}

	public int getDayOfWeek(long time)
	{
		Date date = new Date(time);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * 获取指定的分钟剩余的时间
	 * <p>
	 * 当前时间 15:31<br/>
	 * 到指定时间XX：25<br/>
	 * getNextPointMinuteSurplus(25);<br>
	 * result 54 
	 * </p>
	 * @param pointMinute 指定的分钟
	 * @return
	 * @create 2014年8月6日 Cico.姜彪
	 */
	public final int getNextPointMinuteSurplus(int pointMinute) {
		final LocalDateTime nowTime = LocalDateTime.now();
		/*判断时间差*/
		int timeDiff = pointMinute - nowTime.getMinute();
		/*如果当前时间大于刷新时间则timeDiff分钟后执行*/
		if(timeDiff > 0) {
		//empty
		} else {
			/*如果当前时间小于刷新时间则时间间隔-timeDiff分钟后执行*/
			timeDiff = pointMinute * 60 - timeDiff;
		}
		return timeDiff;
	}
	
	/**
	 * 距离指定分钟时间的毫秒数
	 * @param pointMinute
	 * @return
	 * @create 2014年8月6日 Cico.姜彪
	 */
	public final long getNextPointMinuteSurplusMill(int pointMinute) {
		final LocalDateTime nowTime = LocalDateTime.now();
		final int minute = getNextPointMinuteSurplus(pointMinute);
		final int second = nowTime.getSecond();
		
		return (minute * 60 - second) * 1000;
	}
	
	/**
	 * 获取指定小时最近的一个时间点的距离时间/毫秒
	 * @param hours
	 * @return
	 * @create 2014年9月11日 Cico.姜彪
	 */
	public final long getNextPointHoursSurplus(int hour) {
		final List<Integer> hours = new ArrayList<>(1);
		hours.add(hour);
		return getNextPointHoursSurplus(hours);
	}
	
	/**
	 * 获取指定小时列表最近的一个时间点的距离时间/毫秒
	 * <p>
	 * list[12,15,18]<br/>
	 * 当前时间13:23<br/>
	 * getNextPointHoursSurplus(list);<br/>
	 * return 距离15点的时差毫秒数
	 * </p>
	 * @param hours
	 * @return
	 * @create 2014年8月6日 Cico.姜彪
	 */
	public final long getNextPointHoursSurplus(List<Integer> hours) {
		final LocalDateTime nowTime = LocalDateTime.now();
		final LocalDate nowDate = LocalDate.now();
		/*判断今天的时间是否到刷新时间*/
		LocalDateTime nextUpdateTime;
		for (Integer time : hours)
		{
			nextUpdateTime = LocalDateTime.of(nowDate, LocalTime.of(time, 0, 0));
			if (nextUpdateTime.compareTo(nowTime) == 1)
			{
				return nextUpdateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000 - System.currentTimeMillis();
			} else if (nextUpdateTime.compareTo(nowTime) == 0)
			{
				return nowTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000 - System.currentTimeMillis();
			}
		}
		/*判断明天的时间是否到刷新时间*/
		for (Integer time : hours)
		{
			nextUpdateTime = LocalDateTime.of(nowDate.plusDays(1), LocalTime.of(time, 0, 0));
			if (nextUpdateTime.compareTo(nowTime) == 1)
			{
				return nextUpdateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000 - System.currentTimeMillis();
			} else if (nextUpdateTime.compareTo(nowTime) == 0)
			{
				return nowTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000 - System.currentTimeMillis();
			}
		}
		/*都不存在则为当前时间*/
		return nowTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000 - System.currentTimeMillis();
	}
	
	/**
	 * 获取到周日24点（周一0点剩余的时间/毫秒）
	 * @return
	 * @create 2014年8月11日 Cico.姜彪
	 */
	public final long getMondayWeeHoursMill() {
		/*当前日期*/
		final LocalDate nowDate = LocalDate.now();
		/*当前时间*/
		final LocalDateTime nowTime = LocalDateTime.now();
		/*获取剩余天数*/
		final int surplusDay = nowDate.getDayOfWeek().getValue();
		/*下次凌晨的时间*/
		final LocalDateTime nextMondayWeeHoursTime = nowDate.plusDays(7 - surplusDay).atTime(0, 0);
		
		final long resultSecond = nextMondayWeeHoursTime.atZone(ZoneId.systemDefault()).toEpochSecond()
				- nowTime.atZone(ZoneId.systemDefault()).toEpochSecond();
		/*秒转化为毫秒*/
		return resultSecond * 1000;
	}
	
	
	/**
	 * 获取下个月一号0点剩余时间
	 * @return
	 * @create 2014年11月15日 Cico.姜彪
	 */
	public final long getFristDayZeroByMonth() {
		/*当前日期*/
		LocalDate nowDate = LocalDate.now();
		nowDate = nowDate.withDayOfMonth(1);
		nowDate = nowDate.plusMonths(1);
		return DateConvertUtil.getDateLocalMills(nowDate) - System.currentTimeMillis();
	}
}
