package net.lab1024.tms.admin.module.business.flow.task;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.business.flow.task.domain.FlowTaskEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/17 11:35
 */
@Mapper
@Component
public interface FlowTaskDao extends BaseMapper<FlowTaskEntity> {

    /**
     * 查询审批任务节点
     *
     * @param flowId
     * @return
     */
    List<FlowTaskEntity> selectByFlowId(@Param("flowId") Long flowId, @Param("enterpriseId") Long enterpriseId);

    /**
     * 删除审批任务节点
     *
     * @param flowId
     */
    void deleteByFlowId(@Param("flowId") Long flowId, @Param("enterpriseId") Long enterpriseId);

    /**
     * 查询审批任务节点
     *
     * @param flowIdList
     * @return
     */
    List<FlowTaskEntity> selectByFlowIdList(@Param("flowIdList") List<Long> flowIdList, @Param("enterpriseId") Long enterpriseId);


}
