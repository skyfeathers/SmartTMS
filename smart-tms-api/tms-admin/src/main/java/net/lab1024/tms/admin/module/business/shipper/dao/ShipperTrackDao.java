package net.lab1024.tms.admin.module.business.shipper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.shipper.domain.form.ShipperTrackQueryForm;
import net.lab1024.tms.admin.module.business.shipper.domain.vo.ShipperTrackVO;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperTrackEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/10/23 20:39
 */
@Mapper
@Component
public interface ShipperTrackDao extends BaseMapper<ShipperTrackEntity> {

    /**
     * 分页查询
     * @param page
     * @param queryDTO
     * @return
     */
    List<ShipperTrackVO> query(Page page, @Param("query") ShipperTrackQueryForm queryDTO);


    /**
     * 查询最后一次跟进
     * @param shipperIdList
     * @return
     */
    List<ShipperTrackVO> selectByShipperIdList(@Param("shipperIdList") List<Long> shipperIdList);
    /**
     * 查询某人跟进了哪些机构
     * @param employeeId
     * @return
     */
    List<Long> selectByEmployeeId(@Param("employeeId") Long employeeId);

    /**
     * 跟进详情
     * @param trackId
     * @return
     */
    ShipperTrackVO detail(Long trackId);
}
