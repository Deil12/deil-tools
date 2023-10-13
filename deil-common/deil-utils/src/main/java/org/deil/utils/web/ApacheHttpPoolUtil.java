package org.deil.utils.web;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
//@Configuration
@EnableConfigurationProperties(HttpProperties.class)
public class ApacheHttpPoolUtil {
    private static Logger log = LoggerFactory.getLogger(ApacheHttpPoolUtil.class);

    private static CloseableHttpClient httpClient = null;
    private static final Charset CHAR_SET = Consts.UTF_8;
    private static PoolingHttpClientConnectionManager cm;

    public static final String APP_ID = "appId";
    public static final String APP_SECRET = "appSecret";
    public static final String TIMESTAMP = "timestamp";
    public static final String NONCE = "nonce";
    public static final String SIGN = "sign";
    String uri;
    String appId;
    String appSecret;
    String timestamp;
    String nonce;
    String sign;

    public ApacheHttpPoolUtil() {
        init();
        this.uri = "";
    }

    public ApacheHttpPoolUtil(String uri) {
        this.uri = uri;
    }

    public ApacheHttpPoolUtil(String uri, @Nullable String appId, @Nullable String appSecret, @Nullable String timestamp, @Nullable String nonce, @Nullable String sign) {
        this.uri = uri;
        this.appId = appId;
        this.appSecret = appSecret;
        this.timestamp = timestamp;
        this.nonce = nonce;
        this.sign = sign;
    }

    private void init() {
        cm = new PoolingHttpClientConnectionManager();
        //设置最大连接数
        cm.setMaxTotal(300);
        //设置每个主机地址的并发数
        //cm.setDefaultMaxPerRoute(100);
        cm.setDefaultConnectionConfig(
                ConnectionConfig.custom()
                        .setCharset(CHAR_SET)
                        .build()
        );
        cm.setDefaultSocketConfig(
                SocketConfig.custom()
                        .setSoTimeout(30000)
                        .setSoReuseAddress(true)
                        .build()
        );
        httpClient = HttpClientBuilder.create().setConnectionManager(cm).build();
        log.info("[\033[0;32mApacheHttp连接池初始化...\033[0m]", cm.getTotalStats().toString());
    }

    private CloseableHttpClient getHttpClient() {
        int timeout = 5;
        CloseableHttpClient _httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                /*.setDefaultRequestConfig(
                        RequestConfig.custom()
                                .setConnectTimeout(timeout * 1000) //设置连接超时时间，单位毫秒
                                //.setConnectionRequestTimeout(timeout * 1000) //设置从connect Manager获取Connection 超时时间，单位毫秒
                                .setSocketTimeout(timeout * 1000)
                                .build() //请求获取数据的超时时间，单位毫秒
                ) //使用此方法连接池会关闭*/
                .setConnectionManagerShared(true)
                .build();
        if (cm != null && cm.getTotalStats() != null) {
            log.info("\033[0;34mcurrent client pool: {}\033[0m", cm.getTotalStats().toString());
        }
        return _httpClient;
    }

    private void setHeader(String api, String appId, String appSecret, String timestamp, String nonce, String sign, HttpRequestBase requestBase) {
        //requestBase.addHeader("auth_token", tokenStr);
        requestBase.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");
        requestBase.setHeader("Content-type", "application/json;charset=utf-8");
        requestBase.setHeader("Connection", "Close");

        requestBase.addHeader(APP_ID, appId);
        requestBase.addHeader(APP_SECRET, appSecret);
        requestBase.addHeader(TIMESTAMP, timestamp);
        requestBase.addHeader(NONCE, nonce);
        requestBase.addHeader(SIGN, sign);
    }

    public String get(String api) {
        return this.get(api, new JSONObject());
    }

    public String get(String api, JSONObject jsonBody) {
        String url = this.uri + api;
        HttpGet httpGet = new HttpGet(url);
        try {
            if (httpClient == null) {
                // httpClient = HttpClientBuilder.create().build();
                httpClient = HttpClients.custom()
                        .setConnectionManager(cm)
                        .setConnectionManagerShared(true)
                        .build();
            }
            httpClient = getHttpClient();
            setHeader(api, appId, appSecret, timestamp, nonce, sign, httpGet);
            if (!ObjectUtils.isEmpty(jsonBody)) {
                log.info("[{}]请求参数:{}", url, jsonBody.toJSONString());
                for (Map.Entry<String, Object> entry : jsonBody.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue().toString());//设置请求参数
                }
            }

            HttpResponse response = httpClient.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                String res = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
                httpGet.releaseConnection();
                log.info("[{}]请求成功:{}", url, res);
                return res;
            } else {
                log.info("[{}]请求失败:{}", url, response.getEntity());
            }
        } catch (Exception e) {
            log.error("[{}]请求异常:{}", url, e.getCause());
            e.printStackTrace();
        } finally {
            if (httpGet != null)
                httpGet.releaseConnection();
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    log.error("[{}]请求组件异常:{}", url, e.getCause());
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public String post(String api) {
        return this.post(api, new JSONObject());
    }

    /**
     * @date 2023/10/12, 上午10:53
     * @param api
     * @param strBody a=1&b=2
     * @return java.lang.String
     */
    public String post(String api, String strBody) {
        String url = this.uri + api;
        HttpPost httpPost = new HttpPost(url);
        try {
            if (httpClient == null) {
                // httpClient = HttpClientBuilder.create().build();
                httpClient = HttpClients.custom()
                        .setConnectionManager(cm)
                        .setConnectionManagerShared(true)
                        .build();
            }
            httpClient = getHttpClient();
            setHeader(api, appId, appSecret, timestamp, nonce, sign, httpPost);
            if (!ObjectUtils.isEmpty(strBody)) {
                log.info("[{}]请求参数:{}", url, strBody);
                StringEntity entity = new StringEntity(strBody, Consts.UTF_8);
                entity.setContentEncoding(Consts.UTF_8.name());
                entity.setContentType("application/x-www-form-urlencoded");//发送json数据需要设置contentType
                httpPost.setEntity(entity);//设置请求参数
            }

            HttpResponse response = httpClient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                String res = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
                httpPost.releaseConnection();
                log.info("[{}]请求成功:{}", url, res);
                return res;
            } else {
                log.info("[{}]请求失败:{}", url, response.getEntity());
            }
        } catch (Exception e) {
            log.error("[{}]请求异常:{}", url, e.getCause());
            e.printStackTrace();
        } finally {
            if (httpPost != null)
                httpPost.releaseConnection();
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    log.error("[{}]请求组件异常:{}", url, e.getCause());
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public String post(String api, JSONObject jsonBody) {
        String url = this.uri + api;
        HttpPost httpPost = new HttpPost(url);
        try {
            if (httpClient == null) {
                // httpClient = HttpClientBuilder.create().build();
                httpClient = HttpClients.custom()
                        .setConnectionManager(cm)
                        .setConnectionManagerShared(true)
                        .build();
            }
            httpClient = getHttpClient();
            setHeader(api, appId, appSecret, timestamp, nonce, sign, httpPost);
            if (!ObjectUtils.isEmpty(jsonBody)) {
                log.info("[{}]请求参数:{}", url, jsonBody.toJSONString());
                StringEntity entity = new StringEntity(jsonBody.toString());
                entity.setContentEncoding(Consts.UTF_8.name());
                entity.setContentType("application/x-www-form-urlencoded");//发送json数据需要设置contentType
                httpPost.setEntity(entity);//设置请求参数
            }

            HttpResponse response = httpClient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                String res = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
                httpPost.releaseConnection();
                log.info("[{}]请求成功:{}", url, res);
                return res;
            } else {
                log.info("[{}]请求失败:{}", url, response.getEntity());
            }
        } catch (Exception e) {
            log.error("[{}]请求异常:{}", url, e.getCause());
            e.printStackTrace();
        } finally {
            if (httpPost != null)
                httpPost.releaseConnection();
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    log.error("[{}]请求组件异常:{}", url, e.getCause());
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}