package org.deil.utils.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 日期时间工具
 *
 * @DATE 2023/03/15
 * @CODE Deil
 */
@UtilityClass
public class DateTimeUtil {

    private Logger log = LoggerFactory.getLogger(DateTimeUtil.class);

    //region TANG日期时间相关
    /**
     * minjdk
     *
     * @param format 非UTC
     * @return {@link String }
     * @time 2023/03/25
     */
    public static String parseMINJDK(DateTimeFormat format) {
        return getMINlocaldatetime().format(DateTimeFormatter.ofPattern(format.value));
    }

    /**
     * 1970-01-01T00:00:00.000
     *
     * @return {@link LocalDateTime }
     * @time 2023/04/17
     * @since 1.0.0
     */
    public static LocalDateTime getMINlocaldatetime() {
        return LocalDateTime.parse("1970-01-01T00:00:00.000");
    }

    /**
     * Thu Jan 01 00:00:00 CST 1970
     *
     * @return {@link Date }
     * @time 2023/04/17
     * @since 1.0.0
     */
    public static Date getMINdate() {
        return Date.from(getMINlocaldatetime().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 1970-01-01T00:00:00.000+08:00
     *
     * @return {@link DateTime }
     * @time 2023/04/17
     * @since 1.2.0
     */
    public static DateTime getMINjodatime() {
        return DateTime.parse("1970-01-01T00:00:00.000+08:00");
    }

    /**
     * @param dateTime
     * @return boolean
     * @time 2023/04/17
     * @since 1.2.0
     */
    public static boolean isBeforeJodaMIN(DateTime dateTime) {
        return dateTime.isBefore(getMINjodatime().toInstant());
    }

    /**
     * @param dateTime
     * @return boolean
     * @time 2023/04/17
     * @since 1.2.0
     */
    public static boolean isAfterJodaMIN(DateTime dateTime) {
        return dateTime.isAfter(getMINjodatime().toInstant());
    }

    /**
     * maxvalue jodatime 9999-12-31T23:59:59.999+08:00
     *
     * @return {@link DateTime }
     * @time 2023/04/17
     * @since 1.2.0
     */
    public static DateTime getMAXjoda() {
        return DateTime.parse("9999-12-31T23:59:59.999+08:00");
    }

    /**
     *      <p>yyyy/MM/ddTHH:mm:ss</p>
     *      <p>yyyy/MM/dd HH:mm:ss</p>
     *      <p>yyyy/M/dTHH:mm:ss</p>
     *      <p>yyyy/M/d HH:mm:ss</p>
     *      <p>yyyy/M/dTH:m:s</p>
     *      <p>yyyy/M/d H:m:s</p>
     *      <p>yyyy-MM-ddTHH:mm:ss</p>
     *      <p>yyyy-MM-dd HH:mm:ss</p>
     *      <p>yyyy-MM-ddTHHmmss</p>
     *      <p>yyyy-MM-dd HHmmss</p>
     *      <p>yyyyMMddTHH:mm:ss</p>
     *      <p>yyyyMMdd HH:mm:ss</p>
     *      <p>yyyyMMddTHHmmss</p>
     *      <p>yyyyMMdd HHmmss</p>
     *      <p>yyyyMMddHHmmss</p>
     *      <p>yyyy-MM-dd</p>
     *      <p>yyyyMMdd</p>
     *
     * @param parser
     * @param format
     * @return {@link Date }
     * @time 2023/03/24
     */
    public static Date parseDate(String parser, @Nullable DateTimeFormat format) {
        Date result = null;
        String defParser = StringUtils.defaultIfEmpty(parser, DateTimeFormat.DEFVALUE.value);
        if (StringUtils.isEmpty(parser)) {
            log.info("调用工具DateTimeUtil> 数据封装默认值:{}->{}", defParser, format.value);
        }

        if (defParser.matches("^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(-?|/?)(((0[13578]|1[02])(-?|/?)(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(-?|/?)(0[1-9]|[12][0-9]|30))|(02(-?|/?)(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))(-?|/?)02(-?|/?)29))(T+|\\s+|)([0-1]?[0-9]|2[0-3])(:?)([0-5][0-9])(:?)([0-5][0-9])$")) {
            try {
                result = Date.from(LocalDateTime.parse(defParser.replace("-", "").replace("/", "").replace("T", "").replace(" ", "").replace(":", ""), DateTimeFormatter.ofPattern("yyyyMMddHHmmss")).atZone(ZoneId.systemDefault()).toInstant());
                log.info("调用工具DateTimeUtil> 转换:{} -> {}", defParser, result);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }

        if (defParser.matches("^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(-?|/?)(((0[13578]|1[02])(-?|/?)(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(-?|/?)(0[1-9]|[12][0-9]|30))|(02(-?|/?)(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))(-?|/?)02(-?|/?)29))$")) {
            try {
                result=Date.from(LocalDateTime.of(LocalDate.parse(defParser.replace("-", "").replace("/", ""), DateTimeFormatter.ofPattern("yyyyMMdd")), LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant());
                log.info("调用工具DateTimeUtil> 转换:{} -> {}", defParser, result);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }

        if (defParser.matches("^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(/?)((([0]?[13578]|1[02])(/?)([0]?[1-9]|[12][0-9]|3[01]))|(([0]?[469]|11)(/?)([0]?[1-9]|[12][0-9]|30))|([0]?2(/?)([0]?[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))(/?)[0]?2(/?)29))(T+|\\s+|)([0-1]?[0-9]|2[0-3])(:?)([0]?[0-9]|[1-5]?[0-9])(:?)([0]?[0-9]|[1-5]?[0-9])$")) {
            try {
                DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy/M/d H:m:s");
                DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String defParser2 = LocalDateTime.parse(defParser.replace("T", " "), inputFormat).format(outputFormat);
                result = Date.from(LocalDateTime.parse(defParser2, outputFormat).atZone(ZoneId.systemDefault()).toInstant());
                log.info("调用工具DateTimeUtil> 转换:{} -> {}", defParser, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        throw new RuntimeException("调用工具DateTimeUtil> 转换[" + format.value + "]异常:" + parser + "格式不兼容");
    }

    public static String parseMAXVALUE(DateTimeFormat format) {
        return LocalDateTime.of(9999, 12, 31, 23, 59, 59)
                .format(DateTimeFormatter.ofPattern(format.value));
    }
    //endregion

    //region 当前日期时间
    //region 各类型当前日期时间
    /**
     * 返回当前日期时间
     */
    public static LocalDateTime nowLocalDateTime() {
        return LocalDateTime.now();
    }

    /**
     * 返回当前的日期
     */
    public static LocalDate nowLocalDate() {
        return LocalDate.now();
    }

    /**
     * 返回当前时间
     */
    public static LocalTime nowLocalTime() {
        return LocalTime.now();
    }

    /**
     * java.util.Date
     */
    public static Date nowDate() {
        return new Date();
    }

    /**
     * java.util.Date
     */
    public static synchronized Date nowSyncDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     * java.sql.Timestamp
     */
    public static Timestamp nowTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
    //endregion

    //region String类型当前时间日期
    /**
     * [yyyy-MM-dd HH:mm:ss、yyyy-MM-dd HH:mm:ss SSS、yyMMddHHmmss]
     */
    public static String now(DateTimeFormat dateTimeFormat) {
        return parse(LocalDateTime.now(), dateTimeFormat.value);
    }
    public static String now(String pattern) {
        return parse(LocalDateTime.now(), pattern);
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static String now() {
        return now(DateTimeFormat.DATETIME_NORMAL.value);
    }

    /**
     * yyyy-MM-dd HH:mm:ss SSS
     */
    public static String nowMsec(){
        return now(DateTimeFormat.LONG_DATETIME_NORMAL.value);
    }

    /**
     * yyMMddHHmmss
     */
    public static String nowUnsigned() {
        return now(DateTimeFormat.SHORT_DATETIME_UNSIGNED.value);
    }

    /**
     * [yyyy-MM-dd、yyMMdd、yyyy-MM]
     */
    public static String nowDay(String pattern) {
        return parse(LocalDate.now(), pattern);
    }

    /**
     * yyyy-MM-dd
     */
    public static String nowDay() {
        return nowDay(DateTimeFormat.DATE_NORMAL.value);
    }

    /**
     * yyMMdd
     */
    public static String nowDayUnsigned() {
        return nowDay(DateTimeFormat.SHORT_DATE_UNSIGNED.value);
    }

    /**
     * yyyy-MM
     */
    public static String nowMonth() {
        return nowDay(DateTimeFormat.MONTH_NORMAL.value);
    }

    /**
     * [HHmmss]
     */
    public static String nowTime(String pattern) {
        return parse(LocalTime.now(), pattern);
    }

    /**
     * HHmmss
     */
    public static String nowTime() {
        return nowTime(DateTimeFormat.TIME_UNSIGNED.value);
    }
    //endregion
    //endregion

    //region 转换日期时间
    @Deprecated
    public static <T> T parser(String parser) {
        return (T) "";
    }

    @Deprecated
    public static <T> String formatter(T formatter) {
        return "";
    }

    public static boolean parseExact(String parser, String format, Locale locale, Date out) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, locale);
        ZonedDateTime zonedDateTime = LocalDateTime.parse(parser, formatter).atZone(ZoneId.systemDefault());
        out = Date.from(zonedDateTime.toInstant());
        return true;
    }

    public static boolean tryParseExact(String parser, String format, Locale locale, Date out) {
        try {
            parseExact(parser, format, locale, out);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static Date tryParse(String time, Date timestamp) {
        Date result;
        try {
            result = parseDate(time);
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    public static Timestamp tryParse(String time, Timestamp timestamp) {
        Timestamp result;
        try {
            result = parseTimestamp(time);
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    public static final Date parseDate(String val, String valFormat, Locale locale){
        SimpleDateFormat sdf1 = new SimpleDateFormat(valFormat, locale);
        //SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = null;
        try {
            parse = sdf1.parse(val);
            //System.out.println(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //System.out.println(sdf2.format(parse));
        return parse;
    }

    /**
     * 包含UTC日期格式
     */
    public static final Date parseDate(String dateStr) {
        return parseDate(dateStr, DateTimeFormat.DATETIME_NORMAL);
    }

    /**
     * 包含UTC日期格式
     */
    public static final Date parseDate(String dateStr, String format) throws ParseException {
        Date toDate = new DateTime(new SimpleDateFormat(format)
                .parse((dateStr != null ? dateStr : DateTime.now().toString()).replace("T", " ")))
                .toDate();
        return toDate;
    }

    public static final Date parseDate2(String dateStr, String format){
        Date toDate = null;
        try {
            toDate = new SimpleDateFormat(format).parse(dateStr);
        } catch (ParseException pex) {
            return null;
        }
        return toDate;
    }

    public static String parse(Date date) {
        return parse(date, DateTimeFormat.DATETIME_NORMAL.value);
    }

    public static String parse(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    @Deprecated
    public static Timestamp parseTimestamp(String dateStr) {
        return Timestamp.valueOf(dateStr);
    }

    public static Timestamp parseTimestamp(String dateStr, DateTimeFormat dateTimeFormat) throws ParseException {
        return new Timestamp(new SimpleDateFormat(dateTimeFormat.value).parse(dateStr).getTime());
    }

    public static Timestamp parseTimestamp(String dateStr, String format) throws ParseException {
        return new Timestamp(new SimpleDateFormat(format).parse(dateStr).getTime());
    }

    public static Timestamp parseTimestamp(String dateStr, FormatType formatType) throws ParseException{
        return parseTimestamp(dateStr, formatType.value);
    }

    public static String parse(Timestamp timestamp) {
        return parse(timestamp, DateTimeFormat.DATETIME_NORMAL.value);
    }

    public static String parse(Timestamp timestamp, String pattern) {
        return new SimpleDateFormat(pattern).format(timestamp);
    }

    public static LocalDateTime parseLocalDateTime(String dateTimeStr, String pattern) {
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime parseLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime parseLocalDateTime(String dateTimeStr) {
        return parseLocalDateTime(dateTimeStr, DateTimeFormat.DATETIME_NORMAL.value);
    }

    public static LocalDateTime parseLongLocalDateTime(String longDateTimeStr){
        return parseLocalDateTime(longDateTimeStr, DateTimeFormat.LONG_DATETIME_NORMAL.value);
    }

    public static String parse(LocalDateTime localDateTime) {
        return parse(localDateTime, DateTimeFormat.DATETIME_NORMAL.value);
    }

    public static String parse(LocalDateTime localDateTime, String pattern) {
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate parseLocalDate(String dateStr, String pattern) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate parseLocalDate(String dateStr) {
        return parseLocalDate(dateStr, DateTimeFormat.DATE_NORMAL.value);
    }

    public static String parse(LocalDate localDate) {
        return parse(localDate, DateTimeFormat.DATE_NORMAL.value);
    }

    public static String parse(LocalDate localDate, String pattern) {
        return localDate.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalTime parseLocalTime(String timeStr, String pattern) {
        return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalTime parseLocalTime(String timeStr) {
        return parseLocalTime(timeStr, DateTimeFormat.TIME_UNSIGNED.value);
    }

    public static String parse(LocalTime localTime) {
        return parse(localTime, DateTimeFormat.TIME_UNSIGNED.value);
    }

    public static String parse(LocalTime localTime, String pattern) {
        return localTime.format(DateTimeFormatter.ofPattern(pattern, Locale.CHINA));
    }

    @Deprecated
    public static final String parse(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_NORMAL.value);
        Date date = sdf.parse(dateStr);
        return sdf.format(date);
    }
    //endregion

    //region 日期时间计算
    /**
     * 日期相隔秒
     */
    public static long periodHours(LocalDateTime startDateTime,LocalDateTime endDateTime){
        return Duration.between(startDateTime, endDateTime).get(ChronoUnit.SECONDS);
    }

    /**
     * 日期相隔天数
     */
    public static long periodDays(LocalDate startDate, LocalDate endDate) {
        return startDate.until(endDate, ChronoUnit.DAYS);
    }

    /**
     * 日期相隔周数
     */
    public static long periodWeeks(LocalDate startDate, LocalDate endDate) {
        return startDate.until(endDate, ChronoUnit.WEEKS);
    }

    /**
     * 日期相隔月数
     */
    public static long periodMonths(LocalDate startDate, LocalDate endDate) {
        return startDate.until(endDate, ChronoUnit.MONTHS);
    }

    /**
     * 日期相隔年数
     */
    public static long periodYears(LocalDate startDate, LocalDate endDate) {
        return startDate.until(endDate, ChronoUnit.YEARS);
    }

    /**
     * 是否当天
     */
    public static boolean isToday(LocalDate date) {
        return nowLocalDate().equals(date);
    }

    /**
     * 获取毫秒数
     */
    public static Long toEpochMilli(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 判断是否为闰年
     */
    public static boolean isLeapYear(LocalDate localDate){
        return localDate.isLeapYear();
    }
    //endregion

    /**
     * hour小时后日期时间
     */
    @Deprecated
    public static Date plusHourDate(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.HOUR_OF_DAY, hour);
        return cal.getTime();
    }

    /**
     * day天后日期时间
     */
    @Deprecated
    public static Date plusDayDate(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    /**
     * 获取month所在月的所有天
     * @param month 要查询的日期（如果为null 则默认为当前月）
     * @param dateFormat 返回日期的格式（如果为null 则返回yyyy-MM-dd 格式结果）
     * @return
     */
    public static List<String> getAllDaysOfMonthInString(Date month, DateFormat dateFormat) {
        List<String> rs = new ArrayList<String>();
        DateFormat df = DateFormat.getInstance();
        if (null == dateFormat) {
            df = new SimpleDateFormat(DateTimeFormat.DATE_NORMAL.value);
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
     * @param begin
     * @param end
     * @param dateFormat (如果为null 则返回yyyy-MM-dd格式的日期)
     * @return
     */
    public static List<String> getSpecifyDaysOfMonthInString(Date begin, Date end, DateFormat dateFormat) {
        DateFormat df = null;
        if (null == dateFormat) {
            df = new SimpleDateFormat(DateTimeFormat.DATE_NORMAL.value);
        }
        List<String> rs = new ArrayList<String>();
        List<Date> tmplist = getSpecifyDaysOfMonth(begin, end);
        for (Date date : tmplist) {
            rs.add(Optional.ofNullable(df).orElse(new SimpleDateFormat(DateTimeFormat.DATE_NORMAL.value)).format(date));
        }
        return rs;
    }

    /**
     * 获取指定日期区间所有天
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
     * 获得指定日期的前一天
     * @param specifiedDay YYYY_MM_DD_HH_MM_SS 格式
     * @param formatStr 日期类型
     * @return
     */
    public static String getSpecifiedDayBefore(String specifiedDay, String formatStr) {
        // 可以用new Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(DateTimeFormat.DATETIME_NORMAL.value).parse(specifiedDay);
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        String dayBefore = new SimpleDateFormat(formatStr).format(c.getTime());
        return dayBefore;
    }

    /**
     * 获得指定日期的后一天
     * @param specifiedDay YYYY_MM_DD_HH_MM_SS 格式
     * @param formatStr 日期类型
     * @return
     */
    public static String getSpecifiedDayAfter(String specifiedDay, String formatStr) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(DateTimeFormat.DATETIME_NORMAL.value).parse(specifiedDay);
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        String dayAfter = new SimpleDateFormat(formatStr).format(c.getTime());
        return dayAfter;
    }

    /**
     * 获取本周第一天的日期
     * @return
     */
    public static final String getWeekFirstDay() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_NORMAL.value);
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
        SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_NORMAL.value);
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
        SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_NORMAL.value);
        return sdf.format(cal.getTime());
    }

    public static final String getYesterdayEnd() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_NORMAL.value);
        return sdf.format(cal.getTime()) + " 23:59:59";
    }

    public static final String getCurrDayStart() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_NORMAL.value);
        return sdf.format(cal.getTime());
    }

    /**
     * 功能：获取指定月份的第一天<br/>
     *
     */
    public static final String getStartDayWithMonth(String month) throws ParseException {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_NORMAL.value);
        SimpleDateFormat mf = new SimpleDateFormat(DateTimeFormat.MONTH_NORMAL.value);
        Date date = mf.parse(month);
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 0);// 因为格式化时默认了DATE为本月第一天所以此处为0
        return sdf.format(calendar.getTime());
    }

    /**
     * 功能：获取指定月份的最后一天<br/>
     *
     */
    public static final String getEndDayWithMonth(String month) throws ParseException {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_NORMAL.value);
        SimpleDateFormat mf = new SimpleDateFormat(DateTimeFormat.MONTH_NORMAL.value);
        Date date = mf.parse(month);
        calendar.setTime(date);
        calendar.roll(Calendar.DATE, -1);// api解释roll()：向指定日历字段添加指定（有符号的）时间量，不更改更大的字段
        return sdf.format(calendar.getTime());
    }

    /**
     * 根据时间 yyyy-MM-dd 获取该日期是本月第几周
     *
     */
    public static final int getWeekIndexOfMonth(String dateStr) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_NORMAL.value);
        Date date = sdf.parse(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
        return weekOfMonth;
    }

    /**
     * 获取当前时间到指定时间距离多少秒 功能：<br/>
     *
     */
    public static final int getSecondToDesignationTime(String designationTime) {
        // 24小时制
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date toDate;
        try {
            toDate = dateFormat.parse(designationTime);
            int u = (int) ((toDate.getTime() - dateFormat.parse(now()).getTime()) / 1000);
            return u;
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        return 0;
    }

    public static final int getYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
        //return cal.get(cal.YEAR);
    }

    public static final int getMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH)+1;
    }

    public static final int getDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DATE);
    }

    public static Date plusDate(PlusType plusType, Date date, int plus) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(plusType.calender, plus);
        return cal.getTime();
    }

    public static Timestamp plusTimestamp(PlusType plusType, Timestamp timestamp, int plus) {
        Calendar c = Calendar.getInstance();
        if (timestamp != null) {
            c.setTime(timestamp);
        }
        c.add(plusType.calender, plus);
        //c.add(Calendar.DATE, 1);  //  加一  天
        //c.add(Calendar.MONTH, 1); //  加一个月
        //c.add(Calendar.YEAR,1);   //  加一  年
        return new Timestamp(c.getTimeInMillis());
    }

    public enum PlusType {
        YEAR(Calendar.YEAR),
        MONTH(Calendar.MONTH),
        DAY(Calendar.DATE),
        HOUR(Calendar.HOUR),
        MINUTE(Calendar.MINUTE),
        SECOND(Calendar.SECOND),
        ;
        private int calender;
        PlusType(int calender){
            this.calender = calender;
        }
    }
    public enum FormatType {
        year("yyyy"),
        month("MM"),
        day("dd"),

        /**
         * GMT：Greenwich Mean Time [1]
         * 格林威治标准时间 ; 英国伦敦格林威治定为0°经线开始的地方，地球每15°经度 被分为一个时区，共分为24个时区，相邻时区相差一小时；例: 中国北京位于东八区，GMT时间比北京时间慢8小时。
         *
         * UTC: Coordinated Universal Time
         * 世界协调时间；经严谨计算得到的时间，精确到秒，误差在0.9s以内， 是比GMT更为精确的世界时间
         *
         * DST: Daylight Saving Time
         * 夏季节约时间，即夏令时；是为了利用夏天充足的光照而将时间调早一个小时，北美、欧洲的许多国家实行夏令时；
         *
         * CST:
         * Central Standard Time (USA) UT-6:00 美国标准时间
         * Central Standard Time (Australia) UT+9:30 澳大利亚标准时间
         * China Standard Time UT+8:00 中国标准时间
         * Cuba Standard Time UT-4:00 古巴标准时间
         */
        CST("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"),
        ;

        private String value;

        FormatType(String value) {
            this.value = value;
        }
    }

    public enum DefaultDotNet {
        TIMESTAMP(Timestamp.valueOf("1900-01-01 00:00:00.000")),

        SQLDATE(java.sql.Date.valueOf("1900-01-01")),

        ;

        private final Object value;

        DefaultDotNet(Object value) {
            this.value = value;
        }
    }

}

