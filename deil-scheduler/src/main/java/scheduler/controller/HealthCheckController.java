package scheduler.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @PURPOSE 健康检查 controller 层
 * @DATE 2022/11/30
 * @CODE Deil
 */
// @Api(tags = "自检F5监测")
@RestController
@RequestMapping(value = "/HealthCheck", produces = "application/json;charset=UTF-8")
public class HealthCheckController {

    /**
     * 给唐翼的监控程序用的，用于检测程序是否能提供服务,如果请求不成功，则监控告警
     */
    // @ApiOperation("对接监控程序")
    @GetMapping(value = "/Check")
    public String TangMonitorCheck() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * 域名监测用
     * @return
     */
    // @ApiOperation("对接域名监测程序")
    @GetMapping(value = "/F5Check")
    public String F5Check() {
        return "tang";
    }

}
