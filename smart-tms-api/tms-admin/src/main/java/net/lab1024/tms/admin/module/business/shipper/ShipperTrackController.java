package net.lab1024.tms.admin.module.business.shipper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.shipper.domain.form.ShipperTrackAddForm;
import net.lab1024.tms.admin.module.business.shipper.domain.form.ShipperTrackQueryForm;
import net.lab1024.tms.admin.module.business.shipper.domain.vo.ShipperTrackVO;
import net.lab1024.tms.admin.module.business.shipper.service.ShipperTrackService;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * [ 货主跟进 ]
 *
 * @author yandanyang
 * @date 2020/10/23 20:35
 */
@Api(tags = AdminSwaggerTagConst.Business.SHIPPER_TRACK)
@OperateLog
@RestController
public class ShipperTrackController {

    @Autowired
    private ShipperTrackService shipperTrackService;

    @ApiOperation("货主跟进 - 货主 by yandanyang")
    @PostMapping("/shipper/track/add")
    public ResponseDTO<String> shipperTrack(@Valid @RequestBody ShipperTrackAddForm shipperTrackAddForm, HttpServletRequest request) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        Long requestUserId = requestUser.getUserId();
        String requestUserName = requestUser.getUserName();
        return shipperTrackService.shipperTrack(shipperTrackAddForm, requestUserId, requestUserName, request);
    }

    @ApiOperation("货主跟进详情 - 货主 by yandanyang")
    @GetMapping("/shipper/track/detail/{trackId}")
    public ResponseDTO<ShipperTrackVO> shipperTrackDetail(@PathVariable Long trackId) {
        return shipperTrackService.shipperTrackDetail(trackId);
    }

    @ApiOperation("某个货主的跟进列表 - 货主 by yandanyang")
    @PostMapping("/shipper/track/list")
    public ResponseDTO<PageResult<ShipperTrackVO>> shipperTrackList(@Valid @RequestBody ShipperTrackQueryForm queryDTO) {
        queryDTO.setEmployeeId(SmartRequestUtil.getRequestUser().getUserId());
        return shipperTrackService.shipperTrackList(queryDTO);
    }

}
