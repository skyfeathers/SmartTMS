package net.lab1024.tms.fixedasset.module.business.requisitionrevert.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.fixedasset.module.business.asset.domain.vo.AssetVO;
import net.lab1024.tms.fixedasset.module.business.requisitionrevert.constants.RequisitionStatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 详情
 *
 * @author lidoudou
 * @date 2023/3/21 上午9:08
 */
@Data
public class RequisitionRevertVO {

    @ApiModelProperty("ID")
    private Long requisitionRevertId;

    @ApiModelProperty("编号")
    private String requisitionRevertNo;

    @ApiModelProperty("使用日期")
    private LocalDate useTime;

    @ApiModelProperty("使用公司 - ID")
    private Long enterpriseId;

    @ApiModelProperty("使用公司")
    private String enterpriseName;

    @ApiModelProperty("使用部门 - ID")
    private Long departmentId;

    @ApiModelProperty("使用部门")
    private String departmentName;

    @ApiModelProperty("使用人 - ID")
    private Long userId;

    @ApiModelProperty("使用人")
    private String userName;

    @ApiModelProperty("存放位置 - ID")
    private Long locationId;

    @ApiModelProperty("存放位置")
    private String locationName;

    @ApiModelPropertyEnum(value = RequisitionStatusEnum.class, desc = "领用状态")
    private Integer status;

    @ApiModelProperty("申请说明")
    private String remark;

    private Integer type;

    @ApiModelProperty("创建人ID")
    private Long createUserId;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("资产列表")
    private List<AssetVO> assetList;
}
