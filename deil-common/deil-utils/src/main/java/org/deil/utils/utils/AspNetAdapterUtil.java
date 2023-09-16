package org.deil.utils.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.deil.utils.log.LogUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * .Net适配工具，适配.Net各种方法
 */
@Slf4j
@UtilityClass
public class AspNetAdapterUtil {

    /**
     * 浮点数转整型
     * <p>.net使用的 Convert.ToInt32(decimal) 方法，会这样执行：</p>
     * <pre>
     *  1.如果整数位为偶数，则舍弃小数位，只保留整数
     *  2.如果整数位为奇数，则根据第一个小数位做四舍五入
     * </pre>
     *
     * @param decimal 浮点数
     * @return 整型
     */
    public static int convertInt(BigDecimal decimal) {
        // 判空
        if (Objects.isNull(decimal)) {
            // 返回.Net默认值 0
            return 0;
        }

        // 获取整数位
        int num = decimal.intValue();

        if ((num & 1) == 1) {
            // 奇数，做四舍五入
            decimal = decimal.setScale(0, RoundingMode.HALF_UP);
            num = decimal.intValue();
        }

        return num;
    }


    /**
     * 字符转Decimal
     * <p>.net使用的 Convert.ToDecimal(string) 方法，会这样执行：</p>
     * <pre>
     *  1.如果字符串有值，并且可以被转换，则正常转换；如果为null，则为默认值0
     * </pre>
     *
     * @param value 浮点数字符
     * @return 整型
     */
    public static BigDecimal convertDecimal(String value) {
        return (Objects.isNull(value)) ? BigDecimal.ZERO : new BigDecimal(value);
    }


    /**
     * 解析字符串为{@link Integer} （方便调用包装类的方法）
     *
     * @param source 字符串
     * @return 数值
     */
    public static Integer parseInt(String source) {
        int result;

        try {
            result = Integer.parseInt(source);
        } catch (Exception e) {
            log.error("{} 调用parseInteger() 转换失败，数据为：{}",
                    LogUtil.getLastCallStackTraceElement(new String[0], new String[]{"AspNetAdapterUtil.parseInt"}),
                    Objects.equals("", source) ? "''" : source);

            // 默认值
            result = -1;
        }

        return result;
    }


}
