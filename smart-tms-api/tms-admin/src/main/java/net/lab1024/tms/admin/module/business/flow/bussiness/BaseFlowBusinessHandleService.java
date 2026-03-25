package net.lab1024.tms.admin.module.business.flow.bussiness;

import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessCancelBO;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessEndBO;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessExtendBO;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessStartBO;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/18 9:27
 */
public abstract class BaseFlowBusinessHandleService implements IFlowBusinessHandleService {

    @Autowired
    private FlowBusinessService flowBusinessService;

    /**
     * 各业务注册的对应的审批类型
     * @param flowTypeEnum
     */
    protected void register(FlowTypeEnum flowTypeEnum) {
        flowBusinessService.registerFlowBusiness(flowTypeEnum, this);
    }

    /**
     * 业务数据获取
     * 一般为业务表单的详情数据
     *
     * @param businessId
     * @return
     */
    @Override
    public abstract Object getBusinessData(Long businessId, String businessCode);

    /**
     * 扩展字段
     * 一般用于审批列表的查询 和展示
     * @param businessData
     * @return
     */
    @Override
    public FlowBusinessExtendBO getExtendField(Object businessData) {
        return null;
    }

    /**
     * 流程开启的业务处理
     * @param flowBusinessStartBO
     */
    @Override
    public void flowStartHandle(FlowBusinessStartBO flowBusinessStartBO, DataTracerRequestForm dataTracerRequestForm) {
    }

    /**
     * 流程结束的业务处理
     * @param endBO
     */
    @Override
    public void flowEndHandle(FlowBusinessEndBO endBO, DataTracerRequestForm dataTracerRequestForm) {

    }

    /**
     * 流程撤销的业务处理
     * @param cancelBO
     */
    @Override
    public void flowCancelHandle(FlowBusinessCancelBO cancelBO, DataTracerRequestForm dataTracerRequestForm) {

    }
}
