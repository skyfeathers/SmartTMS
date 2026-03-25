package net.lab1024.tms.admin.module.business.vehicle.domain.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 车辆信息导入对象
 *
 * @author lidoudou
 * @date 2022/10/31 上午9:11
 */
@Data
public class VehicleImportDTO {

    @Excel(name = "速记码")
    private String shorthand;

    @Excel(name = "车牌号")
    private String vehicleNumber;

    /**
     * 经营方式 需特殊处理的标注出来
     */
    @Excel(name = "经营方式")
    private String businessMode;

    /**
     * 所属公司
     */
    @Excel(name = "所属公司")
    private String enterpriseName;

    /**
     * 绑定司机
     */
    @Excel(name = "绑定司机")
    private String driverName;

    /**
     * 绑定挂车
     */
    @Excel(name = "绑定挂车")
    private String bracketNo;

    /**
     * 负责人
     */
    @Excel(name = "负责人")
    private String managerName;

    @Excel(name = "机动车登记证书编号")
    private String vehicleRegistrationCertificateNo;

    /**
     * 车辆审验到期时间
     */
    @Excel(name = "车辆审验到期时间")
    private String vehicleAuditExpireDate;

    @Excel(name = "发动机号")
    private String engineNumber;

    @Excel(name = "品牌型号")
    private String model;

    /**
     * 车牌颜色
     */
    @Excel(name = "车牌颜色")
    private String vehiclePlateColorCode;

    /**
     * 车辆类型
     */
    @Excel(name = "车辆类型")
    private String vehicleType;

    @Excel(name = "车辆识别代码(车架号)")
    private String vin;

    @Excel(name = "发证机关")
    private String issuingOrganizations;

    /**
     * 注册日期
     */
    @Excel(name = "注册日期")
    private String registerDate;

    /**
     * 发证日期
     */
    @Excel(name = "发证日期")
    private String issueDate;

    /**
     * 总质量
     */
    @Excel(name = "总质量(kg)")
    private String grossMass;

    /**
     * 核定载质量
     */
    @Excel(name = "核定载质量(载重kg)")
    private String vehicleTonnage;

    @Excel(name = "外廓尺寸")
    private String gabarite;

    @Excel(name = "道路运输证号")
    private String roadTransportCertificateNumber;

    /**
     * 道运证有效期开始日期
     */
    @Excel(name = "道运证有效期开始日期")
    private String roadTransportCertificateStartDate;

    /**
     * 道运证有效期结束日期
     */
    @Excel(name = "道运证有效期结束日期")
    private String roadTransportCertificateExpireDate;
}
