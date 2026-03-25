package net.lab1024.tms.admin.module.business.contract.esign;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.esign.domain.entity.ESignNoticeRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * e签宝回调记录
 *
 * @author lihaifan
 * @date 2022/9/16 16:22
 */
@Mapper
@Component
public interface ESignNoticeRecordDao extends BaseMapper<ESignNoticeRecordEntity> {

    /**
     * 查询流程
     * @param flowId
     * @return
     */
    List<ESignNoticeRecordEntity> queryByFlowId(@Param("flowId") String flowId);
}
