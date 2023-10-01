package org.deil.utils.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.deil.utils.log.LogUtil;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class BeanUtil {

    /**
     * 定义换行
     */
    public static final String CTL = StringUtils.CR + StringUtils.LF;

    public static final String UNKNOWN_IP = "unknown";


    /**
     * 获取目标主机的ip
     *
     * @return ip地址
     */
    public static String getRemoteHost() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String ip = "";
        if (Objects.nonNull(servletRequestAttributes)) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        }

        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * 参考文章： http://developer.51cto.com/art/201111/305181.htm
     * <p>
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * <p>
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     * <p>
     * 用户真实IP为： 192.168.1.110
     *
     * @param request 请求对象
     * @return 真实IP
     */
    public static String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    public static String getRequestUrl() {
        String requestUrl = null;
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(servletRequestAttributes)) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            requestUrl = request.getRequestURI();
        }
        return requestUrl;
    }


    /**
     * 截取字符（适配函数）
     * <p>
     * 在.net中，比方说SubString(2, 4)，函数表示从第3位(从0开始计算)开始，截取往后4位(包括自身)
     * </p>
     *
     * @param text       文本内容
     * @param startIndex 截取位置
     * @param length     截取数量
     * @return 结果文本
     */
    public static String adaptNetSubString(String text, int startIndex, int length) {
        return text.substring(startIndex, startIndex + length);
    }

    /**
     * 截取字符（适配函数）
     * <p>
     * 单个形参，也就是不截取范围，而是单独截取某个字符，参数即为字符索引
     * </p>
     *
     * @param text       文本内容
     * @param startIndex 截取位置
     * @return 结果文本
     */
    public static String adaptNetSubString(String text, int startIndex) {
        return text.substring(startIndex);
    }


    ///**
    // * 分割内容（适配函数）
    // * <p> 在.net中，string.Split(ArraySplit, StringSplitOptions.RemoveEmptyEntries) </p>
    // * <p>这样的函数效果是会去除分割后的数组中的长度为0的元素，但是有长度的空串或者非空串是不去除的</p>
    // *
    // * @param content   内容
    // * @param separator 分隔符
    // * @return 去除空元素后的所有报文
    // */
    // public static List<String> splitContentRemoveEmpty(String content, String separator) {
    //    // 分割
    //    String[] array = content.split(separator);
    //    // 去除空元素（长度不为为 0或 不为null）
    //    return Arrays.stream(array).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
    //}


    ///**
    // * 分割内容（适配函数）
    // * <p> 在.net中，string.Split(ArraySplit, StringSplitOptions.RemoveEmptyEntries) </p>
    // * <p>这样的函数效果是会去除分割后的数组中的长度为0的元素，但是有长度的空串或者非空串是不去除的</p>
    // *
    // * @param content   内容
    // * @param separator 分隔符
    // * @return 去除空元素后的所有报文
    // */
    // public static String[] splitContentRemoveEmptyArray(String content, String separator) {
    //    // 分割
    //    String[] array = content.split(separator);
    //    // 去除空元素（长度不为为 0或 不为null）
    //    return Arrays.stream(array).filter(StringUtils::isNotEmpty).toArray(String[]::new);
    //}


    ///**
    // * 分割内容（适配函数）
    // * <p> 在.net中，string.Split(ArraySplit, StringSplitOptions.None) </p>
    // * <p>这样的函数效果是不对内容做其他操作，但是如果分隔符后没有字符，也会附带一个空在数组中，</p>
    // * <p>也就是说，有多少个分隔符存在字符串，分割数组就要有分隔符数+1的元素存在</p>
    // *
    // * @param content   内容
    // * @param separator 分隔符
    // * @return 去除空元素后的所有报文
    // */
    // public static String[] splitContentNoneArray(String content, String separator) {
    //    List<String> list = splitContentNone(content, separator);
    //    return list.toArray(new String[0]);
    //}


    ///**
    // * 分割内容（适配函数）
    // * <p> 在.net中，string.Split(ArraySplit, StringSplitOptions.None) </p>
    // * <p>这样的函数效果是不对内容做其他操作，但是如果分隔符后没有字符，也会附带一个空在数组中，</p>
    // * <p>也就是说，有多少个分隔符存在字符串，分割数组就要有分隔符数+1的元素存在</p>
    // *
    // * @param content   内容
    // * @param separator 分隔符
    // * @return 去除空元素后的所有报文
    // */
    // public static List<String> splitContentNone(String content, String separator) {
    //    // 分割
    //    String[] array = content.split(separator);
    //
    //    // 计算分隔符出现次数，加1，因为如果有2个分隔符，那就表示要有3份数据
    //    int count = ReUtil.count(separator, content);
    //
    //    // 如果数量为0，表示没有，直接返回
    //    if (count > 0) {
    //        count++;
    //    }
    //
    //    List<String> resultList = new ArrayList<>(Arrays.asList(array));
    //
    //    // 如果长度不一致，则可能存在空没有分隔到
    //    if (array.length < count) {
    //        // 差值
    //        int deviation = count - array.length;
    //        for (int i = 0; i < deviation; i++) {
    //            resultList.add("");
    //        }
    //    }
    //
    //    return resultList;
    //}


    /**
     * 月份转换（英文大写转数值）
     *
     * @param strMouth 月份英文大写
     * @return 结果
     */
    public static String convertMonth(String strMouth) {
        String month;
        switch (strMouth) {
            case "JAN":
                month = "01";
                break;
            case "FEB":
                month = "02";
                break;
            case "MAR":
                month = "03";
                break;
            case "APR":
                month = "04";
                break;
            case "MAY":
                month = "05";
                break;
            case "JUN":
                month = "06";
                break;
            case "JUL":
                month = "07";
                break;
            case "AUG":
                month = "08";
                break;
            case "SEP":
                month = "09";
                break;
            case "OCT":
                month = "10";
                break;
            case "NOV":
                month = "11";
                break;
            case "DEC":
                month = "12";
                break;
            default:
                month = "00";
                break;
        }
        return month;
    }


    /**
     * 去除对象中字符串前后空格 (支持Map、JavaBean、List)
     *
     * @param obj 对象
     */
    public static void trimObjParam(Object obj) {
        if (Objects.isNull(obj)) {
            return;
        }

        // 给 List、JavaBean 字符串去除前后空格
        if (obj instanceof Collection) {
            for (Object el : (Collection<?>) obj) {
                if (el instanceof Map) {
                    // 处理 Map
                    trimMap(el);
                } else {
                    // 处理 POJO
                    trimParam(el);
                }
            }
        } else {
            if (obj instanceof Map) {
                // 处理 Map
                trimMap(obj);
            } else {
                // 处理 POJO
                trimParam(obj);
            }
        }
    }


    @SuppressWarnings("unchecked")
    public static void trimMap(Object obj) {
        Map<String, Object> map = (Map<String, Object>) obj;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            // 获取值
            Object value = entry.getValue();

            // 类型判断
            if (value instanceof String) {
                // 覆盖原值
                map.put(entry.getKey(), ((String) value).trim());
            }
        }
    }


    /**
     * 将对象中所有String类型字段去除前后空格
     *
     * @param obj 类
     */
    public static void trimParam(Object obj) {
        Assert.notNull(obj, "[去除字符串空格] 对象参数不能为空!");
        Class<?> clz = obj.getClass();
        try {
            for (Field field : clz.getDeclaredFields()) {
                ReflectionUtils.makeAccessible(field);
                String name = field.getType().getName();
                if (!"java.lang.String".equals(name)) {
                    continue;
                }
                String value = (String) field.get(obj);
                if (Objects.isNull(value)) {
                    continue;
                }
                field.set(obj, value.trim());
            }
        } catch (Exception e) {
            log.error("\n{}", LogUtil.stacktraceToFiveLineString(e));
            // 2023/5/27 去除多余日志
            // e.printStackTrace();
        }
    }


    public static void trimPropertySelective(Object obj, String... properties) {
        if (Objects.isNull(obj)) {
            return;
        }

        // 给 List、JavaBean 字符串去除前后空格
        if (obj instanceof Collection) {
            for (Object el : (Collection<?>) obj) {
                if (el instanceof Map) {
                    // 处理 Map
                    trimMapSelective(el, properties);
                } else {
                    // 处理 POJO
                    trimParamSelective(el, properties);
                }
            }
        } else {
            if (obj instanceof Map) {
                // 处理 Map
                trimMapSelective(obj, properties);
            } else {
                // 处理 POJO
                trimParamSelective(obj, properties);
            }
        }
    }

    private static void trimParamSelective(Object element, String... properties) {
        Class<?> clz = element.getClass();
        try {
            for (Field field : clz.getDeclaredFields()) {
                ReflectionUtils.makeAccessible(field);

                String fieldName = field.getName();
                boolean ignore = Objects.nonNull(properties)
                        && Arrays.stream(properties).anyMatch(el -> SqlUtil.isMatch(el.trim(), fieldName));

                String name = field.getType().getName();
                if (!"java.lang.String".equals(name)) {
                    continue;
                }
                String value = (String) field.get(element);
                if (Objects.isNull(value)) {
                    continue;
                }
                field.set(element, value.trim());
            }
        } catch (Exception e) {
            log.error("\n{}", LogUtil.stacktraceToFiveLineString(e));
            // 2023/5/27 去除多余日志
            // e.printStackTrace();
        }

    }


    @SuppressWarnings({"unchecked"})
    private static void trimMapSelective(Object element, String... properties) {
        Map<String, Object> map = (Map<String, Object>) element;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            // 获取值
            Object value = entry.getValue();

            // 类型判断
            if (value instanceof String) {
                boolean ignore = Objects.nonNull(properties)
                        && Arrays.stream(properties).anyMatch(el -> SqlUtil.isMatch(el.trim(), entry.getKey()));

                if (ignore) {
                    // 覆盖原值
                    map.put(entry.getKey(), ((String) value).trim());
                }
            }
        }
    }


    /**
     * 给 List 或 Bean 中的null值置为空串
     *
     * @param obj 对象 [List 或 Bean]
     */
    public static void fillBlankParam(Object obj) {
        fillBlankParam(obj, new String[]{});
    }


    /**
     * 给 List 或 Bean 中的null值置为空串
     *
     * @param obj              对象 [List 或 Bean]
     * @param ignoreProperties 被忽略处理的属性
     */
    public static void fillBlankParam(Object obj, String[] ignoreProperties) {
        if (Objects.isNull(obj) || obj instanceof Map) {
            return;
        }

        // 给 List、JavaBean 设置空串
        if (obj instanceof Collection) {
            for (Object o : (Collection<?>) obj) {
                // 暂时无法处理 Map，因为 null值存储在Map，并不清楚具体类型
                fillBeanBlankParam(o, ignoreProperties);
            }
        } else {
            fillBeanBlankParam(obj, ignoreProperties);
        }
    }


    /**
     * 将对象中所有String类型为 null的设置为空串
     *
     * @param obj 对象
     */
    private static void fillBeanBlankParam(Object obj, String[] ignoreProperties) {
        if (Objects.isNull(obj)) {
            return;
        }

        Class<?> clz = obj.getClass();
        try {
            for (Field field : getAllField(clz)) {
                ReflectionUtils.makeAccessible(field);

                String fieldName = field.getName();
                boolean ignore = Objects.nonNull(ignoreProperties)
                        && Arrays.stream(ignoreProperties).anyMatch(el -> SqlUtil.isMatch(el.trim(), fieldName));

                String name = field.getType().getName();
                if (!"java.lang.String".equals(name) || ignore) {
                    continue;
                }
                String value = (String) field.get(obj);
                if (Objects.isNull(value)) {
                    field.set(obj, "");
                }
            }
        } catch (Exception e) {
            log.error("\n{}", LogUtil.stacktraceToFiveLineString(e));
            // 2023/5/27 去除多余日志
            // e.printStackTrace();
        }
    }


    /**
     * 给指定的属性做null转空串（字符串类型）
     *
     * @param obj        对象
     * @param properties 指定属性
     */
    public static void fillBlankParamSelective(Object obj, String... properties) {
        if (Objects.isNull(obj) || obj instanceof Map) {
            return;
        }

        // 给 List、JavaBean 设置空串
        if (obj instanceof Collection) {
            for (Object o : (Collection<?>) obj) {
                // 暂时无法处理 Map，因为 null值存储在Map，并不清楚具体类型
                fillBeanBlankParamSelective(o, properties);
            }
        } else {
            fillBeanBlankParamSelective(obj, properties);
        }
    }


    private static void fillBeanBlankParamSelective(Object obj, String[] properties) {
        if (Objects.isNull(obj)) {
            return;
        }

        try {
            for (Field field : getAllField(obj.getClass())) {
                ReflectionUtils.makeAccessible(field);

                String fieldName = field.getName();
                boolean needHandle = Objects.nonNull(properties)
                        && Arrays.stream(properties).anyMatch(el -> SqlUtil.isMatch(el.trim(), fieldName));

                String name = field.getType().getName();
                if (!"java.lang.String".equals(name) || !needHandle) {
                    continue;
                }

                if (Objects.isNull(field.get(obj))) {
                    ReflectionUtils.setField(field, obj, StringUtils.EMPTY);
                }
            }
        } catch (Exception e) {
            log.error("\n{}", LogUtil.stacktraceToFiveLineString(e));
            // 2023/5/27 去除多余日志
            // e.printStackTrace();
        }
    }


    /**
     * 时间转换（将原有的时间转换成另一种格式的时间，简单来说就是要清除某些内容，比如要清除时分秒等）
     *
     * @param date    时间
     * @param pattern 格式
     * @return 转换后的时间
     */
    //public static Date transformDate(Date date, String pattern) {
    //    return cn.hutool.core.date.DateUtil.parse(cn.hutool.core.date.DateUtil.format(date, pattern), pattern);
    //}

    /**
     * 解析时间，附带地区（不同地区时间表现方式不一致）
     *
     * @param dateTime 时间字符串
     * @param locale   地区
     * @param patten   日期格式模式
     * @return 解析后的时间
     */
    @SneakyThrows
    public static Date parseLocale(String dateTime, Locale locale, String patten) {
        return DateUtils.parseDate(dateTime, locale, patten);
    }

    /**
     * 格式化时间，附带地区（不同地区时间表现方式不一致）
     *
     * @param date   时间
     * @param locale 地区
     * @param patten 日期格式模式
     * @return 解析后的时间字符串
     */
    public static String formatLocale(Date date, Locale locale, String patten) {
        return DateFormatUtils.format(date, patten, locale);
    }

    /**
     * 计算日期之间的天数差，以参数一 mainDate 为主
     *
     * @param mainDate 主时间
     * @param value    要计算的时间
     * @return 天数差值
     */
    //public static long calculateDifferenceDays(Date mainDate, Date value) {
    //    // 计算时间差
    //    long l = cn.hutool.core.date.DateUtil.betweenDay(mainDate, value, false);
    //    // 判断主时间是否小于计算时间，如果小于则置为负数
    //    if (mainDate.compareTo(value) < 0) {
    //        l = -l;
    //    }
    //
    //    return l;
    //}

    /**
     * 计算时间之间的时差，以参数一 mainDate 为主
     *
     * @param mainDate 主时间
     * @param value    要计算的时间
     * @return 时差值
     */
    //public static long calculateDifferenceHours(Date mainDate, Date value) {
    //    // 计算时间差
    //    long l = cn.hutool.core.date.DateUtil.between(mainDate, value, DateUnit.HOUR);
    //    // 判断主时间是否小于计算时间，如果小于则置为负数
    //    if (mainDate.compareTo(value) < 0) {
    //        l = -l;
    //    }
    //
    //    return l;
    //}

    /**
     * 计算时间之间的分钟差，以参数一 mainDate 为主
     *
     * @param mainDate 主时间
     * @param value    要计算的时间
     * @return 分差值
     */
    //public static long calculateDifferenceMinutes(Date mainDate, Date value) {
    //    // 计算时间差
    //    long l = cn.hutool.core.date.DateUtil.between(mainDate, value, DateUnit.MINUTE);
    //    // 判断主时间是否小于计算时间，如果小于则置为负数
    //    if (mainDate.compareTo(value) < 0) {
    //        l = -l;
    //    }
    //
    //    return l;
    //}

    /**
     * 替换危险字符
     *
     * @param obj 对象
     */
    public static void replaceChar(Object obj) {
        Class<?> clz = obj.getClass();
        try {
            for (Field field : clz.getDeclaredFields()) {
                ReflectionUtils.makeAccessible(field);
                // 过滤非字符串、空
                String name = field.getType().getName();
                if (!"java.lang.String".equals(name)) {
                    continue;
                }
                String value = (String) field.get(obj);
                if (Objects.isNull(value)) {
                    continue;
                }

                field.set(obj, value.replace("'", "").replace(";", ""));
            }
        } catch (Exception e) {
            log.error("\n{}", LogUtil.stacktraceToFiveLineString(e));
            // 2023/5/27 去除多余日志
            // e.printStackTrace();
        }
    }


    public static void enSafe(Object obj) {
        Class<?> clz = obj.getClass();
        try {
            for (Field field : clz.getDeclaredFields()) {
                ReflectionUtils.makeAccessible(field);
                // 过滤非字符串、空、静态字段
                boolean isStatic = Modifier.isStatic(field.getModifiers());
                String name = field.getType().getName();
                if (!"java.lang.String".equals(name) || isStatic) {
                    continue;
                }
                String value = (String) field.get(obj);
                field.set(obj, Objects.isNull(value) ? "" : value.replace("'", "’"));
            }
        } catch (Exception e) {
            log.error("\n{}", LogUtil.stacktraceToFiveLineString(e));
            // 2023/5/27 去除多余日志
            // e.printStackTrace();
        }
    }


    /**
     * 使用单引号包裹参数，目的是为了将其作为参数传入存储过程中，存储过程部分参数传入要求苛刻，需要使用多个单引号进行转义
     *
     * @param str 参数
     * @return 结果
     */
    public static String wrapString(String str) {
        String[] arr = str.split(",");
        StringBuilder strTemp = new StringBuilder();
        for (String s : arr) {
            strTemp.append("''").append(s).append("'',");
        }
        strTemp = new StringBuilder(BeanUtil.adaptNetSubString(strTemp.toString(), 0, strTemp.length() - 1));
        return strTemp.toString();
    }


    /**
     * 递归获取本类与父类的所有字段
     *
     * @return 字段列表
     */
    public static List<Field> getAllField(Class<?> clz) {
        List<Field> fieldList = new ArrayList<>();
        // 递归获取父类字段
        for (; clz != Object.class; clz = clz.getSuperclass()) {
            final Field[] fields = clz.getDeclaredFields();
            if (fields.length == 0) {
                continue;
            }
            fieldList.addAll(Arrays.asList(fields));
        }
        return fieldList;
    }

    /**
     * 获取类上的指定注解（如果本类没有则递归获取父类上的注解）
     *
     * @param annotationClz 注解Class
     */
    public static <A extends Annotation> A getAnnotation(Class<?> clz, Class<A> annotationClz) {
        // 获取类上的指定注解，本类没有则判断父类
        A annotation = null;
        try {
            for (; clz != Object.class; clz = clz.getSuperclass()) {
                // 首先本类获取
                annotation = clz.getAnnotation(annotationClz);
                if (Objects.nonNull(annotation)) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("\n{}", LogUtil.stacktraceToFiveLineString(e));
            // 2023/5/27 去除多余日志
            // e.printStackTrace();
        }
        return annotation;
    }


    /**
     * 将日期字符串转为Timestamp
     *
     * @param dateStr 日期字符串
     * @return Timestamp时间戳
     */
    //public static Timestamp date2Timestamp(String dateStr) {
    //    return date2Timestamp(cn.hutool.core.date.DateUtil.parse(dateStr));
    //}


    /**
     * 将日期转为Timestamp
     *
     * @param date 日期字符串
     * @return Timestamp时间戳
     */
    public static Timestamp date2Timestamp(Date date) {
        return new Timestamp(date.getTime());
    }


    /**
     * 比较BigDecimal是否相等
     *
     * @param x {@link BigDecimal}
     * @param y {@link BigDecimal}
     * @return true，相等；false，不等
     */
    public static boolean compareBigDecimal(BigDecimal x, BigDecimal y) {
        if (Objects.isNull(x) && Objects.isNull(y)) {
            return true;
        }

        if (Objects.nonNull(x) && Objects.nonNull(y)) {
            return x.compareTo(y) == 0;
        }

        return false;
    }


    public static String padLeft(String text, int totalWidth, char paddingChar) {
        int length = text.length();
        if (length < totalWidth) {
            // 相差
            int differenceValue = totalWidth - length;
            char[] chars = new char[differenceValue];
            for (int i = 0; i < differenceValue; i++) {
                chars[i] = paddingChar;
            }

            text = String.valueOf(chars) + text;
        }

        return text;
    }


    /**
     * 根据类型解析参数值
     *
     * @param type     类型
     * @param valueStr 参数值
     * @return 解析结果值
     */
    public static Object parseValueByType(Class<?> type, String valueStr) {
        Object result = null;

        try {
            // 类型判断
            if (Objects.equals(type, Byte.class) || Objects.equals(type, byte.class)) {
                // byte
                result = Byte.parseByte(valueStr);
            } else if (Objects.equals(type, Short.class) || Objects.equals(type, short.class)) {
                // short
                result = Short.parseShort(valueStr);
            } else if (Objects.equals(type, Integer.class) || Objects.equals(type, int.class)) {
                // int
                result = Integer.parseInt(valueStr);
            } else if (Objects.equals(type, Long.class) || Objects.equals(type, long.class)) {
                // long
                result = Long.parseLong(valueStr);
            } else if (Objects.equals(type, Float.class) || Objects.equals(type, float.class)) {
                // float
                result = Float.parseFloat(valueStr);
            } else if (Objects.equals(type, Double.class) || Objects.equals(type, double.class)) {
                // double
                result = Double.parseDouble(valueStr);
            } else if (Objects.equals(type, Boolean.class) || Objects.equals(type, boolean.class)) {
                // boolean
                result = Boolean.parseBoolean(valueStr);
            } else if (Objects.equals(type, String.class)) {
                // String
                result = valueStr;
            } else if (Objects.equals(type, BigDecimal.class)) {
                // BigDecimal
                result = new BigDecimal(valueStr);
            } else if (Objects.equals(type, java.util.Date.class)) {
                // java.util.Date
                result = parseDate(valueStr);
            } else if (Objects.equals(type, java.sql.Date.class)) {
                // java.sql.Date
                Date date = parseDate(valueStr);
                if (Objects.nonNull(date)) {
                    result = new java.sql.Date(date.getTime());
                }
            } else if (Objects.equals(type, Timestamp.class)) {
                // Timestamp
                Date date = parseDate(valueStr);
                if (Objects.nonNull(date)) {
                    result = new Timestamp(date.getTime());
                }
            }

        } catch (Exception e) {
            log.error("解析失败... \n堆栈：{}", LogUtil.stacktraceToFiveLineString(e));
        }

        return result;
    }


    public static Date parseDate(String defaultValueStr) {
        Date date = null;

        try {
            date = DateUtil.parseDateTime(defaultValueStr);
        } catch (Exception e) {
            // log.error("解析失败，进行二次解析... \n堆栈：{}", LogUtil.stacktraceToFiveLineString(e));
            try {
                date = DateUtils.parseDate(defaultValueStr, org.deil.utils.utils.DateUtil.PARSE_PATTERNS);
            } catch (ParseException ex) {
                try {
                    DateFormat format = new SimpleDateFormat("yyyy/ddMMM", Locale.US);
                    date = format.parse(defaultValueStr);
                } catch (ParseException exc) {
                    log.error("日期格式解析失败... 格式{} 异常{}堆栈:{}", defaultValueStr, exc.getMessage(), /*StrUtil.CRLF,*/ exc);
                }
            }
        }

        return date;
    }


    /**
     * 判断 集合 内是否存在某个元素（忽略大小写）
     *
     * @param collection 集合
     * @param element    字符串元素
     * @return true，存在；false，不存在
     */
    public static boolean containsIgnoreCase(Collection<String> collection, String element) {
        if (null == element) {
            return collection.contains(null);
        } else {
            for (String el : collection) {
                if (element.equalsIgnoreCase(el))
                    return true;
            }
        }

        return false;
    }

}

