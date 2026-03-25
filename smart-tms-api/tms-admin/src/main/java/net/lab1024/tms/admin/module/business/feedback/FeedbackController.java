package net.lab1024.tms.admin.module.business.feedback;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.feedback.domain.FeedbackAddForm;
import net.lab1024.tms.admin.module.business.feedback.domain.FeedbackQueryForm;
import net.lab1024.tms.admin.module.business.feedback.domain.FeedbackVO;
import net.lab1024.tms.admin.module.business.feedback.service.FeedbackService;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.enumeration.UserTypeEnum;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 意见反馈
 *
 * @Author: dangxinfa
 * @Date: 2022/6/28
 */
@Slf4j
@Api(tags = AdminSwaggerTagConst.Business.FEEDBACK)
@RestController
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @ApiOperation("意见反馈-分页查询 by dangxinfa")
    @PostMapping("/feedback/query")
    public ResponseDTO<PageResult<FeedbackVO>> query(@RequestBody @Valid FeedbackQueryForm queryForm) {
        return feedbackService.query(queryForm);
    }

    @RepeatSubmit
    @ApiOperation("意见反馈-新增 by dangxinfa")
    @PostMapping("/feedback/add")
    public ResponseDTO<String> add(@RequestBody @Valid FeedbackAddForm addForm) {
        RequestUser employee = SmartRequestUtil.getRequestUser();
        addForm.setCreateUserId(employee.getUserId());
        addForm.setCreateUserName(employee.getUserName());
        addForm.setCreateUserType(employee.getUserType().getValue());
        addForm.setCreateUserTypeDesc(employee.getUserType().getDesc());
        return feedbackService.add(addForm);
    }
}
