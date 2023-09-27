package org.deil.utils.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.deil.utils.pojo.dto.FailBody;
import org.deil.utils.pojo.vo.ServiceResponseBody;
import org.deil.utils.utils.SpringContextAwareUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;
import java.util.UUID;

/**
 * @PURPOSE 异常处理程序
 * @DATE 2022/09/08
 */
//@ControllerAdvice
@RestControllerAdvice
public class DefaultExceptionAdvice extends ResponseEntityExceptionHandler {
    private Logger log = LoggerFactory.getLogger(DefaultExceptionAdvice.class);

    private final ObjectMapper mapper;
    private final Environment env;
    public DefaultExceptionAdvice(ObjectMapper mapper, Environment env) {
        this.mapper = mapper;
        this.env = env;
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String logId = (String) Optional.ofNullable(request.getAttribute("logId", RequestAttributes.SCOPE_REQUEST)).orElse(UUID.randomUUID().toString().replace("-", ""));
        long requestTimestamp = (long) Optional.ofNullable(request.getAttribute("requestTimestamp", RequestAttributes.SCOPE_REQUEST)).orElse(System.currentTimeMillis());
        logger.error(mapper.createObjectNode()
                .put("Event", "EXCEPTION")
                .put("LogId", logId)
                .put("Request_URI", request.toString())
                .put("UseTime", (System.currentTimeMillis() - requestTimestamp) + " ms")
                .put("Error", ex.toString())
                .put("ErrorMessage", ex.getMessage())
                .toString(), ex);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(FailBody.errorMsg(logId, HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }

    @ResponseBody
    @ExceptionHandler({Exception.class, CustomException.class})
    public ResponseEntity<ServiceResponseBody> defaultExceptionHandler(@RequestAttribute String logId, Exception ex) {
        String msg = "服务器异常，请联系管理员处理。";
        if (!"prod".equalsIgnoreCase(!ObjectUtils.isEmpty(env.getActiveProfiles()) ? env.getActiveProfiles()[0] : env.getDefaultProfiles()[0])) {
            //开发、测试环境的错误消息，全部抛出
            msg = ex.getMessage();
        } else {
            if (ex instanceof CustomException) {
                //自定义的错误消息，直接抛出
                msg = ex.getMessage();
            }
            log.error("系统异常", ex);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(FailBody.errorMsg(logId, HttpStatus.INTERNAL_SERVER_ERROR.value(), msg));
    }

}
