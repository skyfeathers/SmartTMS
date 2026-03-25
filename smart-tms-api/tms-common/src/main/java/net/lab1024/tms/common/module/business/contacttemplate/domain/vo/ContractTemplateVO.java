package net.lab1024.tms.common.module.business.contacttemplate.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/12 15:05
 */
@Data
public class ContractTemplateVO {

    @ApiModelProperty("模板id")
    private Long templateId;

    @ApiModelProperty("模板名称")
    private String templateName;
}
