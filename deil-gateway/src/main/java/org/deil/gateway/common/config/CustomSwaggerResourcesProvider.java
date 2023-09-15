//package com.cargo.gateway.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.context.annotation.Primary;
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Component;
//import springfox.documentation.swagger.web.SwaggerResource;
//import springfox.documentation.swagger.web.SwaggerResourcesProvider;
//
//import java.util.*;
//
///**
// * swagger文档网关聚合配置
// */
//@Component
//@Primary
//public class CustomSwaggerResourcesProvider implements SwaggerResourcesProvider {
//
//    /**
//     * swagger3默认的url后缀
//     * 由于会导致v3接口测试会导致404 替换成v2
//     */
//    private static final String SWAGGER3URL = "/v2/api-docs";
////    private static final String SWAGGER3URL = "/v3/api-docs";
//
//
//    /**
//     * 网关路由
//     */
//    private final RouteLocator routeLocator;
//
//    /**
//     * 网关应用名称
//     */
//    @Value("${spring.application.name}")
//    private String self;
//
//    @Autowired
//    public CustomSwaggerResourcesProvider(RouteLocator routeLocator) {
//        this.routeLocator = routeLocator;
//    }
//
//
//    @Override
//    public List<SwaggerResource> get() {
//        List<SwaggerResource> resources = new ArrayList<>();
//        List<String> routeHosts = new ArrayList<>();
//        //获取所有的路由路径
//        routeLocator.getRoutes().filter(route -> route.getId() != null)
//                .filter(route -> !self.equals(route.getId()))
//                .subscribe(route -> routeHosts.add(route.getId()));
//
//        // 记录已经添加过的server
//        Set<String> dealed = new HashSet<>();
//        routeHosts.forEach(instance -> {
//            // 拼接url
//            String url = "/" + instance.toLowerCase() + SWAGGER3URL;
//            if (!dealed.contains(url)) {
//                dealed.add(url);
//                SwaggerResource swaggerResource = new SwaggerResource();
//                swaggerResource.setUrl(url);
//                swaggerResource.setName(instance);
//                resources.add(swaggerResource);
//            }
//        });
//        return resources;
//    }
//}