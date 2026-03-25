package net.lab1024.tms.admin.module.business.flow.comment;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 审批流评论
 *
 * @author lihaifan
 * @date 2022/2/10 17:15
 */
@Mapper
@Component
public interface FlowCommentDao extends BaseMapper<FlowCommentEntity> {

    /**
     * 根据审批流ID查询
     * @param instanceId
     * @param deletedFlag
     * @return
     */
    List<FlowCommentVO> selectByInstanceId(@Param("instanceId") Long instanceId, @Param("deletedFlag") Boolean deletedFlag);
}
