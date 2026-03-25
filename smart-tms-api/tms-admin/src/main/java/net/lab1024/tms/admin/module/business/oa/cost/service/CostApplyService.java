package net.lab1024.tms.admin.module.business.oa.cost.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.oa.cost.constant.CostApplyStatusEnum;
import net.lab1024.tms.admin.module.business.oa.cost.dao.CostApplyDao;
import net.lab1024.tms.admin.module.business.oa.cost.dao.CostApplyItemDao;
import net.lab1024.tms.admin.module.business.oa.cost.domain.entity.CostApplyEntity;
import net.lab1024.tms.admin.module.business.oa.cost.domain.entity.CostApplyItemEntity;
import net.lab1024.tms.admin.module.business.oa.cost.domain.form.CostApplyAddForm;
import net.lab1024.tms.admin.module.business.oa.cost.domain.form.CostApplyItemAddForm;
import net.lab1024.tms.admin.module.business.oa.cost.domain.form.CostApplyQueryForm;
import net.lab1024.tms.admin.module.business.oa.cost.domain.vo.CostApplyDetailVO;
import net.lab1024.tms.admin.module.business.oa.cost.domain.vo.CostApplyItemVO;
import net.lab1024.tms.admin.module.business.oa.cost.domain.vo.CostApplyVO;
import net.lab1024.tms.admin.module.business.oa.cost.manager.CostApplyManager;
import net.lab1024.tms.admin.module.system.department.dao.DepartmentDao;
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
import net.lab1024.tms.common.module.system.department.domain.DepartmentEntity;
import net.lab1024.tms.common.module.system.employee.CommonEmployeeDao;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 费用申请
 *
 * @author lidoudou
 * @date 2023/3/29 下午4:44
 */
@Service
public class CostApplyService {

    @Autowired
    private CostApplyDao costApplyDao;
    @Autowired
    private SerialNumberService serialNumberService;
    @Autowired
    private CommonEmployeeDao commonEmployeeDao;
    @Autowired
    private CostApplyDataTracerService costApplyDataTracerService;
    @Autowired
    private CostApplyManager costApplyManager;
    @Autowired
    private CommonEnterpriseDao commonEnterpriseDao;
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private CostApplyItemDao costApplyItemDao;

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public PageResult<CostApplyVO> queryPage(CostApplyQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<CostApplyEntity> entityList = costApplyDao.queryPage(page, queryForm);
        List<CostApplyVO> list = SmartBeanUtil.copyList(entityList, CostApplyVO.class);
        buildList(list);
        PageResult<CostApplyVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return pageResult;
    }

    private void buildList(List<CostApplyVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Set<Long> applyUserIdSet = list.stream().map(CostApplyVO::getApplyUserId).collect(Collectors.toSet());
        List<EmployeeEntity> employeeList = commonEmployeeDao.selectBatchIds(applyUserIdSet);
        Map<Long, String> employeeNameMap = employeeList.stream().collect(Collectors.toMap(EmployeeEntity::getEmployeeId, EmployeeEntity::getActualName));


        Set<Long> enterpriseIdSet = list.stream().map(CostApplyVO::getEnterpriseId).collect(Collectors.toSet());
        List<EnterpriseEntity> enterpriseList = commonEnterpriseDao.selectBatchIds(enterpriseIdSet);
        Map<Long, String> enterpriseNameMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));

        Set<Long> departmentIdSet = list.stream().map(CostApplyVO::getDepartmentId).collect(Collectors.toSet());
        List<DepartmentEntity> departmentList = departmentDao.selectBatchIds(departmentIdSet);
        Map<Long, String> departmentNameMap = departmentList.stream().collect(Collectors.toMap(DepartmentEntity::getDepartmentId, DepartmentEntity::getName));

        for (CostApplyVO costApplyVO : list) {
            costApplyVO.setApplyUserName(employeeNameMap.getOrDefault(costApplyVO.getApplyUserId(), StringConst.EMPTY));
            costApplyVO.setEnterpriseName(enterpriseNameMap.getOrDefault(costApplyVO.getEnterpriseId(), StringConst.EMPTY));
            costApplyVO.setDepartmentName(departmentNameMap.getOrDefault(costApplyVO.getDepartmentId(), StringConst.EMPTY));
        }
    }

    /**
     * 添加
     */
    public ResponseDTO<String> add(CostApplyAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        CostApplyEntity applyEntity = SmartBeanUtil.copy(addForm, CostApplyEntity.class);
        applyEntity.setStatus(CostApplyStatusEnum.AUDIT.getValue());

        String applyNo = serialNumberService.generate(SerialNumberIdEnum.OA_COST_APPLY);
        applyEntity.setApplyNo(applyNo);
        applyEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        applyEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        applyEntity.setTotalAmount(addForm.getItemList().stream().map(CostApplyItemAddForm::getApplyAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        costApplyManager.save(applyEntity, addForm.getItemList());

        CostApplyDetailVO costApplyDetailVO = getDetail(applyEntity.getApplyId());
        costApplyDataTracerService.saveLog(addForm, costApplyDetailVO, dataTracerRequestForm);
        return ResponseDTO.ok();
    }


    /**
     * 批量审核
     *
     * @return
     */
    public ResponseDTO<String> batchAudit(List<Long> applyIdList, CostApplyStatusEnum status, DataTracerRequestForm dataTracerRequestForm) {
        List<CostApplyEntity> applyEntityList = costApplyDao.selectBatchIds(applyIdList);
        List<Long> filterIdList = applyEntityList.stream()
                .filter(e -> CostApplyStatusEnum.AUDIT.getValue().equals(e.getStatus()))
                .map(CostApplyEntity::getApplyId)
                .collect(Collectors.toList());

        if (filterIdList.size() > 0) {
            costApplyDao.batchUpdateStatus(filterIdList, status.getValue());
            costApplyDataTracerService.batchAuditLog(filterIdList, status, dataTracerRequestForm);
        }
        return ResponseDTO.ok();
    }

    /**
     * 获取详情
     *
     * @param applyId
     * @return
     */
    public CostApplyDetailVO getDetail(Long applyId) {
        CostApplyEntity applyEntity = costApplyDao.selectById(applyId);
        if (null == applyEntity) {
            return null;
        }
        CostApplyDetailVO detailVO = SmartBeanUtil.copy(applyEntity, CostApplyDetailVO.class);
        buildList(Arrays.asList(detailVO));
        List<CostApplyItemEntity> costItemList = costApplyItemDao.selectByApplyIdList(Arrays.asList(applyId));
        detailVO.setItemList(SmartBeanUtil.copyList(costItemList, CostApplyItemVO.class));
        return detailVO;
    }
}
