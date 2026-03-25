package net.lab1024.tms.admin.module.business.oilcard.domain.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author yandy
 * @description:
 * @date 2022/9/14 2:20 下午
 */
@Data
public class OilCardExcelImportDTO {

    @Excel(name = "主卡")
    private String masterOilCardNo;

    @Excel(name = "副卡卡号")
    private String salveOilCardNo;

}