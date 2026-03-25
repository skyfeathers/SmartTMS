package net.lab1024.tms.admin.module.business.material.goods.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 业务资料-站点管理
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Data
public class GoodsVO {

    @ApiModelProperty("货物ID")
    private Long goodsId;

    @ApiModelProperty("货物名称")
    private String goodsName;

    @ApiModelProperty("货物类型")
    private String goodsType;

    @ApiModelProperty("货物类型名称")
    private String goodsTypeName;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("创建人ID")
    private Long createUserId;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
