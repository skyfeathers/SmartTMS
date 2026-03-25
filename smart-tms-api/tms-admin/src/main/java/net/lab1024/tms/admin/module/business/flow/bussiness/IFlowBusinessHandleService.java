package net.lab1024.tms.admin.module.business.flow.bussiness;

import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessCancelBO;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessEndBO;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessExtendBO;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessStartBO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/17 10:54
 */
public interface IFlowBusinessHandleService {

    /**
     * 获取流程业务数据
     *
     * @param businessId
     * @return
     */
    Object getBusinessData(Long businessId, String businessCode);

    /**
     * 获取业务扩展字段信息
     * @param businessData
     * @return
     */
    FlowBusinessExtendBO getExtendField(Object businessData);

    /**
     * 流程开始处理
     *
     * @param startBO
     */
    void flowStartHandle(FlowBusinessStartBO startBO, DataTracerRequestForm requestForm);

    /**
     * 流程结束处理
     *
     * @param endBO
     */
    void flowEndHandle(FlowBusinessEndBO endBO, DataTracerRequestForm requestForm);

    /**
     * 流程撤销
     *
     * @param cancelBO
     */
    void flowCancelHandle(FlowBusinessCancelBO cancelBO, DataTracerRequestForm requestForm);
}
