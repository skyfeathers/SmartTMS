package net.lab1024.tms.admin.module.business.pay.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.pay.domain.PayOrderVerificationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/8/15 3:06 下午
 */
@Mapper
@Component
public interface PayOrderVerificationDao extends BaseMapper<PayOrderVerificationEntity> {

    /**
     * 根据付款单id 查询
     * @param payOrderIdList
     * @return
     */
    List<PayOrderVerificationEntity> selectByPayOrderIdList(@Param("payOrderIdList") List<Long> payOrderIdList);
}