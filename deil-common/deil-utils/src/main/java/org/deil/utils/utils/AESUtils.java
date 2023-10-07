package org.deil.utils.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.codec.binary.Base64;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Locale;

@UtilityClass
public class AESUtils {

    /**
     * 加密算法
     */
    private final String AES_ALGORITHM = "AES";

    /**
     * 加解密密钥
     */
    public final String AES_CRYPTKEY = "Ta125eHfk1_58eesw444ferfknygux87";

    /**
     * 算法/加密模式/填充方式
     * jd默认只支持PKCS5，以下代码修改jdk配置支持PKCS7
     *      Security.addProvider(new BouncyCastleProvider());
     */
    private final String AES_PKCS5P = "AES/CBC/PKCS5Padding";
    private final String AES_PKCS7P = "AES/CBC/PKCS7Padding";

    /**
     * 密钥向量
     */
    private final String AES_IVKEY = "c2b9de36-460b-47";

    /**
     * 默认编码
     */
    private final String AES_CHARSET = "UTF-8";

    /**
     * @param data
     * @return {@link String }
     * @TIME 2022/12/26 : 对data加密
     */
    public static String encrypt(String data) {
        return AESUtils.AESEncrypt(data, AES_CRYPTKEY);
    }

    /**
     * @param data
     * @return {@link String }
     * @TIME 2022/12/26 : 对data解密
     */
    public static String decrypt(String data) {
        return AESDecrypt(data, AES_CRYPTKEY);
    }

    /**
     * @param encryptedString 密文
     * @param key             密码
     * @return {@link String }
     * @throws RuntimeException 运行时异常
     * @TIME 2022/12/26 : aesdecrypt
     */
    public static String AESDecrypt(String encryptedString, String key) throws RuntimeException {
        try {
            /*Security.addProvider(new BouncyCastleProvider());*/
            byte[] keyArray = key.getBytes(AES_CHARSET);
            IvParameterSpec iv = new IvParameterSpec(AES_IVKEY.getBytes(AES_CHARSET));
            SecretKeySpec skeySpec = new SecretKeySpec(keyArray, AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_PKCS7P);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec,iv);
            byte[] utf8Sting = encryptedString.getBytes();
            byte[] encryptedData = Base64.decodeBase64(utf8Sting);
            byte[] original = cipher.doFinal(encryptedData);
            String originalString = new String(original, AES_CHARSET);
            return originalString;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param data 需加密明文数据
     * @param key  加密密码
     * @return {@link String }
     * @throws RuntimeException 运行时异常
     * @TIME 2022/12/26 : aesencrypt
     */
    public static String AESEncrypt(String data, String key) throws RuntimeException {
        try {
            /*Security.addProvider(new BouncyCastleProvider());*/
            byte[] plainBytes = data.getBytes(AES_CHARSET);
            byte[] keyBytes = key.getBytes(AES_CHARSET);
            IvParameterSpec iv = new IvParameterSpec(AES_IVKEY.getBytes(AES_CHARSET));
            SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_PKCS7P);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec,iv);
            byte[] encrypted = cipher.doFinal(plainBytes);
            String encryptedString = new String(Base64.encodeBase64(encrypted), AES_CHARSET);
            return encryptedString;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    private static byte[] shortMD5(String b) throws RuntimeException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(b.getBytes(AES_CHARSET));
            byte[] digest = md.digest();
            return digest;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param buf 二进制
     * @return {@link String }
     * @TIME 2022/12/26 : 解析byte2十六进制str
     */
    public static String parseByte2HexStr(byte[] buf) {
        if (null == buf) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase(Locale.US));
        }
        return sb.toString();
    }

    /**
     * @param hexStr 16进制
     * @return {@link byte[] }
     * @TIME 2022/12/26 : 解析十六进制str2字节
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * @return {@link String }
     * @TIME 2022/12/26 : 得到aeskey
     */
    public static String getAESKey() {
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(AES_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 要生成多少位，只需要修改128, 192或256
        kg.init(256);
        SecretKey sk = kg.generateKey();
        byte[] b = sk.getEncoded();
        String hexKey = parseByte2HexStr(b);
        return hexKey;
    }

}
