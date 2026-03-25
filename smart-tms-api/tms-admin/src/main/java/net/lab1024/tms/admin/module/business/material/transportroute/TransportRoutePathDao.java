package net.lab1024.tms.admin.module.business.material.transportroute;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.business.material.transportroute.domain.dto.TransportRoutePathDTO;
import net.lab1024.tms.common.module.business.material.transportroute.domain.TransportRoutePathEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 业务资料-运输路线的地点列表
 *
 * @author lidoudou
 * @date 2022/7/12 下午4:22
 */
@Mapper
@Component
public interface TransportRoutePathDao extends BaseMapper<TransportRoutePathEntity> {

    /**
     * 根据运输路线查询地点列表
     *
     * @param transportRouteIdList
     * @return
     */
    List<TransportRoutePathDTO> selectByTransportRouteIdList(@Param("list") List<Long> transportRouteIdList);
    /**
     * 根据运输路线删除
     *
     * @param transportRouteId
     */
    void deleteByTransportRouteId(@Param("transportRouteId") Long transportRouteId);
}
