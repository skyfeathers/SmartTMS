package net.lab1024.tms.admin.module.business.shipper.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.shipper.dao.*;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperBasicDTO;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperContactDTO;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperInvoiceDTO;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperPrincipalDTO;
import net.lab1024.tms.admin.module.business.shipper.domain.form.*;
import net.lab1024.tms.admin.module.business.shipper.domain.vo.*;
import net.lab1024.tms.admin.module.business.shipper.manager.ShipperManager;
import net.lab1024.tms.admin.module.business.shipper.manager.ShipperPrincipalManager;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.business.shipper.constant.*;
import net.lab1024.tms.common.module.business.shipper.domain.*;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/7/30 14:46
 */
@Service
public class ShipperService {

    @Autowired
    private ShipperDao shipperDao;
    @Autowired
    private ShipperTrackDao shipperTrackDao;
    @Autowired
    private ShipperInvoiceDao shipperInvoiceDao;
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private ShipperManager shipperManager;
    @Autowired
    private ShipperDetailService shipperDetailService;
    @Autowired
    private ShipperTypeDao shipperTypeDao;
    @Autowired
    private ShipperPrincipalDao shipperPrincipalDao;
    @Autowired
    private ShipperPrincipalManager shipperPrincipalManager;
    @Autowired
    private ShipperDataTracerService shipperDataTracerService;
    @Autowired
    private ShipperContactDao shipperContactDao;
    @Autowired
    private DictCacheService dictCacheService;

    /**
     * 分页查询货主信息
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResult<ShipperListVO>> queryByPage(ShipperQueryForm queryDTO) {
        if (queryDTO.getDeletedFlag() == null) {
            queryDTO.setDeletedFlag(false);
        }
        Page page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<ShipperListVO> shipperList = shipperDao.queryByPage(page, queryDTO);
        this.buildShipperType(shipperList);
        this.buildTrackLast(shipperList);
        this.buildPrincipal(shipperList);
        this.buildEnterprise(shipperList);
        PageResult<ShipperListVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, shipperList);
        return ResponseDTO.ok(pageResultDTO);
    }

    /**
     * 构建查询结果的业务关系
     *
     * @param shipperList
     */
    private void buildShipperType(List<ShipperListVO> shipperList) {
        if (CollectionUtils.isEmpty(shipperList)) {
            return;
        }
        List<Long> shipperIdList = shipperList.stream().map(ShipperListVO::getShipperId).collect(Collectors.toList());
        List<ShipperTypeEntity> typeEntityList = shipperTypeDao.selectByShipperIdList(shipperIdList);
        Map<Long, List<Integer>> typeListMap = typeEntityList.stream().collect(Collectors.groupingBy(ShipperTypeEntity::getShipperId, Collectors.mapping(ShipperTypeEntity::getShipperType, Collectors.toList())));

        for (ShipperListVO listVO : shipperList) {
            listVO.setShipperTypeList(typeListMap.getOrDefault(listVO.getShipperId(), Lists.newArrayList()));
        }
    }

    /**
     * 构建跟进记录
     *
     * @param shipperListVOList
     */
    private void buildTrackLast(List<ShipperListVO> shipperListVOList) {
        if (CollectionUtils.isEmpty(shipperListVOList)) {
            return;
        }
        List<Long> shipperIdList = shipperListVOList.stream().map(ShipperListVO::getShipperId).collect(Collectors.toList());
        List<ShipperTrackVO> trackVOList = shipperTrackDao.selectByShipperIdList(shipperIdList);
        if (CollectionUtils.isEmpty(trackVOList)) {
            return;
        }
        Map<Long, List<ShipperTrackVO>> trackListMap = trackVOList.stream().collect(Collectors.groupingBy(ShipperTrackVO::getShipperId));
        for (ShipperListVO companyListVO : shipperListVOList) {
            if (!trackListMap.containsKey(companyListVO.getShipperId())) {
                continue;
            }
            List<ShipperTrackVO> shipperTrackVOList = trackListMap.get(companyListVO.getShipperId());
            shipperTrackVOList.sort((Comparator.comparing(ShipperTrackVO::getTrackTime).reversed()));
            ShipperTrackVO lastTrack = shipperTrackVOList.get(0);
            companyListVO.setLastTrackTime(lastTrack.getTrackTime());
            companyListVO.setLastTrackContent(lastTrack.getTrackContent());
        }
    }

    /**
     * 设置客服负责人信息
     *
     * @param shipperListVOList
     */
    private void buildPrincipal(List<ShipperListVO> shipperListVOList) {
        if (CollectionUtils.isEmpty(shipperListVOList)) {
            return;
        }
        List<Long> shipperIdList = shipperListVOList.stream().map(ShipperListVO::getShipperId).collect(Collectors.toList());
        List<ShipperPrincipalDTO> shipperPrincipalList = shipperPrincipalDao.selectByShipperIdList(shipperIdList);
        if (CollectionUtils.isEmpty(shipperPrincipalList)) {
            return;
        }
        Map<Long, List<ShipperPrincipalDTO>> shipperPrincipalMap = shipperPrincipalList.stream().collect(Collectors.groupingBy(ShipperPrincipalDTO::getShipperId));
        for (ShipperListVO shipperListVO : shipperListVOList) {
            Long shipperId = shipperListVO.getShipperId();
            shipperListVO.setShipperPrincipalList(shipperPrincipalMap.getOrDefault(shipperId, Lists.newArrayList()));
        }
    }

    private void buildEnterprise(List<ShipperListVO> shipperListVOList) {
        if (CollectionUtils.isEmpty(shipperListVOList)) {
            return;
        }
        List<Long> enterpriseIdList = shipperListVOList.stream().map(ShipperListVO::getEnterpriseId).collect(Collectors.toList());
        List<EnterpriseEntity> enterpriseList = enterpriseDao.selectBatchIds(enterpriseIdList);
        if (CollectionUtils.isEmpty(enterpriseList)) {
            return;
        }
        Map<Long, String> enterpriseNameMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));
        for (ShipperListVO shipper : shipperListVOList) {
            shipper.setEnterpriseName(enterpriseNameMap.getOrDefault(shipper.getEnterpriseId(), StringConst.EMPTY));
        }
    }

    /**
     * 查询所有货主
     *
     * @return
     */
    public ResponseDTO<List<ShipperBasicDTO>> queryBasicList(Long enterpriseId) {
        List<ShipperBasicDTO> basicList = shipperDetailService.shipperBasic(enterpriseId);
        return ResponseDTO.ok(basicList);
    }

    /**
     * 保存货主
     *
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> save(ShipperAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        // 同名检测
        List<ShipperBasicDTO> shipperBasicList = shipperDao.selectByName(addForm.getConsignor(), Boolean.FALSE, addForm.getEnterpriseId());
        if (CollectionUtils.isNotEmpty(shipperBasicList)) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "货主名称已存在");
        }

        ShipperEntity shipperEntity = SmartBeanUtil.copy(addForm, ShipperEntity.class);
        shipperEntity.setAuditStatus(AuditStatusEnum.WAIT_AUDIT.getValue());
        shipperEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        shipperEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        shipperManager.shipperSave(addForm, shipperEntity);
        shipperDataTracerService.saveLog(shipperEntity.getShipperId(), addForm, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 更新货主
     *
     * @param updateForm
     * @return
     */
    public ResponseDTO<String> update(ShipperUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        Long shipperId = updateForm.getShipperId();
        ShipperEntity originEntity = shipperDao.selectById(shipperId);
        if (originEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        // 同名检测
        List<ShipperBasicDTO> shipperBasicList = shipperDao.selectByNameExcludeIds(updateForm.getConsignor(), Lists.newArrayList(updateForm.getShipperId()), Boolean.FALSE, updateForm.getEnterpriseId());
        if (CollectionUtils.isNotEmpty(shipperBasicList)) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "货主名称已存在");
        }
        ShipperDetailVO beforeData = this.getDetail(shipperId).getData();
        shipperManager.shipperUpdate(updateForm);
        ShipperDetailVO afterData = this.getDetail(shipperId).getData();
        shipperDataTracerService.updateLog(beforeData, afterData, updateForm, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 删除货主
     *
     * @param shipperIdList
     * @return
     */
    public ResponseDTO<String> batchDelete(List<Long> shipperIdList, DataTracerRequestForm dataTracerRequestForm) {
        shipperManager.shipperBatchDelete(shipperIdList);
        shipperDataTracerService.deleteLog(shipperIdList, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 货主详情
     *
     * @param shipperId
     * @return
     */
    public ResponseDTO<ShipperDetailVO> getDetail(Long shipperId) {
        ShipperDetailVO detailDTO = shipperDao.detailById(shipperId);
        if (detailDTO == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        // 设置业务关系
        detailDTO.setShipperTypeList(shipperDetailService.shipperType(shipperId));
        //设置联系人信息
        detailDTO.setContactList(shipperDetailService.shipperContact(shipperId));
        //设置付款方式信息
        detailDTO.setPaymentWayList(shipperDetailService.shipperPaymentWay(shipperId));
        //开票信息
        detailDTO.setInvoiceList(shipperDetailService.shipperInvoice(shipperId));
        //邮寄地址信息
        detailDTO.setMailAddressList(shipperDetailService.shipperMailAddress(shipperId));
        // 设置客服信息
        detailDTO.setShipperPrincipalList(shipperDetailService.shipperPrincipal(shipperId));
        // 设置所属公司
        EnterpriseEntity enterpriseEntity = enterpriseDao.selectById(detailDTO.getEnterpriseId());
        detailDTO.setEnterpriseName(null != enterpriseEntity ? enterpriseEntity.getEnterpriseName() : StringConst.EMPTY);
        return ResponseDTO.ok(detailDTO);
    }

    /**
     * 获取货主基本信息（名称 状态）
     *
     * @param shipperId
     * @return
     */
    public ResponseDTO<ShipperSimpleVO> simpleDetail(Long shipperId) {
        ShipperBasicDTO shipperBasicDTO = shipperDao.selectSimpleById(shipperId);
        ShipperSimpleVO shipperSimpleVO = SmartBeanUtil.copy(shipperBasicDTO, ShipperSimpleVO.class);
        return ResponseDTO.ok(shipperSimpleVO);
    }

    /**
     * 更新业务负责人
     *
     * @param updateDTO
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> updateManager(ShipperManagerUpdateForm updateDTO, DataTracerRequestForm dataTracerRequestForm) {
        List<Long> shipperIdList = updateDTO.getShipperIdList();
        List<Long> managerIdList = updateDTO.getManagerIdList();
        // 原始manage
        List<ShipperEntity> shipperOriginEntityList = shipperDao.selectBatchIds(shipperIdList);
        List<ShipperPrincipalEntity> shipperPrincipalEntityList = Lists.newArrayList();
        for (Long shipperId : shipperIdList) {
            for (Long employeeId : managerIdList) {
                ShipperPrincipalEntity shipperPrincipalEntity = new ShipperPrincipalEntity();
                shipperPrincipalEntity.setShipperId(shipperId);
                shipperPrincipalEntity.setEmployeeId(employeeId);
                shipperPrincipalEntity.setType(PrincipalTypeEnum.MANAGER.getValue());
                shipperPrincipalEntityList.add(shipperPrincipalEntity);
            }
        }
        shipperManager.updatePrincipal(shipperPrincipalEntityList, shipperIdList, PrincipalTypeEnum.MANAGER.getValue());
        shipperDataTracerService.updateManagerLog(shipperIdList, managerIdList, shipperOriginEntityList, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 更新业务负责人
     *
     * @param updateDTO
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> updatePrincipal(ShipperPrincipalUpdateForm updateDTO, DataTracerRequestForm dataTracerRequestForm) {
        List<Long> shipperIdList = updateDTO.getShipperIdList();
        //查询老数据
        List<ShipperPrincipalDTO> originPrincipalList = shipperPrincipalManager.getBaseMapper().selectByShipperIdListAndType(shipperIdList, updateDTO.getPrincipalType());

        // 组装新的客服数据
        List<ShipperPrincipalEntity> shipperPrincipalEntityList = Lists.newArrayList();
        updateDTO.getEmployeeIdList().forEach(employeeId -> {
            shipperIdList.forEach(shipperId -> {
                ShipperPrincipalEntity entity = new ShipperPrincipalEntity();
                entity.setEmployeeId(employeeId);
                entity.setShipperId(shipperId);
                entity.setType(updateDTO.getPrincipalType());
                shipperPrincipalEntityList.add(entity);
            });
        });
        shipperManager.updatePrincipal(shipperPrincipalEntityList, shipperIdList, updateDTO.getPrincipalType());
        //查询新数据
        List<ShipperPrincipalDTO> newPrincipalList = shipperPrincipalManager.getBaseMapper().selectByShipperIdListAndType(shipperIdList, updateDTO.getPrincipalType());
        shipperDataTracerService.updatePrincipalLog(shipperIdList, updateDTO.getPrincipalType(), originPrincipalList, newPrincipalList, dataTracerRequestForm);
        return ResponseDTO.ok();
    }



    /**
     * 货主批量审核
     *
     * @param batchAuditForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> batchAudit(ShipperBatchAuditForm batchAuditForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Integer> auditStatusList = Lists.newArrayList(AuditStatusEnum.AUDIT_PASS.getValue(),
                AuditStatusEnum.REJECT.getValue());
        if (!auditStatusList.contains(batchAuditForm.getAuditStatus())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "审核状态错误");
        }

        List<Long> shipperIdList = batchAuditForm.getShipperIdList();
        List<ShipperEntity> shipperList = shipperDao.selectShipperList(shipperIdList, AuditStatusEnum.WAIT_AUDIT.getValue(), Boolean.FALSE);
        if (shipperList.size() != shipperIdList.size()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "货主数据异常，请刷新后重试");
        }
        shipperDao.batchUpdateAuditStatus(shipperIdList, batchAuditForm.getAuditStatus(), batchAuditForm.getRemark());
        shipperDataTracerService.batchAuditLog(shipperIdList, batchAuditForm.getAuditStatus(), batchAuditForm.getRemark(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 查询默认的联系人信息，不存在返回null
     *
     * @param shipperId
     * @return
     */
    public ResponseDTO<ShipperContactDTO> selectDefaultContact(Long shipperId) {
        ShipperContactEntity shipperContactEntity = shipperContactDao.selectDefault(shipperId, Boolean.TRUE);
        if (null == shipperContactEntity) {
            return ResponseDTO.ok();
        }
        return ResponseDTO.ok(SmartBeanUtil.copy(shipperContactEntity, ShipperContactDTO.class));
    }

    /**
     * 更新货主的校验状态
     *
     * @param shipperIdList
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> updateVerifyFlag(List<Long> shipperIdList, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(shipperIdList)) {
            return ResponseDTO.ok();
        }
        List<ShipperEntity> shipperList = shipperDao.selectBatchIds(shipperIdList);
        if (CollectionUtils.isEmpty(shipperList)) {
            return ResponseDTO.ok();
        }
        if (shipperIdList.size() != shipperList.size()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "货主不存在");
        }
        List<ShipperEntity> updateList = shipperIdList.stream().map(shipperId -> {
            ShipperEntity shipperEntity = new ShipperEntity();
            shipperEntity.setShipperId(shipperId);
            shipperEntity.setVerifyFlag(Boolean.TRUE);
            return shipperEntity;
        }).collect(Collectors.toList());
        shipperManager.updateBatchById(updateList);
        shipperDataTracerService.batchUpdateVerifyFlag(shipperIdList, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * @param existsForm
     * @return
     */
    public ResponseDTO<Boolean> queryByName(ShipperNameExistsForm existsForm) {
        if (null == existsForm.getShipperId()) {
            // 同名检测
            List<ShipperBasicDTO> shipperBasicList = shipperDao.selectByName(existsForm.getConsignor(), Boolean.FALSE, existsForm.getEnterpriseId());
            if (CollectionUtils.isNotEmpty(shipperBasicList)) {
                return ResponseDTO.ok(Boolean.TRUE);
            }
        }
        List<ShipperBasicDTO> shipperBasicList = shipperDao.selectByNameExcludeIds(existsForm.getConsignor(), Lists.newArrayList(existsForm.getShipperId()), Boolean.FALSE, existsForm.getEnterpriseId());
        if (CollectionUtils.isNotEmpty(shipperBasicList)) {
            return ResponseDTO.ok(Boolean.TRUE);
        }
        return ResponseDTO.ok(Boolean.FALSE);
    }

    /**
     * 根据货主ID获取业务员名称
     *
     * @param shipperIdList
     * @return
     */
    public Map<Long, List<String>> queryPrincipalByShipperIdType(Collection<Long> shipperIdList, PrincipalTypeEnum principalTypeEnum) {
        if (CollectionUtils.isEmpty(shipperIdList)) {
            return Maps.newHashMap();
        }
        List<ShipperPrincipalDTO> shipperPrincipalList = shipperPrincipalDao.selectByShipperIdListAndType(shipperIdList, principalTypeEnum.getValue());
        Map<Long, List<String>> shipperPrincipalMap = shipperPrincipalList.stream().collect(Collectors.groupingBy(ShipperPrincipalDTO::getShipperId, Collectors.mapping(ShipperPrincipalDTO::getEmployeeName, Collectors.toList())));
        return shipperPrincipalMap;
    }

    public List<ShipperExportVO> queryByExport(ShipperQueryForm queryDTO) {
        if (queryDTO.getDeletedFlag() == null) {
            queryDTO.setDeletedFlag(Boolean.FALSE);
        }
        List<ShipperListVO> shipperList = shipperDao.queryByPage(null, queryDTO);
        this.buildShipperType(shipperList);
        this.buildTrackLast(shipperList);
        this.buildPrincipal(shipperList);
        this.buildEnterprise(shipperList);
        if (CollectionUtils.isEmpty(shipperList)) {
            return Lists.newArrayList();
        }
        //根据货主id获取联系人信息
        List<Long> shipperIds = shipperList.stream().map(ShipperListVO::getShipperId).distinct().collect(Collectors.toList());
        List<ShipperContactEntity> shipperContactEntities = shipperContactDao.selectByShipperIdList(shipperIds, null, Boolean.FALSE);
        Map<Long, List<ShipperContactEntity>> listMap = shipperContactEntities.stream().collect(Collectors.groupingBy(ShipperContactEntity::getShipperId));

        List<ShipperExportVO> exportList = shipperList.stream().map(shipperVO -> {
            ShipperExportVO exportVO = SmartBeanUtil.copy(shipperVO, ShipperExportVO.class);
            //货主导出数据赋值
            return getShipperExportVO(listMap, shipperVO, exportVO);
        }).collect(Collectors.toList());
        return exportList;
    }

    private ShipperExportVO getShipperExportVO(Map<Long, List<ShipperContactEntity>> listMap, ShipperListVO shipperVO, ShipperExportVO exportVO) {
        List<Integer> shipperTypeList = shipperVO.getShipperTypeList();
        if (CollectionUtils.isNotEmpty(shipperTypeList)) {
            List<String> strings = shipperTypeList.stream().map(e -> SmartBaseEnumUtil.getEnumDescByValue(e, ShipperTypeEnum.class)).collect(Collectors.toList());
            String shipperType = StringUtils.join(strings,",");
            exportVO.setShipperTypeDesc(shipperType);

        }
        List<ShipperPrincipalDTO> shipperPrincipalList = shipperVO.getShipperPrincipalList();
        if (CollectionUtils.isNotEmpty(shipperPrincipalList)) {
            List<ShipperPrincipalDTO> customerList = shipperPrincipalList.stream().filter(e -> PrincipalTypeEnum.CUSTOMER_SERVICE.getValue().equals(e.getType())).collect(Collectors.toList());
            List<ShipperPrincipalDTO> managerList = shipperPrincipalList.stream().filter(e -> PrincipalTypeEnum.MANAGER.getValue().equals(e.getType())).collect(Collectors.toList());
            List<String> list = customerList.stream().map(ShipperPrincipalDTO::getEmployeeName).collect(Collectors.toList());
            List<String> list2 = managerList.stream().map(ShipperPrincipalDTO::getEmployeeName).collect(Collectors.toList());

            String customerServicePrincipal = StringUtils.join(list, ",");
            String managerPrincipal = StringUtils.join(list2, ",");
            exportVO.setCustomerServicePrincipal(customerServicePrincipal);
            exportVO.setManagerPrincipal(managerPrincipal);
        }
        exportVO.setArea(StringUtils.isEmpty(shipperVO.getArea())?null:dictCacheService.selectValueNameByValueCode(shipperVO.getArea()));
        exportVO.setGrade(SmartBaseEnumUtil.getEnumDescByValue(shipperVO.getGrade(), ShipperGradeEnum.class));
        exportVO.setShipperNatureDesc(SmartBaseEnumUtil.getEnumDescByValue(shipperVO.getShipperNature(),ShipperNatureEnum.class));
        exportVO.setAuditStatus(SmartBaseEnumUtil.getEnumDescByValue(shipperVO.getAuditStatus(),AuditStatusEnum.class));
        exportVO.setTagType(SmartBaseEnumUtil.getEnumDescByValue(shipperVO.getTagType(),ShipperTagEnum.class));
        //设置联系人信息
        List<ShipperContactEntity> contactEntities = listMap.get(shipperVO.getShipperId());
        if(CollectionUtils.isNotEmpty(contactEntities)){
            exportVO.setContactPhone(contactEntities.get(0).getContactPhone());
            exportVO.setContactName(contactEntities.get(0).getContactName());
        }
        return exportVO;
    }

}
