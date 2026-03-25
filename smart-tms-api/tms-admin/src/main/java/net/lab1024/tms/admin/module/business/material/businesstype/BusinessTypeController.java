package net.lab1024.tms.admin.module.business.material.businesstype;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.material.businesstype.domain.BusinessTypeCreateForm;
import net.lab1024.tms.admin.module.business.material.businesstype.domain.BusinessTypeQueryForm;
import net.lab1024.tms.admin.module.business.material.businesstype.domain.BusinessTypeUpdateForm;
import net.lab1024.tms.admin.module.business.material.businesstype.domain.BusinessTypeVO;
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
 * 业务资料-业务类型
 *
 * @author lihaifan
 * @date 2022/6/24 11:31
 */
@Slf4j
@RestController
@OperateLog
@Api(tags = {AdminSwaggerTagConst.Business.MATERIAL_BUSINESS_TYPE})
public class BusinessTypeController {

    @Autowired
    private BusinessTypeService businessTypeService;

    @ApiOperation(value = "分页查询业务类型模块 @author lihaifan")
    @PostMapping("/businessType/page/query")
    public ResponseDTO<PageResult<BusinessTypeVO>> queryByPage(@RequestBody @Valid BusinessTypeQueryForm queryDTO) {
        queryDTO.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return businessTypeService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "查询业务类型列表 @author yandy")
    @GetMapping("/businessType/query/list")
    public ResponseDTO<List<BusinessTypeVO>> queryList() {
        return businessTypeService.queryList(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @ApiOperation(value = "查询业务类型详情 @author lihaifan")
    @GetMapping("/businessType/get/{businessTypeId}")
    public ResponseDTO<BusinessTypeVO> getDetail(@PathVariable Long businessTypeId) {
        return businessTypeService.getDetail(businessTypeId);
    }

    @ApiOperation(value = "新建业务类型 @author lihaifan")
    @PostMapping("/businessType/create")
    public ResponseDTO<String> createBusinessType(@RequestBody @Valid BusinessTypeCreateForm createVO) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        createVO.setCreateUserId(requestUser.getUserId());
        createVO.setCreateUserName(requestUser.getUserName());
        return businessTypeService.createBusinessType(createVO, SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @ApiOperation(value = "编辑业务类型 @author lihaifan")
    @PostMapping("/businessType/update")
    public ResponseDTO<String> updateBusinessType(@RequestBody @Valid BusinessTypeUpdateForm updateVO) {
        return businessTypeService.updateBusinessType(updateVO, SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @ApiOperation(value = "删除业务类型 @author lihaifan")
    @GetMapping("/businessType/delete/{businessTypeId}")
    public ResponseDTO<String> deleteBusinessType(@PathVariable Long businessTypeId) {
        return businessTypeService.deleteBusinessType(businessTypeId);
    }

}
