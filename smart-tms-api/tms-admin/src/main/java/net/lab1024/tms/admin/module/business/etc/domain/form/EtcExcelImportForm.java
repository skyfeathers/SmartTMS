package net.lab1024.tms.admin.module.business.etc.domain.form;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
/**
 * @author yandy
 * @description:
 * @date 2023/3/10 11:29 上午
 */
@Data
public class EtcExcelImportForm {

    @Excel(name = "ETC卡号")
    private String etcNo;

    @Excel(name = "密码")
    private String password;

    @Excel(name = "持卡司机")
    private String driverName;

    @Excel(name = "持卡司机手机号")
    private String driverTelephone;

    @Excel(name = "持卡车辆")
    private String vehicleNumber;

    @Excel(name = "状态")
    private String disabledFlag;

    @Excel(name = "备注")
    private String remark;


}