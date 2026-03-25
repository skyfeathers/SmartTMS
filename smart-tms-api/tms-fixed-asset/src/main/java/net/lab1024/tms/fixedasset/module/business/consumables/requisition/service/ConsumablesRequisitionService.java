package net.lab1024.tms.fixedasset.module.business.consumables.requisition.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.oa.enterprise.dao.CommonEnterpriseDao;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.tms.common.module.support.serialnumber.service.SerialNumberService;
import net.lab1024.tms.common.module.system.department.CommonDepartmentDao;
import net.lab1024.tms.common.module.system.department.domain.DepartmentEntity;
import net.lab1024.tms.common.module.system.employee.CommonEmployeeDao;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import net.lab1024.tms.fixedasset.common.constants.CommonConst;
import net.lab1024.tms.fixedasset.module.business.consumables.requisition.dao.ConsumablesRequisitionItemDao;
import net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.entity.ConsumablesRequisitionEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.entity.ConsumablesRequisitionItemEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.form.ConsumablesRequisitionAddForm;
import net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.form.ConsumablesRequisitionItemAddForm;
import net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.form.ConsumablesRequisitionQueryForm;
import net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.vo.ConsumablesRequisitionItemVO;
import net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.vo.ConsumablesRequisitionVO;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.dao.ConsumablesDao;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.dao.ConsumablesStockDao;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.entity.ConsumablesEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.vo.ConsumablesStockStatisticVO;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.vo.ConsumablesVO;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.service.ConsumablesService;
import net.lab1024.tms.fixedasset.module.business.location.LocationDao;
import net.lab1024.tms.fixedasset.module.business.location.domain.LocationEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 领用Service
 *
 * @author lidoudou
 * @date 2023/3/21 上午9:37
 */
@Service
public class ConsumablesRequisitionService {

    @Autowired
    private ConsumablesRequisitionItemDao requisitionItemDao;
    @Autowired
    private ConsumablesRequisitionManager consumablesRequisitionManager;
    @Autowired
    private SerialNumberService serialNumberService;
    @Autowired
    private CommonEnterpriseDao commonEnterpriseDao;
    @Autowired
    private CommonDepartmentDao departmentDao;
    @Autowired
    private CommonEmployeeDao employeeDao;
    @Autowired
    private ConsumablesRequisitionDataTracerService consumablesRequisitionDataTracerService;
    @Autowired
    private ConsumablesService consumablesService;
    @Autowired
    private ConsumablesDao consumablesDao;
    @Autowired
    private LocationDao locationDao;
    @Autowired
    private ConsumablesStockDao consumablesStockDao;

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public PageResult<ConsumablesRequisitionVO> queryPage(ConsumablesRequisitionQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<ConsumablesRequisitionEntity> requisitionList = consumablesRequisitionManager.getBaseMapper().queryPage(page, queryForm);
        List<ConsumablesRequisitionVO> list = SmartBeanUtil.copyList(requisitionList, ConsumablesRequisitionVO.class);
        buildList(list);
        PageResult<ConsumablesRequisitionVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return pageResult;
    }

    private void buildList(List<ConsumablesRequisitionVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<Long> requisitionIdList = list.stream().map(ConsumablesRequisitionVO::getRequisitionId).collect(Collectors.toList());
        List<ConsumablesRequisitionItemEntity> itemEntityList = requisitionItemDao.queryByRequisitionIdList(requisitionIdList);
        Map<Long, List<ConsumablesRequisitionItemEntity>> requisitionConsumablesMap = itemEntityList.stream().collect(Collectors.groupingBy(ConsumablesRequisitionItemEntity::getRequisitionId));

        List<Long> assetIdList = itemEntityList.stream().map(ConsumablesRequisitionItemEntity::getConsumablesId).collect(Collectors.toList());
        List<ConsumablesVO> consumablesStockList = consumablesService.queryDetailByIdList(assetIdList);
        Map<Long, ConsumablesVO> consumablesStockMap = consumablesStockList.stream().collect(Collectors.toMap(ConsumablesVO::getConsumablesId, Function.identity()));

        // 所属公司
        Set<Long> enterpriseIdSet = list.stream().map(ConsumablesRequisitionVO::getEnterpriseId).collect(Collectors.toSet());
        List<EnterpriseEntity> enterpriseList = commonEnterpriseDao.selectBatchIds(enterpriseIdSet);
        Map<Long, String> enterpriseNameMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));

        // 使用部门
        Set<Long> departmentIdSet = list.stream().map(ConsumablesRequisitionVO::getDepartmentId).collect(Collectors.toSet());
        List<DepartmentEntity> departmentList = departmentDao.selectBatchIds(departmentIdSet);
        Map<Long, String> departmentNameMap = departmentList.stream().collect(Collectors.toMap(DepartmentEntity::getDepartmentId, DepartmentEntity::getName));

        // 使用人
        Set<Long> employeeIdSet = list.stream().map(ConsumablesRequisitionVO::getUserId).collect(Collectors.toSet());
        List<EmployeeEntity> employeeList = employeeDao.selectBatchIds(employeeIdSet);
        Map<Long, String> employeeNameMap = employeeList.stream().collect(Collectors.toMap(EmployeeEntity::getEmployeeId, EmployeeEntity::getActualName));

        // 所属位置
        Set<Long> locationIdSet = list.stream().map(ConsumablesRequisitionVO::getLocationId).collect(Collectors.toSet());
        List<LocationEntity> locationList = locationDao.selectBatchIds(locationIdSet);
        Map<Long, String> locationNameMap = locationList.stream().collect(Collectors.toMap(LocationEntity::getLocationId, LocationEntity::getLocationName));


        for (ConsumablesRequisitionVO requisitionVO : list) {
            requisitionVO.setEnterpriseName(enterpriseNameMap.getOrDefault(requisitionVO.getEnterpriseId(), StringConst.EMPTY));
            requisitionVO.setDepartmentName(departmentNameMap.getOrDefault(requisitionVO.getDepartmentId(), StringConst.EMPTY));
            requisitionVO.setUserName(employeeNameMap.getOrDefault(requisitionVO.getUserId(), StringConst.EMPTY));
            requisitionVO.setLocationName(locationNameMap.getOrDefault(requisitionVO.getLocationId(), StringConst.EMPTY));
            // 设置资产列表
            List<ConsumablesRequisitionItemEntity> requisitionConsumablesList = requisitionConsumablesMap.getOrDefault(requisitionVO.getRequisitionId(), CommonConst.EMPTY_LIST);

            List<ConsumablesRequisitionItemVO> itemList = Lists.newArrayList();
            for (ConsumablesRequisitionItemEntity consumablesRequisitionItem : requisitionConsumablesList) {
                ConsumablesVO consumablesVO = consumablesStockMap.get(consumablesRequisitionItem.getConsumablesId());
                if (null == consumablesVO) {
                    continue;
                }
                ConsumablesRequisitionItemVO itemVO = SmartBeanUtil.copy(consumablesVO, ConsumablesRequisitionItemVO.class);
                itemVO.setCount(consumablesRequisitionItem.getCount());
                itemList.add(itemVO);
            }
            requisitionVO.setItemList(itemList);
        }
    }

    /**
     * 添加领用
     *
     * @param dataTracerRequestForm
     * @param addForm
     */
    public ResponseDTO<String> addRequisition(ConsumablesRequisitionAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        List<ConsumablesRequisitionItemAddForm> consumablesStockFormList = addForm.getItemList();
        List<Long> consumablesIdList = consumablesStockFormList.stream().map(ConsumablesRequisitionItemAddForm::getConsumablesId).collect(Collectors.toList());
        List<ConsumablesEntity> consumablesEntityList = consumablesDao.selectByIdList(consumablesIdList, Boolean.FALSE);
        if (consumablesEntityList.size() != consumablesEntityList.size()) {
            return ResponseDTO.userErrorParam("资产不存在，请刷新后重试～");
        }
        List<ConsumablesStockStatisticVO> stockStatisticList = consumablesStockDao.sumByConsumablesIdListAndLocationId(consumablesIdList, addForm.getLocationId());
        if (CollectionUtils.isEmpty(stockStatisticList)) {
            return ResponseDTO.userErrorParam("领用耗材库存不足");
        }

        Map<Long, String> consumablesNameMap = consumablesEntityList.stream().collect(Collectors.toMap(ConsumablesEntity::getConsumablesId, ConsumablesEntity::getConsumablesName));
        Map<Long, Integer> consumablesRequisitionCountMap = consumablesStockFormList.stream().collect(Collectors.toMap(ConsumablesRequisitionItemAddForm::getConsumablesId, ConsumablesRequisitionItemAddForm::getCount));
        for (ConsumablesStockStatisticVO consumables : stockStatisticList) {
            Integer count = consumablesRequisitionCountMap.getOrDefault(consumables.getConsumablesId(), NumberConst.ZERO);
            if (count > consumables.getStockCount()) {
                return ResponseDTO.userErrorParam("【" + consumablesNameMap.get(consumables.getConsumablesId()) + "】库存数量不足");
            }
        }

        ConsumablesRequisitionEntity requisitionEntity = SmartBeanUtil.copy(addForm, ConsumablesRequisitionEntity.class);
        requisitionEntity.setRequisitionNo(serialNumberService.generate(SerialNumberIdEnum.ASSET_CONSUMABLE_REQUISITION));
        requisitionEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        requisitionEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        consumablesRequisitionManager.saveRequisition(requisitionEntity, addForm.getItemList());
        ConsumablesRequisitionVO consumablesRequisitionVO = getDetail(requisitionEntity.getRequisitionId());
        consumablesRequisitionDataTracerService.saveLog(addForm, consumablesRequisitionVO, dataTracerRequestForm);
//        consumablesStockDataTracerService.applyRequisitionLog(requisitionEntity.getRequisitionNo(), addForm.getConsumablesList(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    public ConsumablesRequisitionVO getDetail(Long requisitionId) {
        ConsumablesRequisitionEntity consumablesRequisitionEntity = consumablesRequisitionManager.getBaseMapper().selectById(requisitionId);
        if (null == consumablesRequisitionEntity) {
            return null;
        }
        ConsumablesRequisitionVO consumablesRequisitionVO = SmartBeanUtil.copy(consumablesRequisitionEntity, ConsumablesRequisitionVO.class);
        buildList(Arrays.asList(consumablesRequisitionVO));
        return consumablesRequisitionVO;
    }
}
