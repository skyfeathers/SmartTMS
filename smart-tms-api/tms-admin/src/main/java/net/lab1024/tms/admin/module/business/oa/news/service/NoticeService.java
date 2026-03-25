package net.lab1024.tms.admin.module.business.oa.news.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.oa.news.dao.NoticeDao;
import net.lab1024.tms.admin.module.business.oa.news.domain.form.NoticeAddForm;
import net.lab1024.tms.admin.module.business.oa.news.domain.form.NoticeQueryForm;
import net.lab1024.tms.admin.module.business.oa.news.domain.form.NoticeUpdateForm;
import net.lab1024.tms.admin.module.business.oa.news.domain.form.NoticeVisibleRangeForm;
import net.lab1024.tms.admin.module.business.oa.news.domain.vo.NoticeTypeVO;
import net.lab1024.tms.admin.module.business.oa.news.domain.vo.NoticeUpdateFormVO;
import net.lab1024.tms.admin.module.business.oa.news.domain.vo.NoticeVO;
import net.lab1024.tms.admin.module.business.oa.news.domain.vo.NoticeVisibleRangeVO;
import net.lab1024.tms.admin.module.business.oa.news.manager.NoticeManager;
import net.lab1024.tms.admin.module.system.department.dao.DepartmentDao;
import net.lab1024.tms.admin.module.system.department.service.DepartmentService;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.oa.news.constant.NoticeVisibleRangeDataTypeEnum;
import net.lab1024.tms.common.module.business.oa.news.domain.NoticeEntity;
import net.lab1024.tms.common.module.system.department.domain.DepartmentEntity;
import net.lab1024.tms.common.module.system.department.domain.DepartmentVO;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 资讯 后台管理业务
 *
 * @author: listen
 * @date: 2022/7/14 21:38
 */
@Service
public class NoticeService {

    @Autowired
    private NoticeDao noticeDao;

    @Autowired
    private NoticeManager noticeManager;

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private NoticeTypeService noticeTypeService;

    /**
     * 查询 通知、公告
     *
     * @param queryForm
     * @return
     */
    public PageResult<NoticeVO> query(NoticeQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<NoticeVO> list = noticeDao.query(page, queryForm);
        LocalDateTime now = LocalDateTime.now();
        list.forEach(e -> e.setPublishFlag(e.getPublishTime().isBefore(now)));
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 添加
     *
     * @param addForm
     * @return
     */
    public ResponseDTO<String> add(NoticeAddForm addForm) {
        // 校验并获取可见范围
        ResponseDTO<String> validate = this.checkAndBuildVisibleRange(addForm);
        if (!validate.getOk()) {
            return ResponseDTO.error(validate);
        }

        // build 资讯
        NoticeEntity noticeEntity = SmartBeanUtil.copy(addForm, NoticeEntity.class);
        // 发布时间：不是定时发布时 默认为 当前
        if (!addForm.getScheduledPublishFlag()) {
            noticeEntity.setPublishTime(LocalDateTime.now());
        }
        // 保存数据
        noticeManager.saveNews(noticeEntity, addForm.getVisibleRangeList());
        return ResponseDTO.ok();
    }

    /**
     * 校验并返回可见范围
     *
     * @param form
     * @return
     */
    private ResponseDTO<String> checkAndBuildVisibleRange(NoticeAddForm form) {
        // 校验资讯分类
        NoticeTypeVO noticeType = noticeTypeService.getByNoticeTypeId(form.getNoticeTypeId());
        if (noticeType == null) {
            return ResponseDTO.userErrorParam("分类不存在");
        }

        if (form.getAllVisibleFlag()) {
            return ResponseDTO.ok();
        }

        /**
         * 校验可见范围
         * 非全部可见时 校验选择的员工|部门
         */
        List<NoticeVisibleRangeForm> visibleRangeUpdateList = form.getVisibleRangeList();
        if (CollectionUtils.isEmpty(visibleRangeUpdateList)) {
            return ResponseDTO.userErrorParam("未设置可见范围");
        }

        // 校验可见范围-> 员工
        List<Long> employeeIdList = visibleRangeUpdateList.stream()
                .filter(e -> NoticeVisibleRangeDataTypeEnum.EMPLOYEE.equalsValue(e.getDataType()))
                .map(NoticeVisibleRangeForm::getDataId)
                .distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(employeeIdList)) {
            employeeIdList = employeeIdList.stream().distinct().collect(Collectors.toList());
            List<Long> dbEmployeeIdList = employeeDao.selectBatchIds(employeeIdList).stream().map(EmployeeEntity::getEmployeeId).collect(Collectors.toList());
            Collection<Long> subtract = CollectionUtils.subtract(employeeIdList, dbEmployeeIdList);
            if (subtract.size() > 0) {
                return ResponseDTO.userErrorParam("员工id不存在：" + subtract);
            }
        }

        // 校验可见范围-> 部门
        List<Long> deptIdList = visibleRangeUpdateList.stream()
                .filter(e -> NoticeVisibleRangeDataTypeEnum.DEPARTMENT.equalsValue(e.getDataType()))
                .map(NoticeVisibleRangeForm::getDataId)
                .distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(deptIdList)) {
            deptIdList = deptIdList.stream().distinct().collect(Collectors.toList());
            List<Long> dbDeptIdList = departmentDao.selectBatchIds(deptIdList).stream().map(DepartmentEntity::getDepartmentId).collect(Collectors.toList());
            Collection<Long> subtract = CollectionUtils.subtract(deptIdList, dbDeptIdList);
            if (subtract.size() > 0) {
                return ResponseDTO.userErrorParam("部门id不存在：" + subtract);
            }
        }
        return ResponseDTO.ok();
    }


    /**
     * 更新
     *
     * @param updateForm
     * @return
     */
    public ResponseDTO<String> update(NoticeUpdateForm updateForm) {
        // 校验并获取可见范围
        ResponseDTO<String> res = this.checkAndBuildVisibleRange(updateForm);
        if (!res.getOk()) {
            return ResponseDTO.error(res);
        }

        // 更新
        NoticeEntity noticeEntity = SmartBeanUtil.copy(updateForm, NoticeEntity.class);
        noticeManager.update(noticeEntity, updateForm.getVisibleRangeList());
        return ResponseDTO.ok();
    }


    /**
     * 删除
     *
     * @param noticeId
     * @return
     */
    public ResponseDTO<String> delete(Long noticeId) {
        NoticeEntity noticeEntity = noticeDao.selectById(noticeId);
        if (null == noticeEntity || noticeEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("通知公告不存在");
        }
        // 更新删除状态
        noticeDao.updateDeletedFlag(noticeId);
        return ResponseDTO.ok();
    }

    /**
     * 获取更新表单用的详情
     *
     * @param noticeId
     * @return
     */
    public NoticeUpdateFormVO getUpdateFormVO(Long noticeId) {
        NoticeEntity noticeEntity = noticeDao.selectById(noticeId);
        if (null == noticeEntity) {
            return null;
        }

        NoticeUpdateFormVO updateFormVO = SmartBeanUtil.copy(noticeEntity, NoticeUpdateFormVO.class);
        if (!updateFormVO.getAllVisibleFlag()) {
            List<NoticeVisibleRangeVO> noticeVisibleRangeList = noticeDao.queryVisibleRange(noticeId);
            List<Long> employeeIdList = noticeVisibleRangeList.stream().filter(e -> NoticeVisibleRangeDataTypeEnum.EMPLOYEE.getValue().equals(e.getDataType()))
                    .map(NoticeVisibleRangeVO::getDataId)
                    .collect(Collectors.toList());

            Map<Long, EmployeeEntity> employeeMap = null;
            if (CollectionUtils.isNotEmpty(employeeIdList)) {
                employeeMap = employeeDao.selectBatchIds(employeeIdList).stream().collect(Collectors.toMap(EmployeeEntity::getEmployeeId, Function.identity()));
            } else {
                employeeMap = new HashMap<>();
            }
            for (NoticeVisibleRangeVO noticeVisibleRange : noticeVisibleRangeList) {
                if (noticeVisibleRange.getDataType().equals(NoticeVisibleRangeDataTypeEnum.EMPLOYEE.getValue())) {
                    EmployeeEntity employeeEntity = employeeMap.get(noticeVisibleRange.getDataId());
                    noticeVisibleRange.setDataName(employeeEntity == null ? StringConst.EMPTY : employeeEntity.getActualName());
                } else {
                    DepartmentVO departmentVO = departmentService.getDepartmentById(noticeVisibleRange.getDataId());
                    noticeVisibleRange.setDataName(departmentVO == null ? StringConst.EMPTY : departmentVO.getName());
                }
            }
            updateFormVO.setVisibleRangeList(noticeVisibleRangeList);
        }
        return updateFormVO;
    }
}
