package net.lab1024.tms.admin.module.business.driver.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;

/***
 * 银行卡信息
 *
 * @author lidoudou
 * @date 2022/6/22 下午2:36
 */
@Data
public class DriverBankVO {

    @ApiModelProperty("银行ID")
    private Long bankId;

    @ApiModelProperty("司机ID")
    private Long driverId;

    @ApiModelProperty("品牌-数据字典(BANK-TYPE)")
    private String bankType;

    @ApiModelProperty("银行名称")
    private String bankName;

    @ApiModelProperty("支行名称")
    private String bankBranchName;

    @ApiModelProperty("开户名")
    private String accountName;

    @ApiModelProperty("银行账号")
    private String bankAccount;

    @ApiModelProperty("附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String bankCardFrontAttachment;

    @ApiModelProperty("是否默认")
    private Boolean defaultFlag;

    private Long createUserId;

    private String createUserName;

    private Integer createUserType;

    private String createUserTypeDesc;
}
