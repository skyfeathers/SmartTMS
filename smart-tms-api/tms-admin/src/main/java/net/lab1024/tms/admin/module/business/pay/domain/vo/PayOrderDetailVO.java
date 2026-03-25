package net.lab1024.tms.admin.module.business.pay.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/8/15 11:53 上午
 */
@Data
public class PayOrderDetailVO extends PayOrderVO {

    @ApiModelProperty("费用项")
    private List<PayOrderCostVO> costList;

}
