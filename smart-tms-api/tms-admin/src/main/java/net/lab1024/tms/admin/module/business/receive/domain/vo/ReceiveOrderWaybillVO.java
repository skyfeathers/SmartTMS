package net.lab1024.tms.admin.module.business.receive.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.waybill.domain.vo.WaybillCostDetailVO;

import java.math.BigDecimal;

/**
 * 应收运单列表
 *
 * @author lidoudou
 * @date 2022/12/13 下午4:24
 */
@Data
public class ReceiveOrderWaybillVO extends WaybillCostDetailVO {

    @ApiModelProperty("本次应收金额")
    private BigDecimal thisReceiveAmount;
}
