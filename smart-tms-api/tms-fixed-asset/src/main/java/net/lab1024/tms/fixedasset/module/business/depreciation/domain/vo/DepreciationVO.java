package net.lab1024.tms.fixedasset.module.business.depreciation.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 折旧表
 *
 * @author lidoudou
 * @date 2023/4/10 下午4
 */
@Data
public class DepreciationVO {

    @ApiModelProperty("折旧ID")
    private Long depreciationId;

    @ApiModelProperty("所属公司")
    private Long enterpriseId;

    @ApiModelProperty("所属公司")
    private String enterpriseName;

    @ApiModelProperty("折旧编号")
    private String depreciationNo;

    @ApiModelProperty("计提日期")
    private LocalDate depreciationDate;

    private String remark;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("资产列表")
    private List<DepreciationItemVO> assetList;
}
