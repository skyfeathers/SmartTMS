package net.lab1024.tms.admin.module.system.dingding;

import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.google.common.collect.Maps;
import net.lab1024.tms.admin.module.system.department.dao.DepartmentDao;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.module.support.dingding.config.DingDingConfigDao;
import net.lab1024.tms.common.module.support.dingding.config.domain.DingDingConfigEntity;
import net.lab1024.tms.common.module.support.dingding.sync.dao.DingDingDepartmentDao;
import net.lab1024.tms.common.module.support.dingding.sync.domain.DingDingDepartmentEntity;
import net.lab1024.tms.common.module.support.dingding.sync.DingDingUserDeptService;
import net.lab1024.tms.common.module.system.department.domain.DepartmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author yandy
 * @description:
 * @date 2023/5/9 2:38 下午
 */
@Service
public class DepartmentDingDingInitService {

    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private DingDingConfigDao dingDingConfigDao;
    @Autowired
    private DingDingDepartmentDao dingDingDepartmentDao;
    @Autowired
    private DingDingUserDeptService dingDingUserDeptService;

    @Transactional(rollbackFor = Throwable.class)
    public void initDepartment(Long enterpriseId) {
        DingDingConfigEntity dingDingConfigEntity = dingDingConfigDao.selectByEnterpriseId(enterpriseId);
        if (null == dingDingConfigEntity) {
            return;
        }
        Long defaultDepartmentParentId = dingDingConfigEntity.getDepartmentId();
        List<OapiV2DepartmentListsubResponse.DeptBaseResponse> enterpriseDeptList = dingDingUserDeptService.queryDeptList(enterpriseId);
        // 钉钉部门ID与TMS部门ID对应关系
        Map<Long, Long> dingTmsDepartmentIdMap = Maps.newHashMap();
        dingTmsDepartmentIdMap.put(NumberConst.ONE_LONG, defaultDepartmentParentId);

        // 当前最大部门排序
        Integer maxSort = departmentDao.selectMaxSort();
        for (OapiV2DepartmentListsubResponse.DeptBaseResponse dingDept : enterpriseDeptList) {
            Long parentId = dingTmsDepartmentIdMap.get(dingDept.getParentId());
            if (null == parentId) {
                continue;
            }
            DepartmentEntity dbDepartmentEntity = departmentDao.selectByParentIdAndName(parentId, dingDept.getName());
            // 数据库中该部门不存在，需要新增
            if (null == dbDepartmentEntity) {
                maxSort = maxSort + 1;
                dbDepartmentEntity = new DepartmentEntity();
                dbDepartmentEntity.setName(dingDept.getName());
                dbDepartmentEntity.setParentId(parentId);
                dbDepartmentEntity.setSort(maxSort);
                departmentDao.insert(dbDepartmentEntity);
            }
            // 部门已经存在，增加关联关系
            DingDingDepartmentEntity dingDingDepartmentEntity = new DingDingDepartmentEntity();
            dingDingDepartmentEntity.setEnterpriseId(enterpriseId);
            dingDingDepartmentEntity.setDepartmentId(dbDepartmentEntity.getDepartmentId());
            dingDingDepartmentEntity.setDeptId(dingDept.getDeptId());
            dingDingDepartmentDao.insert(dingDingDepartmentEntity);

            dingTmsDepartmentIdMap.put(dingDept.getDeptId(), dbDepartmentEntity.getDepartmentId());
        }
    }
}