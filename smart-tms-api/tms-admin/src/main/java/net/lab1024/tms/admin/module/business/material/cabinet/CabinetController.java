package net.lab1024.tms.admin.module.business.material.cabinet;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.material.cabinet.domain.CabinetCreateForm;
import net.lab1024.tms.admin.module.business.material.cabinet.domain.CabinetQueryForm;
import net.lab1024.tms.admin.module.business.material.cabinet.domain.CabinetUpdateForm;
import net.lab1024.tms.admin.module.business.material.cabinet.domain.CabinetVO;
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
 * 业务资料-柜型管理
 *
 * @author lihaifan
 * @date 2022/6/24 11:31
 */
@Slf4j
@RestController
@OperateLog
@Api(tags = {AdminSwaggerTagConst.Business.MATERIAL_CABINET})
public class CabinetController {

    @Autowired
    private CabinetService cabinetService;

    @ApiOperation(value = "分页查询柜型模块 @author lihaifan")
    @PostMapping("/cabinet/page/query")
    public ResponseDTO<PageResult<CabinetVO>> queryByPage(@RequestBody @Valid CabinetQueryForm queryDTO) {
        queryDTO.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return cabinetService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "查询列表 @author yandy")
    @GetMapping("/cabinet/query/list")
    public ResponseDTO<List<CabinetVO>> queryList() {
        return cabinetService.queryList(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }


    @ApiOperation(value = "查询柜型详情 @author lihaifan")
    @GetMapping("/cabinet/get/{cabinetId}")
    public ResponseDTO<CabinetVO> getDetail(@PathVariable Long cabinetId) {
        return cabinetService.getDetail(cabinetId);
    }

    @ApiOperation(value = "新建柜型 @author lihaifan")
    @PostMapping("/cabinet/create")
    public ResponseDTO<String> createCabinet(@RequestBody @Valid CabinetCreateForm createVO) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        createVO.setCreateUserId(requestUser.getUserId());
        createVO.setCreateUserName(requestUser.getUserName());
        return cabinetService.createCabinet(createVO, SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @ApiOperation(value = "编辑柜型 @author lihaifan")
    @PostMapping("/cabinet/update")
    public ResponseDTO<String> updateCabinet(@RequestBody @Valid CabinetUpdateForm updateVO) {
        return cabinetService.updateCabinet(updateVO, SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @ApiOperation(value = "删除柜型 @author lihaifan")
    @GetMapping("/cabinet/delete/{cabinetId}")
    public ResponseDTO<String> deleteCabinet(@PathVariable Long cabinetId) {
        return cabinetService.deleteCabinet(cabinetId);
    }

}
