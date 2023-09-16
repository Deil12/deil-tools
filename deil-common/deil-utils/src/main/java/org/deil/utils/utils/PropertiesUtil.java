package org.deil.utils.utils;

import lombok.extern.slf4j.Slf4j;
import org.deil.utils.log.LogUtil;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @PURPOSE 
 * @DATE 2022/11/11
 * @COPYRIGHT © csair.com 
 */
@Slf4j
public class PropertiesUtil {

    /**
     * @param props
     * @return void
     * @throws
     * @Title: printAllProperty
     * @Description: 输出所有配置信息
     */
    private static void printAllProperty(Properties props) {
        @SuppressWarnings("rawtypes")
        Enumeration en = props.propertyNames();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            String value = props.getProperty(key);
            log.info(key + " : " + value);
        }
    }

    /**
     * 根据key读取value
     *
     * @Title: getProperties_1
     * @Description: 第一种方式：根据文件名使用spring中的工具类进行解析
     *                  filePath是相对路劲，文件需在classpath目录下
     *                   比如：config.properties在包com.test.config下，
     *                路径就是com/test/config/config.properties
     *
     * @param filePath
     * @param keyWord
     * @return
     * @return String
     * @throws
     */
    @Deprecated
    public static String getProperties_1(String filePath, String keyWord){
        Properties prop = null;
        String value = null;
        try {
            // 通过Spring中的PropertiesLoaderUtils工具类进行获取
            prop = PropertiesLoaderUtils.loadAllProperties(filePath);
            // 根据关键字查询相应的值
            value = prop.getProperty(keyWord);
        } catch (IOException e) {
            log.error("\n{}", LogUtil.stacktraceToFiveLineString(e));
            // 2023/5/27 去除多余日志
            // e.printStackTrace();
        }
        return value;
    }

    /**
     * 读取配置文件所有信息
     *
     * @Title: getProperties_1
     * @Description: 第一种方式：根据文件名使用Spring中的工具类进行解析
     *                  filePath是相对路劲，文件需在classpath目录下
     *                   比如：config.properties在包com.test.config下，
     *                路径就是com/test/config/config.properties
     *
     * @param filePath
     * @return void
     * @throws
     */
    @Deprecated
    public static void getProperties_1(String filePath){
        Properties prop = null;
        try {
            // 通过Spring中的PropertiesLoaderUtils工具类进行获取
            prop = PropertiesLoaderUtils.loadAllProperties(filePath);
            printAllProperty(prop);
        } catch (IOException e) {
            log.error("\n{}", LogUtil.stacktraceToFiveLineString(e));
            // 2023/5/27 去除多余日志
            // e.printStackTrace();
        }
    }

    /**
     * 根据key读取value
     *
     * @Title: getProperties_2
     * @Description: 第二种方式：使用缓冲输入流读取配置文件，然后将其加载，再按需操作
     *                    绝对路径或相对路径， 如果是相对路径，则从当前项目下的目录开始计算，
     *                  如：当前项目路径/config/config.properties,
     *                  相对路径就是config/config.properties
     *
     * @param filePath
     * @param keyWord
     * @return
     * @return String
     * @throws
     */
    public static String getProperties_2(String filePath, String keyWord){
        Properties prop = new Properties();
        String value = null;
        try {
            // 通过输入缓冲流进行读取配置文件
            InputStream InputStream = new BufferedInputStream(new FileInputStream(new File(filePath)));
            // 加载输入流
            prop.load(InputStream);
            // 根据关键字获取value值
            value = prop.getProperty(keyWord);
        } catch (Exception e) {
            log.error("\n{}", LogUtil.stacktraceToFiveLineString(e));
            // 2023/5/27 去除多余日志
            // e.printStackTrace();
        }
        return value;
    }

    /**
     * 读取配置文件所有信息
     *
     * @Title: getProperties_2
     * @Description: 第二种方式：使用缓冲输入流读取配置文件，然后将其加载，再按需操作
     *                    绝对路径或相对路径， 如果是相对路径，则从当前项目下的目录开始计算，
     *                  如：当前项目路径/config/config.properties,
     *                  相对路径就是config/config.properties
     *
     * @param filePath
     * @return void
     * @throws
     */
    public static void getProperties_2(String filePath){
        Properties prop = new Properties();
        try {
            // 通过输入缓冲流进行读取配置文件
            InputStream InputStream = new BufferedInputStream(new FileInputStream(new File(filePath)));
            // 加载输入流
            prop.load(InputStream);
            printAllProperty(prop);
        } catch (Exception e) {
            log.error("\n{}", LogUtil.stacktraceToFiveLineString(e));
            // 2023/5/27 去除多余日志
            // e.printStackTrace();
        }
    }

    /**
     * 根据key读取value
     *
     * @Title: getProperties_3
     * @Description: 第三种方式：
     *                    相对路径， properties文件需在classpath目录下，
     *                  比如：config.properties在包com.test.config下，
     *                  路径就是/com/test/config/config.properties
     * @param filePath
     * @param keyWord
     * @return
     * @return String
     * @throws
     */
    public static String getProperties_3(String filePath, String keyWord){
        Properties prop = new Properties();
        String value = null;
        try {
            InputStream inputStream = PropertiesUtil.class.getResourceAsStream(filePath);
            prop.load(inputStream);
            value = prop.getProperty(keyWord);
        } catch (IOException e) {
            log.error("\n{}", LogUtil.stacktraceToFiveLineString(e));
            // 2023/5/27 去除多余日志
            // e.printStackTrace();
        }
        return value;
    }

    /**
     * 读取配置文件所有信息
     *
     * @Title: getProperties_3
     * @Description: 第三种方式：
     *                    相对路径， properties文件需在classpath目录下，
     *                  比如：config.properties在包com.test.config下，
     *                  路径就是/com/test/config/config.properties
     * @param filePath
     * @return
     * @throws
     */
    public static void getProperties_3(String filePath){
        Properties prop = new Properties();
        try {
            InputStream inputStream = PropertiesUtil.class.getResourceAsStream(filePath);
            prop.load(inputStream);
            printAllProperty(prop);
        } catch (IOException e) {
            log.error("\n{}", LogUtil.stacktraceToFiveLineString(e));
            // 2023/5/27 去除多余日志
            // e.printStackTrace();
        }
    }

}
