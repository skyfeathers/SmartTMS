package net.lab1024.tms.common.module.business.contract.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.contract.constant.ContractStatusEnum;

import java.time.LocalDateTime;

/**
 * 合同内容
 *
 * @author lihaifan
 * @date 2022/7/15 11:10
 */
@Data
public class ContractVO extends ContractCreateForm {

    @ApiModelProperty("合同ID")
    private Integer contractId;

    private String contractTypeName;

    @ApiModelProperty("合同编号")
    private String contractCode;

    @ApiModelProperty("合同负责人名称")
    private String responsibleUserName;

    @ApiModelProperty("线上签署合同ID")
    private String onlineContractId;

    @ApiModelProperty("签署人账号ID（第三方）")
    private String signerAccountsId;

    @ApiModelProperty("企业ID")
    private Long enterpriseId;

    @ApiModelProperty("企业名称")
    private String enterpriseName;

    @ApiModelProperty("企业简称称")
    private String enterpriseShortName;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelPropertyEnum(value = ContractStatusEnum.class,desc = "合同状态")
    private Integer status;

    @ApiModelProperty("创建人ID")
    private Long createUserId;

    @ApiModelProperty("创建人姓名")
    private String createUserName;

    @ApiModelProperty("更新人ID")
    private Long updateUserId;

    @ApiModelProperty("更新人姓名")
    private String updateUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
