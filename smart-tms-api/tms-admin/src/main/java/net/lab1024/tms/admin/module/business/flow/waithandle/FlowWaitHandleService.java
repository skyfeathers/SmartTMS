package net.lab1024.tms.admin.module.business.flow.waithandle;

import net.lab1024.tms.admin.module.business.flow.constant.FlowTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/20 12:02
 */
@Service
public class FlowWaitHandleService {

    @Autowired
    private FlowWaitHandleDao flowWaitHandleDao;

    /**
     * 清零
     * @param employeeId
     * @param flowTypeEnum
     */
    public void clearZero(Long employeeId, FlowTypeEnum flowTypeEnum){
        flowWaitHandleDao.clearZero(employeeId,flowTypeEnum.getValue());
    }

    /**
     * 增加待办事项
     * @param employeeId
     * @param flowTypeEnum
     */
    public void increase(Long employeeId,FlowTypeEnum flowTypeEnum){
        flowWaitHandleDao.increase(employeeId,flowTypeEnum.getValue());
    }

}
