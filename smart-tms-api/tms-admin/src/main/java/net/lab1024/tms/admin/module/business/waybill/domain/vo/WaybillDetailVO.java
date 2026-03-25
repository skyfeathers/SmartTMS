package net.lab1024.tms.admin.module.business.waybill.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.order.domain.dto.OrderPathDTO;
import net.lab1024.tms.admin.module.business.receive.domain.vo.ReceiveOrderInvoiceVO;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillYunJianStatusEnum;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhuoda
 * @Date 2022-07-14
 */
@Data
public class WaybillDetailVO extends WaybillVO {

    @ApiModelProperty("货物信息")
    private List<WaybillGoodsVO> goodsList;

    @ApiModelProperty("应付费用信息")
    private List<WaybillCostVO> costList;

    @ApiModelProperty("应收费用信息")
    private List<WaybillReceiveCostVO> receiveCostList;


}
