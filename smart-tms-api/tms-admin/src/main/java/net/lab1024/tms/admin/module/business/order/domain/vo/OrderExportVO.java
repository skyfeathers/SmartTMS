package net.lab1024.tms.admin.module.business.order.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单导出
 *
 * @author lidoudou
 * @date 2022/11/22 上午09:04
 */
@Data
public class OrderExportVO {

    @Excel(name = "订单号", width = 20)
    private String orderNo;

    @Excel(name = "调度员", width = 15)
    private String scheduleName;

    @Excel(name = "货主简称", width = 20)
    private String shortName;

    @DataTracerFieldDoc("订单类型")
    private String orderTypeDesc;

    @Excel(name = "装货时间", format = "yyyy-MM-dd HH:mm", width = 20)
    private LocalDateTime loadTime;

    @Excel(name = "卸货时间", format = "yyyy-MM-dd HH:mm", width = 20)
    private LocalDateTime unloadTime;

    @Excel(name = "最迟提箱时间", format = "yyyy-MM-dd HH:mm", width = 20)
    private LocalDateTime latestPackingTime;

    @Excel(name = "所属区域")
    private String areaDesc;

    @Excel(name = "运输类型")
    private String businessTypeCodeDesc;

    @Excel(name = "业务类型",width = 15)
    private String containerBusinessTypeName;

    @Excel(name = "柜型名称",width = 15)
    private String cabinetName;

    @Excel(name = "装箱地点", width = 30)
    private String containerLocation;

    @Excel(name = "装货地点", width = 30)
    private String placingLocation;

    @Excel(name = "卸货地点", width = 30)
    private String unloadingLocation;

    @Excel(name = "还箱地点", width = 30)
    private String returnContainerLocation;

    @Excel(name = "订单状态")
    private String statusDesc;

    @Excel(name = "企业名称", width = 20)
    private String enterpriseName;

    @Excel(name = "创建人")
    private String createUserName;

    @Excel(name = "创建时间", format = "yyyy-MM-dd HH:mm:ss", width = 20)
    private LocalDateTime createTime;
}
