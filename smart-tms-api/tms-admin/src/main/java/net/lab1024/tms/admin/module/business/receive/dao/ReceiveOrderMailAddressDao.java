package net.lab1024.tms.admin.module.business.receive.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.receive.domain.ReceiveOrderMailAddressEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 应收帐款的邮寄信息
 *
 * @author lidoudou
 * @date 2022/8/3 下午4:48
 */
@Mapper
@Component
public interface ReceiveOrderMailAddressDao extends BaseMapper<ReceiveOrderMailAddressEntity> {

    /**
     * 根据收款单ID列表查询关联信息
     *
     * @param receiveOrderIdList
     * @return
     */
    List<ReceiveOrderMailAddressEntity> selectByReceiveOrderIdList(@Param("receiveOrderIdList") List<Long> receiveOrderIdList);
}
