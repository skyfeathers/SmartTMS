package net.lab1024.tms.admin.module.business.oa.news.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.oa.news.constant.NoticeVisibleRangeDataTypeEnum;

import javax.validation.constraints.NotNull;

/**
 * 资讯 可见范围数据
 *
 * @author Turbolisten
 * @date 2022/7/16 11:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeVisibleRangeForm {

    @ApiModelPropertyEnum(NoticeVisibleRangeDataTypeEnum.class)
    @CheckEnum(value = NoticeVisibleRangeDataTypeEnum.class, required = true, message = "数据类型错误")
    private Integer dataType;

    @ApiModelProperty("员工/部门id")
    @NotNull(message = "员工/部门id不能为空")
    private Long dataId;
}
