package net.lab1024.tms.admin.module.business.flow.instance.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceTaskEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/17 17:52
 */
@Mapper
@Component
public interface FlowInstanceTaskDao extends BaseMapper<FlowInstanceTaskEntity> {

    /**
     * 查询流程实例的每一级任务处理节点
     *
     * @param instanceId
     * @return
     */
    List<FlowInstanceTaskEntity> selectByInstanceId(@Param("instanceId") Long instanceId);

    /**
     * 根据流程实例和任务查询
     * @param instanceId
     * @param taskId
     * @return
     */
    FlowInstanceTaskEntity selectByInstanceAndTaskId(@Param("instanceId") Long instanceId, @Param("taskId") Long taskId);


    /**
     * 根据审批流ID以及任务类型查询
     *
     * @param flowInstanceIdList
     * @param taskType
     * @return
     */
    List<Long> queryMineByInstanceIdListAndTaskType(@Param("flowInstanceIdList") List<Long> flowInstanceIdList,
                                                                  @Param("taskType") Integer taskType,
                                                                  @Param("employeeId") Long employeeId);

}
