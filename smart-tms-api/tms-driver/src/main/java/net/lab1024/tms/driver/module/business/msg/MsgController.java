package net.lab1024.tms.driver.module.business.msg;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.business.msg.MsgCommonService;
import net.lab1024.tms.common.module.business.msg.constant.MsgReceiverTypeEnum;
import net.lab1024.tms.common.module.business.msg.domain.MsgQueryForm;
import net.lab1024.tms.common.module.business.msg.domain.MsgVO;
import net.lab1024.tms.driver.constant.DriverSwaggerTagConst;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(tags = DriverSwaggerTagConst.Business.MSG)
@RestController
public class MsgController {

    @Resource
    private MsgCommonService msgCommonService;

    @ApiOperation("通知消息-分页查询 @listen")
    @PostMapping("/msg/query")
    public ResponseDTO<PageResult<MsgVO>> add(@RequestBody @Valid MsgQueryForm queryForm) {
        RequestUser user = SmartRequestUtil.getRequestUser();
        queryForm.setReceiverType(MsgReceiverTypeEnum.DRIVER.getValue());
        queryForm.setReceiverId(user.getUserId());
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return msgCommonService.query(queryForm);
    }

    @ApiOperation("通知消息-查询未读消息数 @yandy")
    @GetMapping("/msg/query/unread/count")
    public ResponseDTO<Integer> unreadCount() {
        RequestUser user = SmartRequestUtil.getRequestUser();
        MsgQueryForm queryForm = new MsgQueryForm();
        queryForm.setReceiverType(MsgReceiverTypeEnum.DRIVER.getValue());
        queryForm.setReadFlag(false);
        queryForm.setReceiverId(user.getUserId());
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return msgCommonService.queryCount(queryForm);
    }

    @ApiOperation("通知消息-更新已读 @listen")
    @GetMapping("/msg/update/read/{msgId}")
    public ResponseDTO<String> updateReadFlag(@PathVariable Long msgId) {
        msgCommonService.updateReadFlag(msgId, MsgReceiverTypeEnum.DRIVER.getValue(), SmartRequestUtil.getRequestUserId());
        return ResponseDTO.ok();
    }

    @ApiOperation("通知消息-以及消息类型更新已读 @listen")
    @GetMapping("/msg/update/read/msgType/{msgType}")
    public ResponseDTO<String> updateReadFlagByMsgType(@PathVariable Integer msgType) {
        msgCommonService.updateReadFlagByMsgType(msgType, MsgReceiverTypeEnum.DRIVER.getValue(), SmartRequestUtil.getRequestUserId());
        return ResponseDTO.ok();
    }

    @ApiOperation("通知消息-全部已读 @listen")
    @GetMapping("/msg/update/all/read")
    public ResponseDTO<String> updateAllReadFlag() {
        msgCommonService.updateAllReadFlag(MsgReceiverTypeEnum.DRIVER.getValue(), SmartRequestUtil.getRequestUserId());
        return ResponseDTO.ok();
    }

}
