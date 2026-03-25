package net.lab1024.tms.admin.module.business.oilcard.domain.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 副卡导入
 *
 * @Author lidoudou
 * @Date 2023/5/12 上午10:19
 */
@Data
public class SlaveOilCardImportDTO {

    @Excel(name = "副卡全称")
    private String oilCardNo;

    @Excel(name = "主卡全称")
    private String masterCardNo;

    @Excel(name = "油卡品牌")
    private String brand;

    @Excel(name = "密码")
    private String password;

    @Excel(name = "期初余额")
    private BigDecimal beginBalance;

    @Excel(name = "办卡人")
    private String createUserName;
}
