package net.lab1024.tms.driver.module.system.login.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;

@Data
public class DriverLoginVO {

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("司机姓名")
    private String driverName;

    @ApiModelProperty("司机手机号")
    private String telephone;

    @ApiModelProperty("司机照片")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String photo;

}
