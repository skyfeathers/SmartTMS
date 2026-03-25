package net.lab1024.tms.admin.module.business.vehicle.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.DictValueVoDeserializer;
import net.lab1024.tms.common.common.serializer.DictValueVoSerializer;

/**
 * 车辆下拉框
 *
 * @author lidoudou
 * @date 2022/11/2 上午09:58
 */
@Data
public class VehicleSimpleVO  {

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("速记码")
    private String shorthand;

    @ApiModelProperty("车牌号")
    private String vehicleNumber;

    @ApiModelProperty("车辆类型代码")
    @JsonSerialize(using = DictValueVoSerializer.class)
    @JsonDeserialize(using = DictValueVoDeserializer.class)
    private String vehicleType;
}
