package net.lab1024.tms.admin.module.business.receive.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderWaybillDao;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderInvoiceDao;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderMailAddressDao;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderVerificationDao;
import net.lab1024.tms.admin.module.business.receive.domain.dto.ReceiveOrderAmountSumDTO;
import net.lab1024.tms.admin.module.business.receive.domain.dto.WaybillOrderAmountSumDTO;
import net.lab1024.tms.admin.module.business.receive.domain.vo.*;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperDao;
import net.lab1024.tms.admin.module.business.waybill.domain.vo.WaybillCostDetailVO;
import net.lab1024.tms.admin.module.business.waybill.service.WaybillDetailService;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartBigDecimalUtil;
import net.lab1024.tms.common.module.business.receive.domain.ReceiveOrderInvoiceEntity;
import net.lab1024.tms.common.module.business.receive.domain.ReceiveOrderMailAddressEntity;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yandy
 * @description:
 * @date 2022/9/28 2:50 下午
 */
@Service
public class ReceiveOrderCommonService {

    @Autowired
    private WaybillDetailService waybillDetailService;
    @Autowired
    private ShipperDao shipperDao;
    @Autowired
    private ReceiveOrderWaybillDao receiveOrderWaybillDao;
    @Autowired
    private ReceiveOrderMailAddressDao receiveOrderMailAddressDao;
    @Autowired
    private ReceiveOrderInvoiceDao receiveOrderInvoiceDao;
    @Autowired
    private EmployeeDao employeeDao;

    @Resource
    private ReceiveOrderVerificationDao receiveOrderVerificationDao;

    /**
     * 设置已销金额 未销金额 地址
     *
     * @param receiveOrderList
     */
    public void buildAmount(List<ReceiveOrderVO> receiveOrderList) {
        if (CollectionUtils.isEmpty(receiveOrderList)) {
            return;
        }
        List<Long> receiveOrderIdList = receiveOrderList.stream().map(ReceiveOrderVO::getReceiveOrderId).collect(Collectors.toList());
        List<ReceiveOrderAmountSumDTO> amountSumList = receiveOrderWaybillDao.sumVerificationAmount(receiveOrderIdList);
        Map<Long, BigDecimal> amountMap = amountSumList.stream().collect(Collectors.toMap(ReceiveOrderAmountSumDTO::getReceiveOrderId, ReceiveOrderAmountSumDTO::getTotalAmount));

        List<ReceiveOrderMailAddressEntity> receiveOrderMailAddressList = receiveOrderMailAddressDao.selectByReceiveOrderIdList(receiveOrderIdList);
        Map<Long, ReceiveOrderMailAddressVO> mailAddressMap = receiveOrderMailAddressList.stream().collect(Collectors.toMap(ReceiveOrderMailAddressEntity::getReceiveOrderId, e -> SmartBeanUtil.copy(e, ReceiveOrderMailAddressVO.class)));
        receiveOrderList.forEach(item -> {
            BigDecimal verificationAmount = amountMap.getOrDefault(item.getReceiveOrderId(), BigDecimal.ZERO);
            item.setVerificationAmount(verificationAmount);
            item.setUnpaidAmount(SmartBigDecimalUtil.Amount.subtract(item.getTotalAmount(), item.getVerificationAmount()));
            item.setMailAddressVO(mailAddressMap.get(item.getReceiveOrderId()));
        });
    }

    public List<ReceiveOrderInvoiceVO> getInvoiceList(List<Long> receiveOrderIdList) {
        List<ReceiveOrderInvoiceEntity> invoiceEntityList = receiveOrderInvoiceDao.selectByReceiveOrderIdList(receiveOrderIdList);
        if (CollectionUtils.isEmpty(invoiceEntityList)) {
            return Lists.newArrayList();
        }
        List<ReceiveOrderInvoiceVO> invoiceList = SmartBeanUtil.copyList(invoiceEntityList, ReceiveOrderInvoiceVO.class);

        Set<Long> invoiceUserIdList = invoiceEntityList.stream().filter(e -> null != e.getInvoiceUserId()).map(ReceiveOrderInvoiceEntity::getInvoiceUserId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(invoiceUserIdList)) {
            return invoiceList;
        }
        List<EmployeeEntity> employeeList = employeeDao.selectBatchIds(invoiceUserIdList);
        Map<Long, String> employeeNameMap = employeeList.stream().collect(Collectors.toMap(EmployeeEntity::getEmployeeId, EmployeeEntity::getActualName));
        invoiceList.forEach(item -> {
            item.setInvoiceUserName(employeeNameMap.getOrDefault(item.getInvoiceUserId(), StringConst.EMPTY));
        });
        return invoiceList;
    }


    /**
     * 根据收款单查询运单
     *
     * @param receiveOrderId
     * @return
     */
    public List<ReceiveOrderWaybillVO> getWaybillByReceive(Long receiveOrderId) {
        List<Long> waybillIdList = receiveOrderWaybillDao.selectWaybillIdByReceiveOrderId(receiveOrderId);
        if (CollectionUtils.isEmpty(waybillIdList)) {
            return Lists.newArrayList();
        }
        List<WaybillCostDetailVO> waybillList = waybillDetailService.getWaybillCostByIdList(waybillIdList);
        List<WaybillOrderAmountSumDTO> waybillAmountList = receiveOrderWaybillDao.sumAmountByWaybillId(Arrays.asList(receiveOrderId));
        Map<Long, BigDecimal> waybillAmountMap = waybillAmountList.stream().collect(Collectors.toMap(WaybillOrderAmountSumDTO::getWaybillId, WaybillOrderAmountSumDTO::getTotalAmount));
        List<ReceiveOrderWaybillVO> waybillVOList = waybillList.stream().map(e -> {
            ReceiveOrderWaybillVO vo = SmartBeanUtil.copy(e, ReceiveOrderWaybillVO.class);
            vo.setThisReceiveAmount(waybillAmountMap.getOrDefault(e.getWaybillId(), BigDecimal.ZERO));
            return vo;
        }).collect(Collectors.toList());
        return waybillVOList;
    }

    /**
     * 生成账期
     *
     * @param checkDate
     * @param shipperId
     * @return
     */
    public LocalDate generateAccountPeriod(LocalDate checkDate, Long shipperId) {
        ShipperEntity shipperEntity = shipperDao.selectById(shipperId);
        Integer accountPeriod = shipperEntity.getAccountPeriod() != null ? shipperEntity.getAccountPeriod() : 0;
        return checkDate.plusDays(accountPeriod);
    }

}