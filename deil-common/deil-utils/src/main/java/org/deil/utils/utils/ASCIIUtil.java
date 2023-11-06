package org.deil.utils.utils;

import lombok.experimental.UtilityClass;

/**
 * 
 *
 * @DATE 2023.10.30
 * @CODE Deil
 */
@UtilityClass
public class ASCIIUtil {

    public static int compareByName(String name1, String name2) {
        final byte[] bytes1 = name1.getBytes();
        final byte[] bytes2 = name2.getBytes();
        int i = 0;
        byte b1, b2;
        int numCompare = 0;//如果都是数字, 那么需要比较连续数字的大小, 只要高位大, 这个数字就大
        for (; i < Math.min(bytes1.length, bytes2.length); i++) {
            b1 = bytes1[i];
            b2 = bytes2[i];
            if (b1 != b2) {//只有ascii不相等时才比较
                if (numCompare != 0
                        && !(b1 >= 48 && b1 <= 57)
                        && !(b2 >= 48 && b2 <= 57)) {//已经出现过不等的数字，并且这个循环都是字符的情况
                    return numCompare;
                }
                if (b1 >= 48 && b1 <= 57
                        && b2 >= 48 && b2 <= 57) {//只有都是数字才会进入
                    if (numCompare == 0)
                        numCompare = Byte.compare(b1, b2);
                } else {//其中一个是数字，或者都是字符
                    if (numCompare != 0) {//已经出现过数字，那么本次循环哪个是数字，说明哪个数字位数多，那么这个数字就大
                        if (b1 >= 48 && b1 <= 57)
                            return 1;
                        if (b2 >= 48 && b2 <= 57)
                            return -1;
                    }
                    return Byte.compare(b1, b2);
                }
            }
        }
        if (numCompare == 0)//说明长度较小的部分完全一样，比较哪个长度大
            return Integer.compare(bytes1.length, bytes2.length);
        else {
            if (bytes1.length > bytes2.length) {
                if (bytes1[i] >= 48 && bytes1[i] <= 57)
                    return 1;
                else
                    return numCompare;
            }
            if (bytes1.length < bytes2.length) {
                if (bytes2[i] >= 48 && bytes2[i] <= 57)
                    return -1;
                else
                    return numCompare;
            }
            return numCompare;//出现过数字且不相同，并且最后一位不是字符
        }
    }

}
