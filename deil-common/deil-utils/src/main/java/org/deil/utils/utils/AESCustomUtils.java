package org.deil.utils.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import lombok.experimental.UtilityClass;
import org.apache.commons.codec.binary.Base64;

//import org.bouncycastle.jce.provider.BouncyCastleProvider;

@UtilityClass
public class AESCustomUtils {
    public static final String KEY_ALGORITHMS = "AES";

    public static final int KEY_SIZE = 128;

    public static String getKeyAES_128() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey key = keyGen.generateKey();
        return Base64.encodeBase64String(key.getEncoded());
    }

    public static String getKeyAES_256() throws Exception {
        String base64str = getKeyAES_128();
        return base64str;
    }

    public static SecretKey loadKeyAES(String base64Key) {
        byte[] bytes = Base64.decodeBase64(base64Key);
        return new SecretKeySpec(bytes, "AES");
    }

    public static String encrypt(SecretKey key, String encryptData, String encode) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(1, key);
        byte[] encryptBytes = encryptData.getBytes(encode);
        byte[] result = cipher.doFinal(encryptBytes);
        return Base64.encodeBase64String(result);
    }

    public static String encrypt(String base64Key, String encryptData, String encode) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException {
        SecretKey key = loadKeyAES(base64Key);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(1, key);
        byte[] encryptBytes = encryptData.getBytes(encode);
        byte[] result = cipher.doFinal(encryptBytes);
        return Base64.encodeBase64String(result);
    }

    public static String decrypt(SecretKey key, String decryptData, String encode) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(2, key);
        byte[] decryptBytes = Base64.decodeBase64(decryptData);
        byte[] result = cipher.doFinal(decryptBytes);
        return new String(result, encode);
    }

    public static String decrypt(String base64Key, String decryptData, String encode) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException {
        SecretKey key = loadKeyAES(base64Key);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(2, key);
        byte[] decryptBytes = Base64.decodeBase64(decryptData);
        byte[] result = cipher.doFinal(decryptBytes);
        return new String(result, encode);
    }
}

