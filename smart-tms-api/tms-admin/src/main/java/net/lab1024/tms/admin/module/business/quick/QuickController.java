package net.lab1024.tms.admin.module.business.quick;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.quick.domain.form.QuickSearchForm;
import net.lab1024.tms.admin.module.business.quick.domain.form.VehicleQuickCreateForm;
import net.lab1024.tms.admin.module.business.quick.domain.vo.QuickSearchVO;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author yandy
 */
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.Business.QUICK})
public class QuickController {

    @Autowired
    private QuickService quickService;

    @RepeatSubmit
    @ApiOperation(value = "新建司机、车辆、挂车 @author lidoudou")
    @PostMapping("/vehicle/quick/save")
    public ResponseDTO<String> add(@RequestBody @Valid VehicleQuickCreateForm vehicleQuickCreateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return quickService.save(vehicleQuickCreateForm, SmartRequestEnterpriseUtil.getRequestEnterpriseId(), dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "快速查询 @author lidoudou")
    @PostMapping("/quick/search")
    public ResponseDTO<QuickSearchVO> quickSearch(@RequestBody @Valid QuickSearchForm quickSearchForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return quickService.quickSearch(quickSearchForm, SmartRequestEnterpriseUtil.getRequestEnterpriseId(), dataTracerRequestForm);
    }
}
