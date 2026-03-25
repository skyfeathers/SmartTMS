package net.lab1024.tms.admin.module.business.oilcard.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 主卡流水导出
 *
 * @author lidoudou
 * @date 2022/8/10 上午09:28
 */
@Data
public class OilCardBalanceRecordExportVO {

    @Excel(name = "日期", format = "yyyy-MM-dd", width = 10)
    private LocalDateTime createTime;

    @Excel(name = "交易流水号", width = 20)
    private String balanceRecordNo;

    @Excel(name = "主卡号", width = 20)
    private String oilCardNo;

    @Excel(name = "交易金额", width = 20)
    private BigDecimal changeBalance;

    @Excel(name = "主卡交易后余额", width = 20)
    private BigDecimal afterBalance;

    @Excel(name = "副卡号", width = 10)
    private String slaveOilCardNo;

    @Excel(name = "本次交易金额", width = 20)
    private BigDecimal slaveChangeBalance;

    @Excel(name = "累计充值金额", width = 20)
    private BigDecimal slaveAfterBalance;

    @Excel(name = "交易类型", width = 10)
    private String recordTypeDesc;

    @Excel(name = "交易时间", format = "yyyy-MM-dd HH:mm:ss", width = 20)
    private LocalDateTime dealTime;

    @Excel(name = "备注", width = 20)
    private String remark;

}
