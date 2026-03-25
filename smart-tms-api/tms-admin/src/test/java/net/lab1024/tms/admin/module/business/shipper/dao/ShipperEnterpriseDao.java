package net.lab1024.tms.admin.module.business.shipper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.business.shipper.domain.ShipperEnterpriseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


@Mapper
@Component
@Deprecated
public interface ShipperEnterpriseDao extends BaseMapper<ShipperEnterpriseEntity> {

}
