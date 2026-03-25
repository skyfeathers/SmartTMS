package net.lab1024.tms.fixedasset.module.business.scrap.constant;

import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * @Author 1024创新实验室-主任:卓大
 * @Date 2023/3/23 18:36:10
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2023
 */
public enum AssetScrapStatusEnum implements BaseEnum {

    AUDIT(1, "待审核"),
    REJECT(2, "审核驳回"),
    PASS(3, "审核通过"),

    ;

    private Integer status;

    private String desc;

    AssetScrapStatusEnum(Integer status, String desc) {
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
