package net.lab1024.tms.admin.module.business.waybill.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillPathEntity;
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
public interface WaybillPathDao extends BaseMapper<WaybillPathEntity> {

    /**
     * @param waybillId
     */
    void deleteByWaybillId(@Param("waybillId") Long waybillId);

    /**
     * 根据运单ID查询路线
     *
     * @param waybillIdList
     * @return
     */
    List<WaybillPathEntity> selectByWaybillIdList(@Param("waybillIdList") Collection<Long> waybillIdList);
}
