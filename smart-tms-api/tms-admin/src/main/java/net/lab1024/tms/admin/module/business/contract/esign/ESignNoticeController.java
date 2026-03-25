package net.lab1024.tms.admin.module.business.contract.esign;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.annoation.NoNeedLogin;
import net.lab1024.tms.common.common.code.SystemErrorCode;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.constant.SwaggerTagConst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * e签宝回调
 *
 * @author lihaifan
 * @date 2022/7/18 20:43
 */
@Slf4j
@RestController
@Api(tags = {SwaggerTagConst.Admin.E_SIGN})
public class ESignNoticeController {

    @Autowired
    private ESignSafeVerify eSignSafeVerify;
    @Autowired
    private ESignNoticeService eSignNoticeService;

    @NoNeedLogin
    @ApiOperation(value = "e签宝回调 @author lihaifan")
    @PostMapping("/esign/notice")
    public ResponseDTO<String> notice(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = eSignSafeVerify.getRequestBody(request, "UTF-8");
        log.info("e签宝回调requestBody：" + requestBody);
        if (StringUtils.isBlank(requestBody)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "请求参数为空");
        }
        // 验证回调安全性
        Boolean checkPass = eSignSafeVerify.checkPass(request, requestBody);
        if (!checkPass) {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().append("Served at: ").append(request.getContextPath())
                    .append("\n").append("是否通过验证：----》").append(checkPass.toString());
            log.error("e签宝回调签名验证失败");
            return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR, "签名验证失败");
        }
        return eSignNoticeService.notice(requestBody);
    }
}
