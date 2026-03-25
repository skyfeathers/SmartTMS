package net.lab1024.tms.admin.module.business.quick.domain.form;

import lombok.Data;
import net.lab1024.tms.admin.module.business.quick.QuickSearchTypeEnum;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;

/**
 * @author yandy
 * @description:
 * @date 2024/10/25 10:09 上午
 */
@Data
public class QuickSearchForm {

    @ApiModelPropertyEnum(value = QuickSearchTypeEnum.class, desc = "查询类型")
    private Integer searchType;

    private String keywords;

}