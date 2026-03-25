package net.lab1024.tms.admin.module.business.pay.service;

import net.lab1024.tms.admin.module.business.flow.bussiness.BaseFlowBusinessHandleService;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessCancelBO;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessEndBO;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessExtendBO;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessStartBO;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTypeEnum;
import net.lab1024.tms.admin.module.business.oilcard.dao.OilCardDao;
import net.lab1024.tms.admin.module.business.pay.dao.PayOrderDao;
import net.lab1024.tms.admin.module.business.pay.domain.vo.PayOrderDetailVO;
import net.lab1024.tms.admin.module.business.pay.manager.PayOrderManager;
import net.lab1024.tms.common.common.util.SmartBigDecimalUtil;
import net.lab1024.tms.common.module.business.pay.domain.PayOrderEntity;
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
public class OilCardRechargeApplyFlowService extends BaseFlowBusinessHandleService {

    @Autowired
    private PayOrderDetailService payOrderDetailService;
    @Autowired
    private OilCardDao oilCardDao;
    @Autowired
    private PayOrderDao payOrderDao;
    @Autowired
    private PayOrderManager payOrderManager;


    @PostConstruct
    public void init() {
        super.register(FlowTypeEnum.OIL_CARD_AUDIT);
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
        flowBusinessExtendBO.setExtendField1(payOrderDetailVO.getOilCardNo());
        flowBusinessExtendBO.setExtendFieldName1("油卡号");

        flowBusinessExtendBO.setExtendField2(payOrderDetailVO.getWaybillNumber());
        flowBusinessExtendBO.setExtendFieldName2("运单号");

        flowBusinessExtendBO.setExtendField3(SmartBigDecimalUtil.setScale(payOrderDetailVO.getPayableTotalAmount(),2).toString());
        flowBusinessExtendBO.setExtendFieldName3("充值金额");

        flowBusinessExtendBO.setExtendField4(payOrderDetailVO.getDriverName());
        flowBusinessExtendBO.setExtendFieldName4("司机");

        flowBusinessExtendBO.setExtendField5(payOrderDetailVO.getVehicleNumber());
        flowBusinessExtendBO.setExtendFieldName5("车辆");

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
        PayOrderEntity updateEntity = new PayOrderEntity();
        updateEntity.setPayOrderId(startBO.getBusinessId());
        updateEntity.setFlowInstanceId(startBO.getFlowInstanceId());
        payOrderDao.updateById(updateEntity);
    }

    /**
     * 流程结束的业务处理
     * 流程结束的三种状态：通过，驳回，撤销
     *
     * @param endBO
     * @param requestForm
     */
    @Override
    public void flowEndHandle(FlowBusinessEndBO endBO, DataTracerRequestForm requestForm) {
        Long payOrderId = endBO.getBusinessId();
        PayOrderEntity updateEntity = new PayOrderEntity();
        updateEntity.setPayOrderId(payOrderId);
        updateEntity.setAuditStatus(endBO.getAuditStatusEnum().getValue());
        payOrderDao.updateById(updateEntity);
    }


    /**
     * 流程撤销的业务处理
     *
     * @param cancelBO
     * @param requestForm
     */
    @Override
    public void flowCancelHandle(FlowBusinessCancelBO cancelBO, DataTracerRequestForm requestForm) {
        Long payOrderId = cancelBO.getBusinessId();
        payOrderManager.payOrderAuditCancel(payOrderId);
    }
}