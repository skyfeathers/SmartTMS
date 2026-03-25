package net.lab1024.tms.admin.module.business.feedback.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.feedback.domain.FeedbackQueryForm;
import net.lab1024.tms.admin.module.business.feedback.domain.FeedbackVO;
import net.lab1024.tms.common.module.business.feedback.FeedbackEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 意见反馈
 *
 * @Author: dangxinfa
 * @Date: 2022/6/28
 */
@Mapper
@Component
public interface FeedbackDao extends BaseMapper<FeedbackEntity> {

    /**
     * 分页查询
     */
    List<FeedbackVO> queryPage(Page page, @Param("query") FeedbackQueryForm query);
}