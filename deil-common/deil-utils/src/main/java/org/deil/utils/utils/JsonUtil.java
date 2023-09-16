package org.deil.utils.utils;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

/**
 * Json工具
 * <p>统一用Jackson，因为使用 FastJson转换的Json Key首字母会莫名转为小写，主要还是因为Java的内省机制</p>
 */
@Slf4j
@UtilityClass
public class JsonUtil {

    /**
     * 线程安全
     */
    public static final ObjectMapper objectMapper = new ObjectMapper();


    static {
        // 忽略Json中未知属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 只序列化有值属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 空对象也可序列化
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 不序列化为时间戳
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 设置指定时间格式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 深拷贝
     *
     * @param obj 对象
     * @param <T> 参数化类型
     * @return 完全不一致的对象
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static <T> T clone(T obj) {
        if (Objects.isNull(obj)) {
            return null;
        }

        return (T) toJavaBean(toJson(obj), obj.getClass());
    }


    /**
     * Java对象序列化为Json字符串
     *
     * @param obj 对象
     * @return Json字符串
     */
    @SneakyThrows
    public static String toJson(Object obj) {
        return (Objects.isNull(obj)) ? null : objectMapper.writeValueAsString(obj);
    }


    /**
     * Json反序列化为Java对象
     *
     * @param json Json
     * @param clz  对象类型
     * @param <T>  对象的参数化类型
     * @return Java对象
     */
    public static <T> T toJavaBean(String json, Class<T> clz) {
        return toJavaBean(json, clz, false);
    }


    /**
     * Json反序列化为Java List对象
     *
     * @param json Json
     * @param clz  对象类型
     * @param <T>  对象的参数化类型
     * @return Java List对象
     */
    public static <T> List<T> toJavaList(String json, Class<T> clz) {
        return toJavaList(json, clz, false);
    }

    /**
     * Json反序列化为Java对象
     *
     * @param json       Json
     * @param clz        对象类型
     * @param ignoreCase 反序列化时是否忽略Json Key的大小写
     * @param <T>        对象的参数化类型
     * @return Java对象
     */
    @SneakyThrows
    public static <T> T toJavaBean(String json, Class<T> clz, boolean ignoreCase) {
        // fastJson可以忽略大小写差异进行反序列化，而Jackson比较严格
        return (ignoreCase) ? JSON.parseObject(json).toJavaObject(clz) : objectMapper.readValue(json, clz);
    }


    /**
     * Json反序列化为Java List对象
     *
     * @param json       Json
     * @param clz        对象类型
     * @param ignoreCase 反序列化时是否忽略Json Key的大小写
     * @param <T>        对象的参数化类型
     * @return Java List对象
     */
    @SneakyThrows
    public static <T> List<T> toJavaList(String json, Class<T> clz, boolean ignoreCase) {
        return (ignoreCase) ? JSON.parseArray(json).toJavaList(clz) : objectMapper.readValue(json, getCollectionType(clz));
    }


    private static JavaType getCollectionType(Class<?> clz) {
        return objectMapper.getTypeFactory().constructParametricType(List.class, clz);
    }

}
