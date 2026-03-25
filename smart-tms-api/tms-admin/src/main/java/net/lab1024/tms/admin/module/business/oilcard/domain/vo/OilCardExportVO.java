package net.lab1024.tms.admin.module.business.oilcard.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.oa.enterprise.domain.vo.EnterpriseVO;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardFuelTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 油卡详情
 *
 * @author lidoudou
 * @date 2022/6/29 下午5:51
 */
@Data
public class OilCardExportVO {

    @Excel(name = "油卡卡号", width = 20)
    private String oilCardNo;

    @Excel(name = "所属公司", width = 20)
    private String enterpriseName;

    @ApiModelProperty("品牌-数据字典(OIL-CARD-BRAND)")
    @Excel(name = "油卡品牌")
    private String brand;

    @ApiModelPropertyEnum(value = OilCardTypeEnum.class, desc = "油卡类型")
    private Integer type;

    @ApiModelPropertyEnum(value = OilCardFuelTypeEnum.class, desc = "燃料类型")
    private Integer fuelType;

    @Excel(name = "油卡类型")
    private String typeDesc;

    @Excel(name = "油卡燃料类型")
    private String fuelTypeDesc;

    @Excel(name = "持卡时间", format = "yyyy-MM-dd", width = 20)
    private LocalDateTime useTime;

    @ApiModelProperty("领取人")
    private Long receiveUserId;

    @Excel(name = "领取人名称", width = 20)
    private String receiveUserName;

    @ApiModelProperty("领取人手机")
    private String receiveUserPhone;

    @ApiModelProperty("持卡司机")
    private Long useDriverId;

    @Excel(name = "持卡司机姓名", width = 20)
    private String useDriverName;

    @ApiModelProperty("持卡司机手机号")
    private String useDriverPhone;

    @ApiModelProperty("持卡车")
    private Long useVehicleId;

    @Excel(name = "持卡车", width = 20)
    private String useVehicleNumber;

    @Excel(name = "期初余额", width = 20)
    private BigDecimal beginBalance;

    @Excel(name = "当前余额")
    private BigDecimal balance;

    @Excel(name = "待分配金额合计", width = 20)
    private BigDecimal preDistributionBalance;

    @ApiModelProperty("用途-数据字典(OIL-CARD-PURPOSE)")
    @Excel(name = "用途")
    private String purpose;

    @Excel(name = "计划充值金额", width = 20)
    private BigDecimal preRechargeAmount;
    @Excel(name = "油卡状态")
    private String disabledFlagDesc;

    @Excel(name = "创建人", width = 20)
    private String createUserName;

    @Excel(name = "创建时间", format = "yyyy-MM-dd HH:mm:ss", width = 30)
    private LocalDateTime createTime;

    @ApiModelProperty("禁用状态")
    private Boolean disabledFlag;

    @ApiModelProperty("油卡ID")
    private Long oilCardId;

    @ApiModelProperty("所属公司名称")
    private List<EnterpriseVO> enterpriseList;


}
