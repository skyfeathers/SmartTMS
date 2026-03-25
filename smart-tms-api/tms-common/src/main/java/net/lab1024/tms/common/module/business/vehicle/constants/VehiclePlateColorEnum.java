package net.lab1024.tms.common.module.business.vehicle.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/***
 * 车牌颜色
 *
 * @author lidoudou
 * @date 2022/6/25 下午5:07
 */
@AllArgsConstructor
@Getter
public enum VehiclePlateColorEnum implements BaseEnum {

    /**
     * 1 蓝色
     */
    BLUE(1, "蓝色"),

    /**
     * 2 黄色
     */
    YELLOW(2, "黄色"),

    /**
     * 3 黑色
     */
    BLACK(3, "黑色"),

    /**
     * 4 白色
     */
    WHITE(4, "白色"),

    /**
     * 5 绿色
     */
    GREEN(5, "绿色"),

    /**
     * 9 其它
     */
    OTHER(9, "其它"),

    /**
     * 91 农黄色
     */
    AGRICULTURAL_YELLOW(91, "农黄色"),

    /**
     * 92 农绿色
     */
    AGRICULTURAL_GREEN(92, "农绿色"),

    /**
     * 93 黄绿色
     */
    YELLOW_GREEN(93, "黄绿色"),

    /**
     * 94 渐变绿
     */
    GRADIENT_GREEN(94, "渐变绿"),

    ;

    private final Integer value;

    private final String desc;
}
