package net.lab1024.tms.admin.module.business.insurance;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.insurance.domain.form.InsuranceAddForm;
import net.lab1024.tms.admin.module.business.insurance.domain.form.InsuranceQueryForm;
import net.lab1024.tms.admin.module.business.insurance.domain.form.InsuranceUpdateForm;
import net.lab1024.tms.admin.module.business.insurance.domain.vo.InsuranceExcelVO;
import net.lab1024.tms.admin.module.business.insurance.domain.vo.InsuranceVO;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.domain.ValidateList;
import net.lab1024.tms.common.common.excel.ExcelStyle;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@OperateLog
@Api(tags = {AdminSwaggerTagConst.Business.INSURANCE})
public class InsuranceController {

    @Autowired
    private InsuranceService insuranceService;

    @ApiOperation(value = "分页查询保险 @author lidoudou")
    @PostMapping("/insurance/page/query")
    public ResponseDTO<PageResult<InsuranceVO>> queryByPage(@RequestBody @Valid InsuranceQueryForm queryDTO) {
        queryDTO.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return insuranceService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "新建保险 @author lidoudou")
    @PostMapping("/insurance/save")
    public ResponseDTO<String> add(@RequestBody @Valid InsuranceAddForm addDTO, HttpServletRequest request) {
        addDTO.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return insuranceService.save(addDTO, dataTracerRequestForm);
    }

    @ApiOperation(value = "编辑保险 @author lidoudou")
    @PostMapping("/insurance/update")
    public ResponseDTO<String> update(@RequestBody @Valid InsuranceUpdateForm updateDTO, HttpServletRequest request) {
        updateDTO.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return insuranceService.update(updateDTO, dataTracerRequestForm);
    }

    @ApiOperation(value = "保险删除 @author lidoudou")
    @PostMapping("/insurance/batchDelete")
    public ResponseDTO<String> batchDelete(@RequestBody @Valid ValidateList<Long> insuranceIdList, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return insuranceService.batchDelete(insuranceIdList, dataTracerRequestForm);
    }

    @ApiOperation(value = "保险导出 by lidoudou")
    @GetMapping("/insurance/export")
    public void export(InsuranceQueryForm queryForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        List<InsuranceExcelVO> list = insuranceService.queryByExport(queryForm);
        ExportParams exportParams = new ExportParams();
        String title = "保险列表";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, InsuranceExcelVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }
}
