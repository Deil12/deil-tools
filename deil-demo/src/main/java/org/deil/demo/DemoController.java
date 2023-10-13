package org.deil.demo;

import lombok.RequiredArgsConstructor;
import org.deil.utils.exception.CustomException;
import org.deil.utils.log.Log;
import org.deil.utils.pojo.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Log
//@LogTrace
@RestController
@RequestMapping
@RequiredArgsConstructor
public class DemoController {
    private Logger log = LoggerFactory.getLogger(DemoController.class);

    //region 多线程
    @Log
    //@LogTrace
    @ResponseBody
    @PostMapping("testThread")
    public ResponseEntity<Result> testThread(@RequestAttribute String logId) {
        int i = 0;
        while (i++ < 5) {
            demoService.testHttp();
        }
        throw new CustomException(123,"q3rew");
        //return ResponseEntity.ok(Result.OK(logId));
    }
    //endregion

    //region 加验签
    @ResponseBody
    @PostMapping("testSignature")
    public ResponseEntity<Result> testSignature(@RequestAttribute String logId) {
        return ResponseEntity.ok(Result.OK(logId));
    }
    //endregion

    @Log
    //@LogTrace
    @ResponseBody
    @PostMapping("test01")
    public ResponseEntity<Result> doTest0(@RequestAttribute String logId) {
        String logID = "safdasfa";
        log.info("入参 logID={}", logID);
        testTrace();
        log.info("调用结束 logID={}", logID);
        return ResponseEntity.ok(Result.OK(logId));
    }

    //region 多请求
    //@LogTrace
    @ResponseBody
    @PostMapping("testReq00")
    public ResponseEntity<Result> testReq00(@RequestAttribute String logId) {
        String logID = logId;
        log.info("入参 logID={}", logID);
        testTrace();
        log.info("调用结束 logID={}", logID);
        return ResponseEntity.ok(Result.OK(logId));
    }

    //@LogTrace
    @ResponseBody
    @PostMapping("testReq01")
    public ResponseEntity<Result> testReq01(@RequestAttribute String logId) {
        String logID = logId;
        log.info("入参 logID={}", logID);
        testTrace();
        log.info("调用结束 logID={}", logID);
        return ResponseEntity.ok(Result.OK(logId));
    }

    //@LogTrace
    @ResponseBody
    @PostMapping("testReq02")
    public ResponseEntity<Result> testReq02(@RequestAttribute String logId) {
        String logID = logId;
        log.info("入参 logID={}", logID);
        testTrace();
        log.info("调用结束 logID={}", logID);
        return ResponseEntity.ok(Result.OK(logId));
    }

    //@LogTrace
    @ResponseBody
    @PostMapping("testReq03")
    public ResponseEntity<Result> testReq03(@RequestAttribute String logId) {
        String logID = logId;
        log.info("入参 logID={}", logID);
        testTrace();
        log.info("调用结束 logID={}", logID);
        return ResponseEntity.ok(Result.OK(logId));
    }

    //@LogTrace
    @ResponseBody
    @PostMapping("testReq04")
    public ResponseEntity<Result> testReq04(@RequestAttribute String logId) {
        String logID = logId;
        log.info("入参 logID={}", logID);
        testTrace();
        log.info("调用结束 logID={}", logID);
        return ResponseEntity.ok(Result.OK(logId));
    }

    //@LogTrace
    @ResponseBody
    @PostMapping("testReq05")
    public ResponseEntity<Result> testReq05(@RequestAttribute String logId) {
        String logID = logId;
        log.info("入参 logID={}", logID);
        testTrace();
        log.info("调用结束 logID={}", logID);
        return ResponseEntity.ok(Result.OK(logId));
    }

    //@LogTrace
    @ResponseBody
    @PostMapping("testReq06")
    public ResponseEntity<Result> testReq06(@RequestAttribute String logId) {
        String logID = logId;
        log.info("入参 logID={}", logID);
        testTrace();
        log.info("调用结束 logID={}", logID);
        return ResponseEntity.ok(Result.OK(logId));
    }

    //@LogTrace
    @ResponseBody
    @PostMapping("testReq07")
    public ResponseEntity<Result> testReq07(@RequestAttribute String logId) {
        String logID = logId;
        log.info("入参 logID={}", logID);
        testTrace();
        log.info("调用结束 logID={}", logID);
        return ResponseEntity.ok(Result.OK(logId));
    }

    //@LogTrace
    @ResponseBody
    @PostMapping("testReq08")
    public ResponseEntity<Result> testReq08(@RequestAttribute String logId) {
        String logID = logId;
        log.info("入参 logID={}", logID);
        testTrace();
        log.info("调用结束 logID={}", logID);
        return ResponseEntity.ok(Result.OK(logId));
    }

    //@LogTrace
    @ResponseBody
    @PostMapping("testReq09")
    public ResponseEntity<Result> testReq09(@RequestAttribute String logId) {
        String logID = logId;
        log.info("入参 logID={}", logID);
        testTrace();
        log.info("调用结束 logID={}", logID);
        return ResponseEntity.ok(Result.OK(logId));
    }


    //@LogTrace
    @ResponseBody
    @PostMapping("testReq10")
    public ResponseEntity<Result> testReq10(@RequestAttribute String logId) {
        String logID = logId;
        log.info("入参 logID={}", logID);
        testTrace();
        log.info("调用结束 logID={}", logID);
        return ResponseEntity.ok(Result.OK(logId));
    }

    //@LogTrace
    @ResponseBody
    @PostMapping("testReq11")
    public ResponseEntity<Result> testReq11(@RequestAttribute String logId) {
        String logID = logId;
        log.info("入参 logID={}", logID);
        testTrace();
        log.info("调用结束 logID={}", logID);
        return ResponseEntity.ok(Result.OK(logId));
    }

    //@LogTrace
    @ResponseBody
    @PostMapping("testReq12")
    public ResponseEntity<Result> testReq12(@RequestAttribute String logId) {
        String logID = logId;
        log.info("入参 logID={}", logID);
        testTrace();
        log.info("调用结束 logID={}", logID);
        return ResponseEntity.ok(Result.OK(logId));
    }

    //@LogTrace
    @ResponseBody
    @PostMapping("testReq13")
    public ResponseEntity<Result> testReq13(@RequestAttribute String logId) {
        String logID = logId;
        log.info("入参 logID={}", logID);
        testTrace();
        log.info("调用结束 logID={}", logID);
        return ResponseEntity.ok(Result.OK(logId));
    }

    //@LogTrace
    @ResponseBody
    @PostMapping("testReq14")
    public ResponseEntity<Result> testReq14(@RequestAttribute String logId) {
        String logID = logId;
        log.info("入参 logID={}", logID);
        testTrace();
        log.info("调用结束 logID={}", logID);
        return ResponseEntity.ok(Result.OK(logId));
    }

    //@LogTrace
    @ResponseBody
    @PostMapping("testReq15")
    public ResponseEntity<Result> testReq15(@RequestAttribute String logId) {
        String logID = logId;
        log.info("入参 logID={}", logID);
        testTrace();
        log.info("调用结束 logID={}", logID);
        return ResponseEntity.ok(Result.OK(logId));
    }

    //@LogTrace
    @ResponseBody
    @PostMapping("testReq16")
    public ResponseEntity<Result> testReq16(@RequestAttribute String logId) {
        String logID = logId;
        log.info("入参 logID={}", logID);
        testTrace();
        log.info("调用结束 logID={}", logID);
        return ResponseEntity.ok(Result.OK(logId));
    }

    //@LogTrace
    @ResponseBody
    @PostMapping("testReq17")
    public ResponseEntity<Result> testReq17(@RequestAttribute String logId) {
        String logID = logId;
        log.info("入参 logID={}", logID);
        testTrace();
        log.info("调用结束 logID={}", logID);
        return ResponseEntity.ok(Result.OK(logId));
    }

    //@LogTrace
    @ResponseBody
    @PostMapping("testReq18")
    public ResponseEntity<Result> testReq18(@RequestAttribute String logId) {
        String logID = logId;
        log.info("入参 logID={}", logID);
        testTrace();
        log.info("调用结束 logID={}", logID);
        return ResponseEntity.ok(Result.OK(logId));
    }

    //@LogTrace
    @ResponseBody
    @PostMapping("testReq19")
    public ResponseEntity<Result> testReq19(@RequestAttribute String logId) {
        String logID = logId;
        log.info("入参 logID={}", logID);
        testTrace();
        log.info("调用结束 logID={}", logID);
        return ResponseEntity.ok(Result.OK(logId));
    }
    //endregion

    @Resource
    private DemoService demoService;

    private void testTrace() {
        log.info("这是一行info日志");
        demoService.testHttp();
        log.error("这是一行error日志");
    }
}
