package net.lab1024.tms.admin.module.business.shipper.domain.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;

/**
 * 货主导入对象
 *
 * @author lidoudou
 * @date 2022/10/31 下午1:35
 */
@Data
public class ShipperExcelImportDTO {

    @Excel(name = "货主名称")
    private String consignor;

    @Excel(name = "货主简称")
    private String shortName;

    /**
     * 货主类型 ShipperNatureEnum
     */
    @Excel(name = "货主类型")
    private String shipperNature;

    /**
     * 所属公司
     */
    @Excel(name = "所属公司")
    private String enterpriseName;

    /**
     * 是否需要开票
     */
    @Excel(name = "是否需要开票")
    private String makeInvoiceFlag;

    /**
     * 业务负责人
     */
    @Excel(name = "业务负责人")
    private String managerName;

    /**
     * 客服负责人
     */
    @Excel(name = "客服负责人")
    private String customerName;

    /**
     * 税点
     */
    @Excel(name = "税点")
    private String taxPoint;

    /**
     * 账期
     */
    @Excel(name = "账期")
    private String accountPeriod;

    // ========================== 联系人信息 ==========================

    /**
     * 联系人姓名
     */
    @Excel(name = "联系人姓名")
    private String contactName;

    /**
     * 联系电话
     */
    @Excel(name = "联系电话")
    private String contactPhone;

    // ========================== 开票信息 ==========================

    @Excel(name = "纳税人识别号")
    private String invoiceNo;

    @Excel(name = "开票抬头")
    private String invoiceName;

    @Excel(name = "开票银行")
    private String invoiceBankName;

    @Excel(name = "开票银行账号")
    private String invoiceBankAccount;

    @Excel(name = "开户行号")
    private String invoiceBankNo;

    @Excel(name = "开户行地址")
    private String invoiceBankAddress;

    @Excel(name = "开票电话")
    private String invoicePhone;

// ========================== 付款方式 ==========================

    /**
     * 付款方式
     */
    @Excel(name = "付款方式")
    private String paymentType;

    /**
     * 是否为公户
     */
    @Excel(name = "是否为公户")
    private String publicAccountFlag;

    @DataTracerFieldDoc("开户名")
    private String accountName;

    @Excel(name = "银行账号")
    private String accountNumber;

    @Excel(name = "银行名称")
    private String bankName;

    @Excel(name = "支行名称")
    private String bankBranchName;

    // ========================== 邮寄地址 ==========================
    @Excel(name = "收件人姓名")
    private String receivePerson;

    @Excel(name = "收件人电话")
    private String receivePersonPhone;

    @Excel(name = "详细地址")
    private String receiveAddress;
}