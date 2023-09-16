package org.deil.utils.web;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Sign工具类
 *
 * @DATE 2023/02/16
 * @CODE Deil
 */
public class SignUtil {

	private static Logger logger = LoggerFactory.getLogger(SignUtil.class); // 日志记录

	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	public static String getSignature(String appId, String appSecret, String uri, String timestamp, String nonce) {
		String sourceStr = appId + "|" + uri + "|" + timestamp + "|" + nonce;
		if (appSecret == null) {
			return null;
		}
		return genHMAC(sourceStr, appSecret);
	}

	/**
	 * HmacSHA1签名
	 *
	 * @param data 需加密数据
	 * @param key  密钥
	 * @return
	 */
	public  static  String genHMAC(String data, String key) {
		byte[] result = null;
		try {
			// 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
			SecretKeySpec signinKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
			// 生成一个指定 Mac 算法 的 Mac 对象
			Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			// 用给定密钥初始化 Mac 对象
			mac.init(signinKey);
			// 完成 Mac 操作
			byte[] rawHmac = mac.doFinal(data.getBytes());
			result = Base64.encodeBase64(rawHmac);

		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			logger.error(e.getMessage(), e);
		}
		if (null != result) {
			return new String(result);
		} else {
			return null;
		}
	}

}
