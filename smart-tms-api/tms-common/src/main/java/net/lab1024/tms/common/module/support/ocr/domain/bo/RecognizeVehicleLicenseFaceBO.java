package net.lab1024.tms.common.module.support.ocr.domain.bo;

import lombok.Data;

/**
 * 行驶证正面
 * @author yandy
 * @description:
 * @date 2022/7/19 2:04 下午
 */
@Data
public class RecognizeVehicleLicenseFaceBO {

    /**
     * 住址 成都市龙泉驿区山泉镇联合村
     */
    private String address;

    /**
     * 发动机编号 8B213508
     */
    private String engineNumber;

    /**
     * 发证日期 2015-06-04
     */
    private String issueDate;

    /**
     * 品牌型号  北京现代牌BH7164MX
     */
    private String model;

    /**
     * 所有人
     */
    private String owner;

    /**
     * 车牌号
     */
    private String licensePlateNumber;

    /**
     * 注册日期 2008-07-08
     */
    private String registrationDate;

    /**
     * 使用性质 非营运
     */
    private String useNature;

    /**
     * 车型 小型轿车
     */
    private String vehicleType;

    /**
     * 车辆识别号码 LBEHDAEB58Y038860
     */
    private String vinCode;

    /**
     * 发证机关 四川省成都市公安局交通警察支队
     */
    private String issueAuthority;


}