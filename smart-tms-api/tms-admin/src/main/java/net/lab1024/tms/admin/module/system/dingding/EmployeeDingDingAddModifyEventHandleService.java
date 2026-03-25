package net.lab1024.tms.admin.module.system.dingding;

import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.common.constants.CommonConst;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.admin.module.system.employee.service.EmployeeService;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.module.support.dingding.callback.domain.dto.DingDingEventUserDTO;
import net.lab1024.tms.common.module.support.dingding.callback.service.IDingCallBackHandleService;
import net.lab1024.tms.common.module.support.dingding.constants.DingDingEventTypeEnum;
import net.lab1024.tms.common.module.support.dingding.sync.DingDingUserDeptService;
import net.lab1024.tms.common.module.support.dingding.sync.dao.DingDingDepartmentDao;
import net.lab1024.tms.common.module.support.dingding.sync.dao.DingDingEmployeeDao;
import net.lab1024.tms.common.module.support.dingding.sync.domain.DingDingDepartmentEntity;
import net.lab1024.tms.common.module.support.dingding.sync.domain.DingDingEmployeeEntity;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yandy
 * @description:
 * @date 2023/5/9 12:45 上午
 */
@Service
public class EmployeeDingDingAddModifyEventHandleService implements IDingCallBackHandleService<DingDingEventUserDTO> {

    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private DingDingEmployeeDao dingDingEmployeeDao;
    @Autowired
    private DingDingUserDeptService dingDingUserDeptService;
    @Autowired
    private DingDingDepartmentDao dingDingDepartmentDao;
    @Override
    public List<String> registerEvent() {
        return Lists.newArrayList(DingDingEventTypeEnum.USER_ADD_ORG.getValue(),DingDingEventTypeEnum.USER_MODIFY_ORG.getValue());
    }

    @Override
    public void handle(Long enterpriseId, DingDingEventUserDTO dingEvent) {
        List<String> userIdList = dingEvent.getUserId();
        if(CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        // 钉钉部门与 系统部门对应关系
        List<DingDingDepartmentEntity> dingDingDepartmentEntityList = dingDingDepartmentDao.selectByEnterpriseId(enterpriseId);
        Map<Long, Long> deptIdMap = dingDingDepartmentEntityList.stream().collect(Collectors.toMap(DingDingDepartmentEntity::getDeptId, DingDingDepartmentEntity::getDepartmentId));
        // 系统员工列表
        List<EmployeeEntity> employeeEntityList = employeeDao.selectListByDeletedFlag(Boolean.FALSE);
        Map<String, Long> employeePhoneMap = employeeEntityList.stream().collect(Collectors.toMap(EmployeeEntity::getPhone, EmployeeEntity::getEmployeeId));

        for (String userId : userIdList) {
            // 根据用户ID获取详情
            OapiV2UserGetResponse.UserGetResponse userGetResponse = dingDingUserDeptService.getByUserId(enterpriseId, userId);
            if (null == userGetResponse) {
                continue;
            }
            String mobile = userGetResponse.getMobile();
            Long employeeId = employeePhoneMap.get(mobile);
            if (employeeId == null) {
                EmployeeEntity employeeEntity = this.buildEmployee(userGetResponse, deptIdMap);
                employeeEntity.setEnterpriseId(enterpriseId);
                employeeDao.insert(employeeEntity);
                employeeId = employeeEntity.getEmployeeId();
            }else {
                // 存在进行更新 密码不更新
                EmployeeEntity employeeEntity = this.buildEmployee(userGetResponse, deptIdMap);
                employeeEntity.setLoginPwd(null);
                employeeEntity.setEmployeeId(employeeId);
                employeeDao.updateById(employeeEntity);
            }
            // 员工与订单关系
            this.saveDingEmployee(enterpriseId, employeeId, userId, userGetResponse.getUnionid());
        }
    }


    private void saveDingEmployee(Long enterpriseId, Long employeeId, String userId, String unionId){
        DingDingEmployeeEntity dingDingEmployeeEntity = dingDingEmployeeDao.selectByEnterpriseUserId(enterpriseId, userId);
        if (dingDingEmployeeEntity != null) {
            return;
        }
        dingDingEmployeeEntity = new DingDingEmployeeEntity();
        dingDingEmployeeEntity.setEnterpriseId(enterpriseId);
        dingDingEmployeeEntity.setEmployeeId(employeeId);
        dingDingEmployeeEntity.setUserId(userId);
        dingDingEmployeeEntity.setUnionId(unionId);
        dingDingEmployeeDao.insert(dingDingEmployeeEntity);
    }


    private EmployeeEntity buildEmployee(OapiV2UserGetResponse.UserGetResponse userGetResponse, Map<Long, Long> deptIdMap) {
        String mobile = userGetResponse.getMobile();
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setActualName(userGetResponse.getName());
        employeeEntity.setPhone(mobile);
        List<Long> deptIdList = userGetResponse.getDeptIdList();
        Long tmsDepartmentId = deptIdMap.get(deptIdList.get(NumberConst.ZERO));
        employeeEntity.setDepartmentId(tmsDepartmentId);
        employeeEntity.setLoginName(mobile);
        employeeEntity.setLoginPwd(EmployeeService.getEncryptPwd(CommonConst.Password.DEFAULT));
        return employeeEntity;
    }
}