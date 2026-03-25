package net.lab1024.tms.common.module.support.dingding;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.annoation.NoNeedLogin;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.constant.SwaggerTagConst;
import net.lab1024.tms.common.module.support.dingding.callback.service.DingDingCallBackService;
import net.lab1024.tms.common.module.support.dingding.config.domain.DingDingConfigForm;
import net.lab1024.tms.common.module.support.dingding.config.domain.DingDingConfigVO;
import net.lab1024.tms.common.module.support.dingding.config.DingDingConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 钉钉消息订阅
 *
 * @author lidoudou
 * @date 2023/4/20 下午3:08
 */
@RestController
@Api(tags = SwaggerTagConst.Support.DING_DING)
public class DingDingController {

    @Autowired
    private DingDingCallBackService dingDingCallBackService;
    @Autowired
    private DingDingConfigService dingDingConfigService;

    @ApiOperation(value = "钉钉事件订阅 @author lidoudou")
    @PostMapping("/dingtalk/callback/{enterpriseId}")
    @NoNeedLogin
    public Map<String, String> callBack(
            @PathVariable("enterpriseId") Long enterpriseId,
            @RequestParam(value = "msg_signature", required = false) String msgSignature,
            @RequestParam(value = "timestamp", required = false) String timeStamp,
            @RequestParam(value = "nonce", required = false) String nonce,
            @RequestBody(required = false) JSONObject json) {
        return dingDingCallBackService.callback(enterpriseId, msgSignature, timeStamp, nonce, json);
    }

    @ApiOperation("修改钉钉配置 by lidoudou")
    @PostMapping("/dingtalk/config/save")
    public ResponseDTO<String> save(@RequestBody @Valid DingDingConfigForm configForm) {
        return dingDingConfigService.saveConfig(configForm);
    }

    @ApiOperation("查询企业钉钉配置 by lidoudou")
    @GetMapping("/dingtalk/config/{enterpriseId}")
    public ResponseDTO<DingDingConfigVO> queryByKey(@PathVariable("enterpriseId") Long enterpriseId) {
        return dingDingConfigService.getByEnterpriseId(enterpriseId);
    }

}
