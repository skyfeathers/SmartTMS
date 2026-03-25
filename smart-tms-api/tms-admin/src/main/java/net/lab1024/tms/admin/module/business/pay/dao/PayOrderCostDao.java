package net.lab1024.tms.admin.module.business.pay.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.business.pay.domain.bo.PayOrderCostBO;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.dto.EmployeePayOrderCostDTO;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.form.CustomerServiceDayStatisticQueryForm;
import net.lab1024.tms.admin.module.business.waybill.domain.dto.WaybillCostAmountUpdateBO;
import net.lab1024.tms.common.module.business.pay.domain.PayOrderCostEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/8/15 3:06 下午
 */
@Mapper
@Component
public interface PayOrderCostDao extends BaseMapper<PayOrderCostEntity> {

    /**
     * 根据付款单查询
     *
     * @param payOrderId
     * @return
     */
    List<PayOrderCostEntity> selectByPayOrderId(@Param("payOrderId") Long payOrderId);

    /**
     * 根据付款单查询
     *
     * @param payOrderIdList
     * @return
     */
    List<PayOrderCostEntity> selectByPayOrderIdList(@Param("payOrderIdList") List<Long> payOrderIdList);

}