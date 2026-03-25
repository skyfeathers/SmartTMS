package net.lab1024.tms.admin.module.business.waybill.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.business.waybill.domain.vo.WaybillSplitTransportVO;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillSplitTransportEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;


/**
 * @author yandy
 * @description:
 * @date 2022/8/12 4:18 下午
 */
@Mapper
@Component
public interface WaybillSplitTransportDao extends BaseMapper<WaybillSplitTransportEntity> {


    /**
     * 根据运单查询
     *
     * @param waybillId
     * @return
     */
    List<WaybillSplitTransportVO> selectByWaybillId(@Param("waybillId") Long waybillId);

    List<Long> selectDriverIdByWaybillIdList(@Param("waybillIdList") List<Long> waybillIdList);

    WaybillSplitTransportVO detail(@Param("splitTransportId") Long splitTransportId);
}
