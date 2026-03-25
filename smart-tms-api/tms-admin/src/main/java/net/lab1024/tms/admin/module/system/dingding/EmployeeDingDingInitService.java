package net.lab1024.tms.admin.module.system.dingding;

import com.dingtalk.api.response.OapiV2UserListResponse;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.common.constants.CommonConst;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.admin.module.system.employee.service.EmployeeService;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEmployeeEntity;
import net.lab1024.tms.common.module.support.dingding.sync.*;
import net.lab1024.tms.common.module.support.dingding.sync.dao.DingDingDepartmentDao;
import net.lab1024.tms.common.module.support.dingding.sync.domain.DingDingDepartmentEntity;
import net.lab1024.tms.common.module.support.dingding.sync.domain.DingDingEmployeeEntity;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yandy
 * @description:
 * @date 2023/5/9 2:38 下午
 */
@Service
public class EmployeeDingDingInitService {

    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private DingDingEmployeeManager dingDingEmployeeManager;
    @Autowired
    private DingDingUserDeptService dingDingUserDeptService;
    @Autowired
    private DingDingDepartmentDao dingDingDepartmentDao;

    @Transactional(rollbackFor = Throwable.class)
    public void initUser(Long enterpriseId) {
        List<DingDingDepartmentEntity> dingDingDepartmentEntityList = dingDingDepartmentDao.selectByEnterpriseId(enterpriseId);
        if (CollectionUtils.isEmpty(dingDingDepartmentEntityList)) {
            return;
        }
        // 从钉钉查询用户列表，对比存储关联关系
        List<OapiV2UserListResponse.ListUserResponse> dingUserList = Lists.newArrayList();
        for (DingDingDepartmentEntity deptConfig : dingDingDepartmentEntityList) {
            List<OapiV2UserListResponse.ListUserResponse> dingDeptUserList = dingDingUserDeptService.queryUserList(enterpriseId, deptConfig.getDeptId());
            if (CollectionUtils.isNotEmpty(dingDeptUserList)) {
                dingUserList.addAll(dingDeptUserList);
            }
        }
        if (CollectionUtils.isEmpty(dingUserList)) {
            return;
        }
        // 查询数据库存在的员工
        List<EmployeeEntity> employeeEntityList = employeeDao.selectListByDeletedFlag(Boolean.FALSE);
        Map<String, Long> employeePhoneMap = employeeEntityList.stream().collect(Collectors.toMap(EmployeeEntity::getPhone, EmployeeEntity::getEmployeeId));
        // 查询当前企业下的员工ID列表
        List<Long> enterpriseEmployeeIdList = employeeDao.selectEmployeeIdByEnterpriseIdList(Lists.newArrayList(enterpriseId));
        // 当前企业下钉钉部门ID：TMS部门ID对应
        Map<Long, Long> deptIdMap = dingDingDepartmentEntityList.stream().collect(Collectors.toMap(DingDingDepartmentEntity::getDeptId, DingDingDepartmentEntity::getDepartmentId));

        List<EnterpriseEmployeeEntity> enterpriseEmployeeInsertList = Lists.newArrayList();
        List<DingDingEmployeeEntity> dingDingEmployeeEntityList = Lists.newArrayList();
        for (OapiV2UserListResponse.ListUserResponse item : dingUserList) {
            String actualName = item.getName(); // 姓名
            String userId = item.getUserid(); // 用户ID
            String unionId = item.getUnionid();// 用户在当前开发者企业帐号范围内的唯一标识。
            String mobile = item.getMobile();// 手机号
            List<Long> deptIdList = item.getDeptIdList();
            // 手机号已经在系统中存在，新增关联关系
            Long employeeId = employeePhoneMap.get(mobile);
            if (null == employeeId) {
                EmployeeEntity employeeEntity = new EmployeeEntity();
                employeeEntity.setActualName(actualName);
                employeeEntity.setPhone(mobile);
                Long tmsDepartmentId = deptIdMap.get(deptIdList.get(NumberConst.ZERO));
                employeeEntity.setDepartmentId(tmsDepartmentId);
                employeeEntity.setLoginName(mobile);
                employeeEntity.setLoginPwd(EmployeeService.getEncryptPwd(CommonConst.Password.DEFAULT));
                employeeEntity.setEnterpriseId(enterpriseId);
                employeeDao.insert(employeeEntity);
                employeeId = employeeEntity.getEmployeeId();
                employeePhoneMap.put(mobile, employeeId);
            }
            // 员工在当前企业不存在，则增加关联关系
            if (!enterpriseEmployeeIdList.contains(employeeId)) {
                EnterpriseEmployeeEntity enterpriseEmployeeEntity = new EnterpriseEmployeeEntity();
                enterpriseEmployeeEntity.setEmployeeId(employeeId);
                enterpriseEmployeeEntity.setEnterpriseId(enterpriseId);
                enterpriseEmployeeInsertList.add(enterpriseEmployeeEntity);
            }
            // 员工与钉钉对应关系
            DingDingEmployeeEntity dingDingEmployeeEntity = new DingDingEmployeeEntity();
            dingDingEmployeeEntity.setEnterpriseId(enterpriseId);
            dingDingEmployeeEntity.setEmployeeId(employeeId);
            dingDingEmployeeEntity.setUserId(userId);
            dingDingEmployeeEntity.setUnionId(unionId);
            dingDingEmployeeEntityList.add(dingDingEmployeeEntity);
        }
        if (CollectionUtils.isNotEmpty(dingDingEmployeeEntityList)) {
            dingDingEmployeeManager.saveBatch(dingDingEmployeeEntityList);
        }
    }
}