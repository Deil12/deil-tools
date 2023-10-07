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

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ApacheHttpUtil {

    private static Logger logger = LoggerFactory.getLogger(ApacheHttpUtil.class);

    private static RequestConfig requestConfig = null;

    public static final String APP_ID = "appId";

    public static final String TIMESTAMP = "timestamp";

    public static final String SIGN = "sign";

    public static final String NONCE = "nonce";

    // 网关地址
    String url;

    //appId请向开发人员索要
    String appId;

    //appSecret请向开发人员索要
    String appSecret;

    public ApacheHttpUtil(String url, String appId, String appSecret) {
        this.url = url;
        this.appId = appId;
        this.appSecret = appSecret;
    }

    static {
        // 设置请求和传输超时时间
        requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
    }

    /**
     * @time Time() : post请求传输json参数
     * @param url       url地址
     * @param jsonBody json参数
     * @return {@link JSONObject }
     */
    public JSONObject httpPost(String url, JSONObject jsonBody) {
        // post请求返回结果
        CloseableHttpClient httpClient = null;
        JSONObject jsonResult = new JSONObject();
        HttpPost httpPost = new HttpPost(this.url + url);
        // 设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        try {
            httpClient = new SSLClient();
            if (null != jsonBody) {
                System.out.println(jsonBody);
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(jsonBody.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
                httpPost.setEntity(entity);
                setHeader(url, appId, appSecret, httpPost);
                logger.info("请求参数：" + jsonBody.toJSONString());
            }
            CloseableHttpResponse result = httpClient.execute(httpPost);
            int statusCode = result.getStatusLine().getStatusCode();
            System.out.println("statusCode = " + statusCode);
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
                } catch (Exception e) {
                    logger.error("post请求提交失败:" + this.url + url, e);
                }
            } else {
                logger.info("post请求提交失败:{}", result);
            }
        } catch (Exception e) {
            logger.error("post请求提交失败");
        } finally {
            httpPost.releaseConnection();
            try {
                Optional.ofNullable(httpClient).orElse(new SSLClient()).close();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return jsonResult;
    }

    /**
     * @time Time() :
     * post请求传输String参数 例如：name=Jack&sex=1&type=2
     * Content-type:application/x-www-form-urlencoded
     * @param url      url地址
     * @param strParam 参数
     * @return {@link JSONObject }
     */
    public JSONObject httpPost(String url, String strParam) {
        // post请求返回结果
        HttpPost httpPost = new HttpPost(this.url + url);
        CloseableHttpClient httpClient = null;
        JSONObject jsonResult = new JSONObject();
        try {
            httpClient = new SSLClient();
            httpPost.setConfig(requestConfig);
            if (null != strParam) {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(strParam, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType(ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
                httpPost.setEntity(entity);
                setHeader(url, appId, appSecret, httpPost);
            }
            logger.info("请求参数：" + strParam);
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
                } catch (Exception e) {
                    logger.error("post请求提交失败:" + this.url + url, e);
                }
            }
        } catch (IOException e) {
            logger.error("post请求提交失败:" + url, e);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            logger.error(e1.getMessage());
        } finally {
            httpPost.releaseConnection();
            try {
                Optional.ofNullable(httpClient).orElse(new SSLClient()).close();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return jsonResult;
    }

    private void setHeader(String url, String appId, String appSecret, HttpRequestBase http) {
        String timestamps = new Date().getTime() + "";
        String nonce = UUID.randomUUID().toString();
        http.addHeader(APP_ID, appId);
        http.addHeader(TIMESTAMP, timestamps);
        http.addHeader(NONCE, nonce);
        http.addHeader(SIGN, SignUtil.getSignature(appId, appSecret, url, timestamps, nonce));
    }

}
