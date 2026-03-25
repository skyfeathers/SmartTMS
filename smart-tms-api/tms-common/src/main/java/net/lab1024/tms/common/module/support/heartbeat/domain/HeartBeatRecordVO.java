package net.lab1024.tms.common.module.support.heartbeat.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 
 * [  ]
 * 
 * @author 罗伊
 * @date  
 */
@Data
public class HeartBeatRecordVO {

    private Integer heartBeatRecordId;

    @ApiModelProperty("项目路径")
    private String projectPath;

    @ApiModelProperty("服务器ip")
    private String serverIp;

    @ApiModelProperty("进程号")
    private Integer processNo;

    @ApiModelProperty("进程开启时间")
    private Date processStartTime;

    @ApiModelProperty("心跳当前时间")
    private Date heartBeatTime;


}
