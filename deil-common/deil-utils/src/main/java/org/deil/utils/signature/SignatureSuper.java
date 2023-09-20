package org.deil.utils.signature;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.deil.utils.utils.RSAUtil;
import org.deil.utils.pojo.vo.VOKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
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

@Component
public class SignatureSuper {

	private static Logger log = LoggerFactory.getLogger(SignatureSuper.class);

	private static final String ENCODING = "UTF-8";
	private static String SIGN_PRI_KEY = "private.pem";
	private static String SIGN_PUB_KEY = "public.pem";

	private static String HTTP_DATA_KEY = "data";

	private static SignatureProperties signatureProperties;

	public SignatureSuper(SignatureProperties signatureProperties){
		SignatureSuper.signatureProperties = signatureProperties;
	}

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
				if (StringUtils.isNotBlank(String.valueOf(entry.getValue())) && !VOKey.SIGNKEY_SIGNATURE.equals(entry.getKey())) {
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
			dataMap.put(VOKey.SIGNKEY_SIGNATURE, sign);
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
			if (!treeMap.containsKey(VOKey.SIGNKEY_SIGNATURE)) {
				throw new RuntimeException("签名缺失");
				//return false;
			}
			if (!CollectionUtils.isEmpty(treeMap)) {
				StringBuffer buf = new StringBuffer();
				String signature = "";
				for (String key : treeMap.keySet()) {
					if (VOKey.SIGNKEY_APPSECRET.equals(key)) {
						if (signatureProperties.getAppInfo().containsKey(treeMap.get(VOKey.SIGNKEY_APPID))
								&& signatureProperties.getAppInfo().get(treeMap.get(VOKey.SIGNKEY_APPID)) != null
								&& signatureProperties.getAppInfo().get(treeMap.get(VOKey.SIGNKEY_APPID)).equals(treeMap.get(VOKey.SIGNKEY_APPSECRET))) {
							// 混淆值 appId、appSecret
							buf.append(key).append("=").append(treeMap.get(key)).append("&");
						} else {
							// 未申请appId、appSecret验签失败
							throw new RuntimeException("签名appSecret错误");
							//return false;
						}
						continue;
					}
					if (VOKey.SIGNKEY_TIMESTAMP.equals(key)) {
						/** {@link LocalDateTime} <yyyy-MM-ddTHH:mm:ss.sss/>*/
						//if (!(String.valueOf(Duration.between(LocalDateTime.parse(treeMap.get(VOKey.SIGNKEY_TIMESTAMP)), LocalDateTime.now()).toMinutes())).matches("^[0-2]$")) {
						/** {@link java.sql.Timestamp} 秒级时间戳*/
						if (Math.toIntExact(Duration.between(LocalDateTime.ofEpochSecond(Long.valueOf(treeMap.get(VOKey.SIGNKEY_TIMESTAMP)), 0, ZoneOffset.of("+8")), LocalDateTime.now()).toMinutes()) > signatureProperties.getOutOfTime()) {
							// 超时3分钟验签失败
							throw new RuntimeException("签名超时");
							//return false;
						}
					}

					if (StringUtils.isNoneBlank(treeMap.get(key)) && !VOKey.SIGNKEY_SIGNATURE.equals(key)) {
						buf.append(key).append("=").append(treeMap.get(key)).append("&");
					} else if (VOKey.SIGNKEY_SIGNATURE.equals(key)) {
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
						SignatureSuper.class.getClassLoader().getResourceAsStream(SIGN_PRI_KEY),
						ENCODING);
		return privatekey;
	}

	private static String getPublickey() throws IOException {
		String publickey =
				IOUtils.toString(
						SignatureSuper.class.getClassLoader().getResourceAsStream(SIGN_PUB_KEY),
						ENCODING);
		return publickey;
	}

	private static String getTimeStamp(String date) {
		return Date.from(LocalDateTime.parse(date/*.substring(0, date.indexOf("."))*/.replace("-", "").replace("T", "").replace(" ", "").replace(":", ""), DateTimeFormatter.ofPattern("yyyyMMddHHmmss.SSS")).atZone(ZoneId.systemDefault()).toInstant()).toString();
	}
	//endregion

}
