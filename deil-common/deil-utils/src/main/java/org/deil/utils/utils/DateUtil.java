package org.deil.utils.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 日期处理
 *
 * @DATE 2023/02/06
 * @CODE Deil
 */
@UtilityClass
public class DateUtil {
	/**
	 * 时间转字符串（国际化）
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String dateToStringByEN(Date date, String pattern){
		if (date == null){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
		String format = sdf.format(date);
		return format;
	}
	public static String dateToStringByLocale(Date date, String pattern){
		if (date == null){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINESE);
		String format = sdf.format(date);
		return format;
	}
	//region 构造
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String YYYYMMDD_HH_MM_SS = "yyyy/MM/dd HH:mm:SS";

    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYYDDMMM = "yyyyddMMM";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";

    public static final String HHmmss = "HHmmss";
    public static final String HHMMSSSSS = "HHmmssSSS";

    public static final String YYYYMM = "yyyyMM";

    public static final String MINVALUE = "0001/1/1 0:00:00";

    public static final String MAXVALUE = "9999/12/31 23:59:59";
    //endregion

    /**
     * 总的
     */
    public static final String[] PARSE_PATTERNS = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss",
            "MM dd yyyy", "MM dd yyyy h:mma", "MM dd yyyy hh:mma"};

    /**
     * 日期
     */
    public static final String[] PARSE_PATTERNS_DATE = {
            "yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd"};

    /**
     * 时分秒
     */
    public static final String[] PARSE_PATTERNS_TIME = {"HH:mm:ss"};


    /**
     * 对标.net DateTime.MinValue
     *
     * @return {@link Object }
     * @throws ParseException 解析异常
     * @time 2023/02/06
     */
    @SneakyThrows
    public static Object MinValueObj() {
        return new SimpleDateFormat(YYYYMMDD_HH_MM_SS).parseObject(MINVALUE);
    }

    @SneakyThrows
    public static Date MinValueDate() {
        return (Date) MinValueObj();
    }

    public static Timestamp MinValueTimeStamp() throws ParseException {
        return new Timestamp(MinValueDate().getTime());
    }

	@Deprecated
	public static DateTime MinValueDateTime() throws ParseException {
		return new DateTime(MinValueDate());
	}

    /**
     * 将字符解析为时间类型
     *
     * @param dateStr 时间字符
     * @return 时间
     */
    public static Date parseDateTime(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }

        Date date = null;
        try {
            // 日期
            if (dateStr.length() == 10) {
                date = DateUtils.parseDate(dateStr, PARSE_PATTERNS_DATE);
            }
            // 完整时间
            if (dateStr.length() == 19) {
                date = DateUtils.parseDate(dateStr, PARSE_PATTERNS);
            }
            // 时分秒
            if (dateStr.length() == 8) {
                date = DateUtils.parseDate(dateStr, PARSE_PATTERNS_TIME);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("解析日期异常，日期串为:" + dateStr);
        }

        return date;
    }


    /**
     * 对标.net DateTime.MaxValue
     *
     * @return {@link Object }
     * @throws ParseException 解析异常
     * @time 2023/02/06
     */
    public static Object MaxValueObj() throws ParseException {
        return new SimpleDateFormat(YYYYMMDD_HH_MM_SS).parseObject(MAXVALUE);
    }

    public static Date MaxValueDate() throws ParseException {
        return (Date) MaxValueObj();
    }

    public static Timestamp MaxValueTimeStamp() throws ParseException {
        return new Timestamp(MaxValueDate().getTime());
    }

	@Deprecated
	public static DateTime MaxValueDateTime() throws ParseException {
		return new DateTime(MaxValueDate());
	}
	//endregion

	//region 各类当前时间
	public static Date nowDate() {
		return new Date();
	}

	public static Timestamp nowTimestamp() {
		return new Timestamp(nowDate().getTime());
	}

	public static DateTime nowDateTime() {
		return DateTime.now();
	}

	public static LocalDateTime nowLocalDateTime() {
		return LocalDateTime.now();
	}
	//endregion

    public static String toDateTime(LocalDateTime date) {
        return toDateTime(date, YYYY_MM_DD_HH_MM_SS);
    }

    public static String toDateTime(LocalDateTime dateTime, String pattern) {
        return dateTime.format(DateTimeFormatter.ofPattern(pattern, Locale.SIMPLIFIED_CHINESE));
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }


    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }


    public static String toDateText(LocalDate date, String pattern) {
        if (date == null || pattern == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(pattern, Locale.SIMPLIFIED_CHINESE));
    }

    /**
     * 从给定的date，加上hour小时 求指定date时间后hour小时的时间
     *
     * @param date 指定的时间
     * @param hour 多少小时后
     * @return
     */
    public static Date addExtraHour(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.HOUR_OF_DAY, hour);
        return cal.getTime();
    }

    /**
     * 从给定的date，加上increase天
     *
     * @param date
     * @param increase
     * @return
     */
    public static Date increaseDay2Date(Date date, int increase) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.DAY_OF_MONTH, increase);
        return cal.getTime();
    }

    /**
     * 把字符串日期默认转换为yyyy-mm-dd格式的Data对象
     *
     * @param strDate
     * @return
     */
    public static Date format(String strDate, String format) {
        Date d = null;
        if (null == strDate || "".equals(strDate))
            return null;
        else
            try {
                d = getFormatter(format).parse(strDate);
            } catch (ParseException pex) {
                return null;
            }
        return d;
    }

    /**
     * 获取一个简单的日期格式化对象
     *
     * @return 一个简单的日期格式化对象
     */
    private static SimpleDateFormat getFormatter(String parttern) {
        return new SimpleDateFormat(parttern);
    }

    /**
     * 格式化当前org.joda.time.DateTime
     *
     * @return {@link String }
     * @time 2023/02/07
     */
    public static String formatNow() {
        return DateTime.now().toString(YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 格式化当前org.joda.time.DateTime
     *
     * @param format 格式
     * @return {@link String }
     * @time 2023/02/07
     */
    public static String formatNow(String format) {
        String s = Optional.ofNullable(format).orElse(YYYY_MM_DD_HH_MM_SS);
        return DateTime.now().toString(s);
    }

    /**
     * 获取month所在月的所有天
     *
     * @param month      要查询的日期（如果为null 则默认为当前月）
     * @param dateFormat 返回日期的格式（如果为null 则返回yyyy-MM-dd 格式结果）
     * @return
     */
    public static List<String> getAllDaysOfMonthInString(Date month, DateFormat dateFormat) {
        List<String> rs = new ArrayList<String>();
        DateFormat df = null;
        if (null == dateFormat) {
            df = new SimpleDateFormat("yyyy-MM-dd");
        }
        Calendar cad = Calendar.getInstance();
        if (null != month) {
            cad.setTime(month);
        }
        int day_month = cad.getActualMaximum(Calendar.DAY_OF_MONTH); // 获取当月天数
        for (int i = 0; i < day_month; i++) {
            cad.set(Calendar.DAY_OF_MONTH, i + 1);
            rs.add(df.format(cad.getTime()));

        }
        return rs;
    }

    /**
     * 获取month所在月的所有天
     *
     * @param month 要查询的日期（如果为null 则默认为当前月）
     * @return 日期List
     */
    public static List<Date> getAllDaysOfMonth(Date month) {
        List<Date> rs = new ArrayList<Date>();
        Calendar cad = Calendar.getInstance();
        if (null != month) {
            cad.setTime(month);
        }
        int day_month = cad.getActualMaximum(Calendar.DAY_OF_MONTH); // 获取当月天数
        for (int i = 0; i < day_month; i++) {
            cad.set(Calendar.DAY_OF_MONTH, i + 1);
            rs.add(cad.getTime());

        }
        return rs;
    }

    /**
     * 获取指定日期区间所有天
     *
     * @param begin
     * @param end
     * @param dateFormat (如果为null 则返回yyyy-MM-dd格式的日期)
     * @return
     */
    public static List<String> getSpecifyDaysOfMonthInString(Date begin, Date end, DateFormat dateFormat) {
        DateFormat df = null;
        if (null == dateFormat) {
            df = new SimpleDateFormat("yyyy-MM-dd");
        }
        List<String> rs = new ArrayList<String>();
        List<Date> tmplist = getSpecifyDaysOfMonth(begin, end);
        for (Date date : tmplist)
            rs.add(df.format(date));
        return rs;
    }

    /**
     * 获取指定日期区间所有天
     *
     * @param begin
     * @param end
     * @return
     */
    public static List<Date> getSpecifyDaysOfMonth(Date begin, Date end) {
        List<Date> rs = new ArrayList<Date>();
        Calendar cad = Calendar.getInstance();
        int day_month = -1;
        if (null == begin) {// 设置开始日期为指定日期
            // day_month = cad.getActualMaximum(Calendar.DAY_OF_MONTH); // 获取当月天数
            cad.set(Calendar.DAY_OF_MONTH, 1);// 设置开始日期为当前月的第一天
            begin = cad.getTime();
        }
        cad.setTime(begin);
        if (null == end) {// 如果结束日期为空 ，设置结束日期为下月的第一天
            day_month = cad.getActualMaximum(Calendar.DAY_OF_MONTH); // 获取当月天数
            cad.set(Calendar.DAY_OF_MONTH, day_month + 1);
            end = cad.getTime();
        }
        cad.set(Calendar.DAY_OF_MONTH, 1);// 设置开始日期为当前月的第一天
        Date tmp = begin;
        int i = 1;
        while (true) {
            cad.set(Calendar.DAY_OF_MONTH, i);
            i++;
            tmp = cad.getTime();
            if (tmp.before(end)) {
                rs.add(cad.getTime());
            } else {
                break;
            }
        }
        return rs;
    }

    /**
     * 获取当前日期
     *
     * @return 一个包含年月日的<code>Date</code>型日期
     */
    public static synchronized Date getCurrDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * 获取当前完整时间,样式: yyyy－MM－dd hh:mm:ss
     *
     * @return 一个包含年月日时分秒的<code>String</code>型日期。yyyy-MM-dd hh:mm:ss
     */
    public static String getCurrDateTimeStr() {
        return format(getCurrDate(), YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获得指定日期的前一天
     *
     * @param specifiedDay YYYY_MM_DD_HH_MM_SS 格式
     * @param formatStr    日期类型
     * @return
     */
    public static String getSpecifiedDayBefore(String specifiedDay, String formatStr) {// 可以用new
        // Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        String dayBefore = new SimpleDateFormat(formatStr).format(c.getTime());
        return dayBefore;
    }

    /**
     * 获得指定日期的后一天
     *
     * @param specifiedDay YYYY_MM_DD_HH_MM_SS 格式
     * @param formatStr    日期类型
     * @return
     */
    public static String getSpecifiedDayAfter(String specifiedDay, String formatStr) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        String dayAfter = new SimpleDateFormat(formatStr).format(c.getTime());
        return dayAfter;
    }

    /**
     * 获取本周第一天的日期
     *
     * @return
     */
    public static final String getWeekFirstDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 2;
        cal.add(Calendar.DATE, -day_of_week);
        return sdf.format(cal.getTime());
    }

    /**
     * 获取当前月的第一天
     *
     * @return
     */
    public static final String getCurrentMonthFirstDay() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 当前月的第一天
        cal.set(GregorianCalendar.DAY_OF_MONTH, 1);
        Date beginTime = cal.getTime();
        return sdf.format(beginTime);
    }

    /**
     * 获取昨天开始时间
     *
     * @return
     */
    public static final String getYesterdayStart() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    public static final String getYesterdayEnd() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime()) + " 23:59:59";
    }

    public static final String getCurrDayStart() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    /**
     * 功能：获取指定月份的第一天<br/>
     */
    public static final String getStartDayWithMonth(String month) throws ParseException {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat mf = new SimpleDateFormat("yyyy-MM");
        Date date = mf.parse(month);
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 0);// 因为格式化时默认了DATE为本月第一天所以此处为0
        return sdf.format(calendar.getTime());
    }

    /**
     * 功能：获取指定月份的最后一天<br/>
     */
    public static final String getEndDayWithMonth(String month) throws ParseException {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat mf = new SimpleDateFormat("yyyy-MM");
        Date date = mf.parse(month);
        calendar.setTime(date);
        calendar.roll(Calendar.DATE, -1);// api解释roll()：向指定日历字段添加指定（有符号的）时间量，不更改更大的字段
        return sdf.format(calendar.getTime());
    }

    public static final String formatYearMonthDay(String dateStr) throws ParseException {
        if (StringUtils.isNotBlank(dateStr)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateStr);
            return sdf.format(date);
        } else {
            return "";
        }
    }

    /**
     * 功能：<br/>
     * 根据时间 yyyy-MM-dd 获取该日期是本月第几周
     */
    public static final int getWeekIndexOfMonth(String dateStr) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
        return weekOfMonth;
    }

    /**
     * 获取当前时间到指定时间距离多少秒 功能：<br/>
     */
    public static final int getSecondToDesignationTime(String designationTime) {
        // 24小时制
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date toDate;
        try {
            toDate = dateFormat.parse(designationTime);
            int u = (int) ((toDate.getTime() - dateFormat.parse(DateUtil.getCurrDateTimeStr()).getTime()) / 1000);
            return u;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static final int getYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(cal.YEAR);
    }

    public static final int getMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(cal.MONTH) + 1;
    }

    public static final int getDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(cal.DATE);
    }

}
