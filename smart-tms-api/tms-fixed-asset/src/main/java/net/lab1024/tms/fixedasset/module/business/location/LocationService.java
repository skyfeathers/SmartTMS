package net.lab1024.tms.fixedasset.module.business.location;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.oa.enterprise.dao.CommonEnterpriseDao;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.fixedasset.module.business.location.domain.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 位置管理
 *
 * @author lidoudou
 * @date 2023/3/14 下午5:26
 */
@Service
@Slf4j
public class LocationService {

    @Autowired
    private LocationDao locationDao;
    @Autowired
    private CommonEnterpriseDao enterpriseDao;

    /**
     * 分页查询位置模块
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResult<LocationVO>> queryByPage(LocationQueryForm queryDTO) {
        queryDTO.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<LocationVO> locationVOS = locationDao.queryPage(page, queryDTO);
        buildList(locationVOS);
        PageResult<LocationVO> pageResult = SmartPageUtil.convert2PageResult(page, locationVOS);
        return ResponseDTO.ok(pageResult);
    }

    private void buildList(List<LocationVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Set<Long> enterpriseIdSet = list.stream().map(LocationVO::getEnterpriseId).collect(Collectors.toSet());
        List<EnterpriseEntity> enterpriseEntityList = enterpriseDao.selectBatchIds(enterpriseIdSet);
        Map<Long, String> enterpriseNameMap = enterpriseEntityList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));
        list.forEach(item -> {
            item.setEnterpriseName(enterpriseNameMap.getOrDefault(item.getEnterpriseId(), StringConst.EMPTY));
        });
    }

    /**
     * 查询不分页列表，有权限
     * @param enterpriseId
     * @return
     */
    public ResponseDTO<List<LocationVO>> queryList(Long enterpriseId) {
        List<LocationVO> locationVOList = locationDao.queryList(Boolean.FALSE, Boolean.FALSE,enterpriseId);
        return ResponseDTO.ok(locationVOList);
    }

    /**
     * 查询位置详情
     *
     * @param locationtId
     * @return
     */
    public ResponseDTO<LocationVO> getDetail(Long locationtId) {
        // 校验位置是否存在
        LocationVO locationtDetail = locationDao.getDetail(locationtId, Boolean.FALSE);
        if (Objects.isNull(locationtDetail)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "位置不存在");
        }
        return ResponseDTO.ok(locationtDetail);
    }

    /**
     * 新建位置
     *
     * @param createVO
     * @return
     */
    public ResponseDTO<String> createLocation(LocationCreateForm createVO) {
        // 验证位置名称是否重复
        LocationEntity validateLocation = locationDao.queryByLocationName(createVO.getLocationName(), createVO.getEnterpriseId(), null, Boolean.FALSE);
        if (Objects.nonNull(validateLocation)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "位置名称重复");
        }
        // 数据插入
        LocationEntity insertLocation = SmartBeanUtil.copy(createVO, LocationEntity.class);
        // 插入 编辑
        locationDao.insert(insertLocation);
        return ResponseDTO.ok();
    }

    /**
     * 编辑位置
     *
     * @param updateVO
     * @return
     */
    public ResponseDTO<String> updateLocation(LocationUpdateForm updateVO) {
        Long locationId = updateVO.getLocationId();
        // 校验位置是否存在
        LocationEntity dbLocationEntity = locationDao.selectById(locationId);
        if (Objects.isNull(dbLocationEntity) || dbLocationEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "位置不存在");
        }
        if (!updateVO.getEnterpriseId().equals(dbLocationEntity.getEnterpriseId())) {
            return ResponseDTO.userErrorParam("数据所属企业和当前登录企业不一致");
        }
        // 验证位置名称是否重复
        LocationEntity validateLocation = locationDao.queryByLocationName(updateVO.getLocationName(),updateVO.getEnterpriseId(), locationId, Boolean.FALSE);
        if (Objects.nonNull(validateLocation)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "位置名称重复");
        }
        // 数据编辑
        LocationEntity updateLocation = SmartBeanUtil.copy(updateVO, LocationEntity.class);
        // 编辑
        locationDao.updateById(updateLocation);
        return ResponseDTO.ok();
    }


    /**
     * 删除位置
     *
     * @param locationtId
     * @return
     */
    public ResponseDTO<String> deleteLocation(Long locationtId) {
        // 校验位置是否存在
        LocationEntity locationtDetail = locationDao.selectById(locationtId);
        if (Objects.isNull(locationtDetail) || locationtDetail.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "位置不存在");
        }
        locationDao.deleteLocation(locationtId, Boolean.TRUE);
        return ResponseDTO.ok();
    }
}
