package net.lab1024.tms.admin.module.business.pay.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.flow.listener.FlowTaskListener;
import net.lab1024.tms.admin.module.business.flow.listener.ITaskAuditListenerService;
import net.lab1024.tms.admin.module.business.flow.listener.TaskAuditListenerBO;
import net.lab1024.tms.admin.module.business.oilcard.dao.OilCardDao;
import net.lab1024.tms.admin.module.business.pay.dao.PayOrderDao;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import net.lab1024.tms.common.module.business.pay.domain.PayOrderEntity;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yandy
 * @description:
 * @date 2022/10/19 4:24 下午
 */
@Slf4j
@FlowTaskListener(value = "oilCardRechargeCheckTaskListener", desc = "油卡充值-增加预充值金额")
public class OilCardRechargeCheckTaskListener implements ITaskAuditListenerService {

    @Autowired
    private PayOrderDao payOrderDao;
    @Autowired
    private OilCardDao oilCardDao;

    @Override
    public void auditPass(TaskAuditListenerBO taskAuditListenerBO) {
        Long payOrderId = taskAuditListenerBO.getBusinessId();
        PayOrderEntity payOrderEntity = payOrderDao.selectById(payOrderId);
        if (payOrderEntity == null) {
            return ;
        }
        Long oilCardId = payOrderEntity.getOilCardId();
        OilCardEntity oilCardEntity = oilCardDao.selectById(oilCardId);
        if (oilCardEntity == null) {
            return ;
        }
        Long masterOilCardId = oilCardEntity.getMasterOilCardId();
        OilCardEntity masterOilCardEntity = oilCardDao.selectById(masterOilCardId);
        if (masterOilCardEntity == null) {
            return ;
        }
    }

    @Override
    public void auditReject(TaskAuditListenerBO taskAuditListenerBO) {

    }
}