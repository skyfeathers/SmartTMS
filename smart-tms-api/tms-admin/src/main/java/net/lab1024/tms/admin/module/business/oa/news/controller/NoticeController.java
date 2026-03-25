package net.lab1024.tms.admin.module.business.oa.news.controller;

import cn.hutool.extra.servlet.ServletUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.oa.news.domain.form.*;
import net.lab1024.tms.admin.module.business.oa.news.domain.vo.*;
import net.lab1024.tms.admin.module.business.oa.news.service.NoticeEmployeeService;
import net.lab1024.tms.admin.module.business.oa.news.service.NoticeService;
import net.lab1024.tms.admin.module.business.oa.news.service.NoticeTypeService;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 公告、通知、新闻等等
 *
 * @author zhuoda
 * @date 2022/08/12 21:40
 */
@Api(tags = AdminSwaggerTagConst.Business.OA_NOTICE)
@RestController
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeTypeService noticeTypeService;

    @Autowired
    private NoticeEmployeeService noticeEmployeeService;

    // --------------------- 通知公告类型 -------------------------

    @ApiOperation("通知公告类型-获取全部 @author zhuoda")
    @GetMapping("/oa/noticeType/getAll")
    public ResponseDTO<List<NoticeTypeVO>> getAll() {
        return ResponseDTO.ok(noticeTypeService.getAll());
    }

    @ApiOperation("通知公告类型-添加 @author zhuoda")
    @GetMapping("/oa/noticeType/add/{name}")
    public ResponseDTO<String> add(@PathVariable String name) {
        return noticeTypeService.add(name);
    }

    @ApiOperation("通知公告类型-修改 @author zhuoda")
    @GetMapping("/oa/noticeType/update/{noticeTypeId}/{name}")
    public ResponseDTO<String> update(@PathVariable Long noticeTypeId, @PathVariable String name) {
        return noticeTypeService.update(noticeTypeId, name);
    }

    @ApiOperation("通知公告类型-删除 @author zhuoda")
    @GetMapping("/oa/noticeType/delete/{noticeTypeId}")
    public ResponseDTO<String> deleteNoticeType(@PathVariable Long noticeTypeId) {
        return noticeTypeService.delete(noticeTypeId);
    }

    // --------------------- 【管理】通知公告-------------------------

    @ApiOperation("【管理】通知公告-分页查询 @author zhuoda")
    @PostMapping("/oa/notice/query")
    public ResponseDTO<PageResult<NoticeVO>> query(@RequestBody @Valid NoticeQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return ResponseDTO.ok(noticeService.query(queryForm));
    }

    @ApiOperation("【管理】通知公告-添加 @author zhuoda")
    @PostMapping("/oa/notice/add")
    @RepeatSubmit
    public ResponseDTO<String> add(@RequestBody @Valid NoticeAddForm addForm) {
        addForm.setCreateUserId(SmartRequestUtil.getRequestUserId());
        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return noticeService.add(addForm);
    }

    @ApiOperation("【管理】通知公告-更新 @author zhuoda")
    @PostMapping("/oa/notice/update")
    @RepeatSubmit
    public ResponseDTO<String> update(@RequestBody @Valid NoticeUpdateForm updateForm) {
        updateForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return noticeService.update(updateForm);
    }

    @ApiOperation("【管理】通知公告-更新详情 @author zhuoda")
    @GetMapping("/oa/notice/getUpdateVO/{noticeId}")
    public ResponseDTO<NoticeUpdateFormVO> getUpdateFormVO(@PathVariable Long noticeId) {
        return ResponseDTO.ok(noticeService.getUpdateFormVO(noticeId));
    }

    @ApiOperation("【管理】通知公告-删除 @author zhuoda")
    @GetMapping("/oa/notice/delete/{noticeId}")
    public ResponseDTO<String> delete(@PathVariable Long noticeId) {
        return noticeService.delete(noticeId);
    }

    // --------------------- 【员工】查看 通知公告 -------------------------
    @ApiOperation("【员工】通知公告-查看详情 @author zhuoda")
    @GetMapping("/oa/notice/employee/view/{noticeId}")
    @RepeatSubmit
    public ResponseDTO<NoticeDetailVO> view(@PathVariable Long noticeId, HttpServletRequest request) {
        return noticeEmployeeService.view(
                SmartRequestUtil.getRequestUserId(),
                noticeId,
                ServletUtil.getClientIP(request),
                request.getHeader("User-Agent")
        );
    }

    @ApiOperation("【员工】通知公告-查询全部 @author zhuoda")
    @PostMapping("/oa/notice/employee/query")
    @RepeatSubmit
    public ResponseDTO<PageResult<NoticeEmployeeVO>> queryEmployeeNotice(@RequestBody @Valid NoticeEmployeeQueryForm noticeEmployeeQueryForm) {
        noticeEmployeeQueryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return noticeEmployeeService.queryList(SmartRequestUtil.getRequestUserId(),noticeEmployeeQueryForm);
    }

    @ApiOperation("【员工】通知公告-查询 查看记录 @author zhuoda")
    @PostMapping("/oa/notice/employee/queryViewRecord")
    @RepeatSubmit
    public ResponseDTO<PageResult<NoticeViewRecordVO>> queryViewRecord(@RequestBody @Valid NoticeViewRecordQueryForm noticeViewRecordQueryForm) {
        return ResponseDTO.ok(noticeEmployeeService.queryViewRecord(noticeViewRecordQueryForm));
    }
}