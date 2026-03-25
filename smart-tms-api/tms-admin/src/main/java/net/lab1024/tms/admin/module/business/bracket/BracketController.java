package net.lab1024.tms.admin.module.business.bracket;


import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.bracket.domain.form.*;
import net.lab1024.tms.admin.module.business.bracket.domain.vo.BracketDetailVO;
import net.lab1024.tms.admin.module.business.bracket.domain.vo.BracketExportVO;
import net.lab1024.tms.admin.module.business.bracket.domain.vo.BracketListVO;
import net.lab1024.tms.admin.module.business.bracket.domain.vo.BracketSimpleVO;
import net.lab1024.tms.admin.module.business.driver.domain.form.DriverBusinessModeUpdateForm;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.domain.ValidateList;
import net.lab1024.tms.common.common.excel.ExcelStyle;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 挂车管理
 *
 * @author lidoudou
 * @date 2022/11/23 上午11:46
 */
@Slf4j
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.Business.BRACKET})
public class BracketController {

    @Autowired
    private BracketService bracketService;

    @ApiOperation(value = "分页查询 @author lidoudou")
    @PostMapping("/bracket/page/query")
    public ResponseDTO<PageResult<BracketListVO>> queryByPage(@RequestBody @Valid BracketQueryForm queryDTO) {
        return bracketService.queryByPage(queryDTO, SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @ApiOperation(value = "查询不分页列表 @author lidoudou")
    @PostMapping("/bracket/query/list")
    public ResponseDTO<List<BracketSimpleVO>> queryList() {
        return bracketService.queryList(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @ApiOperation(value = "详情 @author lidoudou")
    @GetMapping("/bracket/detail/{bracketId}")
    public ResponseDTO<BracketDetailVO> detail(@PathVariable Long bracketId) {
        return bracketService.getDetail(bracketId, SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @RepeatSubmit
    @ApiOperation(value = "新建 @author lidoudou")
    @PostMapping("/bracket/save")
    public ResponseDTO<String> add(@RequestBody @Valid BracketAddForm addDTO, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return bracketService.save(addDTO, SmartRequestEnterpriseUtil.getRequestEnterpriseId(), dataTracerRequestForm);
    }
    @RepeatSubmit
    @ApiOperation(value = "编辑经营模式 @author lidoudou")
    @PostMapping("/bracket/businessMode/update")
    public ResponseDTO<String> businessModeUpdate(@RequestBody @Valid BracketBusinessModeUpdateForm updateForm, HttpServletRequest request) {
        return bracketService.businessModeUpdate(updateForm, SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @RepeatSubmit
    @ApiOperation(value = "编辑 @author lidoudou")
    @PostMapping("/bracket/update")
    public ResponseDTO<String> update(@RequestBody @Valid BracketUpdateForm updateDTO, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return bracketService.update(updateDTO, SmartRequestEnterpriseUtil.getRequestEnterpriseId(), dataTracerRequestForm);
    }

    @ApiOperation(value = "批量删除 @author lidoudou")
    @PostMapping("/bracket/batch/delete")
    public ResponseDTO<String> delete(@RequestBody @Valid ValidateList<Long> bracketIdList, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return bracketService.deleteBracket(bracketIdList, dataTracerRequestForm);
    }


    @ApiOperation("批量审核 by lidoudou")
    @PostMapping("/bracket/batch/audit")
    public ResponseDTO<String> batchAudit(@RequestBody @Valid BracketBatchAuditForm batchAuditDTO, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return bracketService.batchAudit(batchAuditDTO, dataTracerRequestForm);
    }

    @ApiOperation("批量修改负责人 by lidoudou")
    @PostMapping("/bracket/manager/update")
    public ResponseDTO<String> batchUpdateManager(@RequestBody @Valid BracketManagerUpdateForm updateManagerForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return bracketService.batchUpdateManager(updateManagerForm,dataTracerRequestForm);
    }


    @ApiOperation(value = "挂车导出 by lidoudou")
    @GetMapping("/bracket/export")
    public void export(BracketQueryForm queryForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        List<BracketExportVO> list = bracketService.queryByExport(queryForm, SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        ExportParams exportParams = new ExportParams();
        String title = "挂车列表";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, BracketExportVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }
}
