package org.deil.utils.web;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Deprecated
public class ApacheHttpUtil {

    private static Logger log = LoggerFactory.getLogger(ApacheHttpUtil.class);

    private static RequestConfig requestConfig = null;

    public static final String APP_ID = "appId";

    public static final String TIMESTAMP = "timestamp";

    public static final String SIGN = "sign";

    public static final String NONCE = "nonce";

    // 网关地址
    String uri;

    //appId请向开发人员索要
    String appId;

    //appSecret请向开发人员索要
    String appSecret;

    public ApacheHttpUtil(String uri, String appId, String appSecret) {
        this.uri = uri;
        this.appId = appId;
        this.appSecret = appSecret;
    }

    static {
        // 设置请求和传输超时时间
        requestConfig = RequestConfig.custom()
                .setSocketTimeout(10000)
                .setConnectTimeout(10000)
                .build();
    }

    private void setHeader(String url, String appId, String appSecret, HttpRequestBase http) {
        String timestamps = new Date().getTime() + "";
        String nonce = UUID.randomUUID().toString().replace("-", "");
        http.addHeader(APP_ID, appId);
        http.addHeader(TIMESTAMP, timestamps);
        http.addHeader(NONCE, nonce);
        http.addHeader(SIGN, SignUtil.getSignature(appId, appSecret, url, timestamps, nonce));
    }

    /**
     * @time Time() : post请求传输json参数
     * @param api       url地址
     * @param jsonBody json参数
     * @return {@link JSONObject }
     */
    public JSONObject post(String api, JSONObject jsonBody) {
        String url = this.uri + api;
        // post请求返回结果
        CloseableHttpClient httpClient = null;
        JSONObject jsonResult = new JSONObject();
        HttpPost httpPost = new HttpPost(url);
        // 设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        try {
            httpClient = new SSLClient();
            if (!ObjectUtils.isEmpty(jsonBody)) {
                log.info(jsonBody.toString());
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(jsonBody.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
                httpPost.setEntity(entity);
                setHeader(api, appId, appSecret, httpPost);
                log.info("请求参数：" + jsonBody.toJSONString());
            }
            CloseableHttpResponse result = httpClient.execute(httpPost);
            int statusCode = result.getStatusLine().getStatusCode();
            log.info("statusCode = " + statusCode);
            // 请求发送成功，并得到响应
            if (statusCode == HttpStatus.SC_OK) {
                String str = "";
                try {
                    // 读取服务器返回过来的json字符串数据
                    str = EntityUtils.toString(result.getEntity(), "utf-8");
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> map = mapper.readValue(str, Map.class);
                    // 把json字符串转换成json对象
                    //logger.info("响应参数：" + str);
                    jsonResult = JSONObject.parseObject(JSONObject.toJSONString(map));
                    log.info("[{}]请求成功:{}" + url, jsonResult);
                } catch (Exception e) {
                    log.error("[{}]请求异常:{}", url, e.getCause());
                    e.printStackTrace();
                }
            } else {
                log.info("[{}]请求失败:{}" + url, result);
            }
        } catch (Exception e) {
            log.error("[{}]请求异常:{}", url, e.getCause());
            e.printStackTrace();
        } finally {
            httpPost.releaseConnection();
            try {
                Optional.ofNullable(httpClient).orElse(new SSLClient()).close();
            } catch (Exception e) {
                log.error("[{}]请求组件异常:{}", url, e.getCause());
                e.printStackTrace();
            }
        }
        return jsonResult;
    }

    /**
     * @time Time() :
     * post请求传输String参数 例如：name=Jack&sex=1&type=2
     * Content-type:application/x-www-form-urlencoded
     * @param api      url地址
     * @param strParam 参数
     * @return {@link JSONObject }
     */
    public JSONObject post(String api, String strParam) {
        String url = this.uri + api;
        // post请求返回结果
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient httpClient = null;
        JSONObject jsonResult = new JSONObject();
        try {
            httpClient = new SSLClient();
            httpPost.setConfig(requestConfig);
            if (!ObjectUtils.isEmpty(strParam)) {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(strParam, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType(ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
                httpPost.setEntity(entity);
                setHeader(api, appId, appSecret, httpPost);
            }
            log.info("请求参数：" + strParam);
            CloseableHttpResponse result = httpClient.execute(httpPost);
            // 请求发送成功，并得到响应
            if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String str = "";
                try {
                    // 读取服务器返回过来的json字符串数据
                    str = EntityUtils.toString(result.getEntity(), "utf-8");
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> map = mapper.readValue(str, Map.class);
                    //logger.info("响应参数：" + str);
                    // 把json字符串转换成json对象
                    jsonResult = JSONObject.parseObject(JSONObject.toJSONString(map));
                    log.info("[{}]请求成功:{}" + url, jsonResult);
                } catch (Exception e) {
                    log.error("[{}]请求异常:{}", url, e.getCause());
                    e.printStackTrace();
                }
            } else {
                log.info("[{}]请求失败:{}" + url, result);
            }
        } catch (Exception e) {
            log.error("[{}]请求异常:{}", url, e.getCause());
            e.printStackTrace();
        } finally {
            httpPost.releaseConnection();
            try {
                Optional.ofNullable(httpClient).orElse(new SSLClient()).close();
            } catch (Exception e) {
                log.error("[{}]请求组件异常:{}", url, e.getCause());
                e.printStackTrace();
            }
        }
        return jsonResult;
    }

}
