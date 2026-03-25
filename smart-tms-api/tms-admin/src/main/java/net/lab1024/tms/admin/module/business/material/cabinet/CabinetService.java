package net.lab1024.tms.admin.module.business.material.cabinet;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.material.cabinet.domain.CabinetCreateForm;
import net.lab1024.tms.admin.module.business.material.cabinet.domain.CabinetQueryForm;
import net.lab1024.tms.admin.module.business.material.cabinet.domain.CabinetUpdateForm;
import net.lab1024.tms.admin.module.business.material.cabinet.domain.CabinetVO;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.material.cabinet.CabinetEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 业务资料-柜型管理
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Service
@Slf4j
public class CabinetService {

    @Autowired
    private CabinetDao cabinetDao;

    @Autowired
    private CabinetManage cabinetManage;

    /**
     * 分页查询柜型模块
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResult<CabinetVO>> queryByPage(CabinetQueryForm queryDTO) {
        queryDTO.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<CabinetVO> cabinetVOS = cabinetDao.queryPage(page, queryDTO);
        PageResult<CabinetVO> pageResult = SmartPageUtil.convert2PageResult(page, cabinetVOS);
        return ResponseDTO.ok(pageResult);
    }

    public ResponseDTO<List<CabinetVO>> queryList(Long enterpriseId) {
        List<CabinetVO> cabinetVOList = cabinetDao.queryList(enterpriseId, Boolean.FALSE, Boolean.FALSE);
        return ResponseDTO.ok(cabinetVOList);
    }

    /**
     * 查询柜型详情
     *
     * @param cabinetId
     * @return
     */
    public ResponseDTO<CabinetVO> getDetail(Long cabinetId) {
        // 校验柜型是否存在
        CabinetVO cabinetDetail = cabinetDao.getDetail(cabinetId, Boolean.FALSE);
        if (Objects.isNull(cabinetDetail)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "柜型不存在");
        }
        return ResponseDTO.ok(cabinetDetail);
    }

    /**
     * 新建柜型
     *
     * @param createVO
     * @return
     */
    public ResponseDTO<String> createCabinet(CabinetCreateForm createVO, Long enterpriseId) {
        // 验证柜型名称是否重复
        CabinetEntity validateCabinet = cabinetDao.queryByCabinetName(enterpriseId, createVO.getCabinetName(), null, Boolean.FALSE);
        if (Objects.nonNull(validateCabinet)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "柜型名称重复");
        }
        // 如果当前插入的数据设置为默认柜型 则将其他的默认柜型均设置为否
        List<CabinetEntity> updateDefaultList = Lists.newArrayList();
        if (BooleanUtils.isTrue(createVO.getDefaultFlag())) {
            List<CabinetEntity> cabinetEntities = cabinetDao.queryByDefault(enterpriseId, Boolean.TRUE, Boolean.FALSE, null);
            updateDefaultList = cabinetEntities.stream().map(e -> {
                CabinetEntity updateDefault = new CabinetEntity();
                updateDefault.setCabinetId(e.getCabinetId());
                updateDefault.setDefaultFlag(Boolean.FALSE);
                return updateDefault;
            }).collect(Collectors.toList());
        }
        // 数据插入
        CabinetEntity insertCabinet = SmartBeanUtil.copy(createVO, CabinetEntity.class);
        insertCabinet.setEnterpriseId(enterpriseId);
        // 插入 编辑
        cabinetManage.createCabinet(insertCabinet, updateDefaultList);
        return ResponseDTO.ok();
    }

    /**
     * 编辑柜型
     *
     * @param updateVO
     * @return
     */
    public ResponseDTO<String> updateCabinet(CabinetUpdateForm updateVO, Long enterpriseId) {
        Long cabinetId = updateVO.getCabinetId();
        // 校验柜型是否存在
        CabinetEntity cabinetDetail = cabinetDao.selectById(cabinetId);
        if (Objects.isNull(cabinetDetail) || cabinetDetail.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "柜型不存在");
        }
        // 验证柜型名称是否重复
        CabinetEntity validateCabinet = cabinetDao.queryByCabinetName(enterpriseId, updateVO.getCabinetName(), cabinetId, Boolean.FALSE);
        if (Objects.nonNull(validateCabinet)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "柜型名称重复");
        }
        // 如果当前编辑的数据设置为默认柜型 则将其他的默认柜型均设置为否
        List<CabinetEntity> updateDefaultList = Lists.newArrayList();
        if (BooleanUtils.isTrue(updateVO.getDefaultFlag())) {
            List<CabinetEntity> cabinetEntities = cabinetDao.queryByDefault(enterpriseId, Boolean.TRUE, Boolean.FALSE, updateVO.getCabinetId());
            updateDefaultList = cabinetEntities.stream().map(e -> {
                CabinetEntity updateDefault = new CabinetEntity();
                updateDefault.setCabinetId(e.getCabinetId());
                updateDefault.setDefaultFlag(Boolean.FALSE);
                return updateDefault;
            }).collect(Collectors.toList());
        }
        // 数据编辑
        CabinetEntity updateCabinet = SmartBeanUtil.copy(updateVO, CabinetEntity.class);
        // 编辑
        cabinetManage.updateCabinet(updateCabinet, updateDefaultList);
        return ResponseDTO.ok();
    }


    /**
     * 删除柜型
     *
     * @param cabinetId
     * @return
     */
    public ResponseDTO<String> deleteCabinet(Long cabinetId) {
        // 校验柜型是否存在
        CabinetEntity cabinetDetail = cabinetDao.selectById(cabinetId);
        if (Objects.isNull(cabinetDetail) || cabinetDetail.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "柜型不存在");
        }
        cabinetDao.deleteCabinet(cabinetId, Boolean.TRUE);
        return ResponseDTO.ok();
    }

    /**
     * 根据ID列表获取柜型
     *
     * @param cabinetIdList
     * @return
     */
    public Map<Long, String> getCabinetMap(Collection<Long> cabinetIdList) {
        if (CollectionUtils.isEmpty(cabinetIdList)) {
            return Maps.newHashMap();
        }
        List<CabinetEntity> cabinetEntityList = cabinetDao.selectBatchIds(cabinetIdList);
        Map<Long, String> businessTypeNameMap = cabinetEntityList.stream().collect(Collectors.toMap(CabinetEntity::getCabinetId, CabinetEntity::getCabinetName));
        return businessTypeNameMap;
    }
}
