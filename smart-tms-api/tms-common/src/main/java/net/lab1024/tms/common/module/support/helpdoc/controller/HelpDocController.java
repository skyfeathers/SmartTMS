package net.lab1024.tms.common.module.support.helpdoc.controller;

import cn.hutool.extra.servlet.ServletUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.controller.SupportBaseController;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.constant.SwaggerTagConst;
import net.lab1024.tms.common.module.support.helpdoc.domain.form.*;
import net.lab1024.tms.common.module.support.helpdoc.domain.vo.HelpDocCatalogVO;
import net.lab1024.tms.common.module.support.helpdoc.domain.vo.HelpDocDetailVO;
import net.lab1024.tms.common.module.support.helpdoc.domain.vo.HelpDocVO;
import net.lab1024.tms.common.module.support.helpdoc.domain.vo.HelpDocViewRecordVO;
import net.lab1024.tms.common.module.support.helpdoc.service.HelpDocCatalogService;
import net.lab1024.tms.common.module.support.helpdoc.service.HelpDocService;
import net.lab1024.tms.common.module.support.helpdoc.service.HelpDocUserService;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 帮助文档
 *
 * @author zhuoda
 * @date 2022/08/12 21:40
 */
@Api(tags = SwaggerTagConst.Support.SUPPORT_HELP_DOC)
@RestController
public class HelpDocController extends SupportBaseController {

    @Autowired
    private HelpDocService helpDocService;

    @Autowired
    private HelpDocCatalogService helpDocCatalogService;

    @Autowired
    private HelpDocUserService helpDocUserService;

    // --------------------- 帮助文档 【目录】 -------------------------

    @ApiOperation("帮助文档目录-获取全部 @author zhuoda")
    @GetMapping("/helpDoc/helpDocCatalog/getAll")
    public ResponseDTO<List<HelpDocCatalogVO>> getAll() {
        return ResponseDTO.ok(helpDocCatalogService.getAll());
    }

    @ApiOperation("帮助文档目录-添加 @author zhuoda")
    @PostMapping("/helpDoc/helpDocCatalog/add")
    public ResponseDTO<String> addHelpDocCatalog(@RequestBody @Valid HelpDocCatalogAddForm helpDocCatalogAddForm) {
        return helpDocCatalogService.add(helpDocCatalogAddForm);
    }

    @ApiOperation("帮助文档目录-更新 @author zhuoda")
    @PostMapping("/helpDoc/helpDocCatalog/update")
    public ResponseDTO<String> updateHelpDocCatalog(@RequestBody @Valid HelpDocCatalogUpdateForm helpDocCatalogUpdateForm) {
        return helpDocCatalogService.update(helpDocCatalogUpdateForm);
    }

    @ApiOperation("帮助文档目录-删除 @author zhuoda")
    @GetMapping("/helpDoc/helpDocCatalog/delete/{helpDocCatalogId}")
    public ResponseDTO<String> deleteHelpDocCatalog(@PathVariable Long helpDocCatalogId) {
        return helpDocCatalogService.delete(helpDocCatalogId);
    }

    // --------------------- 帮助文档 【管理:增、删、查、改】-------------------------

    @ApiOperation("【管理】帮助文档-分页查询 @author zhuoda")
    @PostMapping("/helpDoc/query")
    public ResponseDTO<PageResult<HelpDocVO>> query(@RequestBody @Valid HelpDocQueryForm queryForm) {
        return ResponseDTO.ok(helpDocService.query(queryForm));
    }

    @ApiOperation("【管理】帮助文档-获取详情 @author zhuoda")
    @GetMapping("/helpDoc/getDetail/{helpDocId}")
    public ResponseDTO<HelpDocDetailVO> getDetail(@PathVariable Long helpDocId) {
        return ResponseDTO.ok(helpDocService.getDetail(helpDocId));
    }

    @ApiOperation("【管理】帮助文档-添加 @author zhuoda")
    @PostMapping("/helpDoc/add")
    @RepeatSubmit
    public ResponseDTO<String> add(@RequestBody @Valid HelpDocAddForm addForm) {
        return helpDocService.add(addForm);
    }

    @ApiOperation("【管理】帮助文档-更新 @author zhuoda")
    @PostMapping("/helpDoc/update")
    @RepeatSubmit
    public ResponseDTO<String> update(@RequestBody @Valid HelpDocUpdateForm updateForm) {
        return helpDocService.update(updateForm);
    }

    @ApiOperation("【管理】帮助文档-删除 @author zhuoda")
    @GetMapping("/helpDoc/delete/{helpDocId}")
    public ResponseDTO<String> delete(@PathVariable Long helpDocId) {
        return helpDocService.delete(helpDocId);
    }

    @ApiOperation("【管理】帮助文档-根据关联id查询 @author zhuoda")
    @GetMapping("/helpDoc/queryHelpDocByRelationId/{relationId}")
    public ResponseDTO<List<HelpDocVO>> queryHelpDocByRelationId(@PathVariable Long relationId) {
        return ResponseDTO.ok(helpDocService.queryHelpDocByRelationId(relationId));
    }

    //    // ---------------------  帮助文档 【用户】-------------------------
    @ApiOperation("【用户】帮助文档-查看详情 @author zhuoda")
    @GetMapping("/helpDoc/user/view/{helpDocId}")
    @RepeatSubmit
    public ResponseDTO<HelpDocDetailVO> view(@PathVariable Long helpDocId, HttpServletRequest request) {
        return helpDocUserService.view(
                SmartRequestUtil.getRequestUser(),
                helpDocId,
                ServletUtil.getClientIP(request),
                request.getHeader("User-Agent")
        );
    }

    @ApiOperation("【用户】帮助文档-查询全部 @author zhuoda")
    @GetMapping("/helpDoc/user/queryAllHelpDocList")
    @RepeatSubmit
    public ResponseDTO<List<HelpDocVO>> queryAllHelpDocList() {
        return helpDocUserService.queryAllHelpDocList();
    }


    @ApiOperation("【用户】帮助文档-查询 查看记录 @author zhuoda")
    @PostMapping("/helpDoc/user/queryViewRecord")
    @RepeatSubmit
    public ResponseDTO<PageResult<HelpDocViewRecordVO>> queryViewRecord(@RequestBody @Valid HelpDocViewRecordQueryForm helpDocViewRecordQueryForm) {
        return ResponseDTO.ok(helpDocUserService.queryViewRecord(helpDocViewRecordQueryForm));
    }
}