package net.lab1024.tms.common.module.support.reload;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.controller.SupportBaseController;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.constant.SwaggerTagConst;
import net.lab1024.tms.common.module.support.reload.domain.ReloadForm;
import net.lab1024.tms.common.module.support.reload.domain.ReloadItemVO;
import net.lab1024.tms.common.module.support.reload.domain.ReloadResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/***
 *
 * @author 开云
 */
@RestController
@Api(tags = {SwaggerTagConst.Support.SUPPORT_RELOAD})
public class ReloadController extends SupportBaseController {

    @Autowired
    private ReloadService reloadService;

    @ApiOperation(value = "查询reload列表 by 开云")
    @GetMapping("/reload/query")
    public ResponseDTO<List<ReloadItemVO>> query() {
        return reloadService.query();
    }

    @ApiOperation(value = "获取reload result by 开云")
    @GetMapping("/reload/result/{tag}")
    public ResponseDTO<List<ReloadResultVO>> queryReloadResult(@PathVariable("tag") String tag) {
        return reloadService.queryReloadItemResult(tag);
    }

    @ApiOperation(value = "通过tag更新标识 by 开云")
    @PostMapping("/reload/update")
    public ResponseDTO<String> updateByTag(@RequestBody @Valid ReloadForm reloadForm) {
        return reloadService.updateByTag(reloadForm);
    }
}
