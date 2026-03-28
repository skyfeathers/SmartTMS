package net.lab1024.tms.admin.module.business.capitalflow;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.capitalflow.domain.CapitalFlowQueryForm;
import net.lab1024.tms.admin.module.business.capitalflow.domain.CapitalFlowStatisticVO;
import net.lab1024.tms.admin.module.business.capitalflow.domain.PayOrderCapitalFlowVO;
import net.lab1024.tms.admin.module.business.capitalflow.domain.ReceiveCapitalFlowVO;
import net.lab1024.tms.admin.module.business.pay.dao.PayOrderDao;
import net.lab1024.tms.admin.module.business.pay.dao.PayOrderPaymentDao;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderDao;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderVerificationDao;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperDao;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.business.pay.constant.PayOrderStatusEnum;
import net.lab1024.tms.common.module.business.pay.domain.PayOrderEntity;
import net.lab1024.tms.common.module.business.pay.domain.PayOrderPaymentEntity;
import net.lab1024.tms.common.module.business.receive.constant.CheckStatusEnum;
import net.lab1024.tms.common.module.business.receive.domain.ReceiveOrderEntity;
import net.lab1024.tms.common.module.business.receive.domain.ReceiveOrderVerificationEntity;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 资金流水
 *
 * @author lidoudou
 * @date 2022/8/20 上午11:41
 */
@Service
public class CapitalFlowService {

    @Autowired
    private ReceiveOrderVerificationDao receiveOrderVerificationDao;
    @Autowired
    private ReceiveOrderDao receiveOrderDao;
    @Autowired
    private ShipperDao shipperDao;

    @Autowired
    private PayOrderPaymentDao payOrderPaymentDao;
    @Autowired
    private PayOrderDao payOrderDao;

    /**
     * 查询应收的资金流水
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<ReceiveCapitalFlowVO>> queryReceiveByPage(CapitalFlowQueryForm queryForm) {
        queryForm.setReceiveOrderExcludeCheckStatus(CheckStatusEnum.CANCEL.getValue());
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<ReceiveOrderVerificationEntity> verificationEntityList = receiveOrderVerificationDao.queryByPage(page, queryForm);
        List<ReceiveCapitalFlowVO> flowList = SmartBeanUtil.copyList(verificationEntityList, ReceiveCapitalFlowVO.class);
        buildReceiveInfo(flowList);
        PageResult<ReceiveCapitalFlowVO> pageResult = SmartPageUtil.convert2PageResult(page, flowList);
        return ResponseDTO.ok(pageResult);
    }

    private void buildReceiveInfo(List<ReceiveCapitalFlowVO> flowList) {
        if (CollectionUtils.isEmpty(flowList)) {
            return;
        }
        Set<Long> receiveOrderIdList = flowList.stream().map(ReceiveCapitalFlowVO::getReceiveOrderId).collect(Collectors.toSet());
        List<ReceiveOrderEntity> receiveOrderList = receiveOrderDao.selectBatchIds(receiveOrderIdList);

        Map<Long, ReceiveOrderEntity> receiveMap = receiveOrderList.stream().collect(Collectors.toMap(ReceiveOrderEntity::getReceiveOrderId, Function.identity()));
        Set<Long> shipperIdList = receiveOrderList.stream().map(ReceiveOrderEntity::getShipperId).collect(Collectors.toSet());
        List<ShipperEntity> shipperEntityList = shipperDao.selectBatchIds(shipperIdList);
        Map<Long, String> consignorNameMap = shipperEntityList.stream().collect(Collectors.toMap(ShipperEntity::getShipperId, ShipperEntity::getConsignor));
        Map<Long, String> shortNameMap = shipperEntityList.stream().collect(Collectors.toMap(ShipperEntity::getShipperId, ShipperEntity::getShortName));

        flowList.forEach(item -> {
            ReceiveOrderEntity receiveOrderEntity = receiveMap.get(item.getReceiveOrderId());
            if (null != receiveOrderEntity) {
                item.setConsignor(consignorNameMap.getOrDefault(receiveOrderEntity.getShipperId(), StringConst.EMPTY));
                item.setShortName(shortNameMap.getOrDefault(receiveOrderEntity.getShipperId(), StringConst.EMPTY));
                item.setReceiveOrderNumber(receiveOrderEntity.getReceiveOrderNumber());
            }
        });
    }

    /**
     * 查询应付的资金流水
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<PayOrderCapitalFlowVO>> queryPayByPage(CapitalFlowQueryForm queryForm) {
        queryForm.setPayOrderExcludeAuditStatus(FlowAuditStatusEnum.REJECT.getValue());
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<PayOrderEntity> payOrderEntityList = payOrderDao.queryCapitalFlowByPage(page, queryForm);
        List<PayOrderCapitalFlowVO> flowList = SmartBeanUtil.copyList(payOrderEntityList, PayOrderCapitalFlowVO.class);
        buildPayInfo(flowList);
        PageResult<PayOrderCapitalFlowVO> pageResult = SmartPageUtil.convert2PageResult(page, flowList);
        return ResponseDTO.ok(pageResult);
    }

    private void buildPayInfo(List<PayOrderCapitalFlowVO> flowList) {
        if (CollectionUtils.isEmpty(flowList)) {
            return;
        }
        Set<Long> payOrderIdList = flowList.stream().map(PayOrderCapitalFlowVO::getPayOrderId).collect(Collectors.toSet());
        List<PayOrderEntity> payOrderList = payOrderDao.selectBatchIds(payOrderIdList);
        Map<Long, PayOrderEntity> payOrderMap = payOrderList.stream().collect(Collectors.toMap(PayOrderEntity::getPayOrderId, Function.identity()));

        for (PayOrderCapitalFlowVO item : flowList) {
            PayOrderEntity payOrderEntity = payOrderMap.get(item.getPayOrderId());
            if (null != payOrderEntity) {
                item.setPayOrderNumber(payOrderEntity.getPayOrderNumber());
            }
        }
    }

    /**
     * 统计流水总金额
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<CapitalFlowStatisticVO> statistic(CapitalFlowQueryForm queryForm) {
        queryForm.setPayOrderExcludeAuditStatus(PayOrderStatusEnum.CANCEL.getValue());
        CapitalFlowStatisticVO statisticVO = new CapitalFlowStatisticVO();
        statisticVO.setPayAmount(payOrderPaymentDao.sumReceiveAmount(queryForm));
        statisticVO.setReceiveAmount(receiveOrderVerificationDao.sumReceiveAmount(queryForm));
        return ResponseDTO.ok(statisticVO);
    }
}
