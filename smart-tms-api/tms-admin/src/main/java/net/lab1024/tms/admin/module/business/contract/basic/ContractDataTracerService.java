package net.lab1024.tms.admin.module.business.contract.basic;

import net.lab1024.tms.admin.module.business.material.contracttype.ContractTypeDao;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartLocalDateUtil;
import net.lab1024.tms.common.module.business.contract.constant.ContractSignTypeEnum;
import net.lab1024.tms.common.module.business.contract.domain.ContractEntity;
import net.lab1024.tms.common.module.business.material.contracttype.ContractTypeEntity;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author yandy
 * @description:
 * @date 2022/7/21 10:15 下午
 */
@Service
public class ContractDataTracerService {

    @Autowired
    private DataTracerService dataTracerService;
    @Autowired
    private ContractTypeDao contractTypeDao;
    /**
     * 新增日志
     *
     * @param dataTracerRequestForm
     */
    @Async
    public void saveLog(ContractEntity contractEntity, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(Long.valueOf(contractEntity.getContractId()));
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.CONTRACT);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(this.getContractContent(contractEntity));
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }


    /**
     * 更新日志
     *
     * @param contractEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void updateLog(ContractEntity contractEntity, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(Long.valueOf(contractEntity.getContractId()));
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.CONTRACT);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent(this.getContractContent(contractEntity));
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }


    /**
     * 获取合同信息
     *
     * @param contractEntity
     * @return
     */
    private String getContractContent(ContractEntity contractEntity) {
        ContractTypeEntity contractTypeEntity = contractTypeDao.selectById(contractEntity.getContractType());

        StringBuilder builder = new StringBuilder();
        builder.append(DataTracerConstant.TAB)
                .append("【合同形式】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(contractEntity.getSignType(), ContractSignTypeEnum.class)).append(DataTracerConstant.BLANK)
                .append("【合同编号】").append(DataTracerConstant.SPLIT).append(contractEntity.getContractCode()).append(DataTracerConstant.BLANK)
                .append("【合同名称】").append(DataTracerConstant.SPLIT).append(contractEntity.getContractName()).append(DataTracerConstant.BLANK)
                .append("【合同模板】").append(DataTracerConstant.SPLIT).append(contractTypeEntity.getName()).append(DataTracerConstant.BLANK)
                .append("【合同有效期】").append(DataTracerConstant.SPLIT).append(SmartLocalDateUtil.formatYMD(contractEntity.getExpirationDate())).append(DataTracerConstant.BLANK)
                .append("【合同文件】").append(DataTracerConstant.SPLIT).append(contractEntity.getContractFile()).append(DataTracerConstant.BLANK)
                .append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

}