package net.lab1024.tms.admin.module.business.pay.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 付款单运单维度导出
 *
 * @author zhaoxinyang
 * @date 2025/10/30 10:00
 */
@Data
public class PayOrderWaybillExportVO {

    @ApiModelProperty("付款单ID")
    private Long payOrderId;

    @ApiModelProperty("付款单号")
    @Excel(name = "付款单号", width = 20)
    private String payOrderNumber;

    @ApiModelProperty("运单ID")
    private Long waybillId;

    @ApiModelProperty("运单号")
    @Excel(name = "运单编号", width = 20)
    private String waybillNumber;

    @ApiModelProperty("司机ID")
    private Long driverId;

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    // ------------------司机信息--------------------

    @ApiModelProperty("姓名")
    @Excel(name = "姓名", width = 15)
    private String driverName;

    @ApiModelProperty("身份证号")
    @Excel(name = "身份证号", width = 20)
    private String drivingLicense;

    @ApiModelProperty("身份证正面图片")
    private String idCardFrontPic;

    @ApiModelProperty("身份证正面链接")
    @Excel(name = "身份证正面链接", width = 20)
    private String idCardFrontPicUrl;

    @ApiModelProperty("身份证反面图片")
    private String idCardBackPic;

    @ApiModelProperty("身份证反面链接")
    @Excel(name = "身份证反面链接", width = 20)
    private String idCardBackPicUrl;

    @ApiModelProperty("手机号")
    @Excel(name = "手机号", width = 20)
    private String telephone;

    @ApiModelProperty("驾驶证正本图")
    private String drivingLicenseFrontPic;

    @ApiModelProperty("驾驶证正面链接")
    @Excel(name = "驾驶证正面链接", width = 20)
    private String drivingLicenseFrontPicUrl;

    @ApiModelProperty("驾驶证副本图")
    private String drivingLicenseBackPic;

    @ApiModelProperty("驾驶证反面链接")
    @Excel(name = "驾驶证反面链接", width = 20)
    private String drivingLicenseBackPicUrl;

    @ApiModelProperty("驾驶证号")
    @Excel(name = "驾驶证号", width = 20)
    private String drivingLicenseNo;

    @ApiModelProperty("从业资格证图片")
    private String qualificationCertificatePic;

    @ApiModelProperty("从业资格证链接")
    @Excel(name = "从业资格证链接", width = 20)
    private String qualificationCertificatePicUrl;

    @ApiModelProperty("从业资格证号")
    @Excel(name = "从业资格证号", width = 20)
    private String qualificationCertificate;

    // ------------------车辆信息--------------------

    @ApiModelProperty("道路运输证号")
    @Excel(name = "道路运输证号", width = 20)
    private String roadTransportCertificateNumber;

    @ApiModelProperty("车牌号")
    @Excel(name = "车牌号", width = 15)
    private String vehicleNumber;

    @ApiModelProperty("车辆类型代码")
    private String vehicleType;

    @ApiModelProperty("车辆类型")
    @Excel(name = "车辆类型", width = 15)
    private String vehicleTypeDesc;

    @ApiModelProperty("车牌颜色代码")
    private Integer vehiclePlateColorCode;

    @ApiModelProperty("车牌颜色")
    @Excel(name = "车牌颜色", width = 10)
    private String vehiclePlateColorDesc;

    @ApiModelProperty("车辆能源类型代码")
    private String vehicleEnergyType;

    @ApiModelProperty("车辆能源类型")
    @Excel(name = "车辆能源类型", width = 10)
    private String vehicleEnergyTypeDesc;

    @ApiModelProperty("外廓尺寸")
    @Excel(name = "外廓尺寸", width = 20)
    private String gabarite;

    @ApiModelProperty("车辆载质量")
    @Excel(name = "车辆载质量", width = 10)
    private BigDecimal vehicleTonnage;

    @ApiModelProperty("车辆总质量")
    @Excel(name = "车辆总质量", width = 10)
    private BigDecimal grossMass;

    @ApiModelProperty("行驶证附件")
    private String drivingLicenseAttachment;

    @ApiModelProperty("行驶证正页链接")
    @Excel(name = "行驶证正页链接", width = 20)
    private String drivingLicenseAttachmentUrl;

    @ApiModelProperty("行驶证副本-附件")
    private String drivingLicenseEctypeAttachment;

    @ApiModelProperty("行驶证副页链接")
    @Excel(name = "行驶证副页链接", width = 20)
    private String drivingLicenseEctypeAttachmentUrl;

    @ApiModelProperty("道路运输证-附件")
    private String roadTransportCertificateAttachment;

    @ApiModelProperty("道路运输证链接")
    @Excel(name = "道路运输证链接", width = 20)
    private String roadTransportCertificateAttachmentUrl;

}
