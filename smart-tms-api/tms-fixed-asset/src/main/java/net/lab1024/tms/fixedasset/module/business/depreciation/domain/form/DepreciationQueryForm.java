package net.lab1024.tms.fixedasset.module.business.depreciation.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

import java.time.LocalDate;

/**
 * 资产折旧查询字段
 *
 * @author lidoudou
 * @date 2023/4/10 下午4:25
 */
@Data
public class DepreciationQueryForm extends PageParam {

    @ApiModelProperty(value = "关键字")
    private String keywords;

    @ApiModelProperty("开始时间")
    private LocalDate startTime;

    @ApiModelProperty("结束")
    private LocalDate endTime;

    @ApiModelProperty(hidden = true)
    private Boolean deletedFlag;

    @ApiModelProperty(value = "所属公司",hidden = true)
    private Long enterpriseId;

}
