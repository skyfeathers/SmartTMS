package net.lab1024.tms.admin.module.business.waybill.service;

import net.lab1024.tms.admin.module.business.flow.bussiness.BaseFlowBusinessHandleService;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessCancelBO;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessEndBO;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessExtendBO;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessStartBO;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTypeEnum;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.business.waybill.domain.vo.WaybillDetailVO;
import net.lab1024.tms.admin.module.business.waybill.manager.WaybillManager;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
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
public class WaybillFlowService extends BaseFlowBusinessHandleService {

    @Autowired
    private WaybillDetailService waybillDetailService;
    @Autowired
    private WaybillManager waybillManager;
    @Autowired
    private WaybillDao waybillDao;



    @PostConstruct
    public void init() {
        super.register(FlowTypeEnum.WAY_BILL_AUDIT);
    }

    /**
     * 业务数据获取
     * 一般为业务表单的详情数据
     *
     * @param businessId
     * @return
     */
    @Override
    public WaybillDetailVO getBusinessData(Long businessId, String businessCode){
        return waybillDetailService.getDetail(businessId);
    }

    /**
     * 扩展字段
     * 一般用于审批列表的查询 和展示
     * @param businessData
     * @return
     */
    @Override
    public FlowBusinessExtendBO getExtendField(Object businessData) {
        if(businessData == null){
            return null;
        }
        WaybillDetailVO waybillVO = (WaybillDetailVO) businessData;
        FlowBusinessExtendBO flowBusinessExtendBO = new FlowBusinessExtendBO();
        flowBusinessExtendBO.setExtendField1(waybillVO.getWaybillNumber());
        flowBusinessExtendBO.setExtendFieldName1("运单号");

        flowBusinessExtendBO.setExtendField2(waybillVO.getShortName());
        flowBusinessExtendBO.setExtendFieldName2("货主简称");

        if(waybillVO.getFleetId() != null){
            flowBusinessExtendBO.setExtendField3(waybillVO.getFleetName());
            flowBusinessExtendBO.setExtendFieldName3("车队");
        }
        if(waybillVO.getDriverId() != null){
            flowBusinessExtendBO.setExtendField4(waybillVO.getDriverName());
            flowBusinessExtendBO.setExtendFieldName4("司机");
        }
        if(waybillVO.getVehicleId() != null){
            flowBusinessExtendBO.setExtendField5(waybillVO.getVehicleNumber());
            flowBusinessExtendBO.setExtendFieldName5("车辆");
        }

        return flowBusinessExtendBO;
    }

    /**
     * 流程开启的业务处理
     * @param startBO
     * @param requestForm
     */
    @Override
    public void flowStartHandle(FlowBusinessStartBO startBO, DataTracerRequestForm requestForm) {
        WaybillEntity waybillEntity = new WaybillEntity();
        waybillEntity.setWaybillId(startBO.getBusinessId());
        waybillEntity.setFlowInstanceId(startBO.getFlowInstanceId());
        waybillEntity.setAuditStatus(FlowAuditStatusEnum.WAIT.getValue());
        waybillDao.updateById(waybillEntity);
    }

    /**
     * 流程结束的业务处理
     * 流程结束的三种状态：通过，驳回，撤销
     * @param endBO
     * @param requestForm
     */
    @Override
    public void flowEndHandle(FlowBusinessEndBO endBO, DataTracerRequestForm requestForm) {
        if(FlowAuditStatusEnum.PASS == endBO.getAuditStatusEnum()){
            waybillManager.waybillAuditPass(endBO.getBusinessId());
        }
        if(FlowAuditStatusEnum.REJECT == endBO.getAuditStatusEnum()){
            waybillManager.waybillAuditReject(endBO.getBusinessId());
        }

    }

    /**
     * 流程撤销的业务处理
     * @param cancelBO
     * @param requestForm
     */
    @Override
    public void flowCancelHandle(FlowBusinessCancelBO cancelBO, DataTracerRequestForm requestForm) {
        waybillManager.waybillAuditCancel(cancelBO.getBusinessId());
    }
}