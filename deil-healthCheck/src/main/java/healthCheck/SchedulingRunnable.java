package healthCheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @PURPOSE 具体执行逻辑
 * @DATE 2022/11/30
 * @CODE Deil
 * @see Runnable
 */
public class SchedulingRunnable implements Runnable {
    private static Logger log = LoggerFactory.getLogger(SchedulingRunnable.class);
    private final BaseProperties baseProperties;
    private final String server;
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Short>> concurrentHashMap;

    private Short TIMES = 0;

    public SchedulingRunnable(BaseProperties baseProperties,
                              ConcurrentHashMap<String, ConcurrentHashMap<String, Short>> concurrentHashMap) {
        this.baseProperties = baseProperties;
        this.server = new String(baseProperties.getServerName().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        this.concurrentHashMap = concurrentHashMap;
    }

    /**
     * @TIME 2022/11/30 : 执行定时检查
     */
    @Override
    public void run() {
        ConcurrentHashMap<String, Short> map = concurrentHashMap.computeIfAbsent(server, k -> new ConcurrentHashMap<>());
        if (baseProperties.isToRestart()) {
            try {
                get(baseProperties.getCheckUrl(), null);
                baseProperties.setToRestart(false);
            } catch (Exception e) {
            }
            log.info("[{}]服务异常，待手动重启", server);
            return;
        }
        if (baseProperties.isEnabled()) {
            String scriptLocation = baseProperties.getScriptLocation();
            try {
                get(baseProperties.getCheckUrl(), null);
                log.info("\033[0;30m[{}]服务正常\033[0m", server);
            } catch (Exception e) {
                log.error("\033[0;31m[" + server + "]服务异常\n" +
                        e.getMessage() + "\033[0m"
                );
                //计数重启
                map.put(server, TIMES++);
                if (map.get(server) < 5) {
                    log.info("[{}]服务异常{}，执行重启脚本[{}]", server, map.get(server), scriptLocation);
                    try {
                        Runtime.getRuntime().exec(scriptLocation);
                        log.info("[{}]重启成功", server);
                    } catch (Exception ex) {
                        log.info("[{}]重启失败, {}", server, ex.getMessage());
                    }
                } else {
                    TIMES = 0;
                    map.clear();
                    baseProperties.setToRestart(true);
                }
                if (concurrentHashMap.size() > 10000) {
                    concurrentHashMap.clear();
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
