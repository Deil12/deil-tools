package org.deil.utils.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.deil.utils.log.LogUtil;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;

/**
 * DES 加密工具类
 */
@Slf4j
@UtilityClass
public class DESUtils {

    /**
     * 默认密钥向量
     */
    private static final byte[] DES_IVKEY = {0x12, 0x34, 0x56, 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF};

    /**
     * 密钥
     */
    private static final String DES_CRYPTKEY = "A25CA3FA";

    /**
     * 密钥算法
     */
    private final String DES_ALGORITHM = "DES";

    /**
     * 加密/解密算法-工作模式-填充模式
     */
    private final String DES_PKCS5P = "DES/CBC/PKCS5Padding";

    /**
     * 默认编码
     */
    private final String DES_CHARSET = "UTF-8";

    /**
     * 生成key
     *
     * @param password
     * @return
     * @throws Exception
     */
    private Key generateKey(String password) throws Exception {
        DESKeySpec dks = new DESKeySpec(password.getBytes(DES_CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES_ALGORITHM);
        return keyFactory.generateSecret(dks);
    }


    /**
     * DES解密字符串
     *
     * @param data 待解密字符串
     * @return 解密后内容
     */
    public String decrypt(String data) {
        if (data == null) {
            return null;
        }
        try {
            Key secretKey = generateKey(DES_CRYPTKEY);
            Cipher cipher = Cipher.getInstance(DES_PKCS5P);
            IvParameterSpec iv = new IvParameterSpec(DES_IVKEY);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            return new String(cipher.doFinal(java.util.Base64.getDecoder().decode(data.getBytes(DES_CHARSET))), DES_CHARSET);
        } catch (Exception e) {
            log.error("\n{}", LogUtil.stacktraceToFiveLineString(e));
            // 2023/5/27 去除多余日志
            // e.printStackTrace();
            return data;
        }
    }

}
