package org.deil.utils.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

//@EnableWebMvc
//@Configuration
public class SpringHttpUtil {
    private Logger log = LoggerFactory.getLogger(SpringHttpUtil.class);

    private int connectTimeout = 10000;

    private int readTimeout = 3000;

    private int writeTimeout = 3000;

    String url;

    HttpMethod httpMethod;

    MultiValueMap<String, String> params;

    public SpringHttpUtil(String url, HttpMethod httpMethod, MultiValueMap<String, String> params){
        this.url = url;
        this.httpMethod = httpMethod;
        this.params = params;
    }

    /**
     * http post
     * */
    public static String post(String url, MultiValueMap<String, String> params) {
        return  httpRestClient(url, HttpMethod.POST, params);
    }

    /**
     * http get
     * */
    public static String get(String url, MultiValueMap<String, String> params) {
        return  httpRestClient(url, HttpMethod.GET, params);
    }

    /**
     * HttpMethod  post/get
     * */
    private static String httpRestClient(String url, HttpMethod method, MultiValueMap<String, String> params) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10*1000);
        requestFactory.setReadTimeout(10*1000);
        RestTemplate client = new RestTemplate(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        //  执行HTTP请求
        ResponseEntity<String> response;
        try{
            response = client.exchange(url, method, requestEntity, String.class);
            return response.getBody();
        }
        catch (HttpClientErrorException e){
            //log.error("\033[0;31m------------- 出现异常 HttpClientErrorException -------------\n" +
            //        e.getMessage() + "\n" +
            //        e.getStatusText() + "\n" +
            //        "-------------responseBody-------------\n" +
            //        e.getResponseBodyAsString() + "\033[0m"
            //);
            //e.printStackTrace();
            throw e;
        }
        catch (Exception e) {
            //log.error("\033[0;31m------------- HttpRestUtils.httpRestClient() 出现异常 Exception -------------\n" +
            //        e.getMessage() + "\033[0m"
            //);
            throw e;
        }
    }

}
