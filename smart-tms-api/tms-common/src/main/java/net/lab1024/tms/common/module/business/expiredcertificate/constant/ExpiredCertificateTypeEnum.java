package net.lab1024.tms.common.module.business.expiredcertificate.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 到期证件类型 枚举类
 *
 * @author listen
 * @date 2022/07/19 10:20
 */
@Getter
@AllArgsConstructor
public enum ExpiredCertificateTypeEnum implements BaseEnum {

    SHEN_FEN_ZHENG(1, "身份证"),
    JIA_SHI_ZHENG(2, "驾驶证"),
    XING_SHI_ZHENG(3, "车辆行驶证"),
    CONG_YE_ZI_GE_ZHENG(4, "从业资格证"),
    GUA_CHE_XING_SHI_ZHENG(5, "挂车行驶证"),
    HUO_ZHU_HE_TONG(6, "货主合同"),
    SI_JI_HE_TONG(7, "司机合同"),
    SHANG_YE_XIAN(8, "商业险"),
    JIAO_QIANG_XIAN(9, "交强险"),
    DAO_LU_YUN_SHU_XU_KE_ZHENG(10, "道路运输许可证"),
    GUA_KAO_ZHENG(11, "挂靠企业道运证"),
    ;

    private final Integer value;

    private final String desc;
}
