package net.lab1024.tms.admin.module.business.shipper.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 新增货主的开票信息
 *
 * @author lidoudou
 * @date 2022/8/16 下午8:48
 */
@Data
public class ShipperInvoiceUpdateForm extends ShipperInvoiceAddForm {

    @NotNull(message = "开票信息ID不能为空")
    private Long invoiceId;

}
