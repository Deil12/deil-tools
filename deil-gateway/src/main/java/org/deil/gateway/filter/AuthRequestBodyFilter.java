//package org.deil.gateway.filter;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.support.BodyInserterContext;
//import org.springframework.cloud.gateway.support.CachedBodyOutputMessage;
//import org.springframework.cloud.gateway.support.DefaultServerRequest;
//import org.springframework.core.Ordered;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.BodyInserter;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.server.ServerRequest;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@Slf4j
//@Component
//public class AuthRequestBodyFilter implements GatewayFilter, Ordered {
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        if (request.getMethod() != HttpMethod.POST) {
//            return chain.filter(exchange);
//        }
//        return operationExchange(exchange, chain);
//    }
//
//    private Mono<Void> operationExchange(ServerWebExchange exchange, GatewayFilterChain chain) {
//        // mediaType
//        MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
//        // read & modify body
//        ServerRequest serverRequest = new DefaultServerRequest(exchange);
//        Mono<String> modifiedBody = serverRequest.bodyToMono(String.class)
//                .flatMap(body -> {
//                    if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)) {
//                        // 对原先的body进行修改操作
//                        String newBody = "{\"testName\":\"testValue\"}";
//                        return Mono.just(newBody);
//                    }
//                    return Mono.empty();
//                });
//        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
//        HttpHeaders headers = new HttpHeaders();
//        headers.putAll(exchange.getRequest().getHeaders());
//        headers.remove(HttpHeaders.CONTENT_LENGTH);
//        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
//        return bodyInserter.insert(outputMessage, new BodyInserterContext())
//                .then(Mono.defer(() -> {
//                    ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
//                        @Override
//                        public HttpHeaders getHeaders() {
//                            long contentLength = headers.getContentLength();
//                            HttpHeaders httpHeaders = new HttpHeaders();
//                            httpHeaders.putAll(super.getHeaders());
//                            if (contentLength > 0) {
//                                httpHeaders.setContentLength(contentLength);
//                            } else {
//                                httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
//                            }
//                            return httpHeaders;
//                        }
//
//                        @Override
//                        public Flux<DataBuffer> getBody() {
//                            return outputMessage.getBody();
//                        }
//                    };
//                    return chain.filter(exchange.mutate().request(decorator).build());
//                }));
//    }
//
//    @Override
//    public int getOrder() {
//        return -1;
//    }
//}