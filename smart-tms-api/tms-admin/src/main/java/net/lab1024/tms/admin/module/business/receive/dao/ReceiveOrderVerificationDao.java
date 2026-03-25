package net.lab1024.tms.admin.module.business.receive.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.capitalflow.domain.CapitalFlowQueryForm;
import net.lab1024.tms.admin.module.business.receive.domain.dto.ReceiveOrderVerificationAmountDTO;
import net.lab1024.tms.admin.module.business.receive.domain.vo.ReceiveOrderVerificationVO;
import net.lab1024.tms.common.module.business.receive.domain.ReceiveOrderVerificationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 应收帐款的核销记录
 *
 * @author lidoudou
 * @date 2022/7/22 上午10:24
 */
@Mapper
@Component
public interface ReceiveOrderVerificationDao extends BaseMapper<ReceiveOrderVerificationEntity> {

    /**
     * 根据应收款记录统计核销金额合集
     *
     * @param receiveOrderId
     * @return
     */
    BigDecimal sumByReceiveOrderId(@Param("receiveOrderId") Long receiveOrderId);

    /**
     * 根据应收款记录统计核销金额合集
     *
     * @param receiveOrderIdList
     * @return
     */
    List<ReceiveOrderVerificationAmountDTO> sumByReceiveOrderIdList(@Param("receiveOrderIdList") List<Long> receiveOrderIdList);

    /**
     * 根据应收款记录查询核销记录
     *
     * @param receiveOrderId
     * @return
     */
    List<ReceiveOrderVerificationVO> selectByReceiveOrderId(@Param("receiveOrderId") Long receiveOrderId);

    /**
     * 根据应收款记录查询核销记录
     *
     * @param receiveOrderIdList
     * @return
     */
    List<ReceiveOrderVerificationVO> selectByReceiveOrderIdList(@Param("receiveOrderIdList") List<Long> receiveOrderIdList);

    /**
     * 查询资金流水
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<ReceiveOrderVerificationEntity> queryByPage(Page page, @Param("queryForm") CapitalFlowQueryForm queryForm);

    /**
     * 统计核销总金额
     *
     * @param queryForm
     * @return
     */
    BigDecimal sumReceiveAmount(@Param("queryForm") CapitalFlowQueryForm queryForm);

    /**
     * 根据应收ID，获取已销金额总和
     *
     * @param receiveOrderIdList
     * @return
     */
    BigDecimal selectTotalAmount(List<Long> receiveOrderIdList);

    /**
     * 根据收款单ID获取各收款单核销金额
     *
     * @param receiveOrderIdList
     * @return
     */
    List<ReceiveOrderVerificationAmountDTO> selectAmountByReceiveOrderIdList(List<Long> receiveOrderIdList);

}
