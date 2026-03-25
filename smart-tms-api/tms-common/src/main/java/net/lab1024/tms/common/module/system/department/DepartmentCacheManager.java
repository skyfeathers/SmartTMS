package net.lab1024.tms.common.module.system.department;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.system.department.domain.DepartmentEntity;
import net.lab1024.tms.common.module.system.department.domain.DepartmentTreeVO;
import net.lab1024.tms.common.module.system.department.domain.DepartmentVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 罗伊
 * @date 2021-01-31 0:48
 */
@Slf4j
@Service
public class DepartmentCacheManager {

    @Autowired
    private CommonDepartmentDao departmentDao;

    // ----------------------- 查询 -----------------------

    /**
     * 部门列表
     *
     * @return
     */
    public List<DepartmentVO> getDepartmentList(Long enterpriseId) {
        List<DepartmentVO> departmentVOList = departmentDao.listByEnterpriseId(enterpriseId);
        return departmentVOList;
    }

    /**
     * 部门map
     *
     * @return
     */
    public DepartmentVO queryById(Long departmentId) {
        return departmentDao.queryById(departmentId);
    }


    /**
     * 缓存部门树结构
     *
     * @return
     */
    public List<DepartmentTreeVO> getDepartmentTree(Long enterpriseId) {
        List<DepartmentVO> departmentVOList = departmentDao.listByEnterpriseId(enterpriseId);
        return this.buildTree(departmentVOList);
    }

    /**
     * 缓存某个部门的下级id列表
     *
     * @param departmentId
     * @return
     */
    public List<Long> getDepartmentSelfAndChildren(Long departmentId) {
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        List<DepartmentVO> departmentVOList = departmentDao.listByEnterpriseId(departmentEntity.getEnterpriseId());
        List<Long> idList = this.selfAndChildrenIdList(departmentId, departmentVOList);
        return idList;
    }


    /**
     * 部门的路径名称
     *
     * @return
     */
    public String getDepartmentPath(Long departmentId) {
        DepartmentVO departmentVO = departmentDao.queryById(departmentId);
        List<DepartmentVO> departmentVOList = departmentDao.listByEnterpriseId(departmentVO.getEnterpriseId());
        Map<Long, DepartmentVO> departmentMap = departmentVOList.stream().collect(Collectors.toMap(DepartmentVO::getDepartmentId, Function.identity()));
        String pathName = this.buildDepartmentPath(departmentVO, departmentMap);
        return pathName;
    }

    /**
     * 构建父级考点路径
     *
     * @param departmentVO
     * @param departmentMap
     */
    private String buildDepartmentPath(DepartmentVO departmentVO, Map<Long, DepartmentVO> departmentMap) {
        if (Objects.equals(departmentVO.getParentId(), NumberUtils.LONG_ZERO)) {
            return departmentVO.getName();
        }
        //父节点
        DepartmentVO parentDepartment = departmentMap.get(departmentVO.getParentId());
        if (parentDepartment == null) {
            return departmentVO.getName();
        }
        String pathName = buildDepartmentPath(parentDepartment, departmentMap);
        return pathName + "/" + departmentVO.getName();

    }
    // ---------------------- 构造树的一些方法 ------------------------------

    /**
     * 构建部门树结构
     *
     * @param voList
     * @return
     */
    public List<DepartmentTreeVO> buildTree(List<DepartmentVO> voList) {
        if (CollectionUtils.isEmpty(voList)) {
            return Lists.newArrayList();
        }
        List<DepartmentVO> rootList = voList.stream().filter(e -> e.getParentId() == null || Objects.equals(e.getParentId(), NumberUtils.LONG_ZERO)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(rootList)) {
            return Lists.newArrayList();
        }
        List<DepartmentTreeVO> treeVOList = SmartBeanUtil.copyList(rootList, DepartmentTreeVO.class);
        this.recursiveBuildTree(treeVOList, voList);
        return treeVOList;
    }

    /**
     * 构建所有根节点的下级树形结构
     *
     * @param nodeList
     * @param
     */
    private void recursiveBuildTree(List<DepartmentTreeVO> nodeList, List<DepartmentVO> allDepartmentList) {
        int nodeSize = nodeList.size();
        for (int i = 0; i < nodeSize; i++) {
            int preIndex = i - 1;
            int nextIndex = i + 1;
            DepartmentTreeVO node = nodeList.get(i);
            if (preIndex > -1) {
                node.setPreId(nodeList.get(preIndex).getDepartmentId());
            }
            if (nextIndex < nodeSize) {
                node.setNextId(nodeList.get(nextIndex).getDepartmentId());
            }

            ArrayList<Long> selfAndAllChildrenIdList = Lists.newArrayList();
            selfAndAllChildrenIdList.add(node.getDepartmentId());
            node.setSelfAndAllChildrenIdList(selfAndAllChildrenIdList);

            List<DepartmentTreeVO> children = getChildren(node.getDepartmentId(), allDepartmentList);
            if (CollectionUtils.isNotEmpty(children)) {
                node.setChildren(children);
                this.recursiveBuildTree(children, allDepartmentList);
            }
        }
    }


    /**
     * 获取子元素
     *
     * @param departmentId
     * @param voList
     * @return
     */
    private List<DepartmentTreeVO> getChildren(Long departmentId, List<DepartmentVO> voList) {
        List<DepartmentVO> childrenEntityList = voList.stream().filter(e -> departmentId.equals(e.getParentId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(childrenEntityList)) {
            return Lists.newArrayList();
        }
        return SmartBeanUtil.copyList(childrenEntityList, DepartmentTreeVO.class);
    }


    /**
     * 通过部门id,获取当前以及下属部门
     *
     * @param departmentId
     * @param voList
     */
    public List<Long> selfAndChildrenIdList(Long departmentId, List<DepartmentVO> voList) {
        List<Long> selfAndChildrenIdList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(voList)) {
            return selfAndChildrenIdList;
        }
        selfAndChildrenIdList.add(departmentId);
        List<DepartmentTreeVO> children = this.getChildren(departmentId, voList);
        if (CollectionUtils.isEmpty(children)) {
            return selfAndChildrenIdList;
        }
        List<Long> childrenIdList = children.stream().map(DepartmentTreeVO::getDepartmentId).collect(Collectors.toList());
        selfAndChildrenIdList.addAll(childrenIdList);
        for (Long childId : childrenIdList) {
            this.selfAndChildrenRecursion(selfAndChildrenIdList, childId, voList);
        }
        return selfAndChildrenIdList;
    }

    /**
     * 递归查询
     *
     * @param selfAndChildrenIdList
     * @param departmentId
     * @param voList
     */
    public void selfAndChildrenRecursion(List<Long> selfAndChildrenIdList, Long departmentId, List<DepartmentVO> voList) {
        List<DepartmentTreeVO> children = this.getChildren(departmentId, voList);
        if (CollectionUtils.isEmpty(children)) {
            return;
        }
        List<Long> childrenIdList = children.stream().map(DepartmentTreeVO::getDepartmentId).collect(Collectors.toList());
        selfAndChildrenIdList.addAll(childrenIdList);
        for (Long childId : childrenIdList) {
            this.selfAndChildrenRecursion(selfAndChildrenIdList, childId, voList);
        }
    }
}
