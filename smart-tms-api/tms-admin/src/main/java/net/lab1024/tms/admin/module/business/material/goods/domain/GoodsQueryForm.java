package net.lab1024.tms.admin.module.business.material.goods.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import org.hibernate.validator.constraints.Length;

/**
 * 业务资料-货物管理-查询参数
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Data
public class GoodsQueryForm extends PageParam {

    @ApiModelProperty("关键字")
    @Length(max = 200, message = "关键字最多200字符")
    private String keywords;

    @ApiModelProperty(value = "删除状态", hidden = true)
    private Boolean deletedFlag;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;

}
