package net.lab1024.tms.admin.module.business.etc.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.etc.domain.dto.EtcBaseDTO;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * etc详情
 *
 * @author lidoudou
 * @date 2022/6/29 下午5:51
 */
@Data
public class EtcVO extends EtcBaseDTO {

    @ApiModelProperty("etc ID")
    private Long etcId;

    @ApiModelProperty("当前余额")
    private BigDecimal balance;

    @ApiModelProperty("持卡时间")
    private LocalDateTime useTime;

    @ApiModelProperty("所属公司名称")
    @DataTracerFieldDoc("所属公司名称")
    private String enterpriseName;

    @ApiModelProperty("使用司机")
    @DataTracerFieldDoc("使用司机")
    private String userName;

    @ApiModelProperty("使用司机联系方式")
    @DataTracerFieldDoc("使用司机联系方式")
    private String userPhone;

    @ApiModelProperty("持卡车牌号")
    @DataTracerFieldDoc("持卡车")
    private String useVehicleNumber;

    @ApiModelProperty("创建人ID")
    private Long createUserId;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;


}
