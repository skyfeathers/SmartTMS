package net.lab1024.tms.fixedasset.module.business.location;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.fixedasset.constant.FixedAssetSwaggerTagConst;
import net.lab1024.tms.fixedasset.module.business.location.domain.LocationCreateForm;
import net.lab1024.tms.fixedasset.module.business.location.domain.LocationQueryForm;
import net.lab1024.tms.fixedasset.module.business.location.domain.LocationUpdateForm;
import net.lab1024.tms.fixedasset.module.business.location.domain.LocationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 位置管理
 *
 * @author lidoudou
 * @date 2023/3/14 下午5:29
 */
@Slf4j
@RestController
@OperateLog
@Api(tags = {FixedAssetSwaggerTagConst.Business.LOCATION})
public class LocationController {

    @Autowired
    private LocationService locationService;

    @ApiOperation(value = "分页查询位置模块 @author lidoudou")
    @PostMapping("/location/page/query")
    public ResponseDTO<PageResult<LocationVO>> queryByPage(@RequestBody @Valid LocationQueryForm queryDTO) {
        queryDTO.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return locationService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "查询列表 @author yandy")
    @GetMapping("/location/query/list")
    public ResponseDTO<List<LocationVO>> queryList() {
        return locationService.queryList(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }


    @ApiOperation(value = "查询位置详情 @author lidoudou")
    @GetMapping("/location/get/{locationId}")
    public ResponseDTO<LocationVO> getDetail(@PathVariable Long locationId) {
        return locationService.getDetail(locationId);
    }

    @ApiOperation(value = "新建位置 @author lidoudou")
    @PostMapping("/location/create")
    public ResponseDTO<String> createLocation(@RequestBody @Valid LocationCreateForm createVO) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        createVO.setCreateUserId(requestUser.getUserId());
        createVO.setCreateUserName(requestUser.getUserName());

        createVO.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return locationService.createLocation(createVO);
    }

    @ApiOperation(value = "编辑位置 @author lidoudou")
    @PostMapping("/location/update")
    public ResponseDTO<String> updateLocation(@RequestBody @Valid LocationUpdateForm updateVO) {
        updateVO.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return locationService.updateLocation(updateVO);
    }

    @ApiOperation(value = "删除位置 @author lidoudou")
    @GetMapping("/location/delete/{locationId}")
    public ResponseDTO<String> deleteLocation(@PathVariable Long locationId) {
        return locationService.deleteLocation(locationId);
    }

}
