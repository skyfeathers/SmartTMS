package net.lab1024.tms.admin.module.business.driver.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

/***
 * 添加司机dto
 *
 * @author lidoudou
 * @date 2022/6/22 下午2:18
 */
@Data
public class DriverAddForm extends DriverBaseForm {

    @ApiModelProperty("银行卡信息")
    @Size(max = 50, message = "银行卡信息最多50个")
    private List<DriverBankBaseForm> bankList;

    @ApiModelProperty("车辆ID列表")
    private List<Long> vehicleIdList;

}
