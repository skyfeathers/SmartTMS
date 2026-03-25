package net.lab1024.tms.fixedasset.module.business.scrap.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 固定资产-报废 列表VO
 *
 * @Author 卓大
 * @Date 2023-03-23 15:01:51
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */

@Data
public class ScrapVO {

    private Long scrapId;

    @ApiModelProperty(value = "报废单号")
    private String scrapCode;

    @ApiModelProperty("所属公司")
    private Long enterpriseId;

    @ApiModelProperty("所属公司")
    private String enterpriseName;

    @ApiModelProperty(value = "单据状态")
    private Integer status;

    @ApiModelProperty(value = "报废日期")
    private LocalDateTime scrapTime;

    @ApiModelProperty(value = "说明")
    private String scrapExplain;

    @ApiModelProperty(value = "申请人员")
    private Long applyUserId;

    @ApiModelProperty(value = "申请人员名称")
    private String applyUserName;

    @ApiModelProperty(value = "部门Id")
    private Long departmentId;

    @ApiModelProperty(value = "部门名称")
    private String departmentName;

    @ApiModelProperty(value = "维修花费")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "资产集合")
    private List<ScrapAssetVO> assetList;


}