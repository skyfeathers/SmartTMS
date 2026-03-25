package net.lab1024.tms.admin.module.business.receive.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 提交订单对账
 *
 * @author lidoudou
 * @date 2022/7/20 下午5:41
 */
@Data
public class ReceiveOrderSubmitForm {

    @NotEmpty(message = "运单ID不能为空")
    @ApiModelProperty("运单ID")
    private List<Long> waybillIdList;

    @ApiModelProperty("业务日期")
    @NotNull(message = "业务日期不能为空")
    private LocalDate businessDate;

    @ApiModelProperty("附件")
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String attachment;

    @ApiModelProperty("备注")
    private String remark;



    @ApiModelProperty(hidden = true)
    private Long createUserId;

    @ApiModelProperty(hidden = true)
    private String createUserName;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;

    private Boolean makeInvoiceFlag;
}
