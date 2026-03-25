package net.lab1024.tms.admin.module.business.expiredcertificate;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.expiredcertificate.domain.ExpiredCertificateQueryForm;
import net.lab1024.tms.admin.module.business.expiredcertificate.domain.ExpiredCertificateReminderTimeDTO;
import net.lab1024.tms.admin.module.business.expiredcertificate.domain.ExpiredCertificateVO;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 到期证件
 *
 * @author listen
 * @date 2022/07/19 14:32
 */
@OperateLog
@Api(tags = AdminSwaggerTagConst.Business.EXPIRED_CERTIFICATE)
@RestController
public class ExpiredCertificateController {

    @Autowired
    private ExpiredCertificateService expiredCertificateService;

    @ApiOperation("分页查询 @listen")
    @PostMapping("/expiredCertificate/query")
    public ResponseDTO<PageResult<ExpiredCertificateVO>> add(@RequestBody @Valid ExpiredCertificateQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return expiredCertificateService.query(queryForm);
    }

    @ApiOperation("到期提醒时间设置-查询 @listen")
    @GetMapping("/expiredCertificate/reminderTime/query")
    public ResponseDTO<ExpiredCertificateReminderTimeDTO> queryReminderTime() {
        return expiredCertificateService.queryReminderTime();
    }

    @ApiOperation("到期提醒时间设置-更新 @listen")
    @PostMapping("/expiredCertificate/reminderTime/update")
    public ResponseDTO<String> updateReminderTime(@RequestBody @Valid ExpiredCertificateReminderTimeDTO updateForm) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        updateForm.setUpdateName(requestUser.getUserName());
        return expiredCertificateService.updateReminderTime(updateForm);
    }
}