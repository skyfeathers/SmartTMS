package net.lab1024.tms.common.module.support.ocr.domain.bo;

import lombok.Data;

/**
 * 行驶证背面
 *
 * @author yandy
 * @description:
 * @date 2022/7/19 2:04 下午
 */
@Data
public class RecognizeVehicleLicenseBackBO {

    /**
     * 档案编号 411101042471
     */
    private String recordNumber;

    /**
     * 号牌号码 豫LL2776
     */
    private String licensePlateNumber;

    /**
     * 核载人数 2人
     */
    private String passengerCapacity;

    /**
     * 整备质量 7300kg
     */
    private String curbWeight;

    /**
     * 总质量
     */
    private String totalWeight;

    /**
     * 核定载质量
     */
    private String permittedWeight;

    /**
     * 外廓尺寸 6105×2490×3800mm
     */
    private String overallDimension;

    /**
     * 牵引总质量 34500kg
     */
    private String tractionWeight;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 检验记录 检验有效期至2022年03月豫L
     */
    private String inspectionRecord;

    /**
     * 柴油
     */
    private String energySign;
}