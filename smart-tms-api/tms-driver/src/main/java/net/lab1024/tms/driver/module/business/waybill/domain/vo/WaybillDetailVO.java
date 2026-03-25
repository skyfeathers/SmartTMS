package net.lab1024.tms.driver.module.business.waybill.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.module.business.waybill.domain.vo.WaybillVoucherVO;
import net.lab1024.tms.driver.module.business.order.domain.dto.OrderPathDTO;

import java.util.List;

/**
 * @author zhuoda
 * @Date 2022-07-14
 */
@Data
public class WaybillDetailVO extends WaybillVO {
    
    @ApiModelProperty("货物信息")
    private List<WaybillGoodsVO> goodsList;

    @ApiModelProperty("路线信息")
    private List<OrderPathDTO> pathList;
    
    @ApiModelProperty("物流凭证/磅单信息")
    private List<WaybillVoucherVO> voucherVOList;

}
