package net.lab1024.tms.admin.module.business.shipper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperContactEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.checkerframework.checker.nullness.qual.Nullable;
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
public interface ShipperContactDao extends BaseMapper<ShipperContactEntity> {

    /**
     * 查询货主下的联系人信息
     * @param shipperId
     * @param deletedFlag
     * @return
     */
    List<ShipperContactEntity> selectByShipperId(@Param("shipperId") Long shipperId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 查询货主下的联系人信息
     * @param shipperIdList
     * @param defaultFlag 可以null
     * @param deletedFlag
     * @return
     */
    List<ShipperContactEntity> selectByShipperIdList(@Param("shipperIdList") List<Long> shipperIdList,
                                                     @Nullable @Param("defaultFlag") Boolean defaultFlag,
                                                     @Param("deletedFlag") Boolean deletedFlag);


    /**
     * 逻辑删除
     *
     * @param contactId
     * @param deletedFlag
     */
    void updateDeletedFlagById(@Param("contactId") Long contactId, @Param("deletedFlag") Boolean deletedFlag);

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
     * 查询默认联系人
     * @param shipperId
     * @param defaultFlag
     * @return
     */
    ShipperContactEntity selectDefault(@Param("shipperId") Long shipperId, @Param("defaultFlag") Boolean defaultFlag);
    /**
     * 更新默认标识
     *
     * @param contactId
     * @param defaultFlag
     */
    void updateDefaultFlagById(@Param("contactId") Long contactId, @Param("defaultFlag") Boolean defaultFlag);

}
