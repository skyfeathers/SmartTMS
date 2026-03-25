package net.lab1024.tms.admin.module.business.reportform.employee.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 销售日报表导出
 *
 * @author zhaoxinyang
 * @date 2023/12/10 09:27
 */
@Data
public class SaleDayStatisticExportVO extends SaleDayStatisticVO {

    @ApiModelProperty("序号")
    @Excel(name = "序号", width = 10)
    private String serialNo;

}
