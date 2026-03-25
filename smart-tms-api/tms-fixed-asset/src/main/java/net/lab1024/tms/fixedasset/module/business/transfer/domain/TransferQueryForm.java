package net.lab1024.tms.fixedasset.module.business.transfer.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

import java.time.LocalDate;

/**
 * 分页查询转移
 *
 * @author lidoudou
 * @date 2023/3/20 下午5:43
 */
@Data
public class TransferQueryForm extends PageParam {

    @ApiModelProperty(value = "使用时间 - 开始时间")
    private LocalDate startTime;

    @ApiModelProperty(value = "使用时间 - 结束时间")
    private LocalDate endTime;

    @ApiModelProperty(value = "关键字")
    private String keywords;

    @ApiModelProperty(value = "所属公司", hidden = true)
    private Long enterpriseId;

    @ApiModelProperty(value = "转出位置")
    private Long fromLocationId;

    @ApiModelProperty(value = "转入位置")
    private Long toLocationId;

}