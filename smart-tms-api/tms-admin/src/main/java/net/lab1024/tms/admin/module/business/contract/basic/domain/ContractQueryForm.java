package net.lab1024.tms.admin.module.business.contract.basic.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.contract.constant.ContractSignerTypeEnum;
import net.lab1024.tms.common.module.business.contract.constant.ContractStatusEnum;

import java.time.LocalDate;

/**
 * 合同接口查询
 *
 * @author lihaifan
 * @date 2022/7/15 11:12
 */
@Data
public class ContractQueryForm extends PageParam {

    @ApiModelProperty("关键字")
    private String keywords;

    @ApiModelProperty(value = "企业ID",hidden = true)
    private Long enterpriseId;

    @ApiModelPropertyEnum(value = ContractStatusEnum.class, desc = "合同状态")
    @CheckEnum(value = ContractStatusEnum.class, message = "合同状态错误")
    private Integer status;

    @ApiModelProperty("创建开始时间")
    private LocalDate createStartTime;

    @ApiModelProperty("创建结束时间")
    private LocalDate createEndTime;

    @ApiModelPropertyEnum(value = ContractSignerTypeEnum.class, desc = "签署人类型")
    @CheckEnum(value = ContractSignerTypeEnum.class, message = "签署人类型错误")
    private Integer signerType;

    @ApiModelProperty("签署人归属ID")
    private Long signerBelongId;

    @ApiModelProperty("运单ID")
    private Long waybillId;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("合同类型")
    private Integer contractType;;

    @ApiModelProperty(hidden = true)
    private Boolean deletedFlag;
}
