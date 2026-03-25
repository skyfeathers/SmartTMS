package net.lab1024.tms.common.module.support.dingding.config.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 当前企业的钉钉配置
 *
 * @author lidoudou
 * @date 2023/4/28 下午3:53
 */
@Data
public class DingDingConfigVO {

    @ApiModelProperty("钉钉企业ID")
    private String corpId;

    @ApiModelProperty("appKey")
    private String appKey;

    @ApiModelProperty("appSecret")
    private String appSecret;

    @ApiModelProperty("加密 aes_key")
    private String aesKey;

    @ApiModelProperty("签名 token")
    private String aesToken;

    @ApiModelProperty("同步部门")
    private Long departmentId;

}
