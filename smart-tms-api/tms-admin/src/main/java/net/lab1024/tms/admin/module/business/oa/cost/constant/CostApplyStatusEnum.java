package net.lab1024.tms.admin.module.business.oa.cost.constant;

import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 费用申请状态
 *
 * @author lidoudou
 * @date 2023/3/29 下午4:46
 */
public enum CostApplyStatusEnum implements BaseEnum {

    AUDIT(1, "待审核"),
    REJECT(2, "审核驳回"),

    PASS(3, "审核完成"),

    ;

    private Integer status;

    private String desc;

    CostApplyStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return status;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
