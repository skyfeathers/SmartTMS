package net.lab1024.tms.common.module.support.datatracer.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * [  ]
 *
 * @author 罗伊
 */
public interface DataTracerOperateTypeEnum extends BaseEnum {

    Integer INIT_CODE = 3;

    @AllArgsConstructor
    @Getter
    enum Common implements BaseEnum {
        SAVE(1, "保存"),
        UPDATE(2, "更新"),
        DELETE(3, "删除"),
        AUDIT(4, "审核");

        private final Integer value;

        private final String desc;
    }

}
