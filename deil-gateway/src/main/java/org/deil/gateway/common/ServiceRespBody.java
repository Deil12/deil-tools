package org.deil.gateway.common;

import java.io.Serializable;
import java.util.Objects;

//@ApiModel("后台网关响应体")
public abstract class ServiceRespBody implements Serializable {

    //@ApiModelProperty(value = "日志定位标识", example = "1234567890qwertyuiopasdfghjklzxc")
    private String logId;

    //@ApiModelProperty(value = "响应码", example = "200")
    private Integer code;

    //@ApiModelProperty(value = "响应信息")
    private String message;

    public ServiceRespBody() {
    }

    protected ServiceRespBody(String logId, Integer code, String message) {
        this.logId = logId;
        this.code = code;
        this.message = message;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLogId() {
        return this.logId;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof ServiceRespBody)) {
            return false;
        } else {
            ServiceRespBody that = (ServiceRespBody)o;
            return this.code == that.code && Objects.equals(this.logId, that.logId) && Objects.equals(this.message, that.message);
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.logId, this.code, this.message});
    }

    @Override
    public String toString() {
        return "ServiceRespBody{" +
                "logId='" + logId + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
