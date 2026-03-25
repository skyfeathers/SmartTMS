package net.lab1024.tms.job.module.expiredcertificate;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateModuleTypeEnum;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateStatusEnum;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateTypeEnum;
import net.lab1024.tms.common.module.business.expiredcertificate.domain.ExpiredCertificateEntity;
import net.lab1024.tms.common.module.business.msg.MsgCommonService;
import net.lab1024.tms.common.module.business.msg.constant.MsgReceiverTypeEnum;
import net.lab1024.tms.common.module.business.msg.constant.MsgSubTypeEnum;
import net.lab1024.tms.common.module.business.msg.domain.MsgSendDTO;
import net.lab1024.tms.common.module.system.employee.CommonEmployeeDao;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeBO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 到期证件 定时 业务
 *
 * @author Turbolisten
 * @date 2022/7/19 15:03
 */
@Slf4j
@Service
public class ExpiredCertificateTask {

    @Autowired
    private ExpiredCertificateDao certificateDao;

    @Autowired
    private ExpiredCertificateManager certificateManager;

    @Autowired
    private MsgCommonService msgCommonService;
    @Autowired
    private CommonEmployeeDao commonEmployeeDao;
    /**
     * 定时检查更新证件的到期状态
     * 每天执行1次
     * 发送消息提醒
     */
    @Scheduled(cron = "10 30 8 * * *")
    public void updateStatus() {
        /**
         * 注意这个参数的顺序非常重要
         * 与sql一一对应
         */
        List<ExpiredCertificateEntity> list = certificateDao.queryNeedUpdateStatus(
                ExpiredCertificateStatusEnum.EXPIRED.getValue(),
                ExpiredCertificateStatusEnum.EXPIRED_3_DAYS.getValue(),
                ExpiredCertificateStatusEnum.EXPIRED_7_DAYS.getValue(),
                ExpiredCertificateStatusEnum.EXPIRED_15_DAYS.getValue(),
                ExpiredCertificateStatusEnum.EXPIRED_30_DAYS.getValue());
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        /**
         * 循环 每次处理500条数据
         */
        LocalDate nowDate = LocalDate.now();
        List<List<ExpiredCertificateEntity>> partitions = Lists.partition(list, 500);
        for (List<ExpiredCertificateEntity> partition : partitions) {
            List<ExpiredCertificateEntity> updateList = Lists.newArrayList();
            List<MsgSendDTO> msgSendList = Lists.newArrayList();
            for (ExpiredCertificateEntity certificateEntity : partition) {
                long between = ChronoUnit.DAYS.between(nowDate, certificateEntity.getExpiredTime());
                ExpiredCertificateStatusEnum updateStatus = null;
                if (between <= ExpiredCertificateStatusEnum.EXPIRED.getValue()) {
                    // 已过期
                    updateStatus = ExpiredCertificateStatusEnum.EXPIRED;
                } else if (between <= ExpiredCertificateStatusEnum.EXPIRED_3_DAYS.getValue()) {
                    // 3天内
                    updateStatus = ExpiredCertificateStatusEnum.EXPIRED_3_DAYS;
                } else if (between <= ExpiredCertificateStatusEnum.EXPIRED_7_DAYS.getValue()) {
                    // 7天内
                    updateStatus = ExpiredCertificateStatusEnum.EXPIRED_7_DAYS;
                } else if (between <= ExpiredCertificateStatusEnum.EXPIRED_15_DAYS.getValue()) {
                    // 15天内
                    updateStatus = ExpiredCertificateStatusEnum.EXPIRED_15_DAYS;
                } else if (between <= ExpiredCertificateStatusEnum.EXPIRED_30_DAYS.getValue()) {
                    // 30天内
                    updateStatus = ExpiredCertificateStatusEnum.EXPIRED_30_DAYS;
                }
                if (null == updateStatus || updateStatus.equalsValue(certificateEntity.getStatus())) {
                    continue;
                }
                // 更新到期状态
                ExpiredCertificateEntity updateEntity = new ExpiredCertificateEntity();
                updateEntity.setId(certificateEntity.getId());
                updateEntity.setStatus(updateStatus.getValue());
                updateList.add(updateEntity);

                // 发送提醒消息
                List<MsgSendDTO> msgList = this.buildMsg(certificateEntity);
                if (CollectionUtils.isNotEmpty(msgList)) {
                    msgSendList.addAll(msgList);
                }
            }
            // 批量更新到期状态
            if (CollectionUtils.isNotEmpty(updateList)) {
                certificateManager.updateBatchById(updateList);
                // 发送消息
                msgCommonService.send(msgSendList);
            }
        }
    }

    /**
     * 根据到期证件类型 build 提醒消息
     *
     * @param certificateEntity
     */
    private List<MsgSendDTO> buildMsg(ExpiredCertificateEntity certificateEntity) {
        // 判断到期证件类型
        MsgSubTypeEnum mgSubType = null;

        // 货主
        Integer moduleType = certificateEntity.getModuleType();
        ExpiredCertificateTypeEnum typeEnum = SmartBaseEnumUtil.getEnumByValue(certificateEntity.getType(), ExpiredCertificateTypeEnum.class);
        if (ExpiredCertificateModuleTypeEnum.SHIPPER.equalsValue(moduleType)) {
            switch (typeEnum) {
                case HUO_ZHU_HE_TONG:
                    mgSubType = MsgSubTypeEnum.SHIPPER_HE_TONG_DAO_QI;
                    break;
                default:
            }
        }

        // 司机
        if (ExpiredCertificateModuleTypeEnum.DRIVER.equalsValue(moduleType)) {
            switch (typeEnum) {
                case SHEN_FEN_ZHENG:
                    mgSubType = MsgSubTypeEnum.DRIVER_SHEN_FEN_ZHENG_DAO_QI;
                    break;
                case JIA_SHI_ZHENG:
                    mgSubType = MsgSubTypeEnum.DRIVER_JIA_SHI_ZHENG_DAO_QI;
                    break;
                case CONG_YE_ZI_GE_ZHENG:
                    mgSubType = MsgSubTypeEnum.DRIVER_CONG_YE_ZI_GE_ZHENG_DAO_QI;
                    break;
                case SI_JI_HE_TONG:
                    mgSubType = MsgSubTypeEnum.DRIVER_HE_TONG_DAO_QI;
                    break;
                default:
            }
        }

        // 车辆
        if (ExpiredCertificateModuleTypeEnum.VEHICLE.equalsValue(moduleType)) {
            switch (typeEnum) {
                case XING_SHI_ZHENG:
                    mgSubType = MsgSubTypeEnum.CAR_XING_SHI_ZHENG_DAO_QI;
                    break;
                case SHANG_YE_XIAN:
                case JIAO_QIANG_XIAN:
                    mgSubType = MsgSubTypeEnum.CAR_BAO_XIAN_DAO_QI;
                    break;
                case DAO_LU_YUN_SHU_XU_KE_ZHENG:
                    mgSubType = MsgSubTypeEnum.CAR_DAO_LU_YUN_SHU_XU_KE_ZHENG_DAO_QI;
                    break;
                default:
            }
        }

        // 挂车
        if (ExpiredCertificateModuleTypeEnum.BRACKET.equalsValue(moduleType)) {
            switch (typeEnum) {
                case XING_SHI_ZHENG:
                    mgSubType = MsgSubTypeEnum.BRACKET_XING_SHI_ZHENG_DAO_QI;
                    break;
                case SHANG_YE_XIAN:
                case JIAO_QIANG_XIAN:
                    mgSubType = MsgSubTypeEnum.BRACKET_BAO_XIAN_DAO_QI;
                    break;
                default:
            }
        }
        if (null == mgSubType) {
            return Lists.newArrayList();
        }
        List<Long> receiverIdList = this.getMsgReceiverId(moduleType, certificateEntity.getModuleId());
        if(CollectionUtils.isEmpty(receiverIdList)){
            return Lists.newArrayList();
        }
        List<EmployeeBO> employeeBOList = commonEmployeeDao.selectByIdList(receiverIdList);
        // build 通知消息
        List<MsgSendDTO> msgList = Lists.newArrayList();
        for (EmployeeBO employeeBO : employeeBOList) {
            MsgSendDTO sendDTO = new MsgSendDTO();
            sendDTO.setMsgSubType(mgSubType);
            sendDTO.setReceiverType(MsgReceiverTypeEnum.ADMIN);
            sendDTO.setReceiverId(employeeBO.getEmployeeId());
            sendDTO.setEnterpriseId(employeeBO.getEnterpriseId());
            sendDTO.setDataId(certificateEntity.getModuleId());
            // 参数
            Map<String, Object> contentParam = new HashMap<>(3);
            contentParam.put("name", certificateEntity.getModuleName());
            sendDTO.setContentParam(contentParam);

            msgList.add(sendDTO);
        }
        return msgList;
    }

    /**
     * 获取模块接收人
     *
     * @param moduleType
     * @param moduleId
     * @return
     */
    private List<Long> getMsgReceiverId(Integer moduleType, Long moduleId) {
        List<Long> receiverIdList = Lists.newArrayList();
        ExpiredCertificationModuleEmployeeBO employeeBO = null;
        if (ExpiredCertificateModuleTypeEnum.SHIPPER.equalsValue(moduleType)) {
            employeeBO = certificateDao.selectShipperEmployeeId(moduleId);
        }
        // 司机
        if (ExpiredCertificateModuleTypeEnum.DRIVER.equalsValue(moduleType)) {
            employeeBO = certificateDao.selectDriverEmployeeId(moduleId);
        }
        // 车辆
        if (ExpiredCertificateModuleTypeEnum.VEHICLE.equalsValue(moduleType)) {
            employeeBO = certificateDao.selectVehicleEmployeeId(moduleId);
        }
        // 挂车
        if (ExpiredCertificateModuleTypeEnum.BRACKET.equalsValue(moduleType)) {
            employeeBO = certificateDao.selectBracketEmployeeId(moduleId);
        }
        if (employeeBO == null) {
            return receiverIdList;
        }
        if (employeeBO.getManagerId() != null) {
            receiverIdList.add(employeeBO.getManagerId());
        }
        if (employeeBO.getCreateUserId() != null) {
            receiverIdList.add(employeeBO.getCreateUserId());
        }
        return receiverIdList;
    }
}
