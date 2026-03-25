package net.lab1024.tms.fixedasset.module.business.asset.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

import java.time.LocalDate;
import java.util.List;

/**
 * 固定资产 分页查询表单
 *
 * @Author lidoudou
 * @Date 2023-03-15 14:15:14
 * @Copyright 1024创新实验室
 */

@Data
public class AssetQueryForm extends PageParam {

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "状态")
    private List<Integer> statusList;

    @ApiModelProperty(value = "创建时间 - 开始时间")
    private LocalDate startTime;

    @ApiModelProperty(value = "创建时间 - 结束时间")
    private LocalDate endTime;

    @ApiModelProperty(value = "关键字")
    private String keywords;

    @ApiModelProperty(value = "所属分类")
    private Long categoryId;

    @ApiModelProperty(value = "存放位置")
    private Long locationId;

    @ApiModelProperty(value = "使用部门")
    private Long departmentId;

    @ApiModelProperty(value = "所属企业",hidden = true)
    private Long enterpriseId;

    @ApiModelProperty(hidden = true)
    private Boolean deletedFlag;

}