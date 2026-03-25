package net.lab1024.tms.driver.module.business.feedback.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.enumeration.UserTypeEnum;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.feedback.FeedbackEntity;
import net.lab1024.tms.driver.module.business.feedback.dao.FeedbackDao;
import net.lab1024.tms.driver.module.business.feedback.domain.form.FeedbackAddForm;
import net.lab1024.tms.driver.module.business.feedback.domain.form.FeedbackQueryForm;
import net.lab1024.tms.driver.module.business.feedback.domain.vo.FeedbackQueryVO;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FeedbackService {

    @Resource
    private FeedbackDao feedbackDao;

    /**
     * 意见反馈分页查询
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<FeedbackQueryVO>> query(FeedbackQueryForm queryForm) {
        queryForm.setCreateUserType(UserTypeEnum.DRIVER.getValue());
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<FeedbackQueryVO> feedbackQueryVOList =  feedbackDao.queryPage(page, queryForm);
        if (CollectionUtil.isEmpty(feedbackQueryVOList)) {
            return ResponseDTO.ok(SmartPageUtil.convert2PageResult(page, Lists.newArrayList()));
        }
        PageResult<FeedbackQueryVO> pageResult = SmartPageUtil.convert2PageResult(page, feedbackQueryVOList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 司机添加意见反馈
     *
     * @param addForm
     * @param driver
     * @return
     */
    public ResponseDTO<String> add(FeedbackAddForm addForm, RequestUser driver) {
        addForm.setCreateUserId(driver.getUserId());
        addForm.setCreateUserName(driver.getUserName());
        addForm.setCreateUserType(UserTypeEnum.DRIVER.getValue());
        addForm.setCreateUserTypeDesc(UserTypeEnum.DRIVER.getDesc());

        FeedbackEntity feedbackEntity = SmartBeanUtil.copy(addForm, FeedbackEntity.class);
        feedbackDao.insert(feedbackEntity);
        return ResponseDTO.ok();
    }
}
