package net.lab1024.tms.common.module.support.baidumap.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 地址转换
 *
 * @author zhaoxinyang
 * @date 2024/10/31 14:59
 */
@Data
public class BaiduAddressSuggestionForm {

    @ApiModelProperty("地址")
    private String address;

}
