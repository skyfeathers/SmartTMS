package net.lab1024.tms.common.module.business.contract.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.contract.constant.ContractSignTypeEnum;
import net.lab1024.tms.common.module.business.contract.constant.ContractSignerTypeEnum;
import net.lab1024.tms.common.module.business.contract.constant.ContractStatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 合同管理
 *
 * @author lihaifan
 * @date 2022/7/15 11:10
 */
@TableName("t_contract")
@Data
public class ContractEntity {

    /**
     * 合同ID
     */
    @TableId(type = IdType.AUTO)
    private Integer contractId;

    /**
     * 合同编号
     */
    private String contractCode;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 合同类型
     *
     */
    private Integer contractType;

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 到期日期
     */
    private LocalDate expirationDate;

    /**
     * 合同负责人
     */
    private Long responsibleUserId;

    /**
     * 合同签署类型
     *
     * @see ContractSignTypeEnum
     */
    private Integer signType;

    /**
     * 合同文件
     */
    private String contractFile;

    /**
     * 线上签署合同ID
     */
    private String onlineContractId;

    /**
     * 签署人类型（货主/车队/司机）
     *
     * @see ContractSignerTypeEnum
     */
    private Integer signerType;

    /**
     * 签署人归属ID（货主/车队/司机）
     */
    private Long signerBelongId;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 运单ID
     */
    private Long waybillId;

    /**
     * 运单号
     */
    private String waybillNumber;

    /**
     * 合同状态
     *
     * @see ContractStatusEnum
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除状态
     */
    private Boolean deletedFlag;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 更新人ID
     */
    private Long updateUserId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
