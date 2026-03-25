package net.lab1024.tms.common.module.system.department;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lidoudou
 * @date 2023/3/30 下午5:24
 */
@Service
public class CommonDepartmentService {

    @Autowired
    private DepartmentCacheManager departmentCacheManager;

    /**
     * 自身以及所有下级的部门id列表
     *
     * @param departmentId
     * @return
     */
    public List<Long> selfAndChildrenIdList(Long departmentId) {
        return departmentCacheManager.getDepartmentSelfAndChildren(departmentId);
    }
}
