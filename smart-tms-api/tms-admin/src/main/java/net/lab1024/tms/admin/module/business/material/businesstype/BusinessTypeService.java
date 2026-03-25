package net.lab1024.tms.admin.module.business.material.businesstype;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.material.businesstype.constant.TripTypeEnum;
import net.lab1024.tms.admin.module.business.material.businesstype.domain.BusinessTypeCreateForm;
import net.lab1024.tms.admin.module.business.material.businesstype.domain.BusinessTypeQueryForm;
import net.lab1024.tms.admin.module.business.material.businesstype.domain.BusinessTypeUpdateForm;
import net.lab1024.tms.admin.module.business.material.businesstype.domain.BusinessTypeVO;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.material.businesstype.BusinessTypeEntity;
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
 * 业务资料-业务类型
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Service
@Slf4j
public class BusinessTypeService {

    @Autowired
    private BusinessTypeDao businessTypeDao;

    @Autowired
    private BusinessTypeManage businessTypeManage;

    /**
     * 分页查询业务类型模块
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResult<BusinessTypeVO>> queryByPage(BusinessTypeQueryForm queryDTO) {
        queryDTO.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<BusinessTypeVO> businessTypeVOS = businessTypeDao.queryPage(page, queryDTO);
        businessTypeVOS.stream().forEach(e -> e.setTripTypeDesc(SmartBaseEnumUtil.getEnumDescByValue(e.getTripType(), TripTypeEnum.class)));
        PageResult<BusinessTypeVO> pageResult = SmartPageUtil.convert2PageResult(page, businessTypeVOS);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 查询业务类型详情
     *
     * @param businessTypeId
     * @return
     */
    public ResponseDTO<BusinessTypeVO> getDetail(Long businessTypeId) {
        // 校验业务类型是否存在
        BusinessTypeVO businessTypeDetail = businessTypeDao.getDetail(businessTypeId, Boolean.FALSE);
        if (Objects.isNull(businessTypeDetail)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "业务类型不存在");
        }
        businessTypeDetail.setTripTypeDesc(SmartBaseEnumUtil.getEnumDescByValue(businessTypeDetail.getTripType(), TripTypeEnum.class));
        return ResponseDTO.ok(businessTypeDetail);
    }

    /**
     * 新建业务类型
     *
     * @param createForm
     * @return
     */
    public ResponseDTO<String> createBusinessType(BusinessTypeCreateForm createForm, Long enterpriseId) {
        // 验证业务代码是否重复
        BusinessTypeEntity validateBusinessType = businessTypeDao.queryByBusinessTypeCode(enterpriseId, createForm.getBusinessTypeCode(), null, Boolean.FALSE);
        if (Objects.nonNull(validateBusinessType)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "业务代码重复");
        }
        // 验证业务名称是否重复
        BusinessTypeEntity validateBusinessName = businessTypeDao.queryByBusinessTypeName(enterpriseId, createForm.getBusinessTypeName(), null, Boolean.FALSE);
        if (Objects.nonNull(validateBusinessName)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "业务名称重复");
        }
        // 如果当前插入的数据设置为默认业务类型 则将其他的默认业务类型均设置为否
        List<BusinessTypeEntity> updateDefaultList = Lists.newArrayList();
        if (BooleanUtils.isTrue(createForm.getDefaultFlag())) {
            List<BusinessTypeEntity> businessTypeEntities = businessTypeDao.queryByDefault(enterpriseId, Boolean.TRUE, Boolean.FALSE, null);
            updateDefaultList = businessTypeEntities.stream().map(e -> {
                BusinessTypeEntity updateDefault = new BusinessTypeEntity();
                updateDefault.setBusinessTypeId(e.getBusinessTypeId());
                updateDefault.setDefaultFlag(Boolean.FALSE);
                return updateDefault;
            }).collect(Collectors.toList());
        }
        // 数据插入
        BusinessTypeEntity insertBusinessType = SmartBeanUtil.copy(createForm, BusinessTypeEntity.class);
        insertBusinessType.setEnterpriseId(enterpriseId);
        // 插入 编辑
        businessTypeManage.createBusinessType(insertBusinessType, updateDefaultList);
        return ResponseDTO.ok();
    }

    /**
     * 编辑业务类型
     *
     * @param updateForm
     * @return
     */
    public ResponseDTO<String> updateBusinessType(BusinessTypeUpdateForm updateForm, Long enterpriseId) {
        Long businessTypeId = updateForm.getBusinessTypeId();
        // 校验业务类型是否存在
        BusinessTypeEntity businessTypeDetail = businessTypeDao.selectById(businessTypeId);
        if (Objects.isNull(businessTypeDetail) || businessTypeDetail.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "业务类型不存在");
        }
        // 验证业务代码是否重复
        BusinessTypeEntity validateBusinessType = businessTypeDao.queryByBusinessTypeCode(enterpriseId, updateForm.getBusinessTypeCode(), businessTypeId, Boolean.FALSE);
        if (Objects.nonNull(validateBusinessType)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "业务代码重复");
        }
        // 验证业务名称是否重复
        BusinessTypeEntity validateBusinessName = businessTypeDao.queryByBusinessTypeName(enterpriseId, updateForm.getBusinessTypeName(), businessTypeId, Boolean.FALSE);
        if (Objects.nonNull(validateBusinessName)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "业务名称重复");
        }
        // 如果当前编辑的数据设置为默认业务类型 则将其他的默认业务类型均设置为否
        List<BusinessTypeEntity> updateDefaultList = Lists.newArrayList();
        if (BooleanUtils.isTrue(updateForm.getDefaultFlag())) {
            List<BusinessTypeEntity> businessTypeEntities = businessTypeDao.queryByDefault(enterpriseId, Boolean.TRUE, Boolean.FALSE, updateForm.getBusinessTypeId());
            updateDefaultList = businessTypeEntities.stream().map(e -> {
                BusinessTypeEntity updateDefault = new BusinessTypeEntity();
                updateDefault.setBusinessTypeId(e.getBusinessTypeId());
                updateDefault.setDefaultFlag(Boolean.FALSE);
                return updateDefault;
            }).collect(Collectors.toList());
        }
        // 数据编辑
        BusinessTypeEntity updateBusinessType = SmartBeanUtil.copy(updateForm, BusinessTypeEntity.class);
        // 编辑
        businessTypeManage.updateBusinessType(updateBusinessType, updateDefaultList);
        return ResponseDTO.ok();
    }


    /**
     * 删除业务类型
     *
     * @param businessTypeId
     * @return
     */
    public ResponseDTO<String> deleteBusinessType(Long businessTypeId) {
        // 校验业务类型是否存在
        BusinessTypeEntity businessTypeDetail = businessTypeDao.selectById(businessTypeId);
        if (Objects.isNull(businessTypeDetail) || businessTypeDetail.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "业务类型不存在");
        }
        businessTypeDao.deleteBusinessType(businessTypeId, Boolean.TRUE);
        return ResponseDTO.ok();
    }

    /**
     * 列表
     *
     * @return
     */
    public ResponseDTO<List<BusinessTypeVO>> queryList(Long enterpriseId) {
        List<BusinessTypeVO> businessTypeVOList = businessTypeDao.queryList(enterpriseId, Boolean.FALSE, Boolean.FALSE);
        businessTypeVOList.stream().forEach(e -> e.setTripTypeDesc(SmartBaseEnumUtil.getEnumDescByValue(e.getTripType(), TripTypeEnum.class)));
        return ResponseDTO.ok(businessTypeVOList);
    }

    /**
     * 根据业务类型ID列表ID获取
     *
     * @param containerTypeIdList
     * @return
     */
    public Map<Long, String> getBusinessTypeMap(Collection<Long> containerTypeIdList) {
        if (CollectionUtils.isEmpty(containerTypeIdList)) {
            return Maps.newHashMap();
        }
        List<BusinessTypeEntity> businessTypeEntityList = businessTypeDao.selectBatchIds(containerTypeIdList);
        Map<Long, String> businessTypeNameMap = businessTypeEntityList.stream().collect(Collectors.toMap(BusinessTypeEntity::getBusinessTypeId, BusinessTypeEntity::getBusinessTypeName));
        return businessTypeNameMap;
    }
}
