package org.deil.utils.web;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

//@EnableWebMvc
//@Configuration
public class SpringHttpUtil {
    private static Logger log = LoggerFactory.getLogger(SpringHttpUtil.class);

    private static int connectTimeout = 10;

    private static int readTimeout = 3;

    private static int writeTimeout = 3;

    static String uri;

    static HttpMethod httpMethod;

    static MultiValueMap<String, String> params;

    public SpringHttpUtil(String uri) {
        this.uri = uri;
    }

    public String get(@Nullable String api, MultiValueMap<String, String> params) {
        return httpRestClient(api, HttpMethod.GET, params);
    }

    public String post(@Nullable String api, MultiValueMap<String, String> params) {
        return httpRestClient(api, HttpMethod.POST, params);
    }

    //public String def(String uri, HttpMethod httpMethod, MultiValueMap<String, String> params) {
    //    this.uri = uri;
    //    this.httpMethod = httpMethod;
    //    this.params = params;
    //    return httpRestClient("", httpMethod, params);
    //}

    private static String httpRestClient(String api, HttpMethod method, MultiValueMap<String, String> params) {
        String url = uri + StringUtils.defaultIfEmpty(api, "");
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout * 1000);
        requestFactory.setReadTimeout(readTimeout * 1000);
        RestTemplate client = new RestTemplate(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        //  执行HTTP请求
        ResponseEntity<String> response;
        try {
            response = client.exchange(url, method, requestEntity, String.class);
            int statusCode = response.getStatusCodeValue();
            if (statusCode == 200) {
                log.info("[{}]请求成功:{}", url, response.getBody());
            } else {
                log.info("[{}]请求失败:{}", url, response.getBody());
            }
            return response.getBody();
        } catch (HttpClientErrorException e) {
            //log.error("\033[0;31m------------- 出现异常 HttpClientErrorException -------------\n" +
            //        e.getMessage() + "\n" +
            //        e.getStatusText() + "\n" +
            //        "-------------responseBody-------------\n" +
            //        e.getResponseBodyAsString() + "\033[0m"
            //);
            log.error("[{}]请求异常{}", url, e.getCause());
            e.printStackTrace();
        } catch (Exception e) {
            log.error("[{}]请求异常", url, e.getCause());
            e.printStackTrace();
        }
        return null;
    }

}
