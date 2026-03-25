package net.lab1024.tms.common.module.business.oa.news.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 资讯 可见范围 数据类型枚举
 *
 * @author listen
 * @date 2022/07/14 21:20
 */
@Getter
@AllArgsConstructor
public enum NoticeVisibleRangeDataTypeEnum implements BaseEnum {

    EMPLOYEE(1, "员工"),

    DEPARTMENT(2, "部门"),

    ;

    private final Integer value;

    private final String desc;
}
