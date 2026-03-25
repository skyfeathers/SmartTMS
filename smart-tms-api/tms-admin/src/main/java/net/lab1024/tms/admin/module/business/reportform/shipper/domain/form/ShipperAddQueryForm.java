package net.lab1024.tms.admin.module.business.reportform.shipper.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2024/5/14 2:56 下午
 */
@Data
public class ShipperAddQueryForm {

    private List<Long> managerIdList;

    @ApiModelProperty(hidden = true)
    private Integer managerType;

    @NotNull(message = "所属年份不能为空")
    private Integer year;

    private Long enterpriseId;
}