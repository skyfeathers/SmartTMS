package net.lab1024.tms.admin.module.business.receive.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yandy
 * @description:
 * @date 2023/11/8 3:41 下午
 */
@Data
public class ReceiveOrderMakeInvoiceExcelVO {

    @Excel(name = "起运地", width = 20)
    private String loadAddress;

    @Excel(name = "到达地", width = 20)
    private String unloadAddress;

    @Excel(name="运输工具种类",width = 20)
    private String transportType;

    @Excel(name="运输工具牌号",width = 20)
    private String vehicleNumber;

    @Excel(name="运输货物名称",width = 20)
    private String goodsName;

    @Excel(name="运单号",width = 20)
    private String waybillNumber;

    @Excel(name = "计算方式")
    private String freightSettleType;

    @Excel(name = "结算数量", width = 20, type = 10)
    private BigDecimal num;

    @Excel(name = "运输费", width = 20, type = 10)
    private BigDecimal freightTotalAmount;

    @Excel(name = "税率", width = 20)
    private String tax;

    @Excel(name = "税额", width = 20, type = 10)
    private BigDecimal taxAmount;

}