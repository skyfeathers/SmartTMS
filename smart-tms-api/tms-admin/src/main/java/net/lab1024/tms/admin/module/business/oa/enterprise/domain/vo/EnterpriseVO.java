package net.lab1024.tms.admin.module.business.oa.enterprise.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.oa.enterprise.constant.EnterpriseTypeEnum;

import java.time.LocalDateTime;

/**
 * OA企业模块返回值
 *
 * @author lihaifan
 * @date 2022/6/22 16:50
 */
@Data
public class EnterpriseVO {

    @ApiModelProperty("企业ID")
    private Long enterpriseId;

    @ApiModelProperty("企业名称")
    private String enterpriseName;

    @ApiModelProperty("平台端")
    private Boolean platformFlag;

    @ApiModelProperty("域名列表(逗号分割)")
    private String domainName;

    @ApiModelProperty("企业logo")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String enterpriseLogo;

    @ApiModelProperty("统一社会信用代码")
    private String unifiedSocialCreditCode;

    @ApiModelProperty("道路运输经营许可证")
    private String networkFreightTransportCode;

    @ApiModelPropertyEnum(desc = "类型", value = EnterpriseTypeEnum.class)
    private Integer type;

    @ApiModelProperty("联系人")
    private String contact;

    @ApiModelProperty("联系人电话")
    private String contactPhone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("省份")
    private Integer province;

    @ApiModelProperty("省份名称")
    private String provinceName;

    @ApiModelProperty("城市")
    private Integer city;

    @ApiModelProperty("城市名称")
    private String cityName;

    @ApiModelProperty("区县")
    private Integer district;

    @ApiModelProperty("区县名称")
    private String districtName;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("营业执照")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String businessLicense;

    @ApiModelProperty("禁用状态")
    private Boolean disabledFlag;

    @ApiModelProperty("创建人ID")
    private Long createUserId;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "企业简称")
    private String enterpriseShortName;

    @ApiModelProperty(value = "企业简称")
    private String websiteName;

    @ApiModelProperty(value = "企业介绍")
    private String websiteDesc;

    @ApiModelProperty(value = "备案号")
    private String beiAnNo;

}
