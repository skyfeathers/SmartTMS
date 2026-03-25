package net.lab1024.tms.admin.module.business.reportform.waybill.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author zhaoxinyang
 * @description: 运单数量导出
 * @date 2023/09/26
 */
@Data
public class WaybillCountVO {

    @ApiModelProperty("客服ID")
    private Long employeeId;

    @ApiModelProperty("客服名字")
    private String employeeName;

    @ApiModelProperty("运输类型")
    private Integer tripType;

    @ApiModelProperty("运输类型描述")
    private String tripTypeDesc;

    @ApiModelProperty("装/卸货日期")
    private LocalDate loadTime;

    @ApiModelProperty("运单数量")
    private Integer waybillCount;

}
