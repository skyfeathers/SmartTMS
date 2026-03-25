package net.lab1024.tms.fixedasset.module.business.scrap.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.fixedasset.module.business.asset.domain.vo.AssetVO;

/**
 * @Author 1024创新实验室-主任:卓大
 * @Date 2023/3/27 11:29:45
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2023
 */

@Data
public class ScrapAssetVO extends AssetVO {

    @ApiModelProperty(value = "报废id")
    private Long scrapId;

}
