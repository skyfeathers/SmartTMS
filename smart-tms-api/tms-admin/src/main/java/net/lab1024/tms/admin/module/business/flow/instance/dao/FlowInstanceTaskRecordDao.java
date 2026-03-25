package net.lab1024.tms.admin.module.business.flow.instance.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceTaskRecordEntity;
import net.lab1024.tms.admin.module.business.flow.instance.domain.vo.FlowInstanceTaskRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/18 11:03
 */
@Mapper
@Component
public interface FlowInstanceTaskRecordDao extends BaseMapper<FlowInstanceTaskRecordEntity> {

    List<FlowInstanceTaskRecordVO> selectByTaskId(@Param("instanceId") Long instanceId, @Param("taskIdList")List<Long> taskIdList);

    /**
     * 获取处理状态是某种状态的处理人
     *
     * @param instanceId
     * @param auditStatus
     * @return
     */
    List<Long> selectAlreadyAuditHandler(@Param("instanceId") Long instanceId, @Param("auditStatus") Integer auditStatus);

    List<FlowInstanceTaskRecordVO> selectByTaskIdList(@Param("instanceIdList") Collection<Long> instanceIdList, @Param("taskIdList")List<Long> taskIdList);
}
