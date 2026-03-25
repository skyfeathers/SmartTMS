package net.lab1024.tms.admin.module.business.review;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.review.domain.ReviewAddForm;
import net.lab1024.tms.admin.module.business.review.domain.ReviewQueryForm;
import net.lab1024.tms.admin.module.business.review.domain.ReviewUpdateForm;
import net.lab1024.tms.admin.module.business.review.domain.ReviewVO;
import net.lab1024.tms.admin.module.business.review.service.ReviewService;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.Business.REVIEW})
public class ReviewController {

    @Resource
    private ReviewService reviewService;

    @ApiOperation(value = "分页查询审车信息 @author zhaoxinyang")
    @PostMapping("/review/page/query")
    public ResponseDTO<PageResult<ReviewVO>> queryByPage(@RequestBody @Valid ReviewQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return reviewService.query(queryForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "新建审车记录 @author zhaoxinyang")
    @PostMapping("/review/add")
    public ResponseDTO<String> add(@RequestBody @Valid ReviewAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return reviewService.add(addForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "修改审车记录 @author zhaoxinyang")
    @PostMapping("/review/update")
    public ResponseDTO<String> update(@RequestBody @Valid ReviewUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        updateForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return reviewService.update(updateForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "删除审车记录 @author zhaoxinyang")
    @GetMapping("/review/delete/{reviewId}")
    public ResponseDTO<String> delete(@PathVariable("reviewId") Long reviewId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return reviewService.delete(reviewId, dataTracerRequestForm);
    }

}