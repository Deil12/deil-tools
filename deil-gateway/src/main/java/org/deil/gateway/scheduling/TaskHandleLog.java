package org.deil.gateway.scheduling;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.InetAddress;

@Slf4j
@Component
@Data
public class TaskHandleLog {

    @Autowired
    private SendLogToFtpConfig sendLogToFtpConfig;

    //获取当前微服务工程根目录下的日志文件夹所在绝对路径 如：E:\ChinaSouthernAirlines\cargo-exchange\Log
    //private String fileNamePath = System.getProperty("user.dir")+"\\Log";

    /**
     * 对应服务，不同服务，log地址不同
     * 每天凌晨零点开启定时任务，将7天前之前的日志压缩！
     */
    //@Scheduled(fixedRate = 3000) //测试：每3秒一次执行
    //@Scheduled(cron = "0 0 0 1/7 * ?")
    @Scheduled(cron = "0 0 0 * * ?")
    public void doTask() {
        String fileNamePath = sendLogToFtpConfig.getLogFilePath();
        if (fileNamePath == null || "".equals(fileNamePath)) {
            fileNamePath = System.getProperty("user.dir") + "\\gatewayLog\\";
        }
        log.info("日志压缩备份-定时任务启动！，文件路径为：{}", fileNamePath);
        try {
            /*TaskUtils.backups(fileNamePath);*/
            //压缩包上传FTP
            if (!sendLogToFtpConfig.isEnabled()) {
                return;//上传FTP开关
            }
            String ip = sendLogToFtpConfig.getLogIp();
            if (!StringUtils.hasText(ip)) {
                ip = InetAddress.getLocalHost().getHostAddress();//没有指定上传ip地址文件夹，则默认本机IP
            }
            String ftpPath = sendLogToFtpConfig.getPath();
            String servicePath = "Gateway/" + ip;
            ftpPath = ftpPath.charAt(ftpPath.length() - 1) == '/' ? ftpPath + servicePath : ftpPath + "/" + servicePath;
            /*TaskUtils.sendZipToFTP(sendLogToFtpConfig.getHostIp(), sendLogToFtpConfig.getPort(), sendLogToFtpConfig.getUserName(), sendLogToFtpConfig.getPassword(), ftpPath);*/
        } catch (Exception e) {
            log.info("日志压缩备份失败：\r\n", e);
        }
    }
}
