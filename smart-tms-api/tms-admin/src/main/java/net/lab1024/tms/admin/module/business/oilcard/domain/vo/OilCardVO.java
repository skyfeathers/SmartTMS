package net.lab1024.tms.admin.module.business.oilcard.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.oa.enterprise.domain.vo.EnterpriseVO;
import net.lab1024.tms.admin.module.business.oilcard.domain.dto.OilCardBaseDTO;

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
public class OilCardVO extends OilCardBaseDTO {

    @ApiModelProperty("油卡ID")
    private Long oilCardId;

    @ApiModelProperty("主卡卡号")
    private String masterOilCardNo;

    @ApiModelProperty("当前余额")
    private BigDecimal balance;

    @ApiModelProperty("待分配金额合计")
    private BigDecimal preDistributionBalance;

    @ApiModelProperty("持卡时间")
    private LocalDateTime useTime;

    @ApiModelProperty("企业ID")
    private Long enterpriseId;

    @ApiModelProperty("企业名称")
    private String enterpriseName;

    @ApiModelProperty("领取人名称")
    private String receiveUserName;

    @ApiModelProperty("领取人手机")
    private String receiveUserPhone;

    @ApiModelProperty("持卡司机姓名")
    private String useDriverName;

    @ApiModelProperty("持卡司机手机号")
    private String useDriverPhone;

    @ApiModelProperty("持卡车牌号")
    private String useVehicleNumber;

    @ApiModelProperty("创建人ID")
    private Long createUserId;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("充值金额")
    private BigDecimal rechargeBalance;

    @ApiModelProperty("副卡分配金额")
    private BigDecimal distributeBalance;
}
