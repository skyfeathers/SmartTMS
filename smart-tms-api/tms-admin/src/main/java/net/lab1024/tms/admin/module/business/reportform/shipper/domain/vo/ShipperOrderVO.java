package net.lab1024.tms.admin.module.business.reportform.shipper.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.shipper.domain.vo.ShipperSimpleVO;

import java.util.List;

/**
 * 统计货主每月下单量
 *
 * @author lidoudou
 * @date 2022/9/20 下午5:29
 */
@Data
public class ShipperOrderVO extends ShipperSimpleVO {

    @ApiModelProperty("月统计列表")
    private List<ShipperCountMonthVO> monthList;
}
