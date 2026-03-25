package net.lab1024.tms.admin.module.business.pay.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.capitalflow.domain.CapitalFlowQueryForm;
import net.lab1024.tms.common.module.business.pay.domain.PayOrderPaymentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/8/15 3:06 下午
 */
@Mapper
@Component
public interface PayOrderPaymentDao extends BaseMapper<PayOrderPaymentEntity> {

    /**
     * 根据付款单id 查询
     *
     * @param payOrderIdList
     * @return
     */
    List<PayOrderPaymentEntity> selectByPayOrderIdList(@Param("payOrderIdList") List<Long> payOrderIdList);

    /**
     * 查询资金流水
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<PayOrderPaymentEntity> queryByPage(Page page, @Param("queryForm") CapitalFlowQueryForm queryForm);

    /**
     * 统计付款总金额
     *
     * @param queryForm
     * @return
     */
    BigDecimal sumReceiveAmount(@Param("queryForm") CapitalFlowQueryForm queryForm);
}