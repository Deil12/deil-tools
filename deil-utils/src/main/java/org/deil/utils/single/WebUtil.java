package org.deil.utils.single;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @PURPOSE 
 * @DATE 2022/08/23
 */
@Slf4j
@UtilityClass
public class WebUtil {

    @Autowired
    private WebClient webClient;

    private String sendWebRequestWHeader(String url, Map<String, String> header, HttpMethod httpMethod, Object postData) {
        String result = "";
        // webclient发送请求
        webClient.method(httpMethod)
                .uri(url)
                .bodyValue(postData)
                .accept(MediaType.APPLICATION_JSON)
                .headers(new Consumer<HttpHeaders>() {
                    @Override
                    public void accept(HttpHeaders httpHeaders) {
                        for (String key : header.keySet()) {
                            httpHeaders.set(key, header.get(key));
                        }
                    }
                })
                .retrieve()

                // 转换返回结果：可自定义，需要有空构造器的bean。
                .bodyToMono(String.class)
                // 请求成功后的响应
                .doOnNext(resp -> log.info("响应内容： [{}]", resp))
                // 发生错误
                .doOnError(throwable -> log.error(throwable.getLocalizedMessage()))
                //同步获取
                .blockOptional().orElseGet(new Supplier<String>() {
                    @Override
                    public String get() {
                        return "";
                    }
                });
                // 异步消费
                //.subscribe();
        return result;
    }

    @SneakyThrows
    private String sendByHttpClient(String url, Map<String, String> header, HttpMethod httpMethod, Object postData) {
        String result = "";
        /*HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        GetMethod getMethod = new org.apache.commons.httpclient.methods.GetMethod(url);
        //GetMethod getMethod = new GetMethod("http://localhost:63342/backgroundService/customsMoveService/check/dependency-check-report.json");
        getMethod.getParams().setParameter(org.apache.commons.httpclient.params.HttpMethodParams.SO_TIMEOUT, 60000);
        getMethod.addRequestHeader("Content-Type", "application/json");

        httpClient.executeMethod(getMethod);

        result = getMethod.getResponseBodyAsString();
        getMethod.releaseConnection();*/
        return result;
    }

}
