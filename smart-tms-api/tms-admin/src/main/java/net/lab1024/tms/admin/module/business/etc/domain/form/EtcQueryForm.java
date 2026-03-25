package net.lab1024.tms.admin.module.business.etc.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

import java.time.LocalDate;

/**
 * etc查询form
 *
 * @author lidoudou
 * @date 2022/6/29 下午5:44
 */
@Data
public class EtcQueryForm extends PageParam {

    @ApiModelProperty("etc卡号/创建人")
    private String keyWords;

    @ApiModelProperty("禁用状态")
    private Boolean disabledFlag;

    @ApiModelProperty(value = "创建时间-开始时间", example = "2022-01-01")
    private LocalDate createStartTime;

    @ApiModelProperty(value = "创建时间-结束时间", example = "2022-01-01")
    private LocalDate createEndTime;

    @ApiModelProperty(value = "删除状态", hidden = true)
    private Boolean deletedFlag;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;
}
