package net.lab1024.tms.admin.module.business.shipper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperPaymentWayDTO;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperPaymentWayEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/7/30 9:37
 */
@Mapper
@Component
public interface ShipperPaymentWayDao extends BaseMapper<ShipperPaymentWayEntity> {

    /**
     * 查询供应商下的付款方式
     * @param shipperId
     * @param deletedFlag
     * @return
     */
    List<ShipperPaymentWayDTO> selectByShipperId(@Param("shipperId") Long shipperId, @Param("deletedFlag") Boolean deletedFlag);

    List<ShipperPaymentWayEntity> selectEntityByShipperId(@Param("shipperId") Long shipperId, @Param("deletedFlag") Boolean deletedFlag);

    List<ShipperPaymentWayDTO> selectByShipperIdAndType(@Param("shipperId") Long shipperId, @Param("paymentType") Integer paymentType, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 逻辑删除
     *
     * @param paymentWayId
     * @param deletedFlag
     */
    void updateDeletedFlagById(@Param("paymentWayId") Long paymentWayId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 逻辑删除
     *
     * @param shipperIdList
     * @param deletedFlag
     */
    void updateDeletedFlag(@Param("shipperIdList") List<Long> shipperIdList, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 真实删除
     * @param shipperId
     */
    void deleteByShipperId(@Param("shipperId") Long shipperId);

    /**
     * 批量供应商下所有付款方式的默认标识
     *
     * @param shipperId
     * @param defaultFlag
     */
    void batchSetDefaultFlag(@Param("shipperId") Long shipperId, @Param("defaultFlag") Boolean defaultFlag);

    /**
     * 设置某个付款方式的默认标识
     * @param paymentWayId
     * @param defaultFlag
     */
    void setDefaultFlag(@Param("paymentWayId") Long paymentWayId, @Param("defaultFlag") Boolean defaultFlag);


}
