package net.lab1024.tms.common.module.support.serialnumber.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.controller.SupportBaseController;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.constant.SwaggerTagConst;
import net.lab1024.tms.common.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.tms.common.module.support.serialnumber.service.SerialNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * id生成器路由
 *
 * @author zhuoda
 */
@Api(tags = SwaggerTagConst.Support.SUPPORT_ID_GENERATOR)
@RestController
public class SerialNumberController extends SupportBaseController {

    @Autowired
    private SerialNumberService serialNumberService;

    @ApiOperation("生成id @author zhuoda")
    @GetMapping("/serialNumber/{serialNumberId}")
    public ResponseDTO<String> generate(@PathVariable Integer serialNumberId) {
        SerialNumberIdEnum serialNumberIdEnum = SmartBaseEnumUtil.getEnumByValue(serialNumberId, SerialNumberIdEnum.class);
        if (null == serialNumberIdEnum) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "SerialNumberId，不存在" + serialNumberId);
        }
        return ResponseDTO.ok(serialNumberService.generate(serialNumberIdEnum));
    }

}
