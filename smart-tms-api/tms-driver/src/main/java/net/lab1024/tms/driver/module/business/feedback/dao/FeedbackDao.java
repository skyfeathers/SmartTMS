package net.lab1024.tms.driver.module.business.feedback.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.common.module.business.feedback.FeedbackEntity;
import net.lab1024.tms.driver.module.business.feedback.domain.form.FeedbackQueryForm;
import net.lab1024.tms.driver.module.business.feedback.domain.vo.FeedbackQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface FeedbackDao extends BaseMapper<FeedbackEntity> {

    /**
     * 意见反馈-分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<FeedbackQueryVO> queryPage(Page page, @Param("queryForm") FeedbackQueryForm queryForm);

}
