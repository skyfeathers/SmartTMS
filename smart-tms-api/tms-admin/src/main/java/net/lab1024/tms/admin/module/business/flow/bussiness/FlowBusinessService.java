package net.lab1024.tms.admin.module.business.flow.bussiness;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessCancelBO;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessEndBO;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessExtendBO;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessStartBO;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTypeEnum;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/17 10:41
 */
@Slf4j
@Service
public class FlowBusinessService {

    private ConcurrentHashMap<FlowTypeEnum, IFlowBusinessHandleService> flowBusinessMap = new ConcurrentHashMap<>();

    /**
     * 业务处理服务类注册
     *
     * @param flowTypeEnum
     * @param flowBusiness
     */
    public void registerFlowBusiness(FlowTypeEnum flowTypeEnum, IFlowBusinessHandleService flowBusiness) {
        IFlowBusinessHandleService handleService = flowBusinessMap.get(flowTypeEnum);
        if(handleService != null){
            throw new BusinessException("重复注册流程业务处理类："+flowTypeEnum.getDesc());
        }
        this.flowBusinessMap.put(flowTypeEnum, flowBusiness);
        log.info("完成{},业务逻辑处理服务注册", flowTypeEnum.getDesc());
    }

    private IFlowBusinessHandleService getFlowBusiness(FlowTypeEnum flowTypeEnum) {
        return this.flowBusinessMap.get(flowTypeEnum);
    }

    /**
     * 获取业务数据
     *
     * @param flowTypeEnum
     * @param businessId
     * @return
     */
    public Object getFlowBusinessData(FlowTypeEnum flowTypeEnum, Long businessId, String businessCode) {
        IFlowBusinessHandleService flowBusiness = this.getFlowBusiness(flowTypeEnum);
        if (flowBusiness == null) {
            throw new BusinessException(String.format("暂未找到%s,对应的业务处理类。。", flowTypeEnum.getDesc()));
        }
        return flowBusiness.getBusinessData(businessId, businessCode);
    }

    /**
     * 获取业务扩展字段信息
     * @param flowTypeEnum
     * @param businessData
     * @return
     */
    public FlowBusinessExtendBO getFlowBusinessExtend(FlowTypeEnum flowTypeEnum, Object businessData) {
        IFlowBusinessHandleService flowBusiness = this.getFlowBusiness(flowTypeEnum);
        if (flowBusiness == null) {
            throw new BusinessException(String.format("暂未找到%s,对应的业务处理类。。", flowTypeEnum.getDesc()));
        }
        return flowBusiness.getExtendField(businessData);
    }

    /**
     * 触发流程开启 各业务注入的动作
     * @param flowTypeEnum
     * @param startBO
     */
    public void triggerStart(FlowTypeEnum flowTypeEnum, FlowBusinessStartBO startBO, DataTracerRequestForm requestForm) {
        IFlowBusinessHandleService flowBusiness = this.getFlowBusiness(flowTypeEnum);
        if (flowBusiness == null) {
            log.error("{},暂未注册流程处理类", flowTypeEnum.getDesc());
            return;
        }
        flowBusiness.flowStartHandle(startBO,requestForm);
    }

    /**
     * 触发流程结束 各业务注入的动作
     * @param flowTypeEnum
     * @param endBO
     */
    public void triggerEnd(FlowTypeEnum flowTypeEnum, FlowBusinessEndBO endBO, DataTracerRequestForm requestForm) {
        IFlowBusinessHandleService flowBusiness = this.getFlowBusiness(flowTypeEnum);
        if (flowBusiness == null) {
            log.error("{},暂未注册流程处理类", flowTypeEnum.getDesc());
            return;
        }
        flowBusiness.flowEndHandle(endBO,requestForm);
    }

    /**
     * 触发流程撤销 各业务注入的动作
     * @param flowTypeEnum
     * @param cancelBO
     */
    public void triggerCancel(FlowTypeEnum flowTypeEnum, FlowBusinessCancelBO cancelBO, DataTracerRequestForm requestForm) {
        IFlowBusinessHandleService flowBusiness = this.getFlowBusiness(flowTypeEnum);
        if (flowBusiness == null) {
            log.error("{},暂未注册流程处理类", flowTypeEnum.getDesc());
            return;
        }
        flowBusiness.flowCancelHandle(cancelBO,requestForm);
    }

}
