package org.deil.utils.signature;

import com.alibaba.fastjson2.JSONObject;
import org.deil.utils.pojo.vo.VOKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Type;

@ControllerAdvice(basePackages = "org.deil.demo")
public class SignReqAdvice implements RequestBodyAdvice {
    private Logger log = LoggerFactory.getLogger(SignReqAdvice.class);

    @Resource
    private SignatureProperties signatureProperties;

    @Override
    public boolean supports(MethodParameter methodParameter,
                            Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        if (!signatureProperties.isEnabled()) {
            return false;
        }
        if (methodParameter.getContainingClass().getAnnotation(signatureProperties.getReqAnnoClass()) != null) {
            return false;
        }
        return methodParameter.getMethodAnnotation(signatureProperties.getReqAnnoClass()) == null;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage,
                                           MethodParameter parameter,
                                           Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body,
                                HttpInputMessage inputMessage,
                                MethodParameter parameter,
                                Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        JSONObject signJson = JSONObject.parseObject(JSONObject.toJSONString(body));
        signJson.put(VOKey.SIGNKEY_APPID, inputMessage.getHeaders().get(VOKey.SIGNKEY_APPID).get(0));
        signJson.put(VOKey.SIGNKEY_APPSECRET, inputMessage.getHeaders().get(VOKey.SIGNKEY_APPSECRET).get(0));
        signJson.put(VOKey.SIGNKEY_TIMESTAMP, inputMessage.getHeaders().get(VOKey.SIGNKEY_TIMESTAMP).get(0));
        signJson.put(VOKey.SIGNKEY_SIGNATURE, inputMessage.getHeaders().get(VOKey.SIGNKEY_SIGNATURE).get(0));
        String jsonData = JSONObject.toJSONString(signJson);
        //验签
        boolean result = SignatureSuper.verifySigns(jsonData);
        log.info("接口:{}, 验签结果:{}, 原始数据:\n{}", parameter.getMethod(), result, jsonData);
        if (result) {
            return body;
        } else {
            throw new RuntimeException("验签异常:" + jsonData);
        }
    }

    @Override
    public Object handleEmptyBody(Object body,
                                  HttpInputMessage inputMessage,
                                  MethodParameter parameter,
                                  Type targetType,
                                  Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

}
