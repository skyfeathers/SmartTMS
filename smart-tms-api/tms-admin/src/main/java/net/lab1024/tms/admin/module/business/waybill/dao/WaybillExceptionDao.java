package net.lab1024.tms.admin.module.business.waybill.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillExceptionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 运单异常
 *
 * @author lihaifan
 * @date 2022/12/3 14:32
 */
@Mapper
@Component
public interface WaybillExceptionDao extends BaseMapper<WaybillExceptionEntity> {

    /**
     * 根据运单ID查询
     * @param waybillIdList
     * @return
     */
    List<WaybillExceptionEntity> selectByWaybillIdList(@Param("waybillIdList") List<Long> waybillIdList, @Param("deletedFlag") Boolean deletedFlag);

}
