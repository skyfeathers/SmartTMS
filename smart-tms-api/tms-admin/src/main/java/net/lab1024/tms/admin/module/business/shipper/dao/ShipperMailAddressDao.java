package net.lab1024.tms.admin.module.business.shipper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperMailAddressDTO;
import net.lab1024.tms.admin.module.business.shipper.domain.form.ShipperMailAddressQueryForm;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperMailAddressEntity;
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
public interface ShipperMailAddressDao extends BaseMapper<ShipperMailAddressEntity> {

    /**
     * 查询货主下的邮寄信息
     *
     * @param shipperId
     * @param deletedFlag
     * @return
     */
    List<ShipperMailAddressEntity> selectByShipperId(@Param("shipperId") Long shipperId, @Param("deletedFlag") Boolean deletedFlag);


    /**
     * 逻辑删除
     *
     * @param mailAddressId
     * @param deletedFlag
     */
    void updateDeletedFlagById(@Param("mailAddressId") Long mailAddressId, @Param("deletedFlag") Boolean deletedFlag);


    /**
     * 更新默认标识
     *
     * @param mailAddressId
     * @param defaultFlag
     */
    void updateDefaultFlagById(@Param("mailAddressId") Long mailAddressId, @Param("defaultFlag") Boolean defaultFlag);

    /**
     * 查询默认的邮寄信息
     *
     * @param shipperId
     * @param defaultFlag
     */
    ShipperMailAddressEntity selectDefault(@Param("shipperId") Long shipperId, @Param("defaultFlag") Boolean defaultFlag);

    /**
     * 逻辑删除
     *
     * @param shipperIdList
     * @param deletedFlag
     */
    void updateDeletedFlag(@Param("shipperIdList") List<Long> shipperIdList, @Param("deletedFlag") Boolean deletedFlag);


    void deleteByShipperId(@Param("shipperId") Long shipperId);

    /**
     * 分页查询
     * @param page
     * @param queryDTO
     * @return
     */
    List<ShipperMailAddressDTO> query(Page page, @Param("queryDTO") ShipperMailAddressQueryForm queryDTO);

    /**
     * 排他更新默认状态
     * @param shipperId
     * @param mailAddressId
     */
    void updateOtherNotDefault(@Param("shipperId") Long shipperId, @Param("mailAddressId") Long mailAddressId, @Param("defaultFlag") Boolean defaultFlag);

}
