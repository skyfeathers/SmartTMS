package net.lab1024.tms.admin.module.business.reportform.home.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author yandy
 * @description:
 * @date 2026/3/17 下午1:38
 */
@Data
public class HomeExpireCertVO {

    private Integer value;

    private String name;

    @ApiModelProperty(hidden = true)
    private Integer status;
}