package net.lab1024.tms.common.module.support.operatelog.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Data
public class OperateLogQueryForm extends PageParam {


    @ApiModelProperty("开始日期")
    private String startDate;

    @ApiModelProperty("结束日期")
    private String endDate;

    @ApiModelProperty("url")
    private String url;


    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("请求结果 false失败 true成功")
    private Boolean successFlag;

    private Long enterpriseId;

}
