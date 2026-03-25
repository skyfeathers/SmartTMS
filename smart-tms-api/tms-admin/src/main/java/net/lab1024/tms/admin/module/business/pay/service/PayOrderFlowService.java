package net.lab1024.tms.admin.module.business.pay.service;

import net.lab1024.tms.admin.module.business.flow.bussiness.BaseFlowBusinessHandleService;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessCancelBO;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessEndBO;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessExtendBO;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessStartBO;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTypeEnum;
import net.lab1024.tms.admin.module.business.pay.dao.PayOrderDao;
import net.lab1024.tms.admin.module.business.pay.domain.vo.PayOrderDetailVO;
import net.lab1024.tms.admin.module.business.pay.manager.PayOrderManager;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBigDecimalUtil;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.business.pay.domain.PayOrderEntity;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author yandy
 * @description:
 * @date 2022/7/25 3:57 下午
 */
@Service
public class  PayOrderFlowService extends BaseFlowBusinessHandleService {

    @Autowired
    private PayOrderDetailService payOrderDetailService;

    @Autowired
    private PayOrderDao payOrderDao;

    @Autowired
    private PayOrderManager payOrderManager;

    @PostConstruct
    public void init() {
        super.register(FlowTypeEnum.PAY_AUDIT);
    }

    /**
     * 业务数据获取
     * 一般为业务表单的详情数据
     *
     * @param businessId
     * @return
     */
    @Override
    public PayOrderDetailVO getBusinessData(Long businessId, String businessCode) {
        return payOrderDetailService.getDetail(businessId);
    }

    /**
     * 扩展字段
     * 一般用于审批列表的查询 和展示
     *
     * @param businessData
     * @return
     */
    @Override
    public FlowBusinessExtendBO getExtendField(Object businessData) {
        if (businessData == null) {
            return null;
        }
        PayOrderDetailVO payOrderDetailVO = (PayOrderDetailVO) businessData;
        FlowBusinessExtendBO flowBusinessExtendBO = new FlowBusinessExtendBO();
        flowBusinessExtendBO.setExtendField1(SmartBigDecimalUtil.setScale(payOrderDetailVO.getPayableTotalAmount(),2).toString());
        flowBusinessExtendBO.setExtendFieldName1("付款金額");

        flowBusinessExtendBO.setExtendField2(payOrderDetailVO.getDriverName() + "/" + payOrderDetailVO.getDriverTelephone());
        flowBusinessExtendBO.setExtendFieldName2("司机");

        flowBusinessExtendBO.setExtendField3(payOrderDetailVO.getVehicleNumber());
        flowBusinessExtendBO.setExtendFieldName3("车辆");

        if (payOrderDetailVO.getFleetId() != null) {
            flowBusinessExtendBO.setExtendField4(payOrderDetailVO.getFleetName() + "/" + payOrderDetailVO.getFleetCaptainName() + "/" + payOrderDetailVO.getFleetCaptainPhone());
            flowBusinessExtendBO.setExtendFieldName4("车队");
        }

        return flowBusinessExtendBO;
    }

    /**
     * 流程开启的业务处理
     *
     * @param startBO
     * @param requestForm
     */
    @Override
    public void flowStartHandle(FlowBusinessStartBO startBO, DataTracerRequestForm requestForm) {
        PayOrderEntity updatePayOrderEntity = new PayOrderEntity();
        updatePayOrderEntity.setPayOrderId(startBO.getBusinessId());
        updatePayOrderEntity.setFlowInstanceId(startBO.getFlowInstanceId());
        updatePayOrderEntity.setAuditStatus(FlowAuditStatusEnum.WAIT.getValue());
        payOrderDao.updateById(updatePayOrderEntity);
    }

    /**
     * 流程结束的业务处理
     *  流程结束的三种状态：通过，驳回，撤销
     * @param endBO
     * @param requestForm
     */
    @Override
    public void flowEndHandle(FlowBusinessEndBO endBO, DataTracerRequestForm requestForm) {
        if (FlowAuditStatusEnum.PASS == endBO.getAuditStatusEnum()) {
            payOrderManager.payOrderAuditPass(endBO.getBusinessId());
        }
        if (FlowAuditStatusEnum.REJECT == endBO.getAuditStatusEnum()) {
            payOrderManager.payOrderAuditReject(endBO.getBusinessId());
        }
    }

    /**
     * 流程撤销的业务处理
     *
     * @param cancelBO
     * @param requestForm
     */
    @Override
    public void flowCancelHandle(FlowBusinessCancelBO cancelBO, DataTracerRequestForm requestForm) {
        payOrderManager.payOrderAuditCancel(cancelBO.getBusinessId());
    }
}