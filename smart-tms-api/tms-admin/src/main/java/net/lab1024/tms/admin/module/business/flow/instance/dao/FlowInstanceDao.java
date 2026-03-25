package net.lab1024.tms.admin.module.business.flow.instance.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.flow.instance.domain.FlowInstanceQueryForm;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceEntity;
import net.lab1024.tms.admin.module.business.flow.instance.domain.vo.FlowInstanceVO;
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
public interface FlowInstanceDao extends BaseMapper<FlowInstanceEntity> {

    /**
     * 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<FlowInstanceVO> query(Page page, @Param("query") FlowInstanceQueryForm queryForm);

    /**
     * 查询我收到的
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<FlowInstanceVO> queryReceived(Page page, @Param("query") FlowInstanceQueryForm queryForm);

    /**
     * 跟进实例状态 和处理人查询总数
     * @param handlerId
     * @param auditStatus
     * @return
     */
    Integer countByHandlerAndStatus(@Param("handlerId") Long handlerId,@Param("auditStatus") Integer auditStatus);
}
