package net.lab1024.tms.fixedasset.module.business.location.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.DictValueVoDeserializer;
import net.lab1024.tms.common.common.serializer.DictValueVoSerializer;

import java.time.LocalDateTime;

/**
 * 位置详情
 *
 * @author lidoudou
 * @date 2023/3/14 下午5:18
 */
@Data
public class LocationVO {

    @ApiModelProperty("位置ID")
    private Long locationId;

    @ApiModelProperty("位置名称")
    private String locationName;

    @ApiModelProperty("所属ID")
    private Long enterpriseId;

    @ApiModelProperty("所属公司名称")
    private String enterpriseName;

    @ApiModelProperty("存放类型-数据字典（LOCATION-TYPE)")
    @JsonSerialize(using = DictValueVoSerializer.class)
    @JsonDeserialize(using = DictValueVoDeserializer.class)
    private String type;

    @ApiModelProperty("备注")
    private String remark;

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
}
