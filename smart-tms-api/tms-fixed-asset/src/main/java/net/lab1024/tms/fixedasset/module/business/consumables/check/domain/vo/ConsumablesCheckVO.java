package net.lab1024.tms.fixedasset.module.business.consumables.check.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 盘点详情
 *
 * @author lidoudou
 * @date 2023/3/24 上36
 */
@Data
public class ConsumablesCheckVO {

    @ApiModelProperty("盘点ID")
    private Long checkId;

    @ApiModelProperty("盘点编号")
    private String checkNo;

    @ApiModelProperty("盘点名称")
    private String checkName;

    @ApiModelProperty("盘点位置 - ID")
    private Long locationId;

    @ApiModelProperty("盘点位置")
    private String locationName;

    @ApiModelProperty("所属公司 - ID")
    private Long enterpriseId;

    @ApiModelProperty("所属公司")
    private String enterpriseName;

    @ApiModelProperty("盘点状态")
    private Integer status;

    @ApiModelProperty("完成时间")
    private LocalDateTime completeTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
