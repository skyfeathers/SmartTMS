package net.lab1024.tms.admin.module.business.oilcard.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class MasterOilCardImportDTO {

    @Excel(name = "主卡卡号")
    private String oilCardNo;

    @Excel(name = "期初余额")
    private String balance;

    @Excel(name = "所属公司")
    private String enterpriseName;

    @Excel(name = "油卡品牌")
    private String model;

    @Excel(name = "卡状态")
    private String disabledFlags;
}
