package net.lab1024.tms.admin.module.business.driver.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 司机导出
 *
 * @author lidoudou
 * @date 2022/11/21 下午2:35
 */
@Data
public class DriverExportVO {

    @Excel(name = "姓名", width = 15)
    private String driverName;

    @Excel(name = "电话", width = 20)
    private String telephone;

    @Excel(name = "关联车辆", width = 20)
    private String vehicleNumberList;

    @Excel(name = "所属公司", width = 30)
    private String enterpriseName;

    @Excel(name = "身份证号", width = 20)
    private String drivingLicense;

    @Excel(name = "身份证有效期", width = 20)
    private LocalDate idCardEffectiveEndDate;

    @Excel(name = "速记码", width = 20)
    private String shorthandCode;

    @Excel(name = "审核状态", width = 20)
    private String auditStatusDesc;

    @Excel(name = "创建人姓名", width = 20)
    private String createUserName;

    @Excel(name = "创建时间", format = "yyyy-MM-dd HH:mm:ss", width = 20)
    private LocalDateTime createTime;
}
