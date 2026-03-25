package net.lab1024.tms.admin.module.business.material.customsbroker;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.material.customsbroker.domain.CustomsBrokerCreateForm;
import net.lab1024.tms.admin.module.business.material.customsbroker.domain.CustomsBrokerQueryForm;
import net.lab1024.tms.admin.module.business.material.customsbroker.domain.CustomsBrokerUpdateForm;
import net.lab1024.tms.admin.module.business.material.customsbroker.domain.CustomsBrokerVO;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 业务资料-报关行
 *
 * @author lihaifan
 * @date 2022/6/24 11:31
 */
@Slf4j
@RestController
@OperateLog
@Api(tags = {AdminSwaggerTagConst.Business.MATERIAL_CUSTOMS_BROKER})
public class CustomsBrokerController {

    @Autowired
    private CustomsBrokerService customsBrokerService;

    @ApiOperation(value = "分页查询报关行模块 @author lihaifan")
    @PostMapping("/customsBroker/page/query")
    public ResponseDTO<PageResult<CustomsBrokerVO>> queryByPage(@RequestBody @Valid CustomsBrokerQueryForm queryDTO) {
        return customsBrokerService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "查询报关行详情 @author lihaifan")
    @GetMapping("/customsBroker/get/{customsBrokerId}")
    public ResponseDTO<CustomsBrokerVO> getDetail(@PathVariable Long customsBrokerId) {
        return customsBrokerService.getDetail(customsBrokerId);
    }

    @ApiOperation(value = "新建报关行 @author lihaifan")
    @PostMapping("/customsBroker/create")
    public ResponseDTO<String> createCustomsBroker(@RequestBody @Valid CustomsBrokerCreateForm createVO) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        createVO.setCreateUserId(requestUser.getUserId());
        createVO.setCreateUserName(requestUser.getUserName());
        return customsBrokerService.createCustomsBroker(createVO);
    }

    @ApiOperation(value = "编辑报关行 @author lihaifan")
    @PostMapping("/customsBroker/update")
    public ResponseDTO<String> updateCustomsBroker(@RequestBody @Valid CustomsBrokerUpdateForm updateVO) {
        return customsBrokerService.updateCustomsBroker(updateVO);
    }

    @ApiOperation(value = "删除报关行 @author lihaifan")
    @GetMapping("/customsBroker/delete/{customsBrokerId}")
    public ResponseDTO<String> deleteCustomsBroker(@PathVariable Long customsBrokerId) {
        return customsBrokerService.deleteCustomsBroker(customsBrokerId);
    }

}
