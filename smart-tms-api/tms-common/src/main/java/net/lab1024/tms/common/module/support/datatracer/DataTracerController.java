package net.lab1024.tms.common.module.support.datatracer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.controller.SupportBaseController;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.constant.SwaggerTagConst;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerDetailVO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerQueryForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerVO;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * [  ]
 *
 * @author 罗伊
 */

@Api(tags = {SwaggerTagConst.Support.SUPPORT_DATA_TRACER})
@RestController
public class DataTracerController extends SupportBaseController {

    @Autowired
    private DataTracerService dataTracerService;

    @ApiOperation("分页查询业务操作日志 - @author 胡克")
    @PostMapping("/data/tracer/log/query")
    public ResponseDTO<PageResult<DataTracerVO>> query(@Valid @RequestBody DataTracerQueryForm queryForm) {
        return dataTracerService.query(queryForm);
    }

    @ApiOperation("获取extData-原数据 - @author 胡克")
    @GetMapping("/data/tracer/extData/{dataTracerId}")
    public ResponseDTO<DataTracerDetailVO> extData(@PathVariable Long dataTracerId) {
        return dataTracerService.extData(dataTracerId);
    }

}
