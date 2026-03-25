package net.lab1024.tms.driver.module.business.waybill.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillGoodsEntity;
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
public interface WaybillGoodsDao extends BaseMapper<WaybillGoodsEntity> {

    /**
     * 查询运单货物信息
     *
     * @param waybillIdList
     * @return
     */
    List<WaybillGoodsEntity> selectByWaybillIdList(@Param("waybillIdList") Collection<Long> waybillIdList);
    
}
