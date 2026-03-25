package net.lab1024.tms.admin.module.business.contract.basic;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.contract.basic.domain.ContractQueryForm;
import net.lab1024.tms.admin.module.business.contract.basic.domain.ContractUpdateStatusForm;
import net.lab1024.tms.common.module.business.contract.domain.ContractEntity;
import net.lab1024.tms.common.module.business.contract.domain.ContractVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 合同管理
 *
 * @author lihaifan
 * @date 2022/7/15 11:10
 */
@Component
@Mapper
public interface ContractDao extends BaseMapper<ContractEntity> {

    /**
     * 分页查询合同模块
     * @param page
     * @param queryForm
     * @return
     */
    List<ContractVO> queryPage(Page page, @Param("queryForm") ContractQueryForm queryForm);

    /**
     * 根据运单查询
     * @param waybillId
     * @param exStatus
     * @return
     */
    ContractEntity selectByWaybillIdAndExcludeStatus(@Param("waybillId") Long waybillId, @Param("exStatus") Integer exStatus);

    /**
     * 根据运单查询
     * @param waybillIdList
     * @param exStatus
     * @return
     */
    List<ContractEntity> selectByWaybillIdListAndExcludeStatus(@Param("waybillIdList") Collection<Long> waybillIdList, @Param("exStatus") Integer exStatus);

    /**
     * 批量修改状态
     *
     * @param batchUpdateStatusForm
     */
    void batchUpdateStatus(ContractUpdateStatusForm batchUpdateStatusForm);

    /**
     * 根据在线合同id查询
     * @param onlineId
     * @return
     */
    ContractEntity selectByOnlineId(@Param("onlineId") String onlineId);
}
