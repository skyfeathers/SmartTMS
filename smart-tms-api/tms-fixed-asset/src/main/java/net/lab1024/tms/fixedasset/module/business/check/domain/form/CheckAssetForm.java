package net.lab1024.tms.fixedasset.module.business.check.domain.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 盘点对象
 *
 * @author lidoudou
 * @date 2023/3/24 下午4:43
 */
@Data
public class CheckAssetForm {

    @NotNull(message = "盘点对象不能为空")
    private Long itemId;

    @NotNull(message = "盘点状态不能为空")
    private Integer status;

    private String remark;
}
