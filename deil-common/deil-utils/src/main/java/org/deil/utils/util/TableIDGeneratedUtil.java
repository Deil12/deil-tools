package org.deil.utils.util;

import lombok.experimental.UtilityClass;

import javax.persistence.IdClass;
import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @PURPOSE 表格idgenerated工具
 * @DATE 2022/11/27
 * @COPYRIGHT © Deil
 */
@UtilityClass
public class TableIDGeneratedUtil {
    /**
     * 包含自增长ID的表
     */
    public static final List<String> CONTAIN_AUTO_ID_TABLES =new ArrayList<>();

    public static void initTables(String pack){
        List<Class<?>> entityClasses=ReflectUtils.getClassListByAnnotation(pack, javax.persistence.Entity.class);

        for (Class ss:entityClasses) {
            parseId(ss);
        }
    }


    /**
     * 主键注解
     */
    private static final List<Class<? extends Annotation>> ID_CLASS_LIST = Arrays.asList(javax.persistence.Id.class, org.springframework.data.annotation.Id.class);
    private static void parseId(Class<?> clazz) {
        Table tableAnnotation = clazz.getAnnotation(Table.class);
        if (tableAnnotation != null) {
            String tableName = tableAnnotation.name().toUpperCase();

            // 尝试从类注解获取主键信息
            IdClass idClassAnnotation = clazz.getAnnotation(IdClass.class);
            if (idClassAnnotation != null) {
                return;
            }
            // 尝试获取第一个带主键注解的字段
            for (Class<? extends Annotation> idClass : ID_CLASS_LIST) {
                Optional<Field> fieldByAnnotation = ReflectUtils.getFieldByAnnotation(clazz, idClass);
                if (fieldByAnnotation.isPresent()) {
                    Field field = fieldByAnnotation.get();

                    String typeName = field.getType().getTypeName();
                    if (typeName.equalsIgnoreCase("int") || typeName.equalsIgnoreCase("long")) {
                        CONTAIN_AUTO_ID_TABLES.add(tableName);
                        return;
                    }
                }
            }

            for (Class<? extends Annotation> idClass : ID_CLASS_LIST) {
                Optional<Method> fieldByAnnotation = ReflectUtils.getMethodByAnnotation(clazz, idClass);
                if (fieldByAnnotation.isPresent()) {
                    Method method = fieldByAnnotation.get();
                    String typeName = method.getReturnType().getSimpleName();
                    clazz.getPackage().getName();
                    if (typeName.equalsIgnoreCase("int") || typeName.equalsIgnoreCase("long")) {
                        CONTAIN_AUTO_ID_TABLES.add(tableName);
                        return;
                    }
                }
            }
        }
    }
}
