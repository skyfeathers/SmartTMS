package net.lab1024.tms.admin.module.business.bracket.domain.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class BracketExcelImportDTO {

    @Excel(name = "挂车车牌号")
    private String bracketNo;

    @Excel(name = "速记码")
    private String shorthandCode;

    @Excel(name = "品牌型号")
    private String type;

    /**
     * 经营方式
     */
    @Excel(name = "经营方式")
    private String businessMode;

    /**
     * 所属公司
     */
    @Excel(name = "所属公司")
    private String enterpriseName;

    /**
     * 负责人
     */
    @Excel(name = "负责人")
    private String managerName;

    @Excel(name = "实际所属人")
    private String owner;

    @Excel(name = "使用性质")
    private String nature;

    @Excel(name = "车辆识别代码")
    private String vin;

    /**
     * 车牌颜色
     */
    @Excel(name = "车牌颜色")
    private String plateColorCode;


    /**
     * 重量
     */
    @Excel(name = "重量（吨）")
    private String weight;

    /**
     * 载重（吨）
     */
    @Excel(name = "载重（吨）")
    private String tonnage;

    /**
     * 注册日期
     */
    @Excel(name = "注册日期")
    private String registerTime;

    /**
     * 发证日期
     */
    @Excel(name = "发证日期")
    private String issueTime;
}
