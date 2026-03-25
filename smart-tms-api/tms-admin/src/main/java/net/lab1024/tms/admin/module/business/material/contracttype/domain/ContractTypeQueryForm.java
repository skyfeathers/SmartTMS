package net.lab1024.tms.admin.module.business.material.contracttype.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import org.hibernate.validator.constraints.Length;

/**
 * @author yandy
 * @description:
 * @date 2022/8/12 10:12 上午
 */
@Data
public class ContractTypeQueryForm extends PageParam {


    @ApiModelProperty("关键字")
    @Length(max = 200, message = "关键字最多200字符")
    private String keywords;

    @ApiModelProperty(value = "删除状态", hidden = true)
    private Boolean deletedFlag;
}