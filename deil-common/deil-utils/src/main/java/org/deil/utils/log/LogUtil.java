package org.deil.utils.log;

import cn.hutool.core.stream.StreamUtil;
import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Objects;
import java.util.StringJoiner;

@Slf4j
@UtilityClass
public class LogUtil {

    public static String functErrStr = "方法报错：[{}()],操作人ip地址：[{}],  入参：[{}]";


    public static void functError(String methodName, String ip, String params, Throwable errorMsg) {
        log.error(functErrStr, methodName, ip, params, errorMsg);
    }


    /**
     * 获取堆栈前5行主要信息
     *
     * @param e 异常对象
     * @return 堆栈信息
     */
    public static String stacktraceToFiveLineString(Throwable e) {
        return stacktraceToString(e, 5);
    }


    /**
     * 获取堆栈信息
     *
     * @param e 异常对象
     * @return 堆栈信息
     */
    public static String stacktraceToString(Throwable e) {
        if (Objects.isNull(e)) {
            return System.lineSeparator() + "异常信息为空!" + System.lineSeparator();
        }

        return stacktraceToString(e, e.getStackTrace().length);
    }


    /**
     * 获取堆栈主要信息
     *
     * @param e 异常对象
     * @return 堆栈信息
     */
    public static String stacktraceToString(Throwable e, int lineNum) {
        if (Objects.isNull(e)) {
            return System.lineSeparator() + "异常信息为空!" + System.lineSeparator();
        }

        String result;

        try {
            StringBuilder builder = new StringBuilder()
                    .append(e.getClass().getName())
                    .append(": ")
                    .append(e.getMessage())
                    .append(System.lineSeparator());

            StackTraceElement[] stackTrace = e.getStackTrace();
            if (stackTrace.length < lineNum) {
                for (StackTraceElement stackTraceElement : stackTrace) {
                    builder.append("\tat ")
                            .append(stackTraceElement.toString())
                            .append(System.lineSeparator());
                }
            } else {
                for (int i = 0; i < lineNum; i++) {
                    if (i > 5 && !stackTrace[i].toString().startsWith("org.deil")) {
                        continue;
                    }
                    builder.append("\tat ")
                            .append(stackTrace[i].toString())
                            .append(System.lineSeparator());
                }
            }

            result = builder.toString();

        } catch (Throwable ex) {
            // 不抛出异常
            result = ExceptionUtils.getStackTrace(e);
        }

        return result;
    }


    /**
     * 获取调用方法的调用点，用处：
     * <pre>
     *     A方法调用本方法(getLastCallStackTraceElement)，可以获取调用A方法的调用处信息
     * </pre>
     *
     * @param ignoreClassPrefix        忽略元素，类全限定名前缀，比如说 [ java.lang.Thread ]，想过滤 Thread 类，就填完整的 [ java.lang.Thread ]；想过滤所有 [ java.lang ] 包下的类，就填 [ java.lang ]
     * @param ignoreClassAndMethodName 忽略元素，可以是 [类全限定名.方法名]，亦可以是 [类名.方法名]，或者是 [类名]
     * @return 调用堆栈信息
     */
    public static StackTraceElement getLastCallStackTraceElement(String[] ignoreClassPrefix, String[] ignoreClassAndMethodName) {
        // 调用链路
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        // 正序遍历，直到业务代码的调用处为止
        for (StackTraceElement element : elements) {
            String className = element.getClassName();
            String methodName = element.getMethodName();

            // (类名、方法名组合或单独类名)
            boolean match = StreamUtil.of(ignoreClassAndMethodName).anyMatch(el -> {
                el = el.trim();
                // 带包路径或者是类名.方法名
                if (el.contains(".")) {
                    // 类权限名.方法名
                    String canonicalName = className + "." + methodName;
                    // 类名.方法名
                    String[] classNameSplit = className.split("\\.");
                    String lastClassName = classNameSplit[classNameSplit.length - 1];
                    String classNameWithMethodName = lastClassName + "." + methodName;

                    return Lists.newArrayList(canonicalName, classNameWithMethodName).contains(el);
                } else {
                    // 单独类名
                    return Objects.equals(el, className);
                }
            });

            // 先过滤自己的
            if ((Objects.equals(Thread.class.getName(), className) && Objects.equals("getStackTrace", methodName))
                    || (Objects.equals(LogUtil.class.getName(), className) && Objects.equals("getLastCallStackTraceElement", methodName))
                    // 过滤指定的 (类全限定名匹配，可以只写包名)
                    || (StreamUtil.of(ignoreClassPrefix).anyMatch(el -> className.startsWith(el.trim())))
                    || match) {

                continue;
            }

            // 返回匹配成功后的第一个
            return element;
        }

        return null;
    }


    /**
     * 获取当前的方法全限定名
     *
     * @return 当前方法全限定名
     */
    public static String getCurrentMethodQualifiedName() {
        // 调用链路
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();

        for (StackTraceElement element : elements) {
            String className = element.getClassName();
            String methodName = element.getMethodName();

            // 先过滤自己的
            if ((Objects.equals(Thread.class.getName(), className) && Objects.equals("getStackTrace", methodName))
                    || (Objects.equals(LogUtil.class.getName(), className) && Objects.equals("getCurrentMethodQualifiedName", methodName)))
                continue;

            // 过滤两个本身方法，直接返回
            return getShortQualifiedName(className) + "#" + methodName + "()";
        }

        return "";
    }


    /**
     * 获取简写全限定名
     *
     * @param clz 类
     * @return 简写全限定名
     */
    public static String getShortQualifiedName(Class<?> clz) {
        if (Objects.isNull(clz)) {
            return "";
        }

        return getShortQualifiedName(clz.getName());
    }


    /**
     * 获取简写全限定名
     *
     * @param canonicalName 类全限定名
     * @return 简写全限定名
     */
    public static String getShortQualifiedName(String canonicalName) {
        if (StringUtils.isBlank(canonicalName)) {
            return "";
        }

        String[] clzPaths = canonicalName.split("\\.");

        StringJoiner joiner = new StringJoiner(".");
        for (int i = 0; i < clzPaths.length; i++) {
            String path = clzPaths[i];

            if (i != clzPaths.length - 1) {
                joiner.add(path.charAt(0) + "");
            } else {
                joiner.add(path);
            }
        }

        return joiner.toString();
    }


}
