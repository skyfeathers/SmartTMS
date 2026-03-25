package net.lab1024.tms.admin.module.business.reportform.shipper.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 货主日报表导出
 *
 * @author lidoudou
 * @date 2023/3/4 下午2:56
 */
@Data
public class ShipperDayStatisticExportVO extends ShipperDayStatisticVO {

    @ApiModelProperty("序号")
    @Excel(name = "序号", width = 10)
    private Integer serialNo;

}
