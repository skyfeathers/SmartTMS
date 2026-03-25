package net.lab1024.tms.admin.module.business.reportform.employee.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 调度日报表导出
 *
 * @author zhaoxinyang
 * @date 2023/12/12 08:31
 */
@Data
public class ScheduleDayStatisticExportVO extends ScheduleDayStatisticVO {

    @ApiModelProperty("序号")
    @Excel(name = "序号", width = 10)
    private String serialNo;

}
