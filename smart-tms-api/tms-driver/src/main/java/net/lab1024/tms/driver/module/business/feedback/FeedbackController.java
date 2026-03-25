package net.lab1024.tms.driver.module.business.feedback;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import net.lab1024.tms.driver.constant.DriverSwaggerTagConst;
import net.lab1024.tms.driver.module.business.feedback.domain.form.FeedbackAddForm;
import net.lab1024.tms.driver.module.business.feedback.domain.form.FeedbackQueryForm;
import net.lab1024.tms.driver.module.business.feedback.domain.vo.FeedbackQueryVO;
import net.lab1024.tms.driver.module.business.feedback.service.FeedbackService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(tags = DriverSwaggerTagConst.Business.FEEDBACK)
@RestController
public class FeedbackController {

    @Resource
    private FeedbackService feedbackService;

    @ApiOperation("意见反馈-分页查询 by zhaoxinyang")
    @PostMapping("/feedback/query")
    public ResponseDTO<PageResult<FeedbackQueryVO>> query(@RequestBody @Valid FeedbackQueryForm queryForm) {
        queryForm.setDriverId(SmartRequestUtil.getRequestUserId());
        return feedbackService.query(queryForm);
    }

    @RepeatSubmit
    @ApiOperation("意见反馈-新增 by zhaoxinyang")
    @PostMapping("/feedback/add")
    public ResponseDTO<String> add(@RequestBody @Valid FeedbackAddForm addForm) {
        RequestUser driver = SmartRequestUtil.getRequestUser();
        return feedbackService.add(addForm, driver);
    }

}
