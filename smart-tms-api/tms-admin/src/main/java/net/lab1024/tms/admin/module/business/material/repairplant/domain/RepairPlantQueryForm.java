package net.lab1024.tms.admin.module.business.material.repairplant.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

/**
 * 维修厂 分页查询
 *
 * @author: Turbolisten
 * @date: 2022/7/13 21:50
 */
@Data
public class RepairPlantQueryForm extends PageParam {

    @ApiModelProperty("搜索词")
    @Length(max = 50, message = "搜索词最多50字")
    private String searchWord;

    @ApiModelProperty("查询开始时间")
    private LocalDate startDate;

    @ApiModelProperty("查询结束时间")
    private LocalDate endDate;

    @ApiModelProperty("禁用状态")
    private Boolean disabledFlag;

    @ApiModelProperty(hidden = true)
    private Boolean deletedFlag;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;
}
