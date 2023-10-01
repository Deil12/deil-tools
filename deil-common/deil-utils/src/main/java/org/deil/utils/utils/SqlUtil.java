package org.deil.utils.utils;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.MappedStatement;
import org.deil.utils.log.LogUtil;
import org.deil.utils.utils.annotation.Field;
import org.deil.utils.utils.annotation.Table;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 负责Sql脚本，主要与{@link SQL} 对象组合使用，拼接成完整SQL语句
 * <p>
 * 可与 @XxxxProvider注解配合使用，比如{@link org.apache.ibatis.annotations.SelectProvider}，作用是提供SQL语句
 * </p>
 *
 * <p>
 * 而结果映射可以使用{@link org.apache.ibatis.annotations.ResultMap}注解标注，可标注Mapper.xml文件内的 ResultMap
 * </p>
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SqlUtil {

    /**
     * 匹配下划线正则
     */
    private static final String UNDERLINE_REGEX = "_[a-z]";

    /**
     * 匹配驼峰正则
     */
    private static final String HUMP_REGEX = "[A-Z]";

    // region 公共方法

    /**
     * 拼接Select查询的查询列，最好搭配ResultMap进行结果映射
     *
     * @param entity  条件实体
     * @param isAlias 如果为true，则启用别名，而给列取的别名为实体中字段本身的名字，但是不排除有些字段无法映射的问题；如果为false，则不生成别名，那就最好启用ResultMap进行映射
     * @return Select查询列数组
     */
    public static String[] joinSelectColumn(Object entity, boolean isAlias) {
        List<String> resultList = new ArrayList<>();

        try {
            // 获得所有字段
            List<java.lang.reflect.Field> allFieldList = BeanUtil.getAllField(entity.getClass());
            // 遍历字段
            for (java.lang.reflect.Field field : allFieldList) {
                ReflectionUtils.makeAccessible(field);
                // 获取注解
                Field annotation = field.getAnnotation(Field.class);

                // 过滤无注解字段
                if (Objects.isNull(annotation)) {
                    continue;
                }

                resultList.add(annotation.value() + (isAlias ? " as " + field.getName() : ""));
            }
        } catch (Exception e) {
            log.error("\n{}", LogUtil.stacktraceToFiveLineString(e));
            // 2023/5/27 去除多余日志
            // e.printStackTrace();
        }

        return resultList.toArray(new String[0]);
    }


    /**
     * 拼接Update -> Set部分SQL脚本（通过对比两个对象之前不同值）
     *
     * @param oldEntity 旧实体
     * @param newEntity 新实体
     * @return 结果
     */
    public static String[] joinUpdateSet(Object oldEntity, Object newEntity, String newKey) {
        List<String> resultList = new ArrayList<>();

        try {
            // 获得所有字段
            List<java.lang.reflect.Field> allFieldList = BeanUtil.getAllField(oldEntity.getClass());
            // 遍历字段
            for (java.lang.reflect.Field field : allFieldList) {
                ReflectionUtils.makeAccessible(field);
                // 获取注解
                Field annotation = field.getAnnotation(Field.class);

                // 获取旧值
                Object oldValue = field.get(oldEntity);
                Object newValue = field.get(newEntity);

                // 过滤无注解（无注解表示非数据库字段）、Set时，过滤主键、值相同也过滤，说明无变化
                if (Objects.isNull(annotation) || annotation.isKey() || Objects.equals(oldValue, newValue)) {
                    continue;
                }

                // 构成语句
                resultList.add(annotation.value() + " = " + "#{" + newKey + "." + field.getName() + "}");
            }
        } catch (Exception e) {
            log.error("\n{}", LogUtil.stacktraceToFiveLineString(e));
            // 2023/5/27 去除多余日志
            // e.printStackTrace();
        }

        return resultList.toArray(new String[0]);
    }


    /**
     * 拼接【Update、Select】 -> Where条件部分，大概结果如下：
     *
     * <pre>
     *     "DATEDIFF(day, FlightDate, '#{oldEntity.flightDate}') = 0)",
     *     "Dest = '#{oldEntity.dest}'"
     * </pre>
     * <p>
     * 若是时间类型，则默认以 Day 为标准进行条件比对
     * </p>
     *
     * @param entity       实体
     * @param entityKey    实体在MyBatis参数化时的名称，比如 #{res.name} 中的 res
     * @param dateFlag     时间比对逻辑
     * @param defaultJudge 启用默认值判断
     * @return 结果
     */
    public static String[] joinWhereCondition(Object entity, String entityKey, int dateFlag, boolean defaultJudge) {
        List<String> resultList = new ArrayList<>();

        try {
            // 获得所有字段
            List<java.lang.reflect.Field> allFieldList = BeanUtil.getAllField(entity.getClass());

            entityKey = StringUtils.isBlank(entityKey) ? StringUtils.EMPTY : entityKey + ".";

            // 遍历字段
            for (java.lang.reflect.Field field : allFieldList) {
                ReflectionUtils.makeAccessible(field);
                // 获取注解
                Field annotation = field.getAnnotation(Field.class);
                // 获取值
                Object value = field.get(entity);

                // 过滤无注解字段、过滤空值
                if (Objects.isNull(annotation) || Objects.isNull(value)) {
                    continue;
                }


                // feat -- 2023/7/14 新增一个默认值校验
                if (defaultJudge) {
                    // 如果属性值与注解上的默认值一致，则不加入条件
                    if (matchDefaultValue(field.getType(), value, annotation.defaultValue())) {
                        continue;
                    }
                }


                // 类型（目前单独针对时间类型）
                boolean isDate = Objects.equals("java.util.Date", field.getType().getName()) || (value instanceof Date);

                // 拼接SQL
                String condition = isDate
                        ? joinDateLogic(annotation, field, entityKey, dateFlag)
                        : String.format("%s = #{%s}", annotation.value(), entityKey + field.getName());

                // 添加
                resultList.add(condition);
            }
        } catch (Exception e) {
            log.error("\n{}", LogUtil.stacktraceToFiveLineString(e));
            // 2023/5/27 去除多余日志
            // e.printStackTrace();
        }

        return resultList.toArray(new String[0]);
    }


    /**
     * 匹配默认值与属性值是否相等（若相等，则可以跳过该属性）
     *
     * @param type            属性类型
     * @param value           属性值
     * @param defaultValueStr 默认值
     * @return true，匹配成功，跳过属性；false，匹配失败，属性可作为条件
     */
    public static boolean matchDefaultValue(Class<?> type, Object value, String defaultValueStr) {
        // 将默认值转换为对应类型值
        Object defaultValue = BeanUtil.parseValueByType(type, defaultValueStr);

        // 日期比较，不相等即作为条件
        if (value instanceof Date) {
            // 只比较年份
            if (Objects.nonNull(defaultValue)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime((Date) value);
                int v = calendar.get(Calendar.YEAR);
                calendar.setTime((Date) defaultValue);
                int dv = calendar.get(Calendar.YEAR);
                return Objects.equals(v, dv);
            }

        } else if (value instanceof BigDecimal) {
            // 如果是 BigDecimal类型，默认值固定为 -1
            BigDecimal decimalValue = (BigDecimal) value;
            // 比较是否相等
            return BeanUtil.compareBigDecimal(decimalValue, new BigDecimal("-1.00"));

        } else if (value instanceof Byte) {
            // 如果是Byte类型，默认值固定为0
            Byte byteValue = (Byte) value;
            // 比较是否相等
            return Objects.equals(byteValue, new Byte("0"));

        } else {
            // 默认值比较
            if (Objects.nonNull(defaultValue)) {
                return Objects.equals(value, defaultValue);
            }
        }

        return false;
    }


    /**
     * 不同的时间判断逻辑
     *
     * @param annotation 字段注解
     * @param field      字段
     * @param entityKey  实体在MyBatis参数化时的名称，比如 #{res.name} 中的 res
     * @param dateFlag   时间判断逻辑标记
     * @return 拼接的SQL条件
     */
    private static String joinDateLogic(Field annotation, java.lang.reflect.Field field, String entityKey, int dateFlag) {
        String result;
        switch (dateFlag) {
            case 1:
                result = String.format("DATEDIFF(day, %s, #{%s}) = 0", annotation.value(), entityKey + field.getName());
                // result = String.format("DATEDIFF(day, " + annotation.value() + ", " + "#{" + entityKey + field.getName() + "}" + ") = 0")
                break;
            case 2:
                result = String.format("%s >= convert(varchar(10), #{%s}, 120) AND %s < convert(varchar(10), dateadd(day, 1, #{%s}), 120)",
                        annotation.value(),
                        field.getName(),
                        annotation.value(),
                        field.getName());
                break;
            default:
                result = annotation.value() + " = " + "#{" + entityKey + field.getName() + "}";
                break;
        }

        return result;
    }


    /**
     * 拼接 INSERT 语句中的 Column列、Value值（适用于{@link SQL}的 VALUES() 方法）
     * <pre>
     *     返回结果：
     *       column: "name, age"
     *       values: "#{name}, #{age}"
     * </pre>
     *
     * @param entity 实体
     * @return 新增语句信息，列、值
     */
    public static InsertInfo joinInsertColumnValue(Object entity) {
        StringBuilder columnBuilder = new StringBuilder();
        StringBuilder valuesBuilder = new StringBuilder();

        try {
            // 获取有值、有注解、且不为自增主键 字段
            List<java.lang.reflect.Field> allFieldList = BeanUtil.getAllField(entity.getClass()).stream().filter(el -> {
                ReflectionUtils.makeAccessible(el);
                // 获取注解
                Field annotation = el.getAnnotation(Field.class);
                // 获取值
                Object value = null;

                try {
                    value = el.get(entity);
                } catch (IllegalAccessException e) {
                    log.error("\n{}", LogUtil.stacktraceToFiveLineString(e));
                    // 2023/5/27 去除多余日志
                    // e.printStackTrace();
                }

                // 表是自增主键
                boolean isAutoKey = Objects.equals(Table.KeyType.AUTO, getTableKeyType(entity));

                // 自增主键不允许出现在新增列中
                // 返回 -> 有注解的 并且 有值的 并且 不是自增主键 或 是自增主键但是该字段并非主键
                return Objects.nonNull(annotation) && Objects.nonNull(value) && (!isAutoKey || !annotation.isKey());
            }).collect(Collectors.toList());

            // 遍历字段
            for (int i = 0, size = allFieldList.size(); i < size; i++) {
                java.lang.reflect.Field field = allFieldList.get(i);
                // 获取注解
                Field annotation = field.getAnnotation(Field.class);

                // 结尾是否添Z加 逗号
                String end = (Objects.equals(i, size - 1)) ? "" : ", ";

                columnBuilder.append(annotation.value()).append(end);
                valuesBuilder.append("#").append("{").append(field.getName()).append("}").append(end);
            }

        } catch (Exception e) {
            log.error("\n{}", LogUtil.stacktraceToFiveLineString(e));
            // 2023/5/27 去除多余日志
            // e.printStackTrace();
        }

        return new InsertInfo(columnBuilder.toString(), valuesBuilder.toString());
    }


    /**
     * 获取表名（通过实体类上的注解{@link Table}）
     *
     * @param entity 对象
     * @return 表名
     */
    public static String getTableName(Object entity) throws Exception {
        return getTable(entity).value();
    }


    /**
     * 获取表的主键类型（通过实体类上的注解{@link Table}）
     *
     * @param entity 对象
     * @return 表名
     */
    @SneakyThrows
    public static Table.KeyType getTableKeyType(Object entity) {
        return getTable(entity).keyType();
    }


    /**
     * 获取表注解
     *
     * @param entity 实体对象
     * @return 注解对象
     */
    private static Table getTable(Object entity) throws Exception {
        Table annotation = BeanUtil.getAnnotation(entity.getClass(), Table.class);
        if (Objects.isNull(annotation)) {
            log.error("请在实体类上加上 @Table 注解标记表名和主键类型！");
            throw new Exception("请在实体类上加上 @Table 注解标记表名和主键类型！");
        }
        return annotation;
    }

    /**
     * 获取实体中的主键名称
     *
     * @param entity              实体
     * @param needAnnotationValue 是否需要注解内的值
     * @return 主键名称
     */
    public static String getKeyPropertyName(Object entity, boolean needAnnotationValue) {
        List<java.lang.reflect.Field> allFieldList = BeanUtil.getAllField(entity.getClass());
        for (java.lang.reflect.Field field : allFieldList) {
            ReflectionUtils.makeAccessible(field);
            Field annotation = field.getAnnotation(Field.class);
            // 过滤无注解、非主键元素
            if (Objects.isNull(annotation) || !annotation.isKey()) {
                continue;
            }

            return needAnnotationValue ? annotation.value() : field.getName();
        }
        return "";
    }


    /**
     * 字段匹配
     *
     * @param clz        字节码
     * @param columnName 列名
     * @return 匹配成功则返回对象对应的属性名，失败则返回空
     */
    public static java.lang.reflect.Field getMatchField(Class<?> clz, String columnName) {
        List<java.lang.reflect.Field> allFieldList = BeanUtil.getAllField(clz);
        for (java.lang.reflect.Field field : allFieldList) {
            ReflectionUtils.makeAccessible(field);
            // 排除无注解属性
            Field annotation = field.getAnnotation(Field.class);
            if (Objects.isNull(annotation)) {
                continue;
            }

            String annotationValue = annotation.value().trim();

            // 名称匹配成功则返回（匹配规则需要确定 大小写、驼峰、下划线 三种比较方式）
            if (isMatch(annotationValue, columnName)) {
                return field;
            }

        }

        return null;
    }


    /**
     * 将字符串进行三种方式的比较
     *
     * @param v1 字符串1
     * @param v2 字符串2
     * @return 比较结果 -> true：相同；false：不相同
     */
    public static boolean isMatch(String v1, String v2) {
        // 名称匹配（不区分大小写）
        return StringUtils.equalsIgnoreCase(v1, v2)
                // 下划线匹配
                || StringUtils.equalsIgnoreCase(hump2Underline(v1), hump2Underline(v2))
                // 驼峰匹配
                || StringUtils.equalsIgnoreCase(underline2Hump(v1), underline2Hump(v2));
    }


    /**
     * 将数组组合成完整SQL（会自带 SELECT、SET、WHERE 等关键字）
     * <pre>
     *     例如：
     *       类型：WHERE
     *       数组：["age = 19", "name = '王'"]
     *       转换结果："WHERE (age = 19 AND name = '王')"
     * </pre>
     *
     * @param array 数组
     * @param type  数组类型
     * @return 脚本
     */
    public static String combineArray(String[] array, SqlArrayType type) {
        if (0 == array.length) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        switch (type) {
            case SELECT_COLUMN:
                builder.append("SELECT ");
                break;
            case UPDATE_SET:
                builder.append(" SET ");
                break;
            case WHERE:
                builder.append(" WHERE ");
                break;
            default:
                break;
        }


        for (int i = 0, length = array.length; i < length; i++) {
            builder.append(array[i]).append((Objects.equals(i, length - 1)) ? "" : type.getConjunction());
        }

        return builder.toString();
    }


    /**
     * 判断对象是否为空，或者其中所有加上{@link Field}注解的元素为空
     *
     * @param obj 对象
     * @return true -> 为空；false -> 不为空
     */
    public static boolean isNullOrBlank(Object obj) {
        if (Objects.isNull(obj)) {
            return true;
        }

        try {
            // 获取所有字段
            List<java.lang.reflect.Field> allFieldList = BeanUtil.getAllField(obj.getClass());
            for (java.lang.reflect.Field field : allFieldList) {
                ReflectionUtils.makeAccessible(field);
                // 获取注解
                Field annotation = field.getAnnotation(Field.class);
                // 过滤无注解属性
                if (Objects.isNull(annotation)) {
                    continue;
                }

                // 只要有一个不为空的，则返回false
                Object value = field.get(obj);
                boolean isString = Objects.equals("java.lang.String", field.getType().getName());

                // 不为空并且不是String
                if (Objects.nonNull(value) && !isString) {
                    return false;
                }

                // 如果是String，不为空
                if (isString && StringUtils.isNotBlank((String) value)) {
                    return false;
                }
            }
        } catch (IllegalAccessException e) {
            log.error("\n{}", LogUtil.stacktraceToFiveLineString(e));
            // 2023/5/27 去除多余日志
            // e.printStackTrace();
        }

        return true;
    }


    /**
     * 获取匹配成功的方法
     *
     * @param id {@link MappedStatement}.id（接口全限定名 + . + 方法名称）
     * @return 与该id匹配的方法对象
     */
    public static Method getMatchMethod(String id) throws Exception {
        // 截取类全限定名
        String className = id.substring(0, id.lastIndexOf("."));
        // 截取方法名
        String methodName = id.substring(id.lastIndexOf(".") + 1);
        // 接口所有方法
        Method[] methods = Class.forName(className).getMethods();
        // 方法匹配
        return Arrays.stream(methods).filter(el -> Objects.equals(el.getName(), methodName)).findFirst().orElse(null);
    }


    /**
     * 是否单一主键
     *
     * @param obj 对象
     * @return true -> 单一主键；false -> 多主键
     */
    public static boolean isSingleKey(Object obj) {
        List<java.lang.reflect.Field> allFieldList = BeanUtil.getAllField(obj.getClass());

        long count = allFieldList.stream().filter(el -> {
            ReflectionUtils.makeAccessible(el);
            Field annotation = el.getAnnotation(Field.class);
            return Objects.nonNull(annotation) && annotation.isKey();
        }).count();

        return count == 1;
    }

    /**
     * 将 下划线格式 转为 驼峰格式，示例：
     * <pre>
     *     isInitialUpperCase为 true 时：
     *          tbl_dex_Send -> TblDexSend
     *
     *     isInitialUpperCase为 false 时：
     *          tbl_dex_Send -> tblDexSend
     * </pre>
     *
     * @param text               字符串
     * @param isInitialUpperCase 是否首字母大写（true -> 大写；false -> 小写）
     * @return 改变后字符串
     */
    public static String underline2Hump(String text, boolean isInitialUpperCase) {
        // 返回默认首字母大写
        text = underline2Hump(text);

        // 转换成字符数组
        char[] ch = text.toCharArray();
        // 首字母是否为字母
        if (Character.isLetter(ch[0])) {
            // ? 大写 : 小写
            ch[0] = isInitialUpperCase ? Character.toUpperCase(ch[0]) : Character.toLowerCase(ch[0]);
        }

        return String.valueOf(ch);
    }


    /**
     * 将 驼峰格式 转为 下划线格式，示例：
     * <pre>
     *      tblDexSend -> tbl_dex_send
     * </pre>
     *
     * @param text 驼峰格式文本
     * @return 下划线格式文本
     */
    public static String hump2Underline(String text) {
        Pattern compile = Pattern.compile(HUMP_REGEX);
        Matcher matcher = compile.matcher(text);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(buffer);

        return (Objects.equals('_', buffer.charAt(0))) ? buffer.substring(1, buffer.length()) : buffer.toString();
    }

    /**
     * 将 下划线格式 转为 驼峰格式
     *
     * @param text 下划线格式文本
     * @return 驼峰格式文本
     */
    public static String underline2Hump(String text) {
        // 判断是否大写
        int flag = checkInitial(text);
        // 更改为小写
        text = text.toLowerCase();
        Pattern compile = Pattern.compile(UNDERLINE_REGEX);
        Matcher matcher = compile.matcher(text);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, matcher.group(0).toUpperCase().replace("_", ""));
        }
        matcher.appendTail(buffer);

        // 如果是大写，则恢复为大写
        if (Objects.equals(1, flag)) {
            buffer.setCharAt(0, Character.toUpperCase(buffer.charAt(0)));
        }

        return buffer.toString();
    }

    /**
     * 检查首字母是否大小写
     *
     * @param text 文本
     * @return 0 -> 非字母；1 -> 首字母大写；2 -> 首字母小写
     */
    public static int checkInitial(String text) {
        char[] ch = text.toCharArray();
        // 首字母
        char initial = ch[0];
        // 判断首字符是否字母
        if (Character.isLetter(initial)) {
            // 是否大写
            return (Character.isUpperCase(initial)) ? 1 : 2;
        } else {
            // 不属于字母
            return 0;
        }
    }

    // endregion


    /**
     * 数组类型
     */
    @Getter
    @RequiredArgsConstructor
    public enum SqlArrayType {

        /**
         * 查询列数组
         */
        SELECT_COLUMN(", "),

        /**
         * 更新列数组
         */
        UPDATE_SET(", "),

        /**
         * 条件列数组
         */
        WHERE(" AND ");

        /**
         * 组合符
         */
        private final String conjunction;
    }


    /**
     * 新增信息
     */
    @Getter
    @RequiredArgsConstructor
    public static class InsertInfo {

        /**
         * 列，多个
         */
        private final String column;

        /**
         * 值，多个
         */
        private final String values;

    }

    @Getter
    @RequiredArgsConstructor
    public static class ColumnInfo {

        /**
         * 数据列名称
         */
        private final String columnName;

        /**
         * 数据列类型
         */
        private final Integer columnType;

    }

}
