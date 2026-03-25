package net.lab1024.tms.admin.module.business.pay.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.flow.custom.FlowTaskHandler;
import net.lab1024.tms.admin.module.business.flow.custom.IFlowCustomTaskHandlerService;
import net.lab1024.tms.admin.module.business.order.dao.OrderDao;
import net.lab1024.tms.admin.module.business.pay.dao.PayOrderCostDao;
import net.lab1024.tms.admin.module.business.pay.domain.vo.PayOrderDetailVO;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.system.department.dao.DepartmentDao;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.module.business.order.domain.OrderEntity;
import net.lab1024.tms.common.module.business.pay.domain.PayOrderCostEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.system.department.domain.DepartmentEntity;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yandy
 * @description:
 * @date 2022/9/16 11:46 上午
 */
@FlowTaskHandler(value = "payOrderFlowSaleHandleService", desc = "付款单审批-运单销售所在部门负责人审批")
public class PayOrderFlowSaleHandleService implements IFlowCustomTaskHandlerService {
    @Autowired
    private PayOrderCostDao payOrderCostDao;
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private DepartmentDao departmentDao;

    @Override
    public List<Long> customHandler(Long initiatorId, Long enterpriserId, Object businessData) {
        PayOrderDetailVO payOrderDetailVO = (PayOrderDetailVO) businessData;
        Long payOrderId = payOrderDetailVO.getPayOrderId();

        List<PayOrderCostEntity> payOrderCostEntityList = payOrderCostDao.selectByPayOrderId(payOrderId);
        //运单信息
        List<Long> waybillIdList = payOrderCostEntityList.stream().map(PayOrderCostEntity::getWaybillId).collect(Collectors.toList());
        List<WaybillEntity> waybillEntityList = waybillDao.selectBatchIds(waybillIdList);

        List<Long> orderIdList =  waybillEntityList.stream().map(WaybillEntity::getOrderId).distinct().collect(Collectors.toList());
        List<OrderEntity> orderEntityList = orderDao.selectBatchIds(orderIdList);

        List<Long> saleIdList =  orderEntityList.stream().map(OrderEntity::getManagerId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(saleIdList)) {
            throw  new BusinessException("订单所属销售不存在，请及时维护");
        }
        List<EmployeeEntity> employeeEntityList = employeeDao.selectBatchIds(saleIdList);

        List<Long> departmentIdList =  employeeEntityList.stream().map(EmployeeEntity::getDepartmentId).distinct().collect(Collectors.toList());
        List<DepartmentEntity> departmentEntityList = departmentDao.selectBatchIds(departmentIdList);

        List<Long> departmentManagerIdList = departmentEntityList.stream().filter(e->e.getManagerId() != null).map(DepartmentEntity::getManagerId).distinct().collect(Collectors.toList());

        if (CollectionUtils.isEmpty(departmentManagerIdList)) {
            throw  new BusinessException("销售所在部门负责人不存在，请及时维护");
        }
        return Lists.newArrayList(departmentManagerIdList);
    }
}