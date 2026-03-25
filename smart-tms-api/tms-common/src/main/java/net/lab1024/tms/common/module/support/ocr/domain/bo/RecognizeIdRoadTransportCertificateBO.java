package net.lab1024.tms.common.module.support.ocr.domain.bo;

import lombok.Data;

/**
 * @author yandy
 * @description:
 * @date 2022/12/3 2:33 下午
 */
@Data
public class RecognizeIdRoadTransportCertificateBO {

    /**
     * 车牌号
     */
    private String vehicleNumber;

    /**
     * 经济类型
     */
    private String economicType;

    /**
     * 经营范围
     */
    private String businessScope;

    /**
     * 车辆类型
     */
    private String vehicleType;

    /**
     * 吨座位
     */
    private String tonSeat;
    /**
     * 备注
     */
    private String remark;

    /**
     * 经营许可证
     */
    private String roadTransportBusinessLicenseNumber;

    /**
     * 车辆毫米_高
     */
    private String vehicleHeight;

    /**
     * 车辆毫米_宽
     */
    private String vehicleWidth;

    /**
     * 车辆毫米_长
     */
    private String vehicleLength;

    /**
     * 发证日期
     */
    private String issueDate;

    /**
     * 地址
     */
    private String address;

    /**
     * 业户名称
     */
    private String enterpriseName;

    /**
     * 初领日期
     */
    private String initialIssueDate;

}