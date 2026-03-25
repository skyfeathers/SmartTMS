package net.lab1024.tms.admin.module.business.shipper.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 货主导出
 *
 * @author zhaikk
 * @date 2023/7/19
 */
@Data
public class ShipperExportVO {

    @Excel(name = "货主简称", width = 15)
    private String shortName;

    @Excel(name = "货主名称", width = 20)
    private String consignor;

    @Excel(name = "所属公司", width = 30)
    private String enterpriseName;

    @Excel(name = "所在地区", width = 20)
    private String area;

    @Excel(name = "货主类型", width = 20)
    private String shipperNatureDesc;

    @Excel(name = "联系人名称", width = 20)
    private String contactName;

    @Excel(name = "联系人电话", width = 20)
    private String contactPhone;

    @Excel(name = "校验状态", width = 20 ,replace = {"已校验_true", "待校验_false"})
    private Boolean verifyFlag;

    @Excel(name = "标签", width = 20)
    private String tagType;

    @Excel(name = "等级", width = 20)
    private String grade;

    @Excel(name = "业务关系", width = 20)
    private String shipperTypeDesc;

    @Excel(name = "业务负责人", width = 20)
    private String managerPrincipal;

    @Excel(name = "客服负责人", width = 20)
    private String customerServicePrincipal;

    @Excel(name = "最近跟进记录", width = 20)
    private String lastTrackContent;

    @Excel(name = "最近跟进时间", format = "yyyy-MM-dd HH:mm:ss", width = 20)
    private LocalDateTime lastTrackTime;

    @Excel(name = "审核状态", width = 20)
    private String auditStatus;

    @Excel(name = "创建人", width = 20)
    private String createUserName;

    @Excel(name = "创建时间", format = "yyyy-MM-dd HH:mm:ss", width = 20)
    private LocalDateTime createTime;
}
