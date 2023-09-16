package org.deil.utils.utils;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@UtilityClass
public class RSAUtil {

	public static final String SIGN_SHA1_WITH_RSA = "SHA1WithRSA";

	public static final String SIGN_SHA256_WITH_RSA = "SHA256WithRSA";

	public static String sign(String content, PrivateKey privateKey, String input_charset) throws Exception {
		if (privateKey == null) {
			throw new RuntimeException("加密私钥为空, 请设置");
		}
		java.security.Signature signature = java.security.Signature.getInstance(SIGN_SHA1_WITH_RSA);
		signature.initSign(privateKey);
		signature.update(content.getBytes(input_charset));
		return Base64.encode(signature.sign());
	}

	public static boolean verify(String content, String sign, PublicKey publicKey, String inputCharset) {
		try {
			java.security.Signature signature = java.security.Signature.getInstance(SIGN_SHA1_WITH_RSA);
			signature.initVerify(publicKey);
			signature.update(content.getBytes(inputCharset));
			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String signSHA256RSA(String content, PrivateKey privateKey, String input_charset)
			throws Exception {
		if (privateKey == null) {
			throw new RuntimeException("加密私钥为空, 请设置");
		}
		java.security.Signature signature = java.security.Signature.getInstance(SIGN_SHA256_WITH_RSA);
		signature.initSign(privateKey);
		signature.update(content.getBytes(input_charset));
		return Base64.encode(signature.sign());
	}
	
	public static boolean verifySHA256(String content, String sign, PublicKey publicKey, String inputCharset) {
		try {
			java.security.Signature signature = java.security.Signature.getInstance(SIGN_SHA256_WITH_RSA);
			signature.initVerify(publicKey);
			signature.update(content.getBytes(inputCharset));
			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static PrivateKey getPrivateKey(String key) throws Exception {
		byte[] keyBytes = buildPKCS8Key(key);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}
	
	private static byte[] buildPKCS8Key(String privateKey) throws IOException {
		if (privateKey.contains("-----BEGIN PRIVATE KEY-----")) {
			return Base64.decode(privateKey.replaceAll("-----\\w+ PRIVATE KEY-----", ""));
		} else if (privateKey.contains("-----BEGIN RSA PRIVATE KEY-----")) {
			final byte[] innerKey = Base64.decode(privateKey.replaceAll("-----\\w+ RSA PRIVATE KEY-----", ""));
			final byte[] result = new byte[innerKey.length + 26];
			System.arraycopy(Base64.decode("MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKY="), 0, result, 0, 26);
			System.arraycopy(BigInteger.valueOf(result.length - 4).toByteArray(), 0, result, 2, 2);
			System.arraycopy(BigInteger.valueOf(innerKey.length).toByteArray(), 0, result, 24, 2);
			System.arraycopy(innerKey, 0, result, 26, innerKey.length);
			return result;
		} else {
			return Base64.decode(privateKey);
		}
	}
	
	public static PublicKey getPublicKey(String key) throws Exception {
		if (key == null) {
			throw new RuntimeException("加密公钥为空, 请设置");
		}
		key = key.replaceAll("\\-{5}[\\w\\s]+\\-{5}[\\r\\n|\\n]", "");
		byte[] buffer = Base64.decode(key);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
		return keyFactory.generatePublic(keySpec);
	}

}

