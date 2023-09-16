import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * 加验签逻辑（重要）
 *
 * @DATE 2023/04/10
 * @CODE Deil
 * @SINCE 1.1.0
 */
@UtilityClass
public class DemoSignatureSuper {

	private static Logger log = LoggerFactory.getLogger(DemoSignatureSuper.class);

	private static final String ENCODING = "UTF-8";
	private static String SIGN_PRI_KEY = "private.pem";
	private static String SIGN_PUB_KEY = "public.pem";

	private static String HTTP_DATA_KEY = "data";

	public static String SIGNKEY_APPID = "appid";
	public static String SIGNKEY_APPSECRET = "appsecret";
	public static String SIGNKEY_TIMESTAMP = "timestamp";
	public static String SIGNKEY_SIGNATURE = "signature";

	// 初始化请求参数
	protected static Map<String, String> initRequsetMsg() {
		Map<String, String> reqMsg = new HashMap<>();
		// 初始混淆值

		return reqMsg;
	}

	/**
	 * 加签
	 *
	 * @param reqMap
	 * @return {@link Map }<{@link String }, {@link String }>
	 * @time 2023/03/11
	 */
	protected static Map<String, String> packSigns(Map<String, String> reqMap) {
		try {
			Map<String, String> dataMap = new HashMap<>();
			dataMap.putAll(reqMap);
			TreeMap<String, String> tMap = new TreeMap<String, String>();
			for (Entry<String, String> entry : dataMap.entrySet()) {
				if (StringUtils.isNotBlank(String.valueOf(entry.getValue())) && !SIGNKEY_SIGNATURE.equals(entry.getKey())) {
					tMap.put(entry.getKey(), entry.getValue());
				}
			}
			StringBuffer buf = new StringBuffer();
			for (String key : tMap.keySet()) {
				buf.append(key).append("=").append(String.valueOf(tMap.get(key))).append("&");
			}
			String signatureStr = buf.substring(0, buf.length() - 1);
			log.debug("加签数据:{}", signatureStr);
			PrivateKey privateKey = RSAUtil.getPrivateKey(getPrivatekey());
			// 加签
			String sign = RSAUtil.sign(signatureStr, privateKey, ENCODING);
			dataMap.put(SIGNKEY_SIGNATURE, sign);
			return dataMap;
		} catch (Exception e) {
			log.info("加签异常:{}", reqMap.get(HTTP_DATA_KEY));
			e.printStackTrace();
		}
		return reqMap;
	}

	/**
	 * 验签
	 *
	 * @param dataBody
	 * @return boolean
	 * @time 2023/03/11
	 */
	protected static boolean verifySigns(String dataBody){
		JSONObject jsonObject = JSON.parseObject(dataBody);
		try {
			TreeMap<String, String> treeMap = new TreeMap<>();
			treeMap.putAll(initRequsetMsg());
			for (Entry<String, Object> entry : jsonObject.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue().toString();
				treeMap.put(key, value);
			}
			if (!treeMap.containsKey(SIGNKEY_SIGNATURE)) {
				throw new RuntimeException("签名缺失");
				//return false;
			}
			if (!CollectionUtils.isEmpty(treeMap)) {
				StringBuffer buf = new StringBuffer();
				String signature = "";
				for (String key : treeMap.keySet()) {
					if (SIGNKEY_APPSECRET.equals(key)) {
						if (true/*signatureProperties.getAppInfo().containsKey(treeMap.get(HTTP_APPID_KEY))
								&& signatureProperties.getAppInfo().get(treeMap.get(HTTP_APPID_KEY)) != null
								&& signatureProperties.getAppInfo().get(treeMap.get(HTTP_APPID_KEY)).equals(treeMap.get(HTTP_APPSECRET_KEY))*/) {
							// 混淆值 appId、appSecret
							buf.append(key).append("=").append(treeMap.get(key)).append("&");
						} else {
							// 未申请appId、appSecret验签失败
							throw new RuntimeException("签名appSecret错误");
							//return false;
						}
						continue;
					}
					if (SIGNKEY_TIMESTAMP.equals(key)) {
						/** {@link LocalDateTime} <yyyy-MM-ddTHH:mm:ss.sss/>*/
						//if (!(String.valueOf(Duration.between(LocalDateTime.parse(treeMap.get(SIGNKEY_TIMESTAMP)), LocalDateTime.now()).toMinutes())).matches("^[0-2]$")) {
						/** {@link java.sql.Timestamp} 秒级时间戳*/
						if (Math.toIntExact(Duration.between(LocalDateTime.ofEpochSecond(Long.valueOf(treeMap.get(SIGNKEY_TIMESTAMP)), 0, ZoneOffset.of("+8")), LocalDateTime.now()).toMinutes()) > 3/*signatureProperties.getOutOfTime()*/) {
							// 超时3分钟验签失败
							throw new RuntimeException("签名超时");
							//return false;
						}
					}

					if (StringUtils.isNoneBlank(treeMap.get(key)) && !SIGNKEY_SIGNATURE.equals(key)) {
						buf.append(key).append("=").append(treeMap.get(key)).append("&");
					} else if (SIGNKEY_SIGNATURE.equals(key)) {
						signature = treeMap.get(key).replace(" ", "+");
					}
				}
				String signData = buf.substring(0, buf.length() - 1);
				log.debug("验签数据:{}", signData);
				PublicKey publicKey = RSAUtil.getPublicKey(getPublickey());

				// 验签
				return RSAUtil.verify(signData, signature, publicKey, ENCODING);
			}
		} catch (Exception e) {
			throw new RuntimeException("验签异常, [" + e.getMessage() + "]");
		}
		return false;
	}

	//region 。。。
	private static String getPrivatekey() throws IOException {
		String privatekey =
				IOUtils.toString(
						DemoSignatureSuper.class.getClassLoader().getResourceAsStream(SIGN_PRI_KEY),
						ENCODING);
		return privatekey;
	}

	private static String getPublickey() throws IOException {
		String publickey =
				IOUtils.toString(
						DemoSignatureSuper.class.getClassLoader().getResourceAsStream(SIGN_PUB_KEY),
						ENCODING);
		return publickey;
	}

	private static String getTimeStamp(String date) {
		return Date.from(LocalDateTime.parse(date/*.substring(0, date.indexOf("."))*/.replace("-", "").replace("T", "").replace(" ", "").replace(":", ""), DateTimeFormatter.ofPattern("yyyyMMddHHmmss.SSS")).atZone(ZoneId.systemDefault()).toInstant()).toString();
	}
	//endregion

}

/**
 * RSA加密工具（重要）
 *
 * @DATE 2023/04/10
 * @CODE Deil
 * @SINCE 1.1.0
 */
@UtilityClass
class RSAUtil {

	public static final String SIGN_SHA1_WITH_RSA = "SHA1WithRSA";

	public static final String SIGN_SHA256_WITH_RSA = "SHA256WithRSA";

	public static String sign(String content, PrivateKey privateKey, String input_charset) throws Exception {
		if (privateKey == null) {
			throw new RuntimeException("加密私钥为空, 请设置");
		}
		java.security.Signature signature = java.security.Signature.getInstance(SIGN_SHA1_WITH_RSA);
		signature.initSign(privateKey);
		signature.update(content.getBytes(input_charset));
		//return new String(java.util.Base64.getEncoder().encode(signature.sign()));
		return Base64.encode(signature.sign());
	}

	public static boolean verify(String content, String sign, PublicKey publicKey, String inputCharset) {
		try {
			java.security.Signature signature = java.security.Signature.getInstance(SIGN_SHA1_WITH_RSA);
			signature.initVerify(publicKey);
			signature.update(content.getBytes(inputCharset));
			//boolean bverify = signature.verify(java.util.Base64.getDecoder().decode(sign.getBytes("UTF-8")));
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

/**
 * 自定义Base64（重要）
 *
 * @DATE 2023/04/10
 * @CODE Deil
 * @SINCE 1.1.0
 */
class Base64 {

	private static char[] base64EncodeChars = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
			'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
			'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
			'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', '+', '/' };

	private static byte[] base64DecodeChars = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1,
			-1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1,
			-1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
			13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1,
			-1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36,
			37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51,
			-1, -1, -1, -1, -1 };

	private Base64() {}

	public static String encode(byte[] data) {
		int len = data.length;
		int i = 0;
		int b1, b2, b3;
		StringBuilder sb = new StringBuilder(len);

		while (i < len) {
			b1 = data[i++] & 0xff;
			if (i == len) {
				sb.append(base64EncodeChars[b1 >>> 2]);
				sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
				sb.append("==");
				break;
			}
			b2 = data[i++] & 0xff;
			if (i == len) {
				sb.append(base64EncodeChars[b1 >>> 2]);
				sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
				sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
				sb.append("=");
				break;
			}
			b3 = data[i++] & 0xff;
			sb.append(base64EncodeChars[b1 >>> 2]);
			sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
			sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
			sb.append(base64EncodeChars[b3 & 0x3f]);
		}
		return sb.toString();
	}

	public static byte[] decode(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		byte[] data = str.getBytes();
		int len = data.length;
		ByteArrayOutputStream buf = new ByteArrayOutputStream(len);
		int i = 0;
		int b1, b2, b3, b4;

		while (i < len) {

			/* b1 */
			do {
				b1 = base64DecodeChars[data[i++]];
			} while (i < len && b1 == -1);
			if (b1 == -1) {
				break;
			}

			/* b2 */
			do {
				b2 = base64DecodeChars[data[i++]];
			} while (i < len && b2 == -1);
			if (b2 == -1) {
				break;
			}
			buf.write((int) ((b1 << 2) | ((b2 & 0x30) >>> 4)));

			/* b3 */
			do {
				b3 = data[i++];
				if (b3 == 61) {
					return buf.toByteArray();
				}
				b3 = base64DecodeChars[b3];
			} while (i < len && b3 == -1);
			if (b3 == -1) {
				break;
			}
			buf.write((int) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));

			/* b4 */
			do {
				b4 = data[i++];
				if (b4 == 61) {
					return buf.toByteArray();
				}
				b4 = base64DecodeChars[b4];
			} while (i < len && b4 == -1);
			if (b4 == -1) {
				break;
			}
			buf.write((int) (((b3 & 0x03) << 6) | b4));
		}
		return buf.toByteArray();
	}

	public static String encodeStr(String str, String charset) throws UnsupportedEncodingException {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		return encode(str.getBytes(charset));
	}

	public static String decodeStr(String str, String charset) throws UnsupportedEncodingException {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		byte[] res = decode(str);
		return new String(res);
	}

}

/**
 * 测试（自行删除）
 *
 * @DATE 2023/04/10
 * @CODE Deil
 * @SINCE 1.1.0
 */
class DemoTest {

	/**
	 * 接口调用前<p>签名混淆值</p>封装
	 *
	 * @return {@link Map }<{@link String }, {@link String }>
	 * @time 2023/04/10
	 * @since 1.1.0
	 */
	protected static Map<String, String> init() {
		Map<String, String> initMap = new HashMap<>();
		initMap.put("appId", "Deil");
		initMap.put("appSecret", "12345678");
		//initMap.put("timeStamp", "2023-04-16T10:07:41.230");
		initMap.put("timeStamp", LocalDateTime.now().toString());
		return initMap;
	}

	public static void main(String[] args) throws Exception {
		Map<String, String> signMap = init();
		//封装请求数据
		signMap.put("data", "请求数据");
		//打包签名数据
		Map<String, String> reqMap = DemoSignatureSuper.packSigns(signMap);

		System.out.println("reqMap = " + reqMap + "\n" +
				"signature = " + reqMap.get("signature") + "\n" +
				"result = " + DemoSignatureSuper.verifySigns(JSONObject.toJSONString(reqMap)));
	}

}
