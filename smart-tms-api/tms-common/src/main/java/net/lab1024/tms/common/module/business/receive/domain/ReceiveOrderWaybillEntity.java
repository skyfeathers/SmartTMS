package net.lab1024.tms.common.module.business.receive.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemCategoryEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 应收帐款 关联订单
 *
 * @author lidoudou
 * @date 2022/8/2 上午11:51
 */
@Data
@TableName("t_receive_order_waybill")
public class ReceiveOrderWaybillEntity {

    @TableId(type = IdType.AUTO)
    private Long receiveWaybillId;

    /**
     * 应收帐款ID
     */
    private Long receiveOrderId;

    /**
     * 运单ID
     */
    private Long waybillId;

    /**
     * 费用
     */
    private BigDecimal receiveAmount;

    private String remark;


    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
