package net.lab1024.tms.fixedasset.module.business.location.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import org.hibernate.validator.constraints.Length;

/**
 * 存放位置-查询参数
 *
 * @author lidoudou
 * @date 2023/3/14 下午5:15
 */
@Data
public class LocationQueryForm extends PageParam {

    @ApiModelProperty("关键字")
    @Length(max = 200, message = "关键字最多200字符")
    private String keywords;

    @ApiModelProperty("禁用状态")
    private Boolean disabledFlag;

    @ApiModelProperty("存放类型-数据字典（LOCATION-TYPE）")
    private String type;

    @ApiModelProperty(value = "删除状态", hidden = true)
    private Boolean deletedFlag;

    @ApiModelProperty(value = "所属公司",hidden = true)
    private Long enterpriseId;

}
