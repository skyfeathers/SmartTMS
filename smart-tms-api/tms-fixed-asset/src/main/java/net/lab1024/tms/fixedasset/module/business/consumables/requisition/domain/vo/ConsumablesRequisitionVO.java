package net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 领用详情
 *
 * @author lidoudou
 * @date 2023/4/14 上午9:45
 */
@Data
public class ConsumablesRequisitionVO {

    @ApiModelProperty("ID")
    private Long requisitionId;

    @ApiModelProperty("编号")
    private String requisitionNo;

    @ApiModelProperty("使用日期")
    private LocalDate useTime;

    @ApiModelProperty("使用公司 - ID")
    private Long enterpriseId;

    @ApiModelProperty("使用公司")
    private String enterpriseName;

    @ApiModelProperty("所属位置 - ID")
    private Long locationId;

    @ApiModelProperty("所属位置")
    private String locationName;

    @ApiModelProperty("使用部门 - ID")
    private Long departmentId;

    @ApiModelProperty("使用部门")
    private String departmentName;

    @ApiModelProperty("使用人 - ID")
    private Long userId;

    @ApiModelProperty("使用人")
    private String userName;

    @ApiModelProperty("申请说明")
    private String remark;

    @ApiModelProperty("创建人ID")
    private Long createUserId;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("资产列表")
    private List<ConsumablesRequisitionItemVO> itemList;
}
