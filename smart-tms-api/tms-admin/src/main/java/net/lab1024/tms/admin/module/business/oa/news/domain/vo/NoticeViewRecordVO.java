package net.lab1024.tms.admin.module.business.oa.news.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 资讯 - 浏览记录 VO
 *
 * @author: listen
 * @date: 2022/7/17 16:06
 */
@Data
public class NoticeViewRecordVO {

    @ApiModelProperty("员工ID")
    private Long employeeId;

    @ApiModelProperty("员工姓名")
    private String employeeName;

    @ApiModelProperty("员工部门名称")
    private String departmentName;

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
