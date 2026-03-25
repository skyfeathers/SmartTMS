package net.lab1024.tms.admin.module.business.material.transportroute;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.material.transportroute.domain.dto.TransportRoutePathDTO;
import net.lab1024.tms.admin.module.business.material.transportroute.domain.form.TransportRouteCreateForm;
import net.lab1024.tms.admin.module.business.material.transportroute.domain.form.TransportRouteQueryForm;
import net.lab1024.tms.admin.module.business.material.transportroute.domain.form.TransportRouteUpdateForm;
import net.lab1024.tms.admin.module.business.material.transportroute.domain.vo.TransportRouteVO;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.material.transportroute.domain.TransportRouteEntity;
import net.lab1024.tms.common.module.business.material.transportroute.domain.TransportRoutePathEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 业务资料-运输路线管理
 *
 * @author lidoudou
 * @date 2022/7/12 下午3:06
 */
@Service
@Slf4j
public class TransportRouteService {

    @Autowired
    private TransportRouteDao transportRouteDao;
    @Autowired
    private TransportRoutePathDao transportRoutePathDao;
    @Autowired
    private TransportRoutePathManager transportRoutePathManager;

    /**
     * 分页查询运输路线模块
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResult<TransportRouteVO>> queryByPage(TransportRouteQueryForm queryDTO) {
        queryDTO.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<TransportRouteVO> transportList = transportRouteDao.queryPage(page, queryDTO);
        this.buildPathList(transportList);
        PageResult<TransportRouteVO> pageResult = SmartPageUtil.convert2PageResult(page, transportList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 查询不分页的列表
     * @return
     */
    public ResponseDTO<List<TransportRouteVO>> queryList(Long enterpriseId, Integer transportRouteType) {
        TransportRouteQueryForm queryForm = new TransportRouteQueryForm();
        queryForm.setDeletedFlag(Boolean.FALSE);
        queryForm.setDisabledFlag(Boolean.FALSE);
        queryForm.setTransportRouteType(transportRouteType);
        queryForm.setEnterpriseId(enterpriseId);
        List<TransportRouteVO> voList = transportRouteDao.queryPage(null, queryForm);
        this.buildPathList(voList);
        return ResponseDTO.ok(voList);
    }

    private void buildPathList(List<TransportRouteVO> transportList) {
        if (CollectionUtils.isEmpty(transportList)) {
            return;
        }
        List<Long> transportRouteIdList = transportList.stream().map(TransportRouteVO::getTransportRouteId).collect(Collectors.toList());
        List<TransportRoutePathDTO> pathList = transportRoutePathDao.selectByTransportRouteIdList(transportRouteIdList);
        Map<Long, List<TransportRoutePathDTO>> pathMap = pathList.stream().collect(Collectors.groupingBy(TransportRoutePathDTO::getTransportRouteId));
        transportList.forEach(item -> {
            List<TransportRoutePathDTO> pathDTOList = pathMap.getOrDefault(item.getTransportRouteId(), Lists.newArrayList());
            pathDTOList.sort((Comparator.comparing(TransportRoutePathDTO::getType)));
            item.setPathList(pathDTOList);
        });
    }

    /**
     * 查询运输路线详情
     *
     * @param transportRouteId
     * @return
     */
    public ResponseDTO<TransportRouteVO> getDetail(Long transportRouteId) {
        // 校验运输路线是否存在
        TransportRouteVO transportRouteVO = transportRouteDao.getDetail(transportRouteId, Boolean.FALSE);
        if (Objects.isNull(transportRouteVO)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "运输路线不存在");
        }
        this.buildPathList(Arrays.asList(transportRouteVO));
        return ResponseDTO.ok(transportRouteVO);
    }

    /**
     * 新建运输路线
     *
     * @param createVO
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<Long> createTransportRoute(TransportRouteCreateForm createVO) {
        // 数据插入
        TransportRouteEntity insertEntity = SmartBeanUtil.copy(createVO, TransportRouteEntity.class);
        transportRouteDao.insert(insertEntity);
        List<TransportRoutePathEntity> pathList = SmartBeanUtil.copyList(createVO.getPathList(), TransportRoutePathEntity.class);
        pathList.forEach(item -> {
            item.setTransportRouteId(insertEntity.getTransportRouteId());
        });
        transportRoutePathManager.saveBatch(pathList);
        return ResponseDTO.ok(insertEntity.getTransportRouteId());
    }

    /**
     * 编辑运输路线
     *
     * @param updateVO
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> updateTransportRoute(TransportRouteUpdateForm updateVO) {
        Long transportRouteId = updateVO.getTransportRouteId();
        // 校验运输路线是否存在
        TransportRouteEntity transportRouteEntity = transportRouteDao.selectById(transportRouteId);
        if (Objects.isNull(transportRouteEntity) || transportRouteEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "运输路线不存在");
        }
        // 数据编辑
        TransportRouteEntity updateEntity = SmartBeanUtil.copy(updateVO, TransportRouteEntity.class);
        transportRouteDao.updateById(updateEntity);
        transportRoutePathDao.deleteByTransportRouteId(transportRouteId);
        List<TransportRoutePathEntity> pathList = SmartBeanUtil.copyList(updateVO.getPathList(), TransportRoutePathEntity.class);
        pathList.forEach(item -> {
            item.setTransportRouteId(transportRouteId);
        });
        transportRoutePathManager.saveBatch(pathList);
        return ResponseDTO.ok();
    }


    /**
     * 删除运输路线
     *
     * @param transportRouteId
     * @return
     */
    public ResponseDTO<String> deleteTransportRoute(Long transportRouteId) {
        // 校验运输路线是否存在
        TransportRouteEntity transportRouteDetail = transportRouteDao.selectById(transportRouteId);
        if (Objects.isNull(transportRouteDetail) || transportRouteDetail.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "运输路线不存在");
        }
        transportRouteDao.deleteTransportRoute(transportRouteId, Boolean.TRUE);
        return ResponseDTO.ok();
    }
}
