package net.lab1024.tms.admin.module.business.bracket.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 列表展示
 *
 * @author lidoudou
 * @date 2022/6/29 上午11:40
 */
@Data
public class BracketExportVO {

    @Excel(name = "挂车车牌号", width = 20)
    private String bracketNo;

    @Excel(name = "负责人", width = 15)
    private String managerName;

    @Excel(name = "所属公司", width = 30)
    private String enterpriseName;

    @Excel(name = "速记码")
    private String shorthandCode;

    @Excel(name = "重量（kg）", width = 20)
    private BigDecimal weight;

    @Excel(name = "载重（kg）", width = 20)
    private BigDecimal tonnage;

    @Excel(name = "型号", width = 20)
    private String type;

    @Excel(name = "商业险到期时间", width = 20, format = "yyyy-MM-dd")
    private LocalDate commercialExpireTime;

    @Excel(name = "交强险到期时间", width = 20, format = "yyyy-MM-dd")
    private LocalDate compulsoryTrafficExpireTime;

    @Excel(name = "状态")
    private String auditStatusDesc;

    @Excel(name = "创建人")
    private String createUserName;

    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
