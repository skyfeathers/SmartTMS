package net.lab1024.tms.admin.module.business.feedback.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.feedback.dao.FeedbackDao;
import net.lab1024.tms.admin.module.business.feedback.domain.FeedbackAddForm;
import net.lab1024.tms.admin.module.business.feedback.domain.FeedbackQueryForm;
import net.lab1024.tms.admin.module.business.feedback.domain.FeedbackVO;
import net.lab1024.tms.admin.module.system.department.service.DepartmentService;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.feedback.FeedbackEntity;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 意见反馈
 *
 * @Author: dangxinfa
 * @Date: 2022/6/28
 */
@Service
public class FeedbackService {

    @Autowired
    private FeedbackDao feedbackDao;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private EmployeeDao employeeDao;

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<FeedbackVO>> query(FeedbackQueryForm queryForm) {
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<FeedbackVO> list = feedbackDao.queryPage(page, queryForm);
        PageResult<FeedbackVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, list);
        if (pageResultDTO.getEmptyFlag()) {
            return ResponseDTO.ok(pageResultDTO);
        }
        // 设置部门全路径
        list.forEach(e -> e.setDepartmentName(departmentService.getDepartmentById(e.getDepartmentId()).getName()));
        return ResponseDTO.ok(pageResultDTO);
    }

    /**
     * 新建
     */
    public ResponseDTO<String> add(FeedbackAddForm addForm) {
        EmployeeEntity employeeEntity = employeeDao.selectById(addForm.getCreateUserId());
        if (null == employeeEntity || employeeEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "员工不存在");
        }

        FeedbackEntity feedback = SmartBeanUtil.copy(addForm, FeedbackEntity.class);
        feedback.setDepartmentId(employeeEntity.getDepartmentId());
        feedbackDao.insert(feedback);
        return ResponseDTO.ok();
    }
}
