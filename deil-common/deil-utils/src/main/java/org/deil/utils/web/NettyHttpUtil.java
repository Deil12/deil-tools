package org.deil.utils.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

/**
 * NettyHttp工具
 *
 * @DATE 2023/01/18
 * @CODE Deil
 */
@EnableWebMvc
@Configuration
public class NettyHttpUtil {
    private Logger log = LoggerFactory.getLogger(NettyHttpUtil.class);

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

}
