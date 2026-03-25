package net.lab1024.tms.admin.module.business.receive.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 应收帐款的邮寄信息
 *
 * @author lidoudou
 * @date 2022/7/20 下午5:07
 */
@Data
public class ReceiveOrderMailAddressVO {

    @ApiModelProperty("收件人姓名")
    private String receivePerson;

    @ApiModelProperty("收件人手机号")
    private String receivePersonPhone;

    @ApiModelProperty("省")
    private String receiveProvinceName;

    private Integer receiveCityCode;

    @ApiModelProperty("市")
    private String receiveCityName;

    @ApiModelProperty("区")
    private String receiveDistrictName;

    @ApiModelProperty("详细地址")
    private String receiveAddress;

    @ApiModelProperty("邮箱")
    private String email;
}
