package net.lab1024.tms.admin.module.business.pay.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.flow.custom.FlowTaskHandler;
import net.lab1024.tms.admin.module.business.flow.custom.IFlowCustomTaskHandlerService;
import net.lab1024.tms.admin.module.business.order.dao.OrderDao;
import net.lab1024.tms.admin.module.business.pay.domain.vo.PayOrderDetailVO;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.system.department.dao.DepartmentDao;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.module.business.order.domain.OrderEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.system.department.domain.DepartmentEntity;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/9/16 11:46 上午
 */
@FlowTaskHandler(value = "oilCardFlowScheduleManagerHandleService", desc = "油卡充值-运单调度所在部门负责人审批")
public class OilCardFlowScheduleManagerHandleService implements IFlowCustomTaskHandlerService {

    @Autowired
    private WaybillDao waybillDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Override
    public List<Long> customHandler(Long initiatorId, Long enterpriseId, Object businessData) {
        PayOrderDetailVO payOrderDetailVO = (PayOrderDetailVO) businessData;
        Long waybillId = payOrderDetailVO.getWaybillId();

        WaybillEntity waybillEntity = waybillDao.selectById(waybillId);

        OrderEntity orderEntity = orderDao.selectById(waybillEntity.getOrderId());
        if (orderEntity.getScheduleId() == null) {
            throw new BusinessException("订单所属调度不存在");
        }
        EmployeeEntity employeeEntity = employeeDao.selectById(orderEntity.getScheduleId());
        if (employeeEntity == null) {
            throw new BusinessException("订单调度员工信息不存在");
        }
        DepartmentEntity departmentEntity = departmentDao.selectById(employeeEntity.getDepartmentId());
        if (departmentEntity.getManagerId() == null) {
            throw new BusinessException("调度所在部门，部门负责人不存");
        }
        return Lists.newArrayList(departmentEntity.getManagerId());
    }
}