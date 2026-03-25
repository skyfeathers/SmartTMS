package net.lab1024.tms.admin.module.business.vehicle.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 车辆导出
 *
 * @author lidoudou
 * @date 2022/11/21 下午3:00
 */
@Data
public class VehicleExportVO {

    @Excel(name = "车牌号", width = 20)
    private String vehicleNumber;

    @Excel(name = "审核状态")
    private String auditStatusDesc;

    @Excel(name = "状态")
    private String disabledFlagDesc;

    @Excel(name = "所属企业名称", width = 20)
    private String enterpriseName;

    @Excel(name = "速记码")
    private String shorthand;

    @Excel(name = "车辆类型", width = 20)
    private String vehicleTypeDesc;

    @Excel(name = "经营方式", width = 20)
    private String businessModeDesc;

    @Excel(name = "关联挂车", width = 20)
    private String bracketNo;

    @Excel(name = "关联司机", width = 20)
    private String driverNameList;

    @Excel(name = "道路运输证号", width = 20)
    private String roadTransportCertificateNumber;

    @Excel(name = "道路运输证有效期结束时间", format = "yyyy-MM-dd", width = 20)
    private LocalDate roadTransportCertificateExpireDate;

    @Excel(name = "车审时间", format = "yyyy-MM-dd", width = 20)
    private LocalDate vehicleAuditExpireDate;

    @Excel(name = "挂靠到期时间", format = "yyyy-MM-dd", width = 20)
    private LocalDate relyEnterpriseExpireDate;

    @Excel(name = "创建人")
    private String createUserName;

    @Excel(name = "创建时间", format = "yyyy-MM-dd HH:mm:ss", width = 20)
    private LocalDateTime createTime;

    @Excel(name = "备注")
    private String remark;
}
