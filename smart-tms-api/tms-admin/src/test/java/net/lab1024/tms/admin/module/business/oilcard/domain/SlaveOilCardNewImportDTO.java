package net.lab1024.tms.admin.module.business.oilcard.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class SlaveOilCardNewImportDTO {

    @Excel(name = "副卡全称")
    private String oilCardNo;

    @Excel(name = "主卡")
    private String masterCardNo;
}
