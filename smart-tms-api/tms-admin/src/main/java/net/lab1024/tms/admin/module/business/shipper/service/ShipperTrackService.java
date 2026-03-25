package net.lab1024.tms.admin.module.business.shipper.service;

import cn.hutool.extra.servlet.ServletUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperDao;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperTrackDao;
import net.lab1024.tms.admin.module.business.shipper.domain.form.ShipperTrackAddForm;
import net.lab1024.tms.admin.module.business.shipper.domain.form.ShipperTrackQueryForm;
import net.lab1024.tms.admin.module.business.shipper.domain.vo.ShipperTrackVO;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperTrackEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/10/23 20:41
 */
@Service
public class ShipperTrackService {

    @Autowired
    private ShipperTrackDao shipperTrackDao;

    @Autowired
    private ShipperDao shipperDao;

    /**
     * 货主跟进
     *
     * @param shipperTrackAddForm
     * @param requestUserId
     * @param requestUserName
     * @return
     */
    public ResponseDTO<String> shipperTrack(ShipperTrackAddForm shipperTrackAddForm, Long requestUserId, String requestUserName, HttpServletRequest request) {
        ShipperEntity shipperEntity = shipperDao.selectById(shipperTrackAddForm.getShipperId());
        if (null == shipperEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        ShipperTrackEntity shipperTrackEntity = SmartBeanUtil.copy(shipperTrackAddForm, ShipperTrackEntity.class);
        shipperTrackEntity.setEmployeeId(requestUserId);
        shipperTrackEntity.setIp(ServletUtil.getClientIP(request));
        shipperTrackDao.insert(shipperTrackEntity);
        return ResponseDTO.ok();
    }

    /**
     * 货主跟进详情
     *
     * @param trackId
     * @return
     */
    public ResponseDTO<ShipperTrackVO> shipperTrackDetail(Long trackId) {
        ShipperTrackVO shipperTrackVO = shipperTrackDao.detail(trackId);
        if (shipperTrackVO == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        return ResponseDTO.ok(shipperTrackVO);
    }

    /**
     * 货主跟进列表
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResult<ShipperTrackVO>> shipperTrackList(ShipperTrackQueryForm queryDTO) {
        Page page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<ShipperTrackVO> shipperTrackVOS = shipperTrackDao.query(page, queryDTO);
        PageResult<ShipperTrackVO> pageResult = SmartPageUtil.convert2PageResult(page, shipperTrackVOS);
        return ResponseDTO.ok(pageResult);
    }
}
