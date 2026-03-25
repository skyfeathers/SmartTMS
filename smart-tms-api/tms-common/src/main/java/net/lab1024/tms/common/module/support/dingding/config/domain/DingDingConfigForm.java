package net.lab1024.tms.common.module.support.dingding.config.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 钉钉信息配置
 *
 * @author lidoudou
 * @date 2023/4/28 下午3:49
 */
@Data
public class DingDingConfigForm {

    @ApiModelProperty("所属企业")
    private Long enterpriseId;

    @ApiModelProperty("钉钉企业ID")
    @NotBlank(message = "CorpId不能为空")
    private String corpId;

    @ApiModelProperty("appKey")
    @NotBlank(message = "appKey不能为空")
    private String appKey;

    @ApiModelProperty("appSecret")
    @NotBlank(message = "appSecret不能为空")
    private String appSecret;

    @ApiModelProperty("加密 aes_key")
    @NotBlank(message = "加密 aes_key不能为空")
    private String aesKey;

    @ApiModelProperty("签名 token")
    @NotBlank(message = "签名 token不能为空")
    private String aesToken;

    @ApiModelProperty("同步部门")
    @NotNull(message = "同步指定部门不能为空")
    private Long departmentId;

}
