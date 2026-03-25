package net.lab1024.tms.admin.module.business.shipper.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperPrincipalDao;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.*;
import net.lab1024.tms.admin.module.business.shipper.domain.form.ShipperAddForm;
import net.lab1024.tms.admin.module.business.shipper.domain.form.ShipperUpdateForm;
import net.lab1024.tms.admin.module.business.shipper.domain.vo.ShipperDetailVO;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.shipper.constant.*;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerDiffBO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerExtraData;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerFieldService;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/9/7 17:55
 */
@Service
public class ShipperDataTracerService {

    @Autowired
    private DataTracerService dataTracerService;
    @Autowired
    private DataTracerFieldService dataTracerFieldService;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private DictCacheService dictCacheService;
    @Autowired
    private ShipperPrincipalDao shipperPrincipalDao;

    /**
     * 删除日志
     *
     * @param shipperIdList
     * @param dataTracerRequestForm
     */
    @Async
    public void deleteLog(List<Long> shipperIdList, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(shipperIdList)) {
            return;
        }

        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long shipperId : shipperIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(shipperId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.SHIPPER);
            dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.DELETE);
            dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.DELETE.getDesc());
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());

            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }

    /**
     * 修改业务负责人日志
     *
     * @param shipperIdList
     * @param managerIdList
     * @param shipperOriginEntityList
     * @param dataTracerRequestForm
     */
    @Async
    public void updateManagerLog(List<Long> shipperIdList, List<Long> managerIdList, List<ShipperEntity> shipperOriginEntityList, DataTracerRequestForm dataTracerRequestForm) {
        //新负责人
        List<EmployeeEntity> newManagerEntityList = employeeDao.selectBatchIds(managerIdList);

        Map<Long, ShipperEntity> shipperEntityMap = shipperOriginEntityList.stream().collect(Collectors.toMap(ShipperEntity::getShipperId, Function.identity()));
        // 原负责人
        List<ShipperPrincipalDTO> shipperPrincipalList = shipperPrincipalDao.selectByShipperIdListAndType(shipperIdList, PrincipalTypeEnum.MANAGER.getValue());
        Map<Long, List<String>> originManagerNameMap = shipperPrincipalList.stream().collect(Collectors.groupingBy(ShipperPrincipalDTO::getShipperId, Collectors.mapping(ShipperPrincipalDTO::getEmployeeName, Collectors.toList())));

        // 操作日志
        for (Long shipperId : shipperIdList) {
            ShipperEntity shipperEntity = shipperEntityMap.get(shipperId);
            if (shipperEntity == null) {
                continue;
            }
            //原始人员信息
            String originManagerName = StringUtils.join(originManagerNameMap.getOrDefault(shipperId, Lists.newArrayList()), StringConst.SEPARATOR);
            //新人员信息
            String newManagerName = "";
            if (CollectionUtils.isNotEmpty(newManagerEntityList)) {
                newManagerName = StringUtils.join(newManagerEntityList.stream().map(EmployeeEntity::getActualName).collect(Collectors.toList()), StringConst.SEPARATOR);
            }
            //操作内容
            String operateContent = "业务负责人由【" + originManagerName + "】变更为【" + newManagerName + "】";

            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(shipperId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.SHIPPER);
            dataTracerForm.setOperateType(ShipperDataTracerOperateTypeEnum.UPDATE_MANAGE);
            dataTracerForm.setOperateContent(operateContent);
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());

            Long operatorId = dataTracerRequestForm.getOperatorId();
            String operatorName = dataTracerRequestForm.getOperatorName();
            Integer operatorType = dataTracerRequestForm.getOperatorType();
            dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
        }
    }

    /**
     * 修改负责人日志
     *
     * @param originPrincipalList
     * @param dataTracerRequestForm
     */
    @Async
    public void updatePrincipalLog(List<Long> shipperIdList, Integer principalType, List<ShipperPrincipalDTO> originPrincipalList, List<ShipperPrincipalDTO> newPrincipalList, DataTracerRequestForm dataTracerRequestForm) {
        Map<Long, List<ShipperPrincipalDTO>> originListMap = originPrincipalList.stream().collect(groupingBy(ShipperPrincipalDTO::getShipperId));
        Map<Long, List<ShipperPrincipalDTO>> newListMap = newPrincipalList.stream().collect(groupingBy(ShipperPrincipalDTO::getShipperId));


        PrincipalTypeEnum principalTypeEnum = SmartBaseEnumUtil.getEnumByValue(principalType, PrincipalTypeEnum.class);
        for (Long shipperId : shipperIdList) {
            List<ShipperPrincipalDTO> originList = originListMap.get(shipperId);
            List<ShipperPrincipalDTO> newList = newListMap.get(shipperId);
            //原始人员信息
            String originPrincipalName = "";
            if (CollectionUtils.isNotEmpty(originList)) {
                originPrincipalName = originList.stream().map(ShipperPrincipalDTO::getEmployeeName).collect(Collectors.joining(","));
            }
            //新人员信息
            String newPrincipalName = "";
            if (CollectionUtils.isNotEmpty(originList)) {
                newPrincipalName = newList.stream().map(ShipperPrincipalDTO::getEmployeeName).collect(Collectors.joining(","));
            }
            //操作内容
            String operateContent = principalTypeEnum.getDesc() + "由【" + originPrincipalName + "】变更为【" + newPrincipalName + "】";

            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(shipperId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.SHIPPER);
            dataTracerForm.setOperateType(ShipperDataTracerOperateTypeEnum.UPDATE_PRINCIPAL);
            dataTracerForm.setOperateContent(operateContent);
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());

            Long operatorId = dataTracerRequestForm.getOperatorId();
            String operatorName = dataTracerRequestForm.getOperatorName();
            Integer operatorType = dataTracerRequestForm.getOperatorType();
            dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
        }
    }

    /**
     * 新建日志
     *
     * @param shipperId
     * @param addForm
     * @param dataTracerRequestForm
     */
    @Async
    public void saveLog(Long shipperId, ShipperAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(shipperId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.SHIPPER);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setExtraData(new DataTracerExtraData(ShipperDetailVO.class, null, addForm));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

    /**
     * 更新日志
     *
     * @param beforeData
     * @param afterData
     * @param updateDTO
     * @param dataTracerRequestForm
     */
    @Async
    public void updateLog(ShipperDetailVO beforeData, ShipperDetailVO afterData, ShipperUpdateForm updateDTO, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(beforeData.getShipperId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.SHIPPER);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent(dataTracerFieldService.beanParse(beforeData, afterData));
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getShipperContent(beforeData), this.getShipperContent(afterData)));
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setExtraData(new DataTracerExtraData(ShipperDetailVO.class, beforeData, afterData));

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

    private String getShipperContent(ShipperDetailVO shipperDetailVO) {
        String baseContent = this.getShipperBaseContent(shipperDetailVO);
        String contactContent = this.getShipperContactContent(shipperDetailVO.getContactList());
        String invoiceContent = this.getShipperInvoiceContent(shipperDetailVO.getInvoiceList());
        String paymentWayContent = this.getShipperPaymentWayContent(shipperDetailVO.getPaymentWayList());
        String mailAddressContent = this.getShipperMailAddressContent(shipperDetailVO.getMailAddressList());

        StringBuilder builder = new StringBuilder();
        builder.append(baseContent)
                .append(contactContent)
                .append(invoiceContent)
                .append(paymentWayContent)
                .append(mailAddressContent);
        return builder.toString();
    }

    /**
     * 基本信息
     *
     * @param shipperDetailVO
     * @return
     */
    private String getShipperBaseContent(ShipperDetailVO shipperDetailVO) {
        StringBuilder builder = new StringBuilder();

        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【货主名称】").append(DataTracerConstant.SPLIT).append(shipperDetailVO.getConsignor()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【所属公司】").append(DataTracerConstant.SPLIT).append(shipperDetailVO.getEnterpriseName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【账期】").append(DataTracerConstant.SPLIT).append(shipperDetailVO.getAccountPeriod()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【税点】").append(DataTracerConstant.SPLIT).append(shipperDetailVO.getTaxPoint()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【扣税比例】").append(DataTracerConstant.SPLIT).append(shipperDetailVO.getDeductTaxRatio()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【是否扣税】").append(DataTracerConstant.SPLIT).append(shipperDetailVO.getDeductTaxFlag() ? "是" : "否").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【所在地区】").append(DataTracerConstant.SPLIT).append(dictCacheService.selectValueNameByValueCodeSplit(shipperDetailVO.getArea())).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【货主类型】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(shipperDetailVO.getShipperNature(), ShipperNatureEnum.class)).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【标签】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(shipperDetailVO.getTagType(), ShipperTagEnum.class)).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【等级】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(shipperDetailVO.getGrade(), ShipperGradeEnum.class)).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【业务关系】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValueList(shipperDetailVO.getShipperTypeList(), ShipperTypeEnum.class)).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【业务负责人】").append(DataTracerConstant.SPLIT).append(getShipperPrincipal(shipperDetailVO.getShipperPrincipalList(), PrincipalTypeEnum.MANAGER.getValue())).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【客服负责人】").append(DataTracerConstant.SPLIT).append(getShipperPrincipal(shipperDetailVO.getShipperPrincipalList(), PrincipalTypeEnum.CUSTOMER_SERVICE.getValue())).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【营业执照】").append(DataTracerConstant.SPLIT).append(shipperDetailVO.getConsignorImage()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【营业执照编号】").append(DataTracerConstant.SPLIT).append(shipperDetailVO.getConsignorId()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【备注】").append(DataTracerConstant.SPLIT).append(shipperDetailVO.getRemark()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 货主联系人信息
     *
     * @param contactList
     * @return
     */
    private String getShipperContactContent(List<ShipperContactDTO> contactList) {
        StringBuilder builder = new StringBuilder();
        builder.append("联系人信息:").append(DataTracerConstant.LINE);
        if (CollectionUtils.isEmpty(contactList)) {
            builder.append(DataTracerConstant.TAB).append(DataTracerConstant.LINE);
            return builder.toString();
        }
        for (ShipperContactDTO shipperContactDTO : contactList) {
            builder.append(DataTracerConstant.TAB)
                    .append("【姓名】").append(DataTracerConstant.SPLIT).append(shipperContactDTO.getContactName()).append(DataTracerConstant.BLANK)
                    .append("【电话】").append(DataTracerConstant.SPLIT).append(shipperContactDTO.getContactPhone()).append(DataTracerConstant.BLANK)
                    .append("【职位】").append(DataTracerConstant.SPLIT).append(shipperContactDTO.getPosition()).append(DataTracerConstant.BLANK)
                    .append("【备注】").append(DataTracerConstant.SPLIT).append(shipperContactDTO.getRemark()).append(DataTracerConstant.BLANK)
                    .append(DataTracerConstant.LINE);
        }
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 货主付款方式信息
     *
     * @param paymentWayList
     * @return
     */
    private String getShipperPaymentWayContent(List<ShipperPaymentWayDTO> paymentWayList) {
        StringBuilder builder = new StringBuilder();
        builder.append("银行账户信息:").append(DataTracerConstant.LINE);
        if (CollectionUtils.isEmpty(paymentWayList)) {
            builder.append(DataTracerConstant.TAB).append(DataTracerConstant.LINE);
            return builder.toString();
        }
        for (ShipperPaymentWayDTO paymentWayDTO : paymentWayList) {
            builder.append(DataTracerConstant.TAB)
                    .append("【收付款方式】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(paymentWayDTO.getPaymentType(), PaymentTypeEnum.class)).append(DataTracerConstant.BLANK)
                    .append("【是否公户】").append(DataTracerConstant.SPLIT).append(paymentWayDTO.getPublicAccountFlag() ? "是" : "否").append(DataTracerConstant.BLANK)
                    .append("【开户名】").append(DataTracerConstant.SPLIT).append(paymentWayDTO.getAccountName()).append(DataTracerConstant.BLANK)
                    .append("【银行账号】").append(DataTracerConstant.SPLIT).append(paymentWayDTO.getAccountNumber()).append(DataTracerConstant.BLANK)
                    .append("【银行名称】").append(DataTracerConstant.SPLIT).append(paymentWayDTO.getBankName()).append(DataTracerConstant.BLANK)
                    .append("【支行名称】").append(DataTracerConstant.SPLIT).append(paymentWayDTO.getBankBranchName()).append(DataTracerConstant.BLANK)
                    .append("【附件信息】").append(DataTracerConstant.SPLIT).append(paymentWayDTO.getReceiveImage()).append(DataTracerConstant.BLANK)
                    .append("【附件信息】").append(DataTracerConstant.SPLIT).append(paymentWayDTO.getAttachment()).append(DataTracerConstant.BLANK)
                    .append(DataTracerConstant.LINE);
        }
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 货主开票信息
     *
     * @param invoiceList
     * @return
     */
    private String getShipperInvoiceContent(List<ShipperInvoiceDTO> invoiceList) {
        StringBuilder builder = new StringBuilder();
        builder.append("开票信息:").append(DataTracerConstant.LINE);
        if (CollectionUtils.isEmpty(invoiceList)) {
            builder.append(DataTracerConstant.TAB).append(DataTracerConstant.LINE);
            return builder.toString();
        }
        for (ShipperInvoiceDTO invoiceDTO : invoiceList) {
            builder.append(DataTracerConstant.TAB)
                    .append("【纳税人识别号】").append(DataTracerConstant.SPLIT).append(invoiceDTO.getInvoiceNo()).append(DataTracerConstant.BLANK)
                    .append("【开票抬头】").append(DataTracerConstant.SPLIT).append(invoiceDTO.getInvoiceName()).append(DataTracerConstant.BLANK)
                    .append("【开票银行】").append(DataTracerConstant.SPLIT).append(invoiceDTO.getInvoiceBankName()).append(DataTracerConstant.BLANK)
                    .append("【银行账号】").append(DataTracerConstant.SPLIT).append(invoiceDTO.getInvoiceBankAccount()).append(DataTracerConstant.BLANK)
                    .append("【开户行号】").append(DataTracerConstant.SPLIT).append(invoiceDTO.getInvoiceBankNo()).append(DataTracerConstant.BLANK)
                    .append("【开户行地址】").append(DataTracerConstant.SPLIT).append(invoiceDTO.getInvoiceBankAddress()).append(DataTracerConstant.BLANK)
                    .append("【开票电话】").append(DataTracerConstant.SPLIT).append(invoiceDTO.getInvoicePhone()).append(DataTracerConstant.BLANK)
                    .append("【开票地址】").append(DataTracerConstant.SPLIT).append(invoiceDTO.getInvoiceAddress()).append(DataTracerConstant.BLANK)
                    .append("【中征码】").append(DataTracerConstant.SPLIT).append(invoiceDTO.getLoanCardNo()).append(DataTracerConstant.BLANK)
                    .append("【禁用状态】").append(DataTracerConstant.SPLIT).append(invoiceDTO.getDisableFlag() ? "禁用" : "启用").append(DataTracerConstant.BLANK)
                    .append("【附件】").append(DataTracerConstant.SPLIT).append(invoiceDTO.getAttachment()).append(DataTracerConstant.BLANK)
                    .append(DataTracerConstant.LINE);
        }
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 货主邮寄信息
     *
     * @param mailAddressList
     * @return
     */
    private String getShipperMailAddressContent(List<ShipperMailAddressDTO> mailAddressList) {
        StringBuilder builder = new StringBuilder();
        builder.append("邮寄信息:").append(DataTracerConstant.LINE);
        if (CollectionUtils.isEmpty(mailAddressList)) {
            builder.append(DataTracerConstant.TAB).append(DataTracerConstant.LINE);
            return builder.toString();
        }
        for (ShipperMailAddressDTO mailAddressDTO : mailAddressList) {
            builder.append(DataTracerConstant.TAB)
                    .append("【姓名】").append(DataTracerConstant.SPLIT).append(mailAddressDTO.getReceivePerson()).append(DataTracerConstant.BLANK)
                    .append("【电话】").append(DataTracerConstant.SPLIT).append(mailAddressDTO.getReceivePersonPhone()).append(DataTracerConstant.BLANK)
                    .append("【是否默认】").append(DataTracerConstant.SPLIT).append(mailAddressDTO.getDefaultFlag() ? "是" : "否").append(DataTracerConstant.BLANK)
                    .append("【所在地区】").append(DataTracerConstant.SPLIT).append(this.getMailArea(mailAddressDTO)).append(DataTracerConstant.BLANK)
                    .append("【详细地址】").append(DataTracerConstant.SPLIT).append(mailAddressDTO.getReceiveAddress()).append(DataTracerConstant.BLANK)
                    .append(DataTracerConstant.LINE);
        }
        return builder.toString().replaceAll("null", "");
    }

    private String getMailArea(ShipperMailAddressDTO mailAddressDTO) {
        if (mailAddressDTO == null) {
            return "";
        }
        String mailArea = "";
        if (StringUtils.isNotBlank(mailAddressDTO.getReceiveProvinceName())) {
            mailArea = mailArea + mailAddressDTO.getReceiveProvinceName();
        }
        if (StringUtils.isNotBlank(mailAddressDTO.getReceiveCityName())) {
            mailArea = mailArea + mailAddressDTO.getReceiveCityName();
        }
        if (StringUtils.isNotBlank(mailAddressDTO.getReceiveDistrictName())) {
            mailArea = mailArea + mailAddressDTO.getReceiveDistrictName();
        }
        return mailArea;
    }

    /**
     * 根据类型获取关联的人员名称
     *
     * @param shipperPrincipalList
     * @return
     */
    private String getShipperPrincipal(List<ShipperPrincipalDTO> shipperPrincipalList, Integer type) {
        if (CollectionUtils.isEmpty(shipperPrincipalList)) {
            return "";
        }
        return shipperPrincipalList.stream().filter(e -> e.getType().equals(type)).map(ShipperPrincipalDTO::getEmployeeName).collect(Collectors.joining(","));
    }

    /**
     * 批量审核日志
     *
     * @param shipperIdList
     * @param auditStatus
     * @param dataTracerRequestForm
     * @param auditRemark
     */
    public void batchAuditLog(List<Long> shipperIdList, Integer auditStatus, String auditRemark, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(shipperIdList)) {
            return;
        }
        AuditStatusEnum auditStatusEnum = SmartBaseEnumUtil.getEnumByValue(auditStatus, AuditStatusEnum.class);
        String content = "修改审核状态为：" + auditStatusEnum.getDesc() + "；备注为：" + (StringUtils.isEmpty(auditRemark) ? "" : auditRemark);
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long shipperId : shipperIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(shipperId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.SHIPPER);
            dataTracerForm.setOperateType(ShipperDataTracerOperateTypeEnum.AUDIT);
            dataTracerForm.setOperateContent(content);
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }

    /**
     * 开票信息日志
     *
     * @param invoiceDTO
     * @param dataTracerRequestForm
     */
    public void saveInvoiceLog(ShipperInvoiceDTO invoiceDTO, DataTracerRequestForm dataTracerRequestForm) {
        String invoiceContent = this.getShipperInvoiceContent(Arrays.asList(invoiceDTO));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(invoiceDTO.getShipperId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.SHIPPER);
        dataTracerForm.setOperateType(ShipperDataTracerOperateTypeEnum.ADD_INVOICE);
        dataTracerForm.setOperateContent(invoiceContent);
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

    public void updateInvoiceLog(ShipperInvoiceDTO invoiceDTO, DataTracerRequestForm dataTracerRequestForm) {
        String invoiceContent = this.getShipperInvoiceContent(Arrays.asList(invoiceDTO));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(invoiceDTO.getShipperId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.SHIPPER);
        dataTracerForm.setOperateType(ShipperDataTracerOperateTypeEnum.UPDATE_INVOICE);
        dataTracerForm.setOperateContent(invoiceContent);
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

    /**
     * 批量审核日志
     *
     * @param shipperIdList
     * @param dataTracerRequestForm
     */
    public void batchUpdateVerifyFlag(List<Long> shipperIdList, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(shipperIdList)) {
            return;
        }
        String content = "货主信息确认无误";
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long shipperId : shipperIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(shipperId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.SHIPPER);
            dataTracerForm.setOperateType(ShipperDataTracerOperateTypeEnum.UPDATE_VERIFY);
            dataTracerForm.setOperateContent(content);
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }

    /**
     * 记录导入新建日志
     *
     * @param shipperList
     * @param dataTracerRequestForm
     */
    @Async
    public void saveByImportLog(List<ShipperEntity> shipperList, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(shipperList)) {
            return;
        }
        String content = "根据短驳订单导入新建货主";
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (ShipperEntity shipperEntity : shipperList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(shipperEntity.getShipperId());
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.SHIPPER);
            dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
            dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
            dataTracerForm.setOperateContent(content);
            dataTracerForm.setExtraData(new DataTracerExtraData(ShipperEntity.class, null, shipperEntity));
            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }
}
