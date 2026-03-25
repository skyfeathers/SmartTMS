package net.lab1024.tms.admin.module.business.material.businesstype.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * @author zhaoxinyang
 */
@Getter
@AllArgsConstructor
public enum TripTypeEnum implements BaseEnum {

    QU_CHENG(1, "去程"),

    HUI_CHENG(2, "回程"),

    DUAN_BO(3, "短驳"),

    ;

    private final Integer value;

    private final String desc;


}
