package net.lab1024.tms.admin.module.business.shipper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperInvoiceDTO;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperInvoiceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/7/30 14:47
 */
@Mapper
@Component
public interface ShipperInvoiceDao extends BaseMapper<ShipperInvoiceEntity> {

    /**
     * 查询货主下的开票信息
     *
     * @param shipperId
     * @param deletedFlag
     * @return
     */
    List<ShipperInvoiceDTO> selectByShipperId(@Param("shipperId") Long shipperId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 查询货主下的开票信息
     *
     * @param shipperIdList
     * @param deletedFlag
     * @return
     */
    List<ShipperInvoiceDTO> selectByShipperIdList(@Param("shipperIdList") List<Long> shipperIdList, @Param("deletedFlag") Boolean deletedFlag);

    List<ShipperInvoiceEntity> selectEntityByShipperId(@Param("shipperId") Long shipperId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 逻辑删除
     *
     * @param invoiceId
     * @param deletedFlag
     */
    void updateDeletedFlagById(@Param("invoiceId") Long invoiceId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 逻辑删除
     *
     * @param shipperIdList
     * @param deletedFlag
     */
    void updateDeletedFlag(@Param("shipperIdList") List<Long> shipperIdList, @Param("deletedFlag") Boolean deletedFlag);


    void deleteByShipperId(@Param("shipperId") Long shipperId);
    /**
     * 查询发票信息详情
     *
     * @param invoiceId
     * @param deletedFlag
     * @return
     */
    ShipperInvoiceDTO getDetail(@Param("invoiceId") Long invoiceId, @Param("deletedFlag") Boolean deletedFlag);
}
