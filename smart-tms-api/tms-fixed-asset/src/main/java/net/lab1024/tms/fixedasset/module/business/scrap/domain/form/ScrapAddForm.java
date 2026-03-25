package net.lab1024.tms.fixedasset.module.business.scrap.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 固定资产-报废 新建表单
 *
 * @Author 卓大
 * @Date 2023-03-23 15:01:51
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */

@Data
public class ScrapAddForm {

    @ApiModelProperty(value = "所属公司",hidden = true)
    private Long enterpriseId;

    @ApiModelProperty(value = "说明")
    @NotEmpty(message = "说明不能为空")
    private String scrapExplain;

    @ApiModelProperty(value = "报废时间")
    @NotNull(message = "报废时间不能为空")
    private LocalDateTime scrapTime;


    @ApiModelProperty(value = "资产id集合")
    @NotEmpty(message = "资产不能为空")
    private List<Long> assetIdList;

}