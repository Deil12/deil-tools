package org.deil.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import org.deil.gateway.common.GateProperties;
import org.deil.gateway.common.AuthAccount;
import org.deil.gateway.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AuthRequestHeadFilter implements GatewayFilter, Ordered {
    private Logger log = LoggerFactory.getLogger(AuthRequestHeadFilter.class);

    public static String SIGNKEY_APPID = "appid";
    public static String SIGNKEY_APPSECRET = "appsecret";
    public static String SIGNKEY_NONCE = "nonce";
    public static String SIGNKEY_TIMESTAMP = "timestamp";
    public static String SIGNKEY_SIGNATURE = "signature";

    public static String REQUEST_TIME_BEGIN = "requestTimeBegin";
    public static String RESPONSE_GATEWAY = "gateway";
    public static String RESPONSE_LOGID = "logId";
    public static String RESPONSE_SETCOOKIE = "Set-Cookie";
    public static String RESPONSE_XFRAMEOPTIONS = "x-frame-options";

    public static String REQUEST_REALCLIENTIP = "real-client-ip";
    public static String REQUEST_BYGATEWAY = "by-gateway";
    public static String REQUEST_LOGID = "log-id";
    public static String REQUEST_SETCOOKIE = "Set-Cookie";
    public static String REQUEST_XFRAMEOPTIONS = "x-frame-options";
    @Resource
    private GateProperties gateProperties;

    private final Long validtime;
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Boolean>> concurrentHashMap;
    private final List<AuthAccount> authAccountList;
    private final List<String> ignorePath;

    public AuthRequestHeadFilter(Long validtime,
                                 List<AuthAccount> authAccountList,
                                 List<String> ignorePath,
                                 ConcurrentHashMap<String, ConcurrentHashMap<String, Boolean>> concurrentHashMap) {
        this.validtime = validtime;
        this.concurrentHashMap = concurrentHashMap;
        this.authAccountList = authAccountList;
        this.ignorePath = ignorePath;
    }

    /**
     * Process the Web request and (optionally) delegate to the next {@code WebFilter}
     * through the given {@link GatewayFilterChain}.
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     * @return {@code Mono<Void>} to indicate when request processing is complete
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //log.info("进入认证过滤器");
        ServerHttpRequest request = exchange.getRequest();
        //真实ip地址
        String realIp = getRealIp(request);
        InetSocketAddress unresolved = InetSocketAddress.createUnresolved(realIp, 0);
        //请求路径信息
        String path = request.getPath().toString();
        if (ignorePath != null) {
            for (String s : ignorePath) {
                //忽略大小写判断是无需认证路径
                if (s.equalsIgnoreCase(path)) {
                    //是无需认证的路径，使用配置的忽略路径改写请求路径，配置忽略路径必须区分大小写
                    request = request.mutate().path(s).remoteAddress(unresolved).build();
                    log.info("忽略路径(" + path + ") >> 不验证请求头,请求IP:" + realIp);
                    return chain.filter(exchange.mutate().request(request).build());
                }
            }
        }
        //请求头
        HttpHeaders headers = request.getHeaders();
        ServerHttpResponse response = exchange.getResponse();

        if (!headers.containsKey(SIGNKEY_APPID) || headers.get(SIGNKEY_APPID).size() < 1) {
            return authFailed("请求头缺失 >> [" + SIGNKEY_APPID + "]", response, realIp, path);
        }
        if (!headers.containsKey(SIGNKEY_TIMESTAMP) || headers.get(SIGNKEY_TIMESTAMP).size() < 1) {
            return authFailed("请求头缺失 >> [" + SIGNKEY_TIMESTAMP + "]", response, realIp, path);
        }
        if (!headers.containsKey(SIGNKEY_NONCE) || headers.get(SIGNKEY_NONCE).size() < 1) {
            return authFailed("请求头缺失 >> [" + SIGNKEY_NONCE + "]", response, realIp, path);
        }
        if (!headers.containsKey(SIGNKEY_SIGNATURE) || headers.get(SIGNKEY_SIGNATURE).size() < 1) {
            return authFailed("请求头缺失 >> [" + SIGNKEY_SIGNATURE + "]", response, realIp, path);
        }

        String appId = headers.get(SIGNKEY_APPID).get(0);

        //添加对appId接口的访问限制
        /*boolean result = handlePortJurisdiction(path, appId);
        if (!result) {
            return authFailed("没有访问权限 >> appId", response, realIp, path);
        }*/

        String timestamp = headers.get(SIGNKEY_TIMESTAMP).get(0);
        String nonce = headers.get(SIGNKEY_NONCE).get(0);
        String sign = headers.get(SIGNKEY_SIGNATURE).get(0);
        AuthAccount authAccount = accountExist(appId);
        if (authAccount == null) {
            return authFailed("appId(" + appId + ")不存在", response, realIp, path);
        }
        //判断账号类型
        int accountType = authAccount.getAccountType();
        //外部账号
        if (accountType == 2) {
            //黑名单
            List<String> blacklist = authAccount.getBlackList();
            //如果黑名单不为空且请求的ip地址在黑名单，阻止请求
            if (blacklist != null && blacklist.size() != 0 && blacklist.contains(realIp)) {
                return authFailed("appId(" + appId + "),ip地址在黑名单,拒绝请求", response, realIp, path);
            }
            //白名单
            List<String> whitelist = authAccount.getWhiteList();
            //如果白名单不为空且请求的ip地址不在白名单内，阻止请求
            if (whitelist != null && whitelist.size() != 0 && !whitelist.contains(realIp)) {
                return authFailed("appId(" + appId + "),ip地址不在白名单内,拒绝请求", response, realIp, path);
            }
        }
        //密钥
        String appSecret = authAccount.getAppSecret();
        //if (!(String.valueOf(Duration.between(LocalDateTime.parse(timestamp), LocalDateTime.now()).toMinutes())).matches("^[0-2]$")) {/** {@link LocalDateTime}*/
        if (!(String.valueOf(Duration.between(LocalDateTime.ofEpochSecond(Long.valueOf(timestamp), 0, ZoneOffset.of("+8")), LocalDateTime.now()).toMinutes())).matches("^[0-2]$")) {/** {@link java.security.Timestamp}*/
            //return authFailed("appId(" + appId + ")请求过期", response, realIp, path);
        }
        ConcurrentHashMap<String, Boolean> map = concurrentHashMap.computeIfAbsent(appId, k -> new ConcurrentHashMap<>());
        if (map.containsKey(nonce)) {
            return authFailed("appId(" + appId + ")随机串已被使用", response, realIp, path);
        } else {
            map.put(nonce, Boolean.TRUE);
        }
        if (concurrentHashMap.size() > 10000) {
            concurrentHashMap.clear();
        }
        //签名并与请求的签名进行对比

        /*String signature = SignUtil.getSignature(appId, appSecret, path, timestamp, nonce);*/
        if (/*!sign.equals(signature)*/false) {
            return authFailed("appId(" + appId + ")签名验证失败", response, realIp, path);
        }
        exchange.getRequest().mutate().remoteAddress(unresolved);
        return chain.filter(exchange);
    }

    /** handlePortJurisdiction 备用方案，防止属性文件失效
     * 接口访问权限
     * false没有访问权限，true有访问权限
     *
     * @param path  访问路径
     * @param appId openapi账号
     * @return
     */
    private boolean handlePortJurisdiction2(String path, List<String> appId) {
        String aId = appId.get(0);
        int index = path.substring(1).indexOf("/") + 1; // 23 + 1 --> 跳过第一个字符 /cargoexchange-open-api/customs/sendCustomTangForSale
        String pathSub = path.substring(index); //  --> /customs/sendCustomTangForSale

        return !(aId.equals("test") && !pathSub.equals("/cargoImp/SaveCargoTelexMsg"));
    }

    /**
     * 接口访问权限
     * false没有访问权限，true有访问权限
     *
     * @param path  访问路径
     * @param appId openapi账号
     * @return
     */
    private boolean handlePortJurisdiction(String path, String appId) {
        /*Map<String, Object> stringObjectMap = ReadProperties.getProperties("portJurisdiction.properties");*/
        Map<String, Object> stringObjectMap = new HashMap<>();

        // 外部的appid和请求地址
        List<String> externalAppIds = Arrays.asList(gateProperties.getExternalAppId().split(","));
        List<String> externalPaths = Arrays.asList(gateProperties.getExternalApi().split(","));

        //第三方的appid和请求地址
        List<String> thirdPartyAppIds = Arrays.asList(gateProperties.getThirdPartyAppId().split(","));
        List<String> thirdPartyPaths = Arrays.asList(gateProperties.getThirdPartyPath().split(","));

        //String aId = appId.get(0);
        //int index = path.substring(1).indexOf("/") + 1; // 23 + 1 --> 跳过第一个字符 /cargoexchange-open-api/customs/sendCustomTangForSale

        int inde = path.lastIndexOf("/");
        String substring = path.substring(0,inde);

        int index = substring.lastIndexOf("/");

        String pathSub = path.substring(index); //  --> /customs/sendCustomTangForSale


        boolean flagExternal = handAppIdAndPath(externalAppIds, externalPaths, appId, pathSub);
        boolean flagThirdParty = handAppIdAndPath(thirdPartyAppIds, thirdPartyPaths, appId, pathSub);

        if (!flagExternal || !flagThirdParty) {
            return false;
        }
        return true;
    }

    private boolean handAppIdAndPath(List<String> externalAppIds, List<String> externalPaths, String aId, String pathSub) {
        for (String externalAppId : externalAppIds) {
            if (externalAppId.equals(aId)) {
                for (String externalPath : externalPaths) {
                    if (externalPath.equals(pathSub)) {
                        return true;
                    }
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Get the order value of this object.
     * <p>Higher values are interpreted as lower priority. As a consequence,
     * the object with the lowest value has the highest priority (somewhat
     * analogous to Servlet {@code load-on-startup} values).
     * <p>Same order values will result in arbitrary sort positions for the
     * affected objects.
     *
     * @return the order value
     * @see #HIGHEST_PRECEDENCE
     * @see #LOWEST_PRECEDENCE
     */
    @Override
    public int getOrder() {
        return -3;
    }

    /**
     * 认证不通过请求
     */
    private Mono<Void> authFailed(String result, ServerHttpResponse response, String realIp, String path) {
        log.error(result + " 请求IP:" + realIp + "请求路径:" + path);
        Result error = Result.FAIL(UUID.randomUUID().toString().replace("-", "").toUpperCase(), result);
        byte[] bytes = JSONObject.toJSONString(error).getBytes(StandardCharsets.UTF_8);
        DataBuffer wrap = response.bufferFactory().wrap(bytes);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(wrap));
    }

    /**
     * 判断账号是否存在 存在即返回当前账号信息
     */
    private AuthAccount accountExist(String appid) {
        return authAccountList.stream()
                .filter(authAccount -> authAccount.getAppid().equals(appid))
                .findFirst().orElse(null);
    }

    /**
     * 获取真实ip地址
     */
    private String getRealIp(ServerHttpRequest request) {
        String ip = request.getHeaders().getFirst("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"UNKNOWN".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.contains(",")) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "UNKNOWN".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "UNKNOWN".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "UNKNOWN".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "UNKNOWN".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "UNKNOWN".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "UNKNOWN".equalsIgnoreCase(ip)) {
            //最后仍没有获取到真实ip地址，使用host
            ip = request.getRemoteAddress().getHostString();
        }
        //本机ip地址替换
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }
}
