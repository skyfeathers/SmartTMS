package net.lab1024.tms.common.module.support.helpdoc.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 帮助中心 - 浏览记录 VO
 *
 * @author: zhuoda
 * @date: 2022/7/17 21:06
 */
@Data
public class HelpDocViewRecordVO {

    @ApiModelProperty("ID")
    private Long userId;

    @ApiModelProperty("姓名")
    private String userName;

    @ApiModelProperty("查看次数")
    private Integer pageViewCount;

    @ApiModelProperty("首次ip")
    private String firstIp;

    @ApiModelProperty("首次用户设备等标识")
    private String firstUserAgent;

    @ApiModelProperty("首次查看时间")
    private LocalDateTime createTime;

    @ApiModelProperty("最后一次 ip")
    private String lastIp;

    @ApiModelProperty("最后一次 用户设备等标识")
    private String lastUserAgent;

    @ApiModelProperty("最后一次查看时间")
    private LocalDateTime updateTime;
}
