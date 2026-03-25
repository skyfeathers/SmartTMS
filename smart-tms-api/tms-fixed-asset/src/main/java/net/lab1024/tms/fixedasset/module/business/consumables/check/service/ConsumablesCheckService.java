package net.lab1024.tms.fixedasset.module.business.consumables.check.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.oa.enterprise.dao.CommonEnterpriseDao;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.system.employee.CommonEmployeeDao;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.check.constants.ConsumablesCheckStatusEnum;
import net.lab1024.tms.fixedasset.module.business.consumables.check.dao.ConsumablesCheckEmployeeDao;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.entity.ConsumablesCheckEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.entity.ConsumablesCheckItemEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.form.*;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.vo.ConsumablesCheckEmployeeVO;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.vo.ConsumablesCheckItemVO;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.vo.ConsumablesCheckVO;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.vo.ConsumablesConsumablesCheckDetailVO;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.dao.ConsumablesDao;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.dao.ConsumablesStockDao;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.entity.ConsumablesEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.vo.ConsumablesStockStatisticVO;
import net.lab1024.tms.fixedasset.module.business.location.LocationDao;
import net.lab1024.tms.fixedasset.module.business.location.domain.LocationEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 易耗品采购
 *
 * @author lidoudou
 * @date 2023/3/20 上午9:14
 */
@Service
public class ConsumablesCheckService {

    @Autowired
    private ConsumablesCheckManager consumablesCheckManager;
    @Autowired
    private ConsumablesCheckItemManager consumablesCheckItemManager;
    @Autowired
    private ConsumablesCheckEmployeeDao consumablesCheckEmployeeDao;
    @Autowired
    private LocationDao locationDao;
    @Autowired
    private CommonEmployeeDao commonEmployeeDao;
    @Autowired
    private ConsumablesDao consumablesDao;
    @Autowired
    private ConsumablesCheckDataTracerService consumablesCheckDataTracerService;
    @Autowired
    private CommonEnterpriseDao commonEnterpriseDao;
    @Autowired
    private ConsumablesStockDao consumablesStockDao;

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public PageResult<ConsumablesCheckVO> queryPage(ConsumablesCheckQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<ConsumablesCheckEntity> consumablesCheckEntityList = consumablesCheckManager.getBaseMapper().queryPage(page, queryForm);
        List<ConsumablesCheckVO> checkList = SmartBeanUtil.copyList(consumablesCheckEntityList, ConsumablesCheckVO.class);
        buildList(checkList);
        PageResult<ConsumablesCheckVO> pageResult = SmartPageUtil.convert2PageResult(page, checkList);
        return pageResult;
    }

    private void buildList(List<ConsumablesCheckVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Set<Long> locationIdSet = list.stream().map(ConsumablesCheckVO::getLocationId).collect(Collectors.toSet());
        List<LocationEntity> locationList = locationDao.selectBatchIds(locationIdSet);
        Map<Long, String> locationNameMap = locationList.stream().collect(Collectors.toMap(LocationEntity::getLocationId, LocationEntity::getLocationName));

        Set<Long> enterpriseIdSet = list.stream().map(ConsumablesCheckVO::getEnterpriseId).collect(Collectors.toSet());
        List<EnterpriseEntity> enterpriseEntityList = commonEnterpriseDao.selectBatchIds(enterpriseIdSet);
        Map<Long, String> enterpriseNameMap = enterpriseEntityList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));

        list.forEach(item -> {
            item.setLocationName(locationNameMap.getOrDefault(item.getLocationId(), StringConst.EMPTY));
            item.setEnterpriseName(enterpriseNameMap.getOrDefault(item.getEnterpriseId(), StringConst.EMPTY));
        });
    }

    /**
     * 添加
     *
     * @param dataTracerRequestForm
     * @param addForm
     */
    public ResponseDTO<String> add(ConsumablesCheckAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        LocationEntity locationEntity = locationDao.selectById(addForm.getLocationId());
        if (null == locationEntity || locationEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("盘点位置不存在");
        }
        List<EmployeeEntity> employeeEntityList = commonEmployeeDao.selectBatchIds(addForm.getEmployeeIdList());
        if (employeeEntityList.size() != addForm.getEmployeeIdList().size()) {
            return ResponseDTO.userErrorParam("盘点人员不存在");
        }
        // 根据盘点位置查询易耗品
        List<ConsumablesEntity> consumablesList = consumablesDao.selectByEnterpriseLocationId(addForm.getEnterpriseId(), addForm.getLocationId());
        if (CollectionUtils.isEmpty(consumablesList)) {
            return ResponseDTO.userErrorParam("该位置没有待盘点易耗品，请重新选择盘点位置");
        }
        List<Long> consumablesIdList = consumablesList.stream().map(ConsumablesEntity::getConsumablesId).collect(Collectors.toList());

        ConsumablesCheckEntity consumablesCheckEntity = SmartBeanUtil.copy(addForm, ConsumablesCheckEntity.class);
        consumablesCheckEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        consumablesCheckEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        consumablesCheckManager.save(consumablesCheckEntity, addForm.getEmployeeIdList(), consumablesIdList);
        ConsumablesConsumablesCheckDetailVO consumablesCheckDetailVO = getDetail(consumablesCheckEntity.getCheckId());
        consumablesCheckDataTracerService.saveLog(addForm, consumablesCheckDetailVO, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 获取盘点单详情
     *
     * @param checkId
     * @return
     */
    public ConsumablesConsumablesCheckDetailVO getDetail(Long checkId) {
        ConsumablesCheckEntity consumablesCheckEntity = consumablesCheckManager.getBaseMapper().selectById(checkId);
        if (null == consumablesCheckEntity) {
            return null;
        }
        ConsumablesConsumablesCheckDetailVO consumablesCheckDetailVO = SmartBeanUtil.copy(consumablesCheckEntity, ConsumablesConsumablesCheckDetailVO.class);
        buildList(Arrays.asList(consumablesCheckDetailVO));
        // 设置盘点清单
        queryConsumablesListByCheckId(consumablesCheckDetailVO);
        // 设置盘点人
        buildEmployeeList(consumablesCheckDetailVO);
        return consumablesCheckDetailVO;
    }

    private void queryConsumablesListByCheckId(ConsumablesConsumablesCheckDetailVO consumablesCheckDetailVO) {
        Long checkId = consumablesCheckDetailVO.getCheckId();
        List<ConsumablesCheckItemEntity> consumablesCheckItemEntityList = consumablesCheckItemManager.getBaseMapper().queryByCheckId(checkId);

        // 设置盘点状态的数量
        Map<Integer, Long> checkItemStatusMap = consumablesCheckItemEntityList.stream().collect(Collectors.groupingBy(ConsumablesCheckItemEntity::getStatus, Collectors.counting()));
        consumablesCheckDetailVO.setProfitCount(checkItemStatusMap.getOrDefault(ConsumablesCheckStatusEnum.PROFIT.getValue(), NumberConst.DEFAULT_PARENT_ID).intValue());
        consumablesCheckDetailVO.setLossCount(checkItemStatusMap.getOrDefault(ConsumablesCheckStatusEnum.LOSS.getValue(), NumberConst.DEFAULT_PARENT_ID).intValue());
        consumablesCheckDetailVO.setNotCheckCount(checkItemStatusMap.getOrDefault(ConsumablesCheckStatusEnum.NOT_CHECK.getValue(), NumberConst.DEFAULT_PARENT_ID).intValue());

        List<Long> consumablesIdList = consumablesCheckItemEntityList.stream().map(ConsumablesCheckItemEntity::getConsumablesId).collect(Collectors.toList());
        List<ConsumablesEntity> consumablesEntityList = consumablesDao.selectBatchIds(consumablesIdList);
        Map<Long, ConsumablesEntity> consumablesMap = consumablesEntityList.stream().collect(Collectors.toMap(ConsumablesEntity::getConsumablesId, Function.identity()));

        List<ConsumablesStockStatisticVO> stockStatisticVOList = consumablesStockDao.sumByConsumablesIdListAndLocationId(consumablesIdList, consumablesCheckDetailVO.getLocationId());
        Map<Long, Integer> stockCountMap = stockStatisticVOList.stream().collect(Collectors.toMap(ConsumablesStockStatisticVO::getConsumablesId, ConsumablesStockStatisticVO::getStockCount));

        Set<Long> employeeIdSet = consumablesCheckItemEntityList.stream().filter(e -> null != e.getEmployeeId()).map(ConsumablesCheckItemEntity::getEmployeeId).collect(Collectors.toSet());
        Map<Long, String> employeeNameMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(employeeIdSet)) {
            List<EmployeeEntity> employeeEntityList = commonEmployeeDao.selectBatchIds(employeeIdSet);
            employeeNameMap = employeeEntityList.stream().collect(Collectors.toMap(EmployeeEntity::getEmployeeId, EmployeeEntity::getActualName));
        }

        List<ConsumablesCheckItemVO> checkConsumablesList = Lists.newArrayList();
        for (ConsumablesCheckItemEntity consumablesCheckItemEntity : consumablesCheckItemEntityList) {
            Long consumablesId = consumablesCheckItemEntity.getConsumablesId();
            ConsumablesEntity consumablesEntity = consumablesMap.get(consumablesId);
            ConsumablesCheckItemVO consumablesCheckItemVO = SmartBeanUtil.copy(consumablesEntity, ConsumablesCheckItemVO.class);
            consumablesCheckItemVO.setItemId(consumablesCheckItemEntity.getItemId());
            consumablesCheckItemVO.setStatus(consumablesCheckItemEntity.getStatus());
            consumablesCheckItemVO.setEmployeeName(employeeNameMap.getOrDefault(consumablesCheckItemEntity.getEmployeeId(), StringConst.EMPTY));
            consumablesCheckItemVO.setCheckTime(consumablesCheckItemEntity.getCheckTime());
            consumablesCheckItemVO.setCount(consumablesCheckItemEntity.getCount());
            consumablesCheckItemVO.setRemark(consumablesCheckItemEntity.getRemark());

            consumablesCheckItemVO.setStockCount(stockCountMap.getOrDefault(consumablesId, NumberConst.ZERO));
            checkConsumablesList.add(consumablesCheckItemVO);
        }
        consumablesCheckDetailVO.setItemList(checkConsumablesList);
    }

    private void buildEmployeeList(ConsumablesConsumablesCheckDetailVO consumablesCheckDetailVO) {
        List<Long> employeeIdList = consumablesCheckEmployeeDao.queryByCheckId(consumablesCheckDetailVO.getCheckId());
        if (CollectionUtils.isEmpty(employeeIdList)) {
            return;
        }
        List<EmployeeEntity> employeeList = commonEmployeeDao.selectBatchIds(employeeIdList);
        consumablesCheckDetailVO.setEmployeeList(SmartBeanUtil.copyList(employeeList, ConsumablesCheckEmployeeVO.class));
    }

    /**
     * 盘点
     *
     * @param consumablesCheckForm
     * @return
     */
    public ResponseDTO<String> check(ConsumablesCheckForm consumablesCheckForm, DataTracerRequestForm dataTracerRequestForm) {
        ConsumablesCheckEntity consumablesCheckEntity = consumablesCheckManager.getBaseMapper().selectById(consumablesCheckForm.getCheckId());
        if (null == consumablesCheckEntity) {
            return ResponseDTO.userErrorParam("盘点单不存在");
        }
        if (consumablesCheckEntity.getCompleteFlag()) {
            return ResponseDTO.userErrorParam("已完成盘点，不能重复操作");
        }
        List<Long> checkEmployeeIdList = consumablesCheckEmployeeDao.queryByCheckId(consumablesCheckForm.getCheckId());
        if (!checkEmployeeIdList.contains(dataTracerRequestForm.getOperatorId())) {
            return ResponseDTO.userErrorParam("您非该单盘点人");
        }

        List<ConsumablesCheckItemForm> checkConsumablesList = consumablesCheckForm.getItemList();
        List<ConsumablesCheckItemEntity> checkItemList = checkConsumablesList.stream().map(consumables -> {
            ConsumablesCheckItemEntity consumablesCheckItemEntity = new ConsumablesCheckItemEntity();
            consumablesCheckItemEntity.setItemId(consumables.getItemId());
            consumablesCheckItemEntity.setEmployeeId(dataTracerRequestForm.getOperatorId());
            consumablesCheckItemEntity.setRemark(consumables.getRemark());
            consumablesCheckItemEntity.setStatus(consumables.getStatus());
            consumablesCheckItemEntity.setCount(consumables.getCount());
            consumablesCheckItemEntity.setCheckTime(LocalDateTime.now());
            return consumablesCheckItemEntity;
        }).collect(Collectors.toList());
        ConsumablesConsumablesCheckDetailVO beforeData = getDetail(consumablesCheckForm.getCheckId());
        consumablesCheckItemManager.updateBatchById(checkItemList);
        ConsumablesConsumablesCheckDetailVO afterData = getDetail(consumablesCheckForm.getCheckId());
        consumablesCheckDataTracerService.updateCheckLog(beforeData, afterData, consumablesCheckForm, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 完成盘点单
     *
     * @param completeForm
     * @return
     */
    public ResponseDTO<String> completeCheck(ConsumablesCheckCompleteForm completeForm, DataTracerRequestForm dataTracerRequestForm) {
        ConsumablesCheckEntity consumablesCheckEntity = consumablesCheckManager.getBaseMapper().selectById(completeForm.getCheckId());
        if (null == consumablesCheckEntity) {
            return ResponseDTO.userErrorParam("盘点单不存在");
        }
        ConsumablesCheckEntity updateEntity = new ConsumablesCheckEntity();
        updateEntity.setCheckId(completeForm.getCheckId());
        updateEntity.setLocationId(consumablesCheckEntity.getLocationId());
        updateEntity.setCompleteFlag(Boolean.TRUE);
        updateEntity.setCompleteTime(completeForm.getCompleteTime());
        consumablesCheckManager.completeCheck(updateEntity, consumablesCheckEntity);
        consumablesCheckDataTracerService.completeLog(completeForm, dataTracerRequestForm);
        return ResponseDTO.ok();
    }
}
