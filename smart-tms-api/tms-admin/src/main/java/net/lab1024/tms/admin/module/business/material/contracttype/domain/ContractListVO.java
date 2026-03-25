package net.lab1024.tms.admin.module.business.material.contracttype.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhuoda
 * @Date 2022-07-18
 */
@Data
public class ContractListVO {

    private Long contractTypeId;

    @ApiModelProperty("名称")
    private String name;
}
