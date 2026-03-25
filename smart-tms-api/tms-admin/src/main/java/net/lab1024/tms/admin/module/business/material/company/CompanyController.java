package net.lab1024.tms.admin.module.business.material.company;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.material.company.domain.CompanyCreateForm;
import net.lab1024.tms.admin.module.business.material.company.domain.CompanyQueryForm;
import net.lab1024.tms.admin.module.business.material.company.domain.CompanyUpdateForm;
import net.lab1024.tms.admin.module.business.material.company.domain.CompanyVO;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 业务资料-公司管理
 *
 * @author lihaifan
 * @date 2022/6/24 11:31
 */
@Slf4j
@RestController
@OperateLog
@Api(tags = {AdminSwaggerTagConst.Business.MATERIAL_COMPANY})
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @ApiOperation(value = "分页查询公司模块 @author lihaifan")
    @PostMapping("/company/page/query")
    public ResponseDTO<PageResult<CompanyVO>> queryByPage(@RequestBody @Valid CompanyQueryForm queryDTO) {
        return companyService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "查询公司详情 @author lihaifan")
    @GetMapping("/company/get/{companyId}")
    public ResponseDTO<CompanyVO> getDetail(@PathVariable Long companyId) {
        return companyService.getDetail(companyId);
    }

    @ApiOperation(value = "新建公司 @author lihaifan")
    @PostMapping("/company/create")
    public ResponseDTO<String> createCompany(@RequestBody @Valid CompanyCreateForm createVO) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        createVO.setCreateUserId(requestUser.getUserId());
        createVO.setCreateUserName(requestUser.getUserName());
        return companyService.createCompany(createVO);
    }

    @ApiOperation(value = "编辑公司 @author lihaifan")
    @PostMapping("/company/update")
    public ResponseDTO<String> updateCompany(@RequestBody @Valid CompanyUpdateForm updateVO) {
        return companyService.updateCompany(updateVO);
    }

    @ApiOperation(value = "删除公司 @author lihaifan")
    @GetMapping("/company/delete/{companyId}")
    public ResponseDTO<String> deleteCompany(@PathVariable Long companyId) {
        return companyService.deleteCompany(companyId);
    }

}
