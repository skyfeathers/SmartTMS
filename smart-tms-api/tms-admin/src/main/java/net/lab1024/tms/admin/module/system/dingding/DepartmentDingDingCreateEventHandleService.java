package net.lab1024.tms.admin.module.system.dingding;

import com.dingtalk.api.response.OapiV2DepartmentGetResponse;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.system.department.dao.DepartmentDao;
import net.lab1024.tms.common.module.support.dingding.callback.domain.dto.DingDingEventDeptDTO;
import net.lab1024.tms.common.module.support.dingding.callback.service.IDingCallBackHandleService;
import net.lab1024.tms.common.module.support.dingding.config.DingDingConfigDao;
import net.lab1024.tms.common.module.support.dingding.config.domain.DingDingConfigEntity;
import net.lab1024.tms.common.module.support.dingding.constants.DingDingEventTypeEnum;
import net.lab1024.tms.common.module.support.dingding.sync.dao.DingDingDepartmentDao;
import net.lab1024.tms.common.module.support.dingding.sync.domain.DingDingDepartmentEntity;
import net.lab1024.tms.common.module.support.dingding.sync.DingDingUserDeptService;
import net.lab1024.tms.common.module.system.department.domain.DepartmentEntity;
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
public class DepartmentDingDingCreateEventHandleService implements IDingCallBackHandleService<DingDingEventDeptDTO> {

    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private DingDingConfigDao dingDingConfigDao;
    @Autowired
    private DingDingDepartmentDao dingDingDepartmentDao;
    @Autowired
    private DingDingUserDeptService dingDingUserDeptService;

    @Override
    public List<String> registerEvent() {
        return Lists.newArrayList(DingDingEventTypeEnum.ORG_DEPT_CREATE.getValue());
    }

    @Override
    public void handle(Long enterpriseId, DingDingEventDeptDTO dingEventDeptDTO) {
        List<Long> deptIdList = dingEventDeptDTO.getDeptId();
        if(CollectionUtils.isEmpty(deptIdList)) {
            return;
        }
        DingDingConfigEntity dingDingConfigEntity = dingDingConfigDao.selectByEnterpriseId(enterpriseId);
        if (null == dingDingConfigEntity) {
            return;
        }
        Long defaultDepartmentParentId = dingDingConfigEntity.getDepartmentId();
        if (defaultDepartmentParentId == null) {
            return;
        }

        List<DingDingDepartmentEntity> dingDingDepartmentEntityList = dingDingDepartmentDao.selectByEnterpriseId(enterpriseId);
        Map<Long, Long> deptIdMap = dingDingDepartmentEntityList.stream().collect(Collectors.toMap(DingDingDepartmentEntity::getDeptId, DingDingDepartmentEntity::getDepartmentId));


        for (Long deptId : deptIdList) {
            OapiV2DepartmentGetResponse.DeptGetResponse deptResp = dingDingUserDeptService.getByDeptId(enterpriseId, deptId);
            DepartmentEntity departmentEntity = new DepartmentEntity();
            departmentEntity.setName(deptResp.getName());
            departmentEntity.setParentId(deptIdMap.getOrDefault(deptId, defaultDepartmentParentId));
            departmentDao.insert(departmentEntity);

            // 部门增加关联关系
            DingDingDepartmentEntity dingDingDepartmentEntity = new DingDingDepartmentEntity();
            dingDingDepartmentEntity.setEnterpriseId(enterpriseId);
            dingDingDepartmentEntity.setDepartmentId(departmentEntity.getDepartmentId());
            dingDingDepartmentEntity.setDeptId(deptId);
            dingDingDepartmentDao.insert(dingDingDepartmentEntity);
        }
    }
}