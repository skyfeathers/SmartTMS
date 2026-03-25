package net.lab1024.tms.driver.module.business.oa.notice;

import cn.hutool.extra.servlet.ServletUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import net.lab1024.tms.driver.constant.DriverSwaggerTagConst;
import net.lab1024.tms.driver.module.business.oa.notice.domain.form.NoticeQueryForm;
import net.lab1024.tms.driver.module.business.oa.notice.domain.vo.NoticeVO;
import net.lab1024.tms.driver.module.business.oa.notice.domain.vo.NoticeSimpleVO;
import net.lab1024.tms.driver.module.business.oa.notice.service.NoticeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(tags = DriverSwaggerTagConst.Business.OA_NOTICE)
@RestController
public class NoticeController {

    @Resource
    private NoticeService noticeService;
    
    @ApiOperation("通知公告分页查询 @author zhaoxinyang")
    @PostMapping("/oa/notice/query")
    public ResponseDTO<PageResult<NoticeSimpleVO>> query(@RequestBody @Valid NoticeQueryForm noticeQueryForm) {
        return ResponseDTO.ok(noticeService.query(noticeQueryForm));
    }

    @ApiOperation("查询并更新查看记录 @author zhaoxinyang")
    @GetMapping("/oa/notice/view/{noticeId}")
    @RepeatSubmit
    public ResponseDTO<NoticeVO> view(@PathVariable Long noticeId, HttpServletRequest request) {
        return noticeService.view(
                SmartRequestUtil.getRequestUserId(),
                noticeId,
                ServletUtil.getClientIP(request),
                request.getHeader("User-Agent")
        );
    }

}
