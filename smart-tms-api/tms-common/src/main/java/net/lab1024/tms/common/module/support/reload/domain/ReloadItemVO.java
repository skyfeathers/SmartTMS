package net.lab1024.tms.common.module.support.reload.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/***
 *
 * @author 开云
 */
@Data
public class ReloadItemVO {

    @ApiModelProperty("加载项标签")
    private String tag;

    @ApiModelProperty("参数")
    private String args;

    @ApiModelProperty("运行标识")
    private String identification;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime createTime;


}
