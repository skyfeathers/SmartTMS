package net.lab1024.tms.admin.module.business.flow.define;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.flow.define.domain.FlowEntity;
import net.lab1024.tms.admin.module.business.flow.define.domain.FlowQueryForm;
import net.lab1024.tms.admin.module.business.flow.define.domain.FlowVO;
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
public interface FlowDao extends BaseMapper<FlowEntity> {

    /**
     * 根据流程类型获取流程定义信息
     * @param flowType
     * @return
     */
    FlowEntity selectByFlowType(@Param("flowType") Integer flowType);

    /**
     * 分页查询审批流程
     *
     * @return
     */
    List<FlowVO> selectList();

}
