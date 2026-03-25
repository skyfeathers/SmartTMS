package net.lab1024.tms.admin.module.business.oa.enterprise.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.common.constants.CommonConst;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.oa.enterprise.domain.form.*;
import net.lab1024.tms.admin.module.business.oa.enterprise.domain.vo.EnterpriseEmployeeVO;
import net.lab1024.tms.admin.module.business.oa.enterprise.domain.vo.EnterpriseListVO;
import net.lab1024.tms.admin.module.business.oa.enterprise.domain.vo.EnterpriseVO;
import net.lab1024.tms.admin.module.business.oa.enterprise.service.EnterpriseService;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * OA企业模块
 *
 * @author lihaifan
 * @date 2022/6/22 16:49
 */
@Slf4j
@RestController
@OperateLog
@Api(tags = {AdminSwaggerTagConst.Business.OA_ENTERPRISE})
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

    @ApiOperation(value = "分页查询企业模块 @author lihaifan")
    @PostMapping("/oa/enterprise/page/query")
    public ResponseDTO<PageResult<EnterpriseVO>> queryByPage(@RequestBody @Valid EnterpriseQueryForm queryDTO) {
        return enterpriseService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "查询企业详情 @author lihaifan")
    @GetMapping("/oa/enterprise/get/{enterpriseId}")
    public ResponseDTO<EnterpriseVO> getDetail(@PathVariable Long enterpriseId) {
        return ResponseDTO.ok(enterpriseService.getDetail(enterpriseId));
    }

    @ApiOperation(value = "新建企业 @author lihaifan")
    @PostMapping("/oa/enterprise/create")
    public ResponseDTO<String> createEnterprise(@RequestBody @Valid EnterpriseCreateForm createVO) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        createVO.setCreateUserId(requestUser.getUserId());
        createVO.setCreateUserName(requestUser.getUserName());
        return enterpriseService.createEnterprise(createVO);
    }

    @ApiOperation(value = "编辑企业 @author lihaifan")
    @PostMapping("/oa/enterprise/update")
    public ResponseDTO<String> updateEnterprise(@RequestBody @Valid EnterpriseUpdateForm updateForm) {
        return enterpriseService.updateEnterprise(updateForm);
    }

    @ApiOperation(value = "初始化账号信息 @author lihaifan")
    @GetMapping("/oa/enterprise/initAccount/{enterpriseId}")
    public ResponseDTO<String> initEnterpriseAccount(@PathVariable Long enterpriseId) {
        return enterpriseService.initEnterpriseAccount(enterpriseId);
    }

    @ApiOperation(value = "编辑企业站点描述 @author lihaifan")
    @PostMapping("/oa/enterprise/updateWebsiteDesc")
    public ResponseDTO<String> updateWebsiteDesc(@RequestBody @Valid EnterpriseWebsiteDescUpdateForm updateForm) {
        return enterpriseService.updateWebsiteDesc(updateForm);
    }

    @ApiOperation(value = "编辑企业站点域名 @author lihaifan")
    @PostMapping("/oa/enterprise/updateDomainName")
    public ResponseDTO<String> updateDomainName(@RequestBody @Valid EnterpriseDomainNameUpdateForm updateForm) {
        return enterpriseService.updateDomainName(updateForm);
    }

    @ApiOperation(value = "删除企业 @author lihaifan")
    @GetMapping("/oa/enterprise/delete/{enterpriseId}")
    public ResponseDTO<String> deleteEnterprise(@PathVariable Long enterpriseId) {
        return enterpriseService.deleteEnterprise(enterpriseId);
    }

    @ApiOperation(value = "企业列表查询 含数据范围 @author lihaifan")
    @GetMapping("/oa/enterprise/query/list")
    public ResponseDTO<List<EnterpriseListVO>> queryList(@RequestParam(value = "type", required = false) Integer type) {
        return enterpriseService.queryList(SmartRequestEnterpriseUtil.getRequestEnterpriseId(), SmartRequestUtil.getRequestUserId(), type);
    }

    @ApiOperation(value = "查询网络货运公司 @author lihaifan")
    @GetMapping("/oa/enterprise/nft/query")
    public ResponseDTO<List<EnterpriseListVO>> queryByType() {
        return enterpriseService.queryNftList();
    }

    @ApiOperation(value = "企业删除员工 @author yandy")
    @PostMapping("/oa/enterprise/delete/employee")
    public ResponseDTO<String> deleteEmployee(@RequestBody @Valid EnterpriseEmployeeForm enterpriseEmployeeForm) {
        Long enterpriseId = enterpriseEmployeeForm.getEnterpriseId();
        synchronized (CommonConst.STRING_POOL.intern(enterpriseId.toString())) {
            return enterpriseService.deleteEmployee(enterpriseEmployeeForm);
        }
    }

    @ApiOperation(value = "企业添加员工 @author yandy")
    @PostMapping("/oa/enterprise/add/employee")
    public ResponseDTO<String> addEmployee(@RequestBody @Valid EnterpriseEmployeeForm enterpriseEmployeeForm) {
        Long enterpriseId = enterpriseEmployeeForm.getEnterpriseId();
        synchronized (CommonConst.STRING_POOL.intern(enterpriseId.toString())) {
            return enterpriseService.addEmployee(enterpriseEmployeeForm);
        }
    }

    @ApiOperation(value = "企业员工列表 @author yandy")
    @GetMapping("/oa/enterprise/employee/list")
    public ResponseDTO<List<EnterpriseEmployeeVO>> employeeList(@RequestParam(required = false) Long enterpriseId) {
        return enterpriseService.employeeList(enterpriseId);
    }

    @ApiOperation(value = "根据货主查询企业列表 @author lidoudou")
    @GetMapping("/oa/enterprise/shipper/list/{shipperId}")
    public ResponseDTO<List<EnterpriseListVO>> queryListByShipperId(@PathVariable Long shipperId) {
        return ResponseDTO.ok(enterpriseService.queryListByShipperId(shipperId));
    }

    //
}
