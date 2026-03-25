package net.lab1024.tms.admin.module.system.login.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.serializer.FileKeySerializer;

/**
 * @author yandy
 * @description:
 * @date 2025/2/5 3:46 下午
 */
@Data
public class LoginEnterpriseVO {

    @ApiModelProperty("企业名称")
    private String companyName;

    @ApiModelProperty("企业logo")
    @JsonSerialize(using = FileKeySerializer.class)
    private String faviconUrl;

    @ApiModelProperty("企业logo")
    @JsonSerialize(using = FileKeySerializer.class)
    private String logoUrl;

    @ApiModelProperty(value = "企业简称")
    private String websiteName;

    @ApiModelProperty(value = "企业介绍")
    private String websiteDesc;

    @ApiModelProperty(value = "备案号")
    private String beiAnNo;



}