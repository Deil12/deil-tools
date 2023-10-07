package org.deil.utils.utils;

public enum DateTimeFormat {

    /**
     * 1900-01-01 00:00:00
     */
    DEFVALUE("1900-01-01 00:00:00"),

    /**
     * 19000101 00:00:00
     */
    DEFVALUE_UNSIGNED("19000101 00:00:00"),

    /**
     * 1900-01-01
     */
    DEFVALUE_DATE("1900-01-01"),

    /**
     * 19000101
     */
    DEFVALUE_DATE_UNSIGNED("19000101"),

    /**
     * 0001-01-01 00:00:00
     */
    MINVALUE("0001-01-01 00:00:00"),

    /**
     * 9999-12-31 23:59:59
     */
    MAXVALUE("9999-12-31 23:59:59"),

    /**
     * HHmmss
     */
    TIME_UNSIGNED("HHmmss"),

    /**
     * yyyy
     */
    YEAR_NORMAL("yyyy"),

    /**
     * yyyy-MM
     */
    MONTH_NORMAL("yyyy-MM"),

    /**
     * yyyyMM
     */
    MONTH_UNSIGNED("yyyyMM"),

    /**
     * yyMMdd
     */
    SHORT_DATE_UNSIGNED("yyMMdd"),

    /**
     * YYMMDD
     */
    SHORT_DATE_UNSIGNED_UP("YYMMDD"),

    /**
     * yyyy-MM-dd
     */
    DATE_NORMAL("yyyy-MM-dd"),

    /**
     * yyyyMMdd
     */
    DATE_UNSIGNED("yyyyMMdd"),

    /**
     * YYYYMMDD
     */
    DATE_UNSIGNED_UP("YYYYMMDD"),

    /**
     * yyMMddHHmmss
     */
    SHORT_DATETIME_UNSIGNED("yyMMddHHmmss"),

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    DATETIME_NORMAL("yyyy-MM-dd HH:mm:ss"),

    /**
     * yyyy/MM/dd HH:mm:ss
     */
    DATETIME_SEPARATOR("yyyy/MM/dd HH:mm:ss"),

    /**
     * yyyy/M/d H:m:s
     */
    DATETIME_SEPARATOR_SHORT("yyyy/M/d H:m:s"),

    /**
     * yyyy/M/d HH:mm:ss
     */
    DATETIME_SEPARATOR_DATE_SHORT("yyyy/M/d HH:mm:ss"),

    /**
     * yyyyMMdd HH:mm:ss
     */
    DATETIME_DATE_UNSIGNED("yyyyMMdd HH:mm:ss"),

    /**
     * yyyyMMddTHHMMSS
     */
    DATETIME_UNSIGNED_UTC("yyyyMMddTHHMMSS"),

    /**
     * YYYYMMDDHHMMSS
     */
    DATETIME_UNSIGNED_UP("YYYYMMDDHHMMSS"),

    /**
     * yyyy-MM-dd HH:mm:ss SSS
     */
    LONG_DATETIME_NORMAL("yyyy-MM-dd HH:mm:ss SSS"),

    /**
     * yyyyMMddHHmmssSSS
     */
    LONG_DATETIME_UNSIGNED("yyyyMMddHHmmssSSS"),

    /**
     * YYYYMMDDHHMMSSSSS
     */
    LONG_DATETIME_UNSIGNED_UP("YYYYMMDDHHMMSSSSS"),

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
    public String value;

    DateTimeFormat(String value) {
        this.value = value;
    }

}
