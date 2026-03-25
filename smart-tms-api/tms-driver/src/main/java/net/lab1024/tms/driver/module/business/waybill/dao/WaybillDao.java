package net.lab1024.tms.driver.module.business.waybill.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.driver.module.business.waybill.domain.form.WaybillQueryForm;
import net.lab1024.tms.driver.module.business.waybill.domain.vo.WaybillVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface WaybillDao extends BaseMapper<WaybillEntity> {
    
    /**
     * 分页查询运单
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<WaybillVO> selectWaybillPage(Page page, @Param("queryForm") WaybillQueryForm queryForm);
    
    /**
     * 查询运单详细信息
     *
     * @param waybillId
     * @return
     */
    WaybillVO selectWaybillDetail(@Param("waybillId") Long waybillId);

    /**
     * 根据司机和运单状态查询运单数量
     *
     * @param driverId
     * @param waybillStatusList
     * @param value
     * @return
     */
    Integer selectWaybillCount(@Param("driverId") Long driverId, @Param("waybillStatusList") List<Integer> waybillStatusList, @Param("excludeStatus") Integer value);

}
