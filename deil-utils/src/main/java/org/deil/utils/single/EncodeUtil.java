package org.deil.utils.single;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.BitSet;

/**
 * @DATE 06/27
 */
@Slf4j
@UtilityClass
public class EncodeUtil {
    private static int BYTE_SIZE = 8;
    public static String CODE_UTF8 = "UTF-8";
    public static String CODE_UTF8_BOM = "UTF-8_BOM";
    public static String CODE_GBK = "GBK";
    public static String CODE_UNICODE = "Unicode";
    public static String CODE_UTF16 = "UTF-16";

    /**
     * 通过文件获取编码集名称
     *
     * @param file
     * @param ignoreBom
     * @return
     * @throws Exception
     */
    public static String getEncode(File file, boolean ignoreBom) throws Exception {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        return getEncode(bis, ignoreBom);
    }

    /**
     * 通过文件缓存流获取编码集名称
     *
     * @param bis
     * @return
     * @throws Exception
     */
    public static String getEncode(@NonNull BufferedInputStream bis, boolean ignoreBom) throws Exception {
        bis.mark(0);

        String encodeType = StringUtils.EMPTY;
        byte[] head = new byte[3];
        bis.read(head);
        if (head[0] == -1 && head[1] == -2) {
            encodeType = CODE_UTF16;
        } else if (head[0] == -2 && head[1] == -1) {
            encodeType = CODE_UNICODE;
        } //带BOM
        else if (head[0] == -17 && head[1] == -69 && head[2] == -65) {
            if (ignoreBom) {
                encodeType = CODE_UTF8;
            } else {
                encodeType = CODE_UTF8_BOM;
            }
        } else if (CODE_UNICODE.equals(encodeType)) {
            encodeType = CODE_UTF16;
        } else if (isUTF8(bis)) {
            encodeType = CODE_UTF8;
        } else {
            encodeType = CODE_GBK;
        }
        return encodeType;
    }

    /**
     * 是否是无BOM的UTF8格式，不判断常规场景，只区分无BOM UTF8和GBK
     *
     * @param bis
     * @return
     */
    private static boolean isUTF8(@NonNull BufferedInputStream bis) throws Exception {
        bis.reset();
        int code = bis.read();
        do {
            BitSet bitSet = convert2BitSet(code);
            //判断是否为单字节
            if (bitSet.get(0)) {
                //多字节时，再读取N个字节
                if (!checkMultiByte(bis, bitSet)) {
                    return false;
                }
            } else {
                //单字节时什么都不用做，再次读取字节
            }
            code = bis.read();
        } while (code != -1);
        return true;
    }

    /**
     * 检测多字节，判断是否为utf8，已经读取了一个字节
     *
     * @param bis
     * @param bitSet
     * @return
     */
    private static boolean checkMultiByte(@NonNull BufferedInputStream bis, @NonNull BitSet bitSet) throws Exception {
        int count = getCountOfSequential(bitSet);
        //已经读取了一个字节，不能再读取
        byte[] bytes = new byte[count - 1];
        bis.read(bytes);
        for (byte b : bytes) {
            if (!checkUtf8Byte(b)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检测单字节，判断是否为utf8
     *
     * @param b
     * @return
     */
    private static boolean checkUtf8Byte(byte b) {
        BitSet bitSet = convert2BitSet(b);
        return bitSet.get(0) && !bitSet.get(1);
    }

    /**
     * 检测bitSet中从开始有多少个连续的1
     *
     * @param bitSet
     * @return
     */
    private static int getCountOfSequential(@NonNull BitSet bitSet) {
        int count = 0;
        for (int i = 0; i < BYTE_SIZE; i++) {
            if (bitSet.get(i)) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }


    /**
     * 将整形转为BitSet
     *
     * @param code
     * @return
     */
    private static BitSet convert2BitSet(int code) {
        BitSet bitSet = new BitSet(BYTE_SIZE);

        for (int i = 0; i < BYTE_SIZE; i++) {
            int tmp3 = code >> (BYTE_SIZE - i - 1);
            int tmp2 = 0x1 & tmp3;
            if (tmp2 == 1) {
                bitSet.set(i);
            }
        }
        return bitSet;
    }

    /**
     * 将一指定编码的文件转换为另一编码的文件
     *
     * @param oldFullFileName
     * @param oldCharsetName
     * @param newFullFileName
     * @param newCharsetName
     */
    public static void convert(String oldFullFileName, String oldCharsetName, String newFullFileName, String newCharsetName) throws Exception {
        log.info("the old file name is : {}, The oldCharsetName is : {}", oldFullFileName, oldCharsetName);
        log.info("the new file name is : {}, The newCharsetName is : {}", newFullFileName, newCharsetName);

        StringBuilder content = new StringBuilder();

        @Cleanup
        BufferedReader bin = new BufferedReader(new InputStreamReader(new FileInputStream(oldFullFileName), oldCharsetName));
        try {
            String line;
            while ((line = bin.readLine()) != null) {
                content.append(line);
                content.append(System.getProperty("line.separator"));
            }
            newFullFileName = newFullFileName.replace("\\", "/");
            File dir = new File(newFullFileName.substring(0, newFullFileName.lastIndexOf("/")));
            if (!dir.exists()) {
                dir.mkdirs();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            bin.close();
        }
        @Cleanup
        Writer out = new OutputStreamWriter(new FileOutputStream(newFullFileName), newCharsetName);
        try {
            out.write(content.toString());
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            out.close();
        }
    }

}
