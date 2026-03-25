package net.lab1024.tms.driver.module.business.waybill.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillVoucherEntity;
import net.lab1024.tms.common.module.business.waybill.domain.vo.WaybillVoucherVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhuoda
 * @Date 2022-07-13
 */
@Mapper
@Component
public interface WaybillVoucherDao extends BaseMapper<WaybillVoucherEntity> {
    
    /**
     * 查询运单物流凭证
     *
     * @param waybillId
     * @return
     */
    List<WaybillVoucherVO> selectByWaybillId(@Param("waybillId") Long waybillId);
    
    /**
     * 查询运单某个类型的凭证
     *
     * @param waybillId
     * @param type
     * @return
     */
    WaybillVoucherEntity selectByWaybillIdAndType(@Param("waybillId") Long waybillId, @Param("type") Integer type);
    
}
