package net.lab1024.tms.admin.module.business.repair.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/***
 * 维修信息
 *
 * @author lidoudou
 * @date 2022/6/25 下午2:27
 */
@Data
public class RepairVO extends RepairBaseDTO {

    @ApiModelProperty("维修ID")
    private Long repairId;

    @ApiModelProperty("车牌号")
    private String vehicleNumber;

    @ApiModelProperty("公司名字")
    private String enterpriseName;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("维修厂家名称")
    private String repairPlantName;

    @ApiModelProperty("维修总金额")
    private BigDecimal sumAmount;

    @ApiModelProperty("维修内容")
    private List<RepairContentVO> contentVOList;
}
