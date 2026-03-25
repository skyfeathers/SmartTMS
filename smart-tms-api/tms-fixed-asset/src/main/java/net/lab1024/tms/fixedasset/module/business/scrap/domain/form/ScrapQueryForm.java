package net.lab1024.tms.fixedasset.module.business.scrap.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

import java.time.LocalDate;
import java.util.List;

/**
 * 固定资产-报废 分页查询表单
 *
 * @Author 卓大
 * @Date 2023-03-23 15:01:51
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */

@Data
public class ScrapQueryForm extends PageParam {

    @ApiModelProperty(value = "关键字")
    private String keywords;

    @ApiModelProperty(value = "报废日期")
    private LocalDate scrapTimeBegin;

    @ApiModelProperty(value = "报废日期")
    private LocalDate scrapTimeEnd;

    @ApiModelProperty("部门id集合")
    private List<Long> departmentIdList;

    @ApiModelProperty(value = "所属公司",hidden = true)
    private Long enterpriseId;
}