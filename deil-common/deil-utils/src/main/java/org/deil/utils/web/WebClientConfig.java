package org.deil.utils.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 网络客户端配置类
 *
 * @DATE 2023/01/18
 * @CODE Deil
 */
@Slf4j
@EnableWebMvc
@Component
@Configuration
public class WebClientConfig implements WebMvcConfigurer {

    private int connectTimeout = 10000;

    private int readTimeout = 3000;

    private int writeTimeout = 3000;

    /**
     * 注入 WebClient
     *
     * @param objectMapper ObjectMapper实例
     * @return WebClient 实例
     * @see WebClient
     */
    @Bean("webClient")
    public WebClient webClient(ObjectMapper objectMapper) {
        HttpClient httpClient = HttpClient.create().tcpConfiguration(client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
                .doOnConnected(
                        conn -> conn.addHandlerLast(new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(writeTimeout, TimeUnit.MILLISECONDS))));

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs((ClientCodecConfigurer clientDefaultCodecsConfigurer) -> {
                    clientDefaultCodecsConfigurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
                    clientDefaultCodecsConfigurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024);
                }).build();

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(strategies)
                .defaultHeaders(httpHeaders -> {
                    //公共header
                })
                .filter(ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
                    log.info("WebClient Send Request: {} {} {}", clientRequest.method(), clientRequest.url(), clientRequest.headers().entrySet());
                    return Mono.just(clientRequest);
                }))
                .build();
    }

    /**
     * 添加拦截器
     * 自定义拦截器；在请求中添加logId值和requestTimestamp值
     * 并对头部Accept-Language进行检查
     *
     * @param registry 注册表
     * @TIME 2023/01/19
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
                request.setAttribute("logId", UUID.randomUUID().toString().replace("-", ""));
                request.setAttribute("requestTimestamp", System.currentTimeMillis());
                /*if (StringUtils.isEmpty(request.getHeader("Accept-Language"))) {
                    //设置当前的请求的语言类型，并写入到cookie中去
                    LocaleContextHolder.resetLocaleContext();
                    LocaleContextHolder.setLocale(Locale.CHINA);
                }*/
                return true;
            }
        });
    }

}
