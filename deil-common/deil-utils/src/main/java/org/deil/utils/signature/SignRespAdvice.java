package org.deil.utils.signature;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.deil.utils.domain.dto.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

/**
 * 响应通知
 *
 * @DATE 2022/09/06
 * @CODE Deil
 * @see ResponseBodyAdvice
 */
@Deprecated
@Slf4j
@ControllerAdvice(basePackages = "**.controller")
public class SignRespAdvice implements ResponseBodyAdvice<Result> {

    private static String HTTP_SIGN_KEY = "signature";

    @Resource
    private SignatureProperties signatureProperties;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (!signatureProperties.isEnabled()) {
            return false;
        }
        if (returnType.getContainingClass().getAnnotation(signatureProperties.getRespAnnoClass()) != null) {
            return false;
        }
        return returnType.getMethodAnnotation(signatureProperties.getRespAnnoClass()) == null;
    }

    @Override
    public Result beforeBodyWrite(Result body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        //通过 ServerHttpRequest的实现类ServletServerHttpRequest 获得HttpServletRequest
        ServletServerHttpRequest request = (ServletServerHttpRequest) serverHttpRequest;
        //获取拦截器对象
        HttpServletRequest httpServletRequest = request.getServletRequest();

        Map<String, String> signMap = SignatureSuper.initRequsetMsg();
        signMap.put("logId", body.getLogId());
        signMap.put("code", body.getCode().toString());
        signMap.put("message", body.getMessage());
        signMap.put("data", Optional.ofNullable(body.getData()).orElse("").toString());
        try {
            HttpHeaders headers = serverHttpResponse.getHeaders();
            //告知加密标记
            headers.add("issigned", "true");
            //FIXME 响应加密
            signMap = SignatureSuper.packSigns(signMap);
            log.info("接口:{}, 加签数据:{}, 原始数据:\n{}", request.getURI(), signMap, body);
        } catch (Exception e) {
            log.error("响应:{}加签异常！\n{}", JSON.toJSONString(body), e);
        }
        return Result.OK(body.getLogId(), signMap.get(HTTP_SIGN_KEY));
    }

}
