package net.lab1024.tms.admin.module.business.shipper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperPrincipalDTO;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperPrincipalEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/***
 * 货主负责人
 * @author lidoudou
 * @date 2022/6/24 上午10:13
 */
@Mapper
@Component
public interface ShipperPrincipalDao extends BaseMapper<ShipperPrincipalEntity> {

    /**
     * 查询货主下的负责人信息
     *
     * @param shipperId
     * @return
     */
    List<ShipperPrincipalDTO> selectByShipperId(@Param("shipperId") Long shipperId);

    /**
     * 查询货主下的负责人信息
     *
     * @param shipperIdList
     * @param type
     * @return
     */
    List<ShipperPrincipalDTO> selectByShipperIdListAndType(@Param("shipperIdList") Collection<Long> shipperIdList, @Param("type") Integer type);

    /**
     * 查询货主下的负责人信息
     *
     * @param shipperIdList
     * @return
     */
    List<ShipperPrincipalDTO> selectByShipperIdList(@Param("shipperIdList") List<Long> shipperIdList);

    /**
     * 删除
     *
     * @param shipperId
     */
    void deleteByShipperIdAndType(@Param("shipperId") Long shipperId, @Param("type") Integer type);

    /**
     * 批量删除
     *
     * @param shipperIdList
     */
    void batchDeleteByShipperIdList(@Param("shipperIdList") List<Long> shipperIdList, @Param("type") Integer type);

    /**
     * 根据货主ID
     *
     * @param shipperId
     */
    void deleteByShipperId(@Param("shipperId") Long shipperId);

    /**
     * 查询货主下的负责人信息
     *
     * @param shipperId
     * @param type
     * @return
     */
    List<ShipperPrincipalDTO> selectByShipperIdAndType(@Param("shipperId") Long shipperId, @Param("type") Integer type);
}
