package scheduler.runnable;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import scheduler.common.domain.BaseTaskProperty;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @PURPOSE 具体执行逻辑
 * @DATE 2022/11/30
 * @CODE Deil
 * @see Runnable
 */
@Slf4j
public class HealthCheckRunnable implements Runnable {

    private final HealthCheckProperty healthCheckProperty;

    public HealthCheckRunnable(HealthCheckProperty healthCheckProperty) {
        this.healthCheckProperty = healthCheckProperty;
    }

    /**
     * @TIME 2022/11/30 : 执行定时检查
     */
    @Override
    public void run() {
        String server = healthCheckProperty.getServerName();
        if (healthCheckProperty.isEnabled()) {
            String scriptLocation = healthCheckProperty.getScriptLocation();
            try {
                get(healthCheckProperty.getCheckUrl(), null);
                //HttpRequest.get(healthCheckProperty.getCheckUrl())
                //        .execute()
                //        .body();
                log.info("\033[42;30m{}服务正常\033[0m", server);
            } catch (IOException e) {
                log.info("\033[42;31m{}服务异常\033[0m，执行重启脚本", server);
                try {
                    Runtime.getRuntime().exec(scriptLocation);
                    log.info("{}重启成功", server);
                } catch (Exception ex) {
                    log.info("{}重启失败, {}", server, ex.getMessage());
                }
            }
        }
    }

    //@Retryable(
    //        value = {RuntimeException.class},
    //        maxAttempts = 5,
    //        backoff = @Backoff(delay = 60000, multiplier = 5)
    //)
    //public boolean tryRestsart(String server, String scriptLocation) throws IOException {
    //    Runtime.getRuntime().exec(scriptLocation);
    //    return false;
    //}
    //
    //@Recover
    //public boolean recoverRestsart(RuntimeException ex, String server, String scriptLocation) throws IOException {
    //    Runtime.getRuntime().exec(scriptLocation);
    //    return false;
    //}


    //region http
    /**
     * http post
     * */
    public static String post(String url, MultiValueMap<String, String> params) throws IOException {
        return  httpRestClient(url, HttpMethod.POST, params);
    }

    /**
     * http get
     * */
    public static String get(String url, MultiValueMap<String, String> params) throws IOException {
        return  httpRestClient(url, HttpMethod.POST, params);
    }

    /**
     * HttpMethod  post/get
     * */
    private static String httpRestClient(String url, HttpMethod method, MultiValueMap<String, String> params) throws IOException {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10*1000);
        requestFactory.setReadTimeout(10*1000);
        RestTemplate client = new RestTemplate(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        //  执行HTTP请求
        ResponseEntity<String> response = null;
        try{
            response = client.exchange(url, HttpMethod.POST, requestEntity, String.class);
            return response.getBody();
        }
        catch (HttpClientErrorException e){
            System.out.println( "------------- 出现异常 HttpClientErrorException -------------");
            System.out.println(e.getMessage());
            System.out.println(e.getStatusText());
            System.out.println( "-------------responseBody-------------");
            System.out.println( e.getResponseBodyAsString());
            e.printStackTrace();
            return "";
        }
        catch (Exception e) {
            System.out.println( "------------- HttpRestUtils.httpRestClient() 出现异常 Exception -------------");
            System.out.println(e.getMessage());
            return "";
        }
    }
    //endregion
}

@Data
class HealthCheckProperty extends BaseTaskProperty {

    /**
     * 执行脚本的位置（提前给执行权限）
     */
    private String scriptLocation = "./restartApp.sh";

    /**
     * 健康检查的url
     */
    private String checkUrl = "http://localhost:8080";

    public String getServerName() {
        return new String("HealthCheck");
    }

}
