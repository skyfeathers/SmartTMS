package net.lab1024.tms.admin.module.business.contract.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.contract.basic.domain.ContractBuildBO;
import net.lab1024.tms.admin.module.business.contract.basic.domain.ContractQueryForm;
import net.lab1024.tms.admin.module.business.contract.basic.domain.ContractUpdateStatusForm;
import net.lab1024.tms.admin.module.business.contract.esign.ESignNoticeRecordDao;
import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import net.lab1024.tms.admin.module.business.fleet.dao.FleetDao;
import net.lab1024.tms.admin.module.business.material.cabinet.CabinetDao;
import net.lab1024.tms.admin.module.business.material.contracttype.ContractTypeDao;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.order.dao.OrderDao;
import net.lab1024.tms.admin.module.business.order.domain.dto.OrderPathDTO;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperDao;
import net.lab1024.tms.admin.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.business.waybill.domain.vo.WaybillCostVO;
import net.lab1024.tms.admin.module.business.waybill.domain.vo.WaybillDetailVO;
import net.lab1024.tms.admin.module.business.waybill.domain.vo.WaybillOilCardRechargeApplyVO;
import net.lab1024.tms.admin.module.business.waybill.domain.vo.WaybillPathVO;
import net.lab1024.tms.admin.module.business.waybill.service.WaybillService;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.*;
import net.lab1024.tms.common.module.business.contacttemplate.ContractTemplateService;
import net.lab1024.tms.common.module.business.contacttemplate.domain.form.ContractGenerateFieldValueForm;
import net.lab1024.tms.common.module.business.contacttemplate.domain.form.ContractGenerateForm;
import net.lab1024.tms.common.module.business.contract.constant.ContractSignTypeEnum;
import net.lab1024.tms.common.module.business.contract.constant.ContractSignerTypeEnum;
import net.lab1024.tms.common.module.business.contract.constant.ContractStatusEnum;
import net.lab1024.tms.common.module.business.contract.constant.ContractTemplateTypeEnum;
import net.lab1024.tms.common.module.business.contract.domain.*;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.esign.ESignService;
import net.lab1024.tms.common.module.business.esign.domain.entity.ESignNoticeRecordEntity;
import net.lab1024.tms.common.module.business.esign.domain.flow.*;
import net.lab1024.tms.common.module.business.fleet.domain.FleetEntity;
import net.lab1024.tms.common.module.business.material.contracttype.ContractTypeEntity;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemCategoryEnum;
import net.lab1024.tms.common.module.business.material.transportroute.constants.PathTypeEnum;
import net.lab1024.tms.common.module.business.oa.enterprise.dao.EnterpriseESignConfigDao;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseESignConfigEntity;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.business.order.domain.OrderEntity;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleTypeEnum;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import net.lab1024.tms.common.module.support.dict.domain.vo.DictValueVO;
import net.lab1024.tms.common.module.support.file.service.FileService;
import net.lab1024.tms.common.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.tms.common.module.support.serialnumber.service.SerialNumberService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 合同管理
 *
 * @author lihaifan
 * @date 2022/7/15 11:10
 */
@Service
@Slf4j
public class ContractService {

    @Autowired
    private ContractDao contractDao;
    @Autowired
    private ContractTypeDao contractTypeDao;
    @Autowired
    private SerialNumberService serialNumberService;
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private FleetDao fleetDao;
    @Autowired
    private ESignService eSignService;
    @Autowired
    private ESignNoticeRecordDao eSignNoticeRecordDao;
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private DriverDao driverDao;
    @Autowired
    private ContractTemplateService contractTemplateService;
    @Autowired
    private ContractManager contractManager;
    @Autowired
    private ContractSignerDao contractSignerDao;
    @Autowired
    private ContractOrgDao contractOrgDao;
    @Autowired
    private ShipperDao shipperDao;
    @Autowired
    private EnterpriseESignConfigDao enterpriseESignConfigDao;
    @Autowired
    private FileService fileService;
    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private DictCacheService dictCacheService;
    @Autowired
    private WaybillService waybillService;
    @Autowired
    private CabinetDao cabinetDao;
    @Autowired
    private ContractDataTracerService contractDataTracerService;

    /**
     * 分页查询合同模块
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<ContractVO>> queryByPage(ContractQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<ContractVO> contractVOS = contractDao.queryPage(page, queryForm);
        PageResult<ContractVO> pageResult = SmartPageUtil.convert2PageResult(page, contractVOS);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 新建合同
     *
     * @param createForm
     * @return
     */
    public ResponseDTO<String> createContract(ContractCreateForm createForm, DataTracerRequestForm dataTracerRequestForm) {
        // 线下签约
        String contractCode = StringUtils.trim(createForm.getContractCode());
        if (ContractSignTypeEnum.OFFLINE.equalsValue(createForm.getSignType()) && StringUtils.isBlank(contractCode)) {
            return ResponseDTO.userErrorParam("合同编号不能为空");
        }
        if (StringUtils.isNotBlank(contractCode) && contractCode.length() > 100) {
            return ResponseDTO.userErrorParam("合同编号不能超过100字符");
        }
        ContractTypeEntity contractTypeEntity = contractTypeDao.selectById(createForm.getContractType());
        if (contractTypeEntity == null) {
            return ResponseDTO.userErrorParam("合同类型不存在");
        }
        if (ContractSignTypeEnum.ONLINE.equalsValue(createForm.getSignType()) && contractTypeEntity.getTemplateId() == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "该类型未设置合同模板，请联系管理员设置后重试");
        }
        // 如果有是线上合同，且没有运单ID，再判断
        if (ContractSignTypeEnum.ONLINE.equalsValue(createForm.getSignType()) && createForm.getWaybillId() == null && createForm.getSignerType() == null && createForm.getSignerBelongId() == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "线上合同请选择运单/货主/车队/司机创建");
        }
        WaybillEntity waybillEntity = null;
        OrderEntity orderEntity = null;
        if (createForm.getWaybillId() != null) {
            waybillEntity = waybillDao.selectById(createForm.getWaybillId());
            if (!createForm.getEnterpriseId().equals(waybillEntity.getEnterpriseId())) {
                return ResponseDTO.userErrorParam("运单所属企业和当前登录企业不一致");
            }
            ContractEntity existEntity = contractDao.selectByWaybillIdAndExcludeStatus(createForm.getWaybillId(), ContractStatusEnum.CANCEL.getValue());
            if (existEntity != null) {
                return ResponseDTO.userErrorParam("此运单已创建过合同，请勿重复创建");
            }
            orderEntity = orderDao.selectById(waybillEntity.getOrderId());
        }
        // 设置编号
        if (StringUtils.isBlank(contractCode)) {
            contractCode = serialNumberService.generate(SerialNumberIdEnum.CONTRACT);
        }
        // 构建合同对象
        ContractEntity createEntity = this.buildContractEntity(contractCode, waybillEntity, orderEntity, createForm);
        // 线下签约保存
        if (ContractSignTypeEnum.OFFLINE.equalsValue(createEntity.getSignType())) {
            contractDao.insert(createEntity);
            contractDataTracerService.saveLog(createEntity, dataTracerRequestForm);
            return ResponseDTO.ok();
        }
        createEntity.setTemplateId(contractTypeEntity.getTemplateId());
        // 线上签署对象
        ResponseDTO<ContractBuildBO> contractBuildBOResponseDTO = buildContractBO(createEntity, contractTypeEntity);
        if (!contractBuildBOResponseDTO.getOk()) {
            return ResponseDTO.error(contractBuildBOResponseDTO);
        }
        ContractBuildBO contractBuildBO = contractBuildBOResponseDTO.getData();
        ESignFlowOneStepCreateForm eSignFlowOneStepCreateForm = contractBuildBO.getESignFlowOneStepCreateForm();
        ESignSignerAccountInfoForm signerAccountInfo = eSignFlowOneStepCreateForm.getSignerAccountInfo();
        ESignOrgAccountInfoForm orgAccountInfo = eSignFlowOneStepCreateForm.getOrgAccountInfo();
        // 状态为待签署
        createEntity.setStatus(ContractStatusEnum.WAIT_SIGN.getValue());
        ResponseDTO<String> onlineContract = createOnlineContract(contractBuildBO);
        if (!onlineContract.getOk()) {
            return ResponseDTO.userErrorParam("合同创建失败");
        }
        createEntity.setOnlineContractId(onlineContract.getData());
        // 签署人信息
        ContractSignerEntity contractSignerEntity = new ContractSignerEntity();
        contractSignerEntity.setAccount(eSignFlowOneStepCreateForm.getSignerAccount());
        contractSignerEntity.setName(signerAccountInfo.getName());
        contractSignerEntity.setCertNo(signerAccountInfo.getCertNo());
        contractSignerEntity.setCertType(signerAccountInfo.getCertType());
        contractSignerEntity.setBankCardNo(signerAccountInfo.getBankCardNo());
        // 签署企业信息
        ContractOrgEntity contractOrgEntity = null;
        if (orgAccountInfo != null) {
            contractOrgEntity = new ContractOrgEntity();
            contractOrgEntity.setAccount(eSignFlowOneStepCreateForm.getAuthorizedAccount());
            contractOrgEntity.setCertNo(orgAccountInfo.getCertNo());
            contractOrgEntity.setCertType(orgAccountInfo.getCertType());
            contractOrgEntity.setLegalRepName(orgAccountInfo.getLegalRepName());
            contractOrgEntity.setLegalRepCertNo(orgAccountInfo.getLegalRepCertNo());
            contractOrgEntity.setLegalRepCertType(orgAccountInfo.getLegalRepCertType());
        }
        contractManager.insertContract(createEntity, contractSignerEntity, contractOrgEntity);
        contractDataTracerService.saveLog(createEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    private ContractEntity buildContractEntity(String contractCode, WaybillEntity waybillEntity,OrderEntity orderEntity, ContractCreateForm createForm) {
        ContractEntity createEntity = SmartBeanUtil.copy(createForm, ContractEntity.class);
        createEntity.setContractCode(contractCode);
        createEntity.setStatus(ContractStatusEnum.WAIT_SIGN.getValue());
        createEntity.setCreateUserId(createForm.getOperatorId());
        createEntity.setUpdateUserId(createForm.getOperatorId());
        if (ContractSignTypeEnum.OFFLINE.equalsValue(createEntity.getSignType())) {
            createEntity.setStatus(ContractStatusEnum.SIGNED.getValue());
        }
        if (waybillEntity != null) {
            createEntity.setWaybillId(waybillEntity.getWaybillId());
            createEntity.setWaybillNumber(waybillEntity.getWaybillNumber());
            createEntity.setOrderId(orderEntity.getOrderId());
            createEntity.setOrderNo(orderEntity.getOrderNo());
        }
        if (waybillEntity != null && WaybillSettleTypeEnum.DRIVER.equalsValue(waybillEntity.getSettleType())) {
            createEntity.setSignerType(ContractSignerTypeEnum.DRIVER.getValue());
            createEntity.setSignerBelongId(waybillEntity.getDriverId());
        }
        if (waybillEntity != null && WaybillSettleTypeEnum.FLEET.equalsValue(waybillEntity.getSettleType())){
            createEntity.setSignerType(ContractSignerTypeEnum.FLEET.getValue());
            createEntity.setSignerBelongId(waybillEntity.getFleetId());
        }
        return createEntity;

    }

    /**
     * 构建发起合同字段
     *
     * @param createEntity
     * @return
     */
    public ResponseDTO<ContractBuildBO> buildContractBO(ContractEntity createEntity, ContractTypeEntity contractTypeEntity) {
        Long enterpriseId = createEntity.getEnterpriseId();
        // 查询企业信息
        if (enterpriseId == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "企业信息不存在");
        }
        EnterpriseEntity enterpriseEntity = enterpriseDao.selectById(enterpriseId);
        if (enterpriseEntity == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "企业信息不存在");
        }
        EnterpriseESignConfigEntity eSignConfig = enterpriseESignConfigDao.getByEnterpriseId(enterpriseId);
        if (eSignConfig == null || StringUtils.isBlank(eSignConfig.getAppId()) || StringUtils.isBlank(eSignConfig.getAppSecret())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "企业：" + enterpriseEntity.getEnterpriseName() + "，未进行电子签约配置，请联系管理员设置后重试");
        }
        // 合同文件所需字段
        ContractGenerateForm contractGenerateForm = new ContractGenerateForm();
        contractGenerateForm.setTemplateId(createEntity.getTemplateId());
        contractGenerateForm.setSerialNumber(createEntity.getContractCode());
        // e签宝所需字段
        ESignFlowOneStepCreateForm eSignFlowOneStepCreateForm = new ESignFlowOneStepCreateForm();
        eSignFlowOneStepCreateForm.setEnterpriseId(createEntity.getEnterpriseId());
        eSignFlowOneStepCreateForm.setBusinessScene(createEntity.getContractName());
        eSignFlowOneStepCreateForm.setThirdOrderNo(createEntity.getContractCode());
        // 获取签署人信息
        String signerAccount = null;
        ESignSignerAccountInfoForm signerAccountInfo = null;
        // 获取签署企业信息
        String authorizedAccount = null;
        ESignOrgAccountInfoForm orgAccountInfo = null;
        // 根据签署人类型设置
        ContractSignerTypeEnum contractSignerTypeEnum = SmartBaseEnumUtil.getEnumByValue(createEntity.getSignerType(), ContractSignerTypeEnum.class);
        switch (contractSignerTypeEnum) {
            case DRIVER:
                DriverEntity driverEntity = driverDao.selectById(createEntity.getSignerBelongId());
                if (driverEntity == null) {
                    return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "未知司机信息，请确认后重试");
                }
                signerAccount = driverEntity.getTelephone();
                signerAccountInfo = new ESignSignerAccountInfoForm();
                signerAccountInfo.setCertType("CRED_PSN_CH_IDCARD");
                signerAccountInfo.setName(driverEntity.getDriverName());
                signerAccountInfo.setCertNo(driverEntity.getDrivingLicense());
                break;
            case FLEET:
                FleetEntity fleetEntity = fleetDao.selectById(createEntity.getSignerBelongId());
                if (fleetEntity == null) {
                    return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "未知车队信息，请确认后重试");
                }
                signerAccount = fleetEntity.getCaptainPhone();
                signerAccountInfo = new ESignSignerAccountInfoForm();
                signerAccountInfo.setCertType("CRED_PSN_CH_IDCARD");
                signerAccountInfo.setName(fleetEntity.getCaptainName());
                signerAccountInfo.setCertNo(fleetEntity.getCaptainIdCard());
                break;
            case SHIPPER:
                // TODO lihaifan 签署人信息未知 signerAccount
                // 签署企业
                signerAccount = "18336769101";
                signerAccountInfo = new ESignSignerAccountInfoForm();
//                signerAccountInfo.setCertType("CRED_PSN_CH_IDCARD");
                signerAccountInfo.setName("李豆豆");
//                signerAccountInfo.setCertNo("410327199804111421");
                ShipperEntity shipperEntity = shipperDao.selectById(createEntity.getSignerBelongId());
                if (shipperEntity == null) {
                    return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "未知货主信息，请确认后重试");
                }
                authorizedAccount = shipperEntity.getConsignor();
                orgAccountInfo = new ESignOrgAccountInfoForm();
                orgAccountInfo.setCertNo(shipperEntity.getConsignorId());
                orgAccountInfo.setCertType("CRED_ORG_USCC");
                orgAccountInfo.setLegalRepName(shipperEntity.getLegalPersonName());
                orgAccountInfo.setLegalRepCertNo(shipperEntity.getLegalPersonIdNumber());
                orgAccountInfo.setLegalRepCertType("CRED_PSN_CH_IDCARD");
                break;
            default:
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "未知合同签署人类型，请确认后重试");
        }
        // 签署人校验
        if (signerAccountInfo == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "未知合同签署人");
        }
        if (StringUtils.isBlank(signerAccount)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "请完善签署人手机号后重试");
        }
        if (StringUtils.isBlank(signerAccountInfo.getName())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "请完善签署人姓名后重试");
        }
        if (StringUtils.isBlank(signerAccountInfo.getCertNo())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "请完善签署人身份证号后重试");
        }
        // e签宝签署人信息
        eSignFlowOneStepCreateForm.setSignerAccount(signerAccount);
        eSignFlowOneStepCreateForm.setSignerAccountInfo(signerAccountInfo);
        // e签宝签署企业信息
        eSignFlowOneStepCreateForm.setAuthorizedAccount(authorizedAccount);
        eSignFlowOneStepCreateForm.setOrgAccountInfo(orgAccountInfo);
        // 合同模板类型
        ContractTemplateTypeEnum templateTypeEnum = SmartBaseEnumUtil.getEnumByValue(contractTypeEntity.getTemplateType(), ContractTemplateTypeEnum.class);
        // 根据合同类型设置内容
        switch (templateTypeEnum) {
            case CONSIGNMENT_TRANSPORTATION_CONTRACT:
                // TODO lihaifan 设置模板内容
                break;
//            case WAYBILL_CONTRACT:
//                if (StringUtils.isBlank(enterpriseEntity.getNetworkFreightTransportCode())) {
//                    return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "请先完善企业：" + enterpriseEntity.getEnterpriseName() + "，道路运输经营许可证");
//                }
//                // 合同文件填充字段
//                List<ContractGenerateFieldValueForm> fields = new ArrayList<>();
//                fields.add(ContractGenerateFieldValueForm.builder()
//                        .fieldKey("driverName")
//                        .fieldValue(signerAccountInfo.getName())
//                        .build());
//                fields.add(ContractGenerateFieldValueForm.builder()
//                        .fieldKey("enterpriseName")
//                        .fieldValue(enterpriseEntity.getEnterpriseName())
//                        .build());
//                fields.add(ContractGenerateFieldValueForm.builder()
//                        .fieldKey("networkFreightTransportCode")
//                        .fieldValue(enterpriseEntity.getNetworkFreightTransportCode())
//                        .build());
//                contractGenerateForm.setFieldValueList(fields);
//                // 设置签署关键字
//                eSignFlowOneStepCreateForm.setPlatformKeyword("签章");
//                eSignFlowOneStepCreateForm.setPlatformTimeKeyword(null);
//                eSignFlowOneStepCreateForm.setUserKeyword("签字");
//                eSignFlowOneStepCreateForm.setUserTimeKeyword(null);
//                break;
            case YX_ROAD_TRANSPORT_CONTRACT:
                ShipperEntity shipperEntity = shipperDao.selectById(createEntity.getSignerBelongId());
                // 合同文件填充字段
                List<ContractGenerateFieldValueForm> yxRoadContractFields = new ArrayList<>();
                yxRoadContractFields.add(ContractGenerateFieldValueForm.builder()
                        .fieldKey("contractCode")
                        .fieldValue(createEntity.getContractCode())
                        .build());
                yxRoadContractFields.add(ContractGenerateFieldValueForm.builder()
                        .fieldKey("shipperName")
                        .fieldValue(shipperEntity.getConsignor())
                        .build());
                // TODO 设置合同有效期、签订日期、甲方乙方的联系信息
                contractGenerateForm.setFieldValueList(yxRoadContractFields);
                // 设置签署关键字
                eSignFlowOneStepCreateForm.setPlatformKeyword("乙方（盖章）");
                eSignFlowOneStepCreateForm.setPlatformTimeKeyword(null);
                eSignFlowOneStepCreateForm.setUserKeyword("甲方（盖章）");
                eSignFlowOneStepCreateForm.setUserTimeKeyword(null);
                break;
            case CONTAINER_CONTRACT:
                ResponseDTO<List<ContractGenerateFieldValueForm>> fieldsResponse = buildContainerContractField(createEntity, enterpriseEntity);
                if (!fieldsResponse.getOk()) {
                    return ResponseDTO.userErrorParam(fieldsResponse.getMsg());
                }
                contractGenerateForm.setFieldValueList(fieldsResponse.getData());
                // 设置签署关键字
                eSignFlowOneStepCreateForm.setPlatformKeyword("甲方代表签字");
                eSignFlowOneStepCreateForm.setPlatformTimeKeyword(null);
                eSignFlowOneStepCreateForm.setUserKeyword("乙方代表签字");
                eSignFlowOneStepCreateForm.setUserTimeKeyword(null);
                break;
            default:
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "未知合同类型，请确认后重试");
        }
        return ResponseDTO.ok(new ContractBuildBO(createEntity.getEnterpriseId(), contractGenerateForm, eSignFlowOneStepCreateForm));
    }

    /**
     * 构建集装箱合同参数
     *
     * @param contractEntity
     * @param enterpriseEntity
     * @return
     */
    private ResponseDTO<List<ContractGenerateFieldValueForm>> buildContainerContractField(ContractEntity contractEntity, EnterpriseEntity enterpriseEntity) {
        Long driverId = contractEntity.getSignerBelongId();
        DriverEntity driverEntity = driverDao.selectById(driverId);
        if (null == driverEntity) {
            return ResponseDTO.userErrorParam("司机信息不存在");
        }
        WaybillEntity waybillEntity = waybillDao.selectById(contractEntity.getWaybillId());

        ResponseDTO<WaybillDetailVO> waybillResponse = waybillService.detail(contractEntity.getWaybillId());
        if (!waybillResponse.getOk()) {
            return ResponseDTO.userErrorParam(waybillResponse.getMsg());
        }
        WaybillDetailVO waybillDetailVO = waybillResponse.getData();


        Long vehicleId = waybillEntity.getVehicleId();
        VehicleEntity vehicleEntity = vehicleDao.selectById(vehicleId);
        DictValueVO dictValueVO = dictCacheService.selectValueByValueCode(vehicleEntity.getVehicleType());
        // 合同文件填充字段
        List<ContractGenerateFieldValueForm> fields = new ArrayList<>();
        fields.add(ContractGenerateFieldValueForm.builder().fieldKey("contractCode")
                .fieldValue(contractEntity.getContractCode()).build());
        fields.add(ContractGenerateFieldValueForm.builder().fieldKey("enterpriseName")
                .fieldValue(enterpriseEntity.getEnterpriseName()).build());
        // 驾驶员信息
        fields.add(ContractGenerateFieldValueForm.builder().fieldKey("driverName")
                .fieldValue(driverEntity.getDriverName()).build());
        fields.add(ContractGenerateFieldValueForm.builder().fieldKey("drivingLicenseNo")
                .fieldValue(driverEntity.getDrivingLicenseNo()).build());
        fields.add(ContractGenerateFieldValueForm.builder().fieldKey("homeAddress")
                .fieldValue(driverEntity.getHomeAddress()).build());
        fields.add(ContractGenerateFieldValueForm.builder().fieldKey("vehicleNumber")
                .fieldValue(vehicleEntity.getVehicleNumber()).build());
        fields.add(ContractGenerateFieldValueForm.builder().fieldKey("vehicleTypeDesc")
                .fieldValue(null != dictValueVO ? dictValueVO.getValueName() : StringConst.EMPTY).build());
        // 第一点
        fields.add(ContractGenerateFieldValueForm.builder().fieldKey("cabinetName")
                .fieldValue(waybillDetailVO.getCabinetName()).build());
        fields.add(ContractGenerateFieldValueForm.builder().fieldKey("containerNumber")
                .fieldValue(waybillDetailVO.getContainerNumber()).build());
        fields.add(ContractGenerateFieldValueForm.builder().fieldKey("goodsChineseTotalAmount")
                .fieldValue(MoneyUtil.toChinese(waybillDetailVO.getReceiveAmount().toString())).build());
        fields.add(ContractGenerateFieldValueForm.builder().fieldKey("goodsTotalAmount")
                .fieldValue(waybillDetailVO.getReceiveAmount().toString()).build());

        // 第二点
        List<WaybillPathVO> waybillPathList = waybillDetailVO.getPathList();
        String deliverGoodsTime = StringConst.EMPTY;
        if (null != waybillDetailVO.getDeliverGoodsTime()) {
            deliverGoodsTime = SmartLocalDateUtil.formatYMDHMS(waybillDetailVO.getDeliverGoodsTime());
        }
        fields.add(ContractGenerateFieldValueForm.builder().fieldKey("deliverGoodsTime")
                .fieldValue(deliverGoodsTime).build());
        fields.add(ContractGenerateFieldValueForm.builder().fieldKey("containerLocation")
                .fieldValue(getPathAddress(waybillPathList, PathTypeEnum.CONTAINER_LOCATION)).build());
        fields.add(ContractGenerateFieldValueForm.builder().fieldKey("loadTime")
                .fieldValue(deliverGoodsTime).build());
        fields.add(ContractGenerateFieldValueForm.builder().fieldKey("placingLocation")
                .fieldValue(getPathAddress(waybillPathList, PathTypeEnum.PLACING_LOADING)).build());

        String receiveGoodsTime = StringConst.EMPTY;
        if (null != waybillDetailVO.getDeliverGoodsTime()) {
            receiveGoodsTime = SmartLocalDateUtil.formatYMDHMS(waybillDetailVO.getReceiveGoodsTime());
        }
        fields.add(ContractGenerateFieldValueForm.builder().fieldKey("unloadingTime")
                .fieldValue(receiveGoodsTime).build());
        fields.add(ContractGenerateFieldValueForm.builder().fieldKey("unloadingLocation")
                .fieldValue(getPathAddress(waybillPathList, PathTypeEnum.UNLOADING_LOCATION)).build());

        // 第三点
        fields.add(ContractGenerateFieldValueForm.builder().fieldKey("freightChineseAmount")
                .fieldValue(MoneyUtil.toChinese(waybillDetailVO.getPayableAmount().toString())).build());
        fields.add(ContractGenerateFieldValueForm.builder().fieldKey("freightAmount")
                .fieldValue(waybillDetailVO.getPayableAmount().toString()).build());
        // 甲方代表
        fields.add(ContractGenerateFieldValueForm.builder().fieldKey("customerServiceName")
                .fieldValue(waybillDetailVO.getOrderCreateUserName()).build());

        String signDate = StringConst.EMPTY;
        if (null != waybillDetailVO.getDeliverGoodsTime()) {
            signDate = SmartLocalDateUtil.formatChineseYMD(waybillDetailVO.getDeliverGoodsTime());
        }
        // 签订日期
        fields.add(ContractGenerateFieldValueForm.builder().fieldKey("signDate")
                .fieldValue(signDate).build());
        return ResponseDTO.ok(fields);
    }

    /**
     * 获取详细地址
     *
     * @param pathList
     * @param pathTypeEnum
     * @return
     */
    private String getPathAddress(List<WaybillPathVO> pathList, PathTypeEnum pathTypeEnum) {
        Optional<WaybillPathVO> containerLocationOptional = pathList.stream().filter(e -> pathTypeEnum.equalsValue(e.getType())).findFirst();
        String containerLocation = StringConst.EMPTY;
        if (containerLocationOptional.isPresent()) {
            WaybillPathVO pathDTO = containerLocationOptional.get();
            containerLocation = (pathDTO.getProvinceName() + pathDTO.getCityName() + pathDTO.getDistrictName() + pathDTO.getAddress()).replaceAll("null", "");
        }
        return containerLocation;
    }


    /**
     * 批量更新状态
     *
     * @param batchUpdateStatusForm
     * @return
     */
    public ResponseDTO<String> batchUpdateStatus(ContractUpdateStatusForm batchUpdateStatusForm) {
        List<Long> contractIdList = batchUpdateStatusForm.getContractIdList();
        List<ContractEntity> contractEntities = contractDao.selectBatchIds(contractIdList);
        contractEntities = contractEntities.stream().filter(e -> BooleanUtils.isFalse(e.getDeletedFlag())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(contractEntities) || contractEntities.size() != contractIdList.size()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        // 判断是否包含电子合同
        boolean havaOnlineContractFlag = contractEntities.stream().anyMatch(e -> StringUtils.isNotBlank(e.getOnlineContractId()));
        if (BooleanUtils.isTrue(havaOnlineContractFlag) && ContractStatusEnum.CANCEL.equalsValue(batchUpdateStatusForm.getStatus())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "包含电子签约合同，请走撤销流程");
        }
        contractDao.batchUpdateStatus(batchUpdateStatusForm);
        return ResponseDTO.ok();
    }

    /**
     * 发起合同
     *
     * @param contractBuildBO
     * @return
     */
    public ResponseDTO<String> createOnlineContract(ContractBuildBO contractBuildBO) {
        ContractGenerateForm generateForm = contractBuildBO.getContractGenerateForm();
        ESignFlowOneStepCreateForm eSignFlowOneStepCreateForm = contractBuildBO.getESignFlowOneStepCreateForm();
        // 生成合同模板
        ResponseDTO<File> fileResponseDTO = contractTemplateService.contractGenerate(generateForm);
        if (!fileResponseDTO.getOk()) {
            return ResponseDTO.error(fileResponseDTO);
        }
        // 上传合同
        File data = fileResponseDTO.getData();
        try {
            String eSignFileId = eSignService.uploadFile(data, contractBuildBO.getEnterpriseId());
            if (StringUtils.isBlank(eSignFileId)) {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "合同上传失败");
            }
            // 发起签署
            eSignFlowOneStepCreateForm.setFileId(eSignFileId);
            ESignOneStepVO flowOneStep = eSignService.createFlowOneStep(eSignFlowOneStepCreateForm);
            String flowId = flowOneStep.getFlowId();
            return ResponseDTO.ok(flowId);
//            return ResponseDTO.ok();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发起合同失败：{}", e);
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "发起合同失败");
        } finally {
            if (data != null && data.exists() && data.isFile()) {
                data.delete();
            }
        }
    }

    /**
     * 获取线上合同预览url
     *
     * @param contractId
     * @return
     */
    public ResponseDTO<ESignExecuteUrlVO> getExecuteUrl(Long contractId, Boolean signedFlag) {
        ContractEntity contractEntity = contractDao.selectById(contractId);
        if (contractEntity == null || BooleanUtils.isTrue(contractEntity.getDeletedFlag())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "合同不存在");
        }
        String onlineContractId = contractEntity.getOnlineContractId();
        if (StringUtils.isBlank(onlineContractId)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "仅线上合同可预览");
        }
        // 查询关联签署人 签署企业
        ContractSignerEntity contractSignerEntity = contractSignerDao.getByContractId(contractId);
        String signerAccount = contractSignerEntity == null ? null : contractSignerEntity.getAccount();
        ContractOrgEntity contractOrgEntity = contractOrgDao.getByContractId(contractId);
        String authorizedAccount = contractOrgEntity == null ? null : contractOrgEntity.getAccount();
        // 构建查询参数
        ESignExecuteUrlForm executeUrlForm = new ESignExecuteUrlForm();
        executeUrlForm.setEnterpriseId(contractEntity.getEnterpriseId());
        executeUrlForm.setFlowId(onlineContractId);
        // 1 - 预览链接（只能查看，不能签署）  2 - 签署链接
        executeUrlForm.setUrlType(BooleanUtils.isTrue(signedFlag) ? 2 : 1);
        executeUrlForm.setClientType("ALL");
        executeUrlForm.setAppScheme(null);
        executeUrlForm.setSignerAccount(signerAccount);
        executeUrlForm.setAuthorizedAccount(authorizedAccount);
        executeUrlForm.setAccountId(null);
        executeUrlForm.setAuthorizedAccountId(null);
        ESignExecuteUrlVO executeUrl = eSignService.getExecuteUrl(executeUrlForm);
        if (executeUrl == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "获取合同地址失败");
        }
        return ResponseDTO.ok(executeUrl);
    }

    /**
     * 下载合同
     *
     * @param contractId
     * @return
     */
    public ResponseDTO<String> downloadContract(Long contractId) {
        ContractEntity contractEntity = contractDao.selectById(contractId);
        if (contractEntity == null || BooleanUtils.isTrue(contractEntity.getDeletedFlag())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "合同不存在");
        }
        String contractUrl = contractEntity.getContractFile();
        String onlineContractId = contractEntity.getOnlineContractId();
        if (StringUtils.isNotBlank(onlineContractId)) {
            ESignDocumentsVO documents = eSignService.getDocuments(onlineContractId, contractEntity.getEnterpriseId());
            if (documents == null) {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "获取合同下载地址失败");
            }
            List<ESignDocumentsItemVO> docs = documents.getDocs();
            if (CollectionUtils.isEmpty(docs)) {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "获取合同下载地址失败");
            }
            ESignDocumentsItemVO eSignDocumentsItemVO = docs.get(0);
            contractUrl = eSignDocumentsItemVO.getFileUrl();
        } else {
            ResponseDTO<String> fileUrl = fileService.getFileUrl(contractUrl);
            if (!fileUrl.getOk()) {
                return ResponseDTO.error(fileUrl);
            }
            contractUrl = fileUrl.getData();
        }
        if (StringUtils.isBlank(contractUrl)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "合同地址不存在");
        }
        return ResponseDTO.ok(contractUrl);
    }

    /**
     * 查询线上合同操作记录
     *
     * @param contractId
     * @return
     */
    public ResponseDTO<List<ContractOnlineRecordVO>> getOnlineRecords(Long contractId) {
        ContractEntity contractEntity = contractDao.selectById(contractId);
        if (contractEntity == null || BooleanUtils.isTrue(contractEntity.getDeletedFlag())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "合同不存在");
        }
        String onlineContractId = contractEntity.getOnlineContractId();
        if (StringUtils.isBlank(onlineContractId)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }
        List<ESignNoticeRecordEntity> eSignNoticeRecordEntities = eSignNoticeRecordDao.queryByFlowId(onlineContractId);
        List<ContractOnlineRecordVO> contractOnlineRecordVOS = SmartBeanUtil.copyList(eSignNoticeRecordEntities, ContractOnlineRecordVO.class);
        return ResponseDTO.ok(contractOnlineRecordVOS);
    }

    /**
     * 合同撤销
     *
     * @param contractId
     * @return
     */
    public ResponseDTO<String> revoke(Long contractId) {
        ContractEntity contractEntity = contractDao.selectById(contractId);
        if (contractEntity == null || BooleanUtils.isTrue(contractEntity.getDeletedFlag())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "合同不存在");
        }
        String onlineContractId = contractEntity.getOnlineContractId();
        if (StringUtils.isBlank(onlineContractId)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "仅线上合同可撤销");
        }
        eSignService.revoke(onlineContractId, contractEntity.getEnterpriseId());
        // 更新合同状态
        ContractEntity updateEntity = new ContractEntity();
        updateEntity.setContractId(contractEntity.getContractId());
        updateEntity.setStatus(ContractStatusEnum.CANCEL.getValue());
        contractDao.updateById(updateEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新合同
     * @param updateForm
     * @return
     */
    public ResponseDTO<String> updateContract(ContractUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        ContractEntity contractEntity = contractDao.selectById(updateForm.getContractId());
        if (contractEntity == null || contractEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "合同不存在");
        }
        if (!contractEntity.getSignType().equals(updateForm.getSignType())) {
            return ResponseDTO.userErrorParam("合同签订形式无法修改");
        }
        if (!ContractSignTypeEnum.OFFLINE.equalsValue(updateForm.getSignType())) {
            return ResponseDTO.userErrorParam("目前只支持线下合同修改");
        }
        String contractCode = StringUtils.trim(updateForm.getContractCode());
        // 合同编号不能为空
        if (StringUtils.isBlank(contractCode)) {
            return ResponseDTO.userErrorParam("合同编号不能为空");
        }
        if (contractCode.length() > 100) {
            return ResponseDTO.userErrorParam("合同编号不能超过100字符");
        }
        // 合同对象
        ContractEntity updateEntity = new ContractEntity();
        updateEntity.setContractId(updateForm.getContractId());
        updateEntity.setContractCode(updateForm.getContractCode());
        updateEntity.setContractName(updateForm.getContractName());
        updateEntity.setContractType(updateForm.getContractType());
        updateEntity.setExpirationDate(updateForm.getExpirationDate());
        updateEntity.setResponsibleUserId(updateForm.getResponsibleUserId());
        updateEntity.setContractFile(updateForm.getContractFile());
        updateEntity.setRemark(updateForm.getRemark());
        contractDao.updateById(updateEntity);
        contractDataTracerService.updateLog(updateEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }
}
