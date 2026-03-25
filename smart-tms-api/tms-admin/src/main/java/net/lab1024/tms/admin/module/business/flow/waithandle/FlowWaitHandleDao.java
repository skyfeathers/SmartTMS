package net.lab1024.tms.admin.module.business.flow.waithandle;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/20 12:02
 */
@Mapper
@Component
public interface FlowWaitHandleDao extends BaseMapper<FlowWaitHandleEntity> {


    /**
     * 待办清零
     * @param employeeId
     * @param flowType
     */
    void clearZero(@Param("employeeId")Long employeeId,@Param("flowType") Integer flowType);

    /**
     * 根据员工查询所有待办信息
     * @param employeeId
     * @return
     */
    List<FlowWaitHandleEntity> selectByEmployeeId(@Param("employeeId")Long employeeId);

    /**
     *
     * @param employeeId
     * @param flowType
     * @return
     */
    FlowWaitHandleEntity selectByEmployeeIdAndFlowType(@Param("employeeId")Long employeeId,@Param("flowType") Integer flowType);

    /**
     * 增加待办数量
     * @param employeeId
     * @param flowType
     */
    void increase(@Param("employeeId")Long employeeId,@Param("flowType") Integer flowType);
}
