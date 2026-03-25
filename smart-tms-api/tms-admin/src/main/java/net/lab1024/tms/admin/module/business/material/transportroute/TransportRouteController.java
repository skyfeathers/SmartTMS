package net.lab1024.tms.admin.module.business.material.transportroute;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.material.transportroute.domain.form.TransportRouteCreateForm;
import net.lab1024.tms.admin.module.business.material.transportroute.domain.form.TransportRouteQueryForm;
import net.lab1024.tms.admin.module.business.material.transportroute.domain.form.TransportRouteUpdateForm;
import net.lab1024.tms.admin.module.business.material.transportroute.domain.vo.TransportRouteVO;
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
 * 业务资料-运输路线管理
 *
 * @author lidoudou
 * @date 2022/6/24 11:31
 */
@Slf4j
@RestController
@OperateLog
@Api(tags = {AdminSwaggerTagConst.Business.MATERIAL_TRANSPORT_ROUTE})
public class TransportRouteController {

    @Autowired
    private TransportRouteService transportRouteService;

    @ApiOperation(value = "分页查询运输路线模块 @author lidoudou")
    @PostMapping("/transport/route/page/query")
    public ResponseDTO<PageResult<TransportRouteVO>> queryByPage(@RequestBody @Valid TransportRouteQueryForm queryDTO) {
        queryDTO.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return transportRouteService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "查询运输路线不分页列表 @author lidoudou")
    @GetMapping("/transport/route/query/list/{transportRouteType}")
    public ResponseDTO<List<TransportRouteVO>> queryList(@PathVariable Integer transportRouteType) {
        return transportRouteService.queryList(SmartRequestEnterpriseUtil.getRequestEnterpriseId(), transportRouteType);
    }

    @ApiOperation(value = "查询运输路线详情 @author lidoudou")
    @GetMapping("/transport/route/get/{transportRouteId}")
    public ResponseDTO<TransportRouteVO> getDetail(@PathVariable Long transportRouteId) {
        return transportRouteService.getDetail(transportRouteId);
    }

    @ApiOperation(value = "新建运输路线 @author lidoudou")
    @PostMapping("/transport/route/create")
    public ResponseDTO<Long> createTransportRoute(@RequestBody @Valid TransportRouteCreateForm createVO) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        createVO.setCreateUserId(requestUser.getUserId());
        createVO.setCreateUserName(requestUser.getUserName());
        createVO.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return transportRouteService.createTransportRoute(createVO);
    }

    @ApiOperation(value = "编辑运输路线 @author lidoudou")
    @PostMapping("/transport/route/update")
    public ResponseDTO<String> updateTransportRoute(@RequestBody @Valid TransportRouteUpdateForm updateVO) {
        updateVO.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return transportRouteService.updateTransportRoute(updateVO);
    }

    @ApiOperation(value = "删除运输路线 @author lidoudou")
    @GetMapping("/transport/route/delete/{transportRouteId}")
    public ResponseDTO<String> deleteTransportRoute(@PathVariable Long transportRouteId) {
        return transportRouteService.deleteTransportRoute(transportRouteId);
    }

}
