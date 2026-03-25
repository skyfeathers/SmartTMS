package net.lab1024.tms.common.module.support.operatelog;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.controller.SupportBaseController;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.constant.SwaggerTagConst;
import net.lab1024.tms.common.module.support.operatelog.domain.OperateLogQueryForm;
import net.lab1024.tms.common.module.support.operatelog.domain.OperateLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * [  ]
 *
 * @author 罗伊
 */
@RestController
@Api(tags = {SwaggerTagConst.Support.SUPPORT_OPERATE_LOG})
public class OperateLogController extends SupportBaseController {

    @Autowired
    private OperateLogService operateLogService;

    @ApiOperation(value = "分页查询 @author 罗伊")
    @PostMapping("/operateLog/page/query")
    public ResponseDTO<PageResult<OperateLogVO>> queryByPage(@RequestBody OperateLogQueryForm queryForm) {
        return operateLogService.queryByPage(queryForm);
    }

    @ApiOperation(value = "详情 @author 罗伊")
    @GetMapping("/operateLog/detail/{operateLogId}")
    public ResponseDTO<OperateLogVO> detail(@PathVariable Long operateLogId) {
        return operateLogService.detail(operateLogId);
    }

}
