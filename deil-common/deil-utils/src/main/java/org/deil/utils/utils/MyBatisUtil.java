package org.deil.utils.utils;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.reflection.ArrayUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.sql.Array;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

@Slf4j
public class MyBatisUtil {

    /*
    SQL脚本去除空格换行 SqlSourceBuilder.removeExtraWhitespaces(original);
    DefaultParameterHandler 设置的预编译参数
    ConnectionLogger       负责打印  [[debug,137] - ==>  Preparing: select Carrier from tbl_carrier where 1=2 or Carrier = ?]
    PreparedStatementLogger 负责打印  [[debug,137] - ==> Parameters: xxx(String)]
     */


    private static Configuration configuration;


    /**
     * 获取定义在Xml或其他地方的 ResultMap对象
     *
     * @param mapperInterface 接口Class
     * @param resultMapId     resultMap的id
     * @return {@link ResultMap} 对象
     */
    public static ResultMap getResultMap(Class<?> mapperInterface, String resultMapId) {
        return getConfiguration().getResultMap(mapperInterface.getName() + "." + resultMapId);
    }


    /**
     * 获取定义在Xml或其他地方的 ResultMap对象
     *
     * @param mapperInterfaceQualifiedName 接口Class全限定名
     * @param resultMapId                  resultMap的id
     * @return {@link ResultMap} 对象
     */
    public static ResultMap getResultMap(String mapperInterfaceQualifiedName, String resultMapId) {
        return getConfiguration().getResultMap(mapperInterfaceQualifiedName + "." + resultMapId);
    }


    /**
     * 获取{@link MappedStatement}
     *
     * @param mapperInterface 接口Class
     * @param methodName      方法名
     * @return {@link MappedStatement}
     */
    public static MappedStatement getMappedStatement(Class<?> mapperInterface, String methodName) {
        return getConfiguration().getMappedStatement(mapperInterface.getName() + "." + methodName);
    }


    /**
     * 获取{@link MappedStatement}
     *
     * @param mapperInterfaceQualifiedName 接口全限定名
     * @param methodName                   方法名
     * @return {@link MappedStatement}
     */
    public static MappedStatement getMappedStatement(String mapperInterfaceQualifiedName, String methodName) {
        return getConfiguration().getMappedStatement(mapperInterfaceQualifiedName + "." + methodName);
    }


    /**
     * 获取 MyBatis定义的参数
     * <pre>
     *     就是@Param("city") String city，把@Param的值当作key
     * </pre>
     *
     * @param method 方法对象
     * @param args   方法参数
     * @return 参数，可用来获取SQL
     */
    public static Object getNamedParams(Method method, Object[] args) {
        ParamNameResolver paramNameResolver = new ParamNameResolver(getConfiguration(), method);
        return paramNameResolver.getNamedParams(args);
    }


    /**
     * 获取SQL封装对象（需要携带参数对象，因为{@link BoundSql}对象是需要通过参数获取完整SQL）
     *
     * @param mappedStatement 可以理解为mapper方法
     * @param namedParams     被包装过的参数
     * @return SQL脚本
     */
    public static BoundSql getBoundSql(MappedStatement mappedStatement, Object namedParams) {
        // 获取SQL及参数包装对象
        return mappedStatement.getBoundSql(namedParams);
    }


    /**
     * 获取Sql执行时所用到的参数
     *
     * @param boundSql        Sql封装对象
     * @param parameterObject Mapper方法入参
     * @return 参数列表
     */
    public static List<ParameterValue> getParameterValueList(BoundSql boundSql, Object parameterObject) {
        Map<Integer, ParameterValue> parameterMap = getParameterMap(boundSql, parameterObject);
        // 排序
        int size = parameterMap.size();
        List<ParameterValue> parameterValueList = new ArrayList<>(size);
        for (int i = 1; i <= size; i++) {
            parameterValueList.add(parameterMap.get(i));
        }

        return parameterValueList;
    }


    /**
     * 获取完整Sql（按照 MyBatis底层源码操作）
     *
     * @param boundSql        Sql包装对象
     * @param parameterObject Mapper方法入参
     * @return 完整SQL脚本
     */
    public static String getFullSql(BoundSql boundSql, Object parameterObject) {
        // 获取带有占位符的Sql
        String sql = boundSql.getSql();
        // 去除换行等空白
        sql = SqlSourceBuilder.removeExtraWhitespaces(sql);
        // 获取参数
        Map<Integer, ParameterValue> parameterMap = getParameterMap(boundSql, parameterObject);
        // 替换?为 {}（简单方案）
        sql = sql.replaceAll("\\?", "{}");

        // 按顺序排列
        int size = parameterMap.size();
        List<String> valueList = new ArrayList<>(size);
        for (int i = 1; i <= size; i++) {
            ParameterValue parameterValue = parameterMap.get(i);
            valueList.add(objectValueString(parameterValue.getValue(), parameterValue.getJdbcType()));
        }

        return new LoggingEvent(null, (Logger) log, null, sql, null, valueList.toArray()).getFormattedMessage();
    }


    /**
     * 获取参数 Map（key: 参数序号；value: 参数值）
     *
     * @param boundSql        Sql包装对象
     * @param parameterObject Sql完整参数
     * @return 参数 Map
     */
    public static Map<Integer, ParameterValue> getParameterMap(BoundSql boundSql, Object parameterObject) {
        // 参数 Map（key -> 序号，value值）
        Map<Integer, ParameterValue> parameterMap = new HashMap<>();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                JdbcType jdbcType = parameterMapping.getJdbcType();
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();

                    if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (parameterObject == null) {
                        value = null;
                    } else if (configuration.getTypeHandlerRegistry().hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else {
                        MetaObject metaObject = configuration.newMetaObject(parameterObject);
                        value = metaObject.getValue(propertyName);
                    }

                    parameterMap.put(i + 1, new ParameterValue(propertyName, value, jdbcType));
                }
            }
        }

        return parameterMap;
    }


    /**
     * 获取Mapper方法返回类型（如果是List，也会拿到其泛型类型）
     *
     * @param mapperInterface mapper接口Class
     * @param methodName      方法名
     * @return 类型
     */
    public static Class<?> getMapperMethodReturnType(Class<?> mapperInterface, String methodName) {
        MappedStatement mappedStatement = getMappedStatement(mapperInterface, methodName);
        if (Objects.isNull(mappedStatement)) {
            return null;
        }

        List<ResultMap> resultMaps = mappedStatement.getResultMaps();
        if (CollectionUtils.isEmpty(resultMaps)) {
            return null;
        }

        ResultMap resultMap = resultMaps.get(0);
        return resultMap.getType();
    }


    /**
     * 获取对象的toString结果
     *
     * @param value 对象
     * @return String
     */
    public static String objectValueString(Object value, JdbcType jdbcType) {
        if (Objects.isNull(value)) {
            return "null";
        }

        if (value instanceof Array) {
            try {
                return ArrayUtil.toString(((Array) value).getArray());
            } catch (SQLException e) {
                return value.toString();
            }
        }

        // 通过获取JdbcType做特殊处理，并通过其类型判断是否加单引号展示
        return typeToString(transformJdbcType(value, jdbcType));
    }


    /**
     * 根据值的不同类型判断是否加单引号
     *
     * @param value 值
     * @return 转换后的值
     */
    private static String typeToString(Object value) {
        if ((value instanceof String) || (value instanceof Date)) {
            return "'" + value + "'";
        } else {
            return value.toString();
        }
    }


    /**
     * 通过JdbcType做特殊处理（需要根据JdbcType转换成不同类型的值）
     *
     * @param value    参数值
     * @param jdbcType 数据库类型
     * @return 转换后的值
     */
    public static Object transformJdbcType(Object value, JdbcType jdbcType) {
        if (Objects.isNull(value)) {
            return "null";
        }

        Object result = value;

        if (Objects.isNull(jdbcType)) {
            if (value instanceof Date) {
                Date date = (Date) value;
                result = new Timestamp(date.getTime());
            }

        } else {
            switch (jdbcType) {
                case DATE:
                    Date date = (Date) value;
                    result = new java.sql.Date(date.getTime());
                    break;
                case TIME:
                    Date time = (Date) value;
                    result = new Time(time.getTime());
                    break;
                // todo 还有的自己补充

            }
        }

        return result;
    }


    /**
     * 获取 MyBatis 全局配置对象（多数据源时可能有多个）
     *
     * @return 全局配置
     */
    public static synchronized Configuration getConfiguration() {
        if (Objects.isNull(configuration)) {
            // 暂时是单数据源可以这样做
            SqlSessionFactory factory = SpringContextAwareUtil.getBean(SqlSessionFactory.class);
            configuration = factory.getConfiguration();
        }

        return configuration;
    }
}

class ParameterValue {
    private final String propertyName;
    private final Object value;
    private final String type;
    private final JdbcType jdbcType;

    public ParameterValue(String propertyName, Object value, JdbcType jdbcType) {
        this.propertyName = propertyName;
        this.value = value;
        this.type = (Objects.isNull(value)) ? "" : value.getClass().getSimpleName();
        this.jdbcType = jdbcType;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Object getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public JdbcType getJdbcType() {
        return jdbcType;
    }
}
