package net.lab1024.tms.fixedasset.module.business.repair.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.serializer.DictValueVoSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 固定资产-维修登记 列表VO
 *
 * @Author 卓大
 * @Date 2023-03-23 15:01:51
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */

@Data
public class RepairVO {

    private Long repairId;

    @ApiModelProperty("所属公司")
    private Long enterpriseId;

    @ApiModelProperty("所属公司")
    private String enterpriseName;

    @ApiModelProperty(value = "维修单号")
    private String repairCode;

    @ApiModelProperty(value = "维修厂家")
    @JsonSerialize(using = DictValueVoSerializer.class)
    private String repairCompany;

    @ApiModelProperty(value = "单据状态")
    private Integer status;

    @ApiModelProperty(value = "业务日期")
    private LocalDate businessDate;

    @ApiModelProperty(value = "维修内容")
    private String content;

    @ApiModelProperty(value = "申请维修人员")
    private Long applyUserId;

    @ApiModelProperty(value = "申请维修人员名称")
    private String applyUserName;


    @ApiModelProperty(value = "维修花费")
    private BigDecimal repairCost;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

}