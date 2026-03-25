package net.lab1024.tms.admin.module.business.oa.enterprise.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.oa.enterprise.domain.form.*;
import net.lab1024.tms.admin.module.business.oa.enterprise.domain.vo.EnterpriseEmployeeVO;
import net.lab1024.tms.admin.module.business.oa.enterprise.domain.vo.EnterpriseListVO;
import net.lab1024.tms.admin.module.business.oa.enterprise.domain.vo.EnterpriseVO;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperDao;
import net.lab1024.tms.admin.module.system.department.dao.DepartmentDao;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.admin.module.system.employee.service.EmployeeService;
import net.lab1024.tms.admin.module.system.role.dao.RoleDao;
import net.lab1024.tms.common.module.system.role.domain.entity.RoleEntity;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.enumeration.GenderEnum;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.module.business.oa.enterprise.constant.EnterpriseTypeEnum;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;
import net.lab1024.tms.common.module.support.systemconfig.SystemConfigKeyEnum;
import net.lab1024.tms.common.module.support.systemconfig.SystemConfigService;
import net.lab1024.tms.common.module.system.department.domain.DepartmentEntity;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * OA企业模块
 *
 * @author lihaifan
 * @date 2022/6/22 16:49
 */
@Service
@Slf4j
public class EnterpriseService {

    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private ShipperDao shipperDao;
    @Autowired
    private EnterpriseBusinessSettingService enterpriseBusinessSettingService;
    @Autowired
    private SystemConfigService systemConfigService;
    /**
     * 分页查询企业模块
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResult<EnterpriseVO>> queryByPage(EnterpriseQueryForm queryDTO) {
        queryDTO.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<EnterpriseVO> enterpriseVOS = enterpriseDao.queryPage(page, queryDTO);
        PageResult<EnterpriseVO> pageResult = SmartPageUtil.convert2PageResult(page, enterpriseVOS);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 查询企业详情
     *
     * @param enterpriseId
     * @return
     */
    public EnterpriseVO getDetail(Long enterpriseId) {
        EnterpriseVO enterpriseDetail = enterpriseDao.getDetail(enterpriseId, Boolean.FALSE);
        return enterpriseDetail;
    }

    /**
     * 新建企业
     *
     * @param createForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> createEnterprise(EnterpriseCreateForm createForm) {
        // 验证企业名称是否重复
        EnterpriseEntity validateEnterprise = enterpriseDao.queryByEnterpriseName(createForm.getEnterpriseName(), null, Boolean.FALSE);
        if (Objects.nonNull(validateEnterprise)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "企业名称重复");
        }
        // 验证企业域名是否重复
        ResponseDTO<String> checkResp = this.checkDomainName(createForm.getDomainName(), null);
        if (!checkResp.getOk()) {
            return checkResp;
        }
        // 数据插入
        EnterpriseEntity insertEnterprise = SmartBeanUtil.copy(createForm, EnterpriseEntity.class);
        enterpriseDao.insert(insertEnterprise);
        this.initAccount(insertEnterprise.getEnterpriseId());

        return ResponseDTO.ok();
    }



    /**
     * 编辑企业
     *
     * @param updateForm
     * @return
     */
    public ResponseDTO<String> updateEnterprise(EnterpriseUpdateForm updateForm) {
        Long enterpriseId = updateForm.getEnterpriseId();
        // 校验企业是否存在
        EnterpriseEntity enterpriseDetail = enterpriseDao.selectById(enterpriseId);
        if (Objects.isNull(enterpriseDetail) || enterpriseDetail.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "企业不存在");
        }
        // 验证企业名称是否重复
        EnterpriseEntity validateEnterprise = enterpriseDao.queryByEnterpriseName(updateForm.getEnterpriseName(), enterpriseId, Boolean.FALSE);
        if (Objects.nonNull(validateEnterprise)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "企业名称重复");
        }
        // 验证企业域名是否重复
        ResponseDTO<String> checkResp = this.checkDomainName(updateForm.getDomainName(), updateForm.getEnterpriseId());
        if (!checkResp.getOk()) {
            return checkResp;
        }
        // 数据编辑
        EnterpriseEntity updateEnterprise = SmartBeanUtil.copy(updateForm, EnterpriseEntity.class);
        enterpriseDao.updateById(updateEnterprise);
        return ResponseDTO.ok();
    }


    /**
     * 删除企业
     *
     * @param enterpriseId
     * @return
     */
    public ResponseDTO<String> deleteEnterprise(Long enterpriseId) {
        // 校验企业是否存在
        EnterpriseEntity enterpriseEntity = enterpriseDao.selectById(enterpriseId);
        if (Objects.isNull(enterpriseEntity) || enterpriseEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "企业不存在");
        }
        if (enterpriseEntity.getPlatformFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "此企业是平台管理端账号，无法删除");
        }
        enterpriseDao.deleteEnterprise(enterpriseId, Boolean.TRUE);
        return ResponseDTO.ok();
    }

    /**
     * 企业列表查询
     *
     * @return
     */
    public ResponseDTO<List<EnterpriseListVO>> queryList(Long enterpriseId, Long employeeId, Integer type) {
        EnterpriseEntity enterpriseEntity = enterpriseDao.selectById(enterpriseId);
        EnterpriseListVO enterpriseListVO = SmartBeanUtil.copy(enterpriseEntity, EnterpriseListVO.class);
        List<EnterpriseListVO> enterpriseListVOS = Lists.newArrayList(enterpriseListVO);
        return ResponseDTO.ok(enterpriseListVOS);
    }

    /**
     * 查询网络货运企业列表
     *
     * @return
     */
    public ResponseDTO<List<EnterpriseListVO>> queryNftList() {
        List<EnterpriseListVO> enterpriseListVOS = enterpriseDao.queryNftList(EnterpriseTypeEnum.NFT.getValue(), Boolean.FALSE, Boolean.FALSE);
        return ResponseDTO.ok(enterpriseListVOS);
    }

    /**
     * 根据货主查询企业列表
     *
     * @return
     */
    public List<EnterpriseListVO> queryListByShipperId(Long shipperId) {
        ShipperEntity shipperEntity = shipperDao.selectById(shipperId);
        List<EnterpriseListVO> enterpriseListVOS = enterpriseDao.queryListByEnterpriseIdList(Arrays.asList(shipperEntity.getEnterpriseId()), Boolean.FALSE, Boolean.FALSE);
        return enterpriseListVOS;
    }

    /**
     * 企业添加员工
     *
     * @param enterpriseEmployeeForm
     * @return
     */
    public ResponseDTO<String> addEmployee(EnterpriseEmployeeForm enterpriseEmployeeForm) {
        return ResponseDTO.ok();
    }

    /**
     * 企业删除员工
     *
     * @param enterpriseEmployeeForm
     * @return
     */
    public ResponseDTO<String> deleteEmployee(EnterpriseEmployeeForm enterpriseEmployeeForm) {
        return ResponseDTO.ok();
    }

    /**
     * 企业下员工列表
     *
     * @param enterpriseId
     * @return
     */
    public ResponseDTO<List<EnterpriseEmployeeVO>> employeeList(Long enterpriseId) {
        List<Long> employeeIdList = employeeDao.getEmployeeId(enterpriseId, false, false);
        if (CollectionUtils.isEmpty(employeeIdList)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }
        List<EmployeeEntity> employeeEntityList = employeeDao.selectBatchIds(employeeIdList);
        List<EnterpriseEmployeeVO> enterpriseEmployeeVOList = SmartBeanUtil.copyList(employeeEntityList, EnterpriseEmployeeVO.class);

        Set<Long> enterpriseIdSet = employeeEntityList.stream().map(EmployeeEntity::getEnterpriseId).collect(Collectors.toSet());
        List<EnterpriseEntity> enterpriseEntityList = enterpriseDao.selectBatchIds(enterpriseIdSet);
        Map<Long,String> enterpriseNameMap = enterpriseEntityList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));
        enterpriseEmployeeVOList.forEach(e->e.setEnterpriseName(enterpriseNameMap.getOrDefault(e.getEnterpriseId(), "")));

        return ResponseDTO.ok(enterpriseEmployeeVOList);
    }

    public ResponseDTO<String> updateWebsiteDesc(EnterpriseWebsiteDescUpdateForm updateForm) {
        Long enterpriseId = updateForm.getEnterpriseId();
        // 校验企业是否存在
        EnterpriseEntity enterpriseDetail = enterpriseDao.selectById(enterpriseId);
        if (Objects.isNull(enterpriseDetail) || enterpriseDetail.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "企业不存在");
        }
        // 数据编辑
        EnterpriseEntity updateEnterprise = new EnterpriseEntity();
        updateEnterprise.setEnterpriseId(updateForm.getEnterpriseId());
        updateEnterprise.setWebsiteDesc(updateForm.getWebsiteDesc());
        enterpriseDao.updateById(updateEnterprise);
        return ResponseDTO.ok();
    }

    public ResponseDTO<String> updateDomainName(EnterpriseDomainNameUpdateForm updateForm) {
        Long enterpriseId = updateForm.getEnterpriseId();
        // 校验企业是否存在
        EnterpriseEntity enterpriseDetail = enterpriseDao.selectById(enterpriseId);
        if (Objects.isNull(enterpriseDetail) || enterpriseDetail.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "企业不存在");
        }
        // 验证企业域名是否重复
        ResponseDTO<String> checkResp = this.checkDomainName(updateForm.getDomainName(), updateForm.getEnterpriseId());
        if (!checkResp.getOk()) {
            return checkResp;
        }
        // 数据编辑
        EnterpriseEntity updateEnterprise = new EnterpriseEntity();
        updateEnterprise.setEnterpriseId(updateForm.getEnterpriseId());
        updateEnterprise.setDomainName(updateForm.getDomainName());
        enterpriseDao.updateById(updateEnterprise);
        return ResponseDTO.ok();
    }

    private ResponseDTO<String> checkDomainName(String domainName, Long excludeEnterpriseId){
        if (StringUtils.isBlank(domainName)) {
            return ResponseDTO.ok();
        }
        List<String> dbDomainName = enterpriseDao.queryByDomainName(excludeEnterpriseId, false);
        if(CollectionUtils.isEmpty(dbDomainName)) {
            return ResponseDTO.ok();
        }
        List<String> dbDomainNameList = Lists.newArrayList();
        dbDomainName.forEach(e->{
            dbDomainNameList.addAll(SmartStringUtil.splitConvertToList(e, ","));
        });
        List<String> checkDomainNameList = SmartStringUtil.splitConvertToList(domainName, ",");
        boolean hasMatch = checkDomainNameList.stream().allMatch(dbDomainNameList::contains);
        if (hasMatch) {
            return ResponseDTO.userErrorParam("域名与其他企业域名重复");
        }
        return ResponseDTO.ok();
    }

    public ResponseDTO<String> initEnterpriseAccount(Long enterpriseId) {
        // 校验企业是否存在
        EnterpriseEntity enterpriseEntity = enterpriseDao.selectById(enterpriseId);
        if (Objects.isNull(enterpriseEntity) || enterpriseEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "企业不存在");
        }
        if (StringUtils.isBlank(enterpriseEntity.getEnterpriseShortName())){
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "请先维护企业简称以便初始化部门数据");
        }
        if (StringUtils.isBlank(enterpriseEntity.getContactPhone()) || StringUtils.isBlank(enterpriseEntity.getContact())){
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "请先维护联系人信息以便初始化员工数据");
        }
        DepartmentEntity rootDepartment = departmentDao.selectByParentId(enterpriseId, NumberUtils.LONG_ZERO);
        if (rootDepartment == null ) {
            rootDepartment = this.initDepartment(enterpriseEntity);
        }
        Integer employeeCount = employeeDao.countByEnterpriseId(enterpriseId, false);
        if (employeeCount == 0 ) {
            this.initEmployee(enterpriseEntity, rootDepartment.getDepartmentId());
        }
        List<RoleEntity> roleEntityList = roleDao.selectByEnterpriseId(enterpriseId);
        List<String> roleCodeList = roleEntityList.stream().filter(e->StringUtils.isNotBlank(e.getRoleCode())).map(RoleEntity::getRoleCode).collect(Collectors.toList());
        String saleRoleCode = systemConfigService.getConfigValue(SystemConfigKeyEnum.SALES_ROLE_CODE);
        String customerRoleCode = systemConfigService.getConfigValue(SystemConfigKeyEnum.CUSTOMER_SERVICE_ROLE_CODE);
        String scheduleRoleCode = systemConfigService.getConfigValue(SystemConfigKeyEnum.SCHEDULE_ROLE_CODE);
        if (!roleCodeList.contains(saleRoleCode)){
            this.initRole(enterpriseId, "销售", saleRoleCode);
        }
        if (!roleCodeList.contains(customerRoleCode)){
            this.initRole(enterpriseId, "客服", customerRoleCode);
        }
        if (!roleCodeList.contains(scheduleRoleCode)){
            this.initRole(enterpriseId, "调度", scheduleRoleCode);
        }
        return ResponseDTO.ok();
    }

    private void initAccount(Long enterpriseId){
        EnterpriseEntity enterpriseEntity = enterpriseDao.selectById(enterpriseId);
        // 初始化部门
        DepartmentEntity departmentEntity = this.initDepartment(enterpriseEntity);
        // 初始化员工
        this.initEmployee(enterpriseEntity, departmentEntity.getDepartmentId());
        // 初始化角色
        String saleRoleCode = systemConfigService.getConfigValue(SystemConfigKeyEnum.SALES_ROLE_CODE);
        this.initRole(enterpriseId, "销售", saleRoleCode);

        String customerRoleCode = systemConfigService.getConfigValue(SystemConfigKeyEnum.CUSTOMER_SERVICE_ROLE_CODE);
        this.initRole(enterpriseId, "客服", customerRoleCode);

        String scheduleRoleCode = systemConfigService.getConfigValue(SystemConfigKeyEnum.SCHEDULE_ROLE_CODE);
        this.initRole(enterpriseId, "调度", scheduleRoleCode);

        String financeRoleCode = systemConfigService.getConfigValue(SystemConfigKeyEnum.FINANCE_ROLE_CODE);
        this.initRole(enterpriseId, "财务", financeRoleCode);

        String summaryRoleCode = systemConfigService.getConfigValue(SystemConfigKeyEnum.SUMMARY_ROLE_CODE);
        this.initRole(enterpriseId, "统计", summaryRoleCode);

        String hrRoleCode = systemConfigService.getConfigValue(SystemConfigKeyEnum.HR_ROLE_CODE);
        this.initRole(enterpriseId, "人事", hrRoleCode);
    }

    private void initEmployee(EnterpriseEntity enterpriseEntity, Long departmentId) {
        String password = EmployeeService.getEncryptPwd(enterpriseEntity.getContactPhone());
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setLoginName(enterpriseEntity.getContactPhone());
        employeeEntity.setLoginPwd(password);
        employeeEntity.setActualName(enterpriseEntity.getContact());
        employeeEntity.setGender(GenderEnum.UNKNOWN.getValue());
        employeeEntity.setPhone(enterpriseEntity.getContactPhone());
        employeeEntity.setDepartmentId(departmentId);
        employeeEntity.setEnterpriseId(enterpriseEntity.getEnterpriseId());
        employeeEntity.setAdministratorFlag(true);
        employeeEntity.setLeaveFlag(false);
        employeeEntity.setLastEditPwdDate(LocalDate.now());
        employeeEntity.setDisabledFlag(false);
        employeeEntity.setDeletedFlag(false);
        employeeDao.insert(employeeEntity);
    }

    private void initRole(Long enterpriseId,String roleName, String roleCode) {
        RoleEntity scheduleRoleEntity = new RoleEntity();
        scheduleRoleEntity.setEnterpriseId(enterpriseId);
        scheduleRoleEntity.setRoleName(roleName);
        scheduleRoleEntity.setRoleCode(roleCode);
        scheduleRoleEntity.setSystemFlag(true);
        roleDao.insert(scheduleRoleEntity);
    }

    private DepartmentEntity initDepartment(EnterpriseEntity enterpriseEntity) {
        DepartmentEntity departmentEntity = new DepartmentEntity();
        departmentEntity.setEnterpriseId(enterpriseEntity.getEnterpriseId());
        departmentEntity.setName(enterpriseEntity.getEnterpriseShortName());
        departmentEntity.setParentId(0L);
        departmentEntity.setSort(0);
        departmentDao.insert(departmentEntity);
        return departmentEntity;
    }


}
