package net.lab1024.tms.driver.module.business.driver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Mapper
@Component
public interface DriverDao extends BaseMapper<DriverEntity> {
    
    /**
     * 通过手机号查询司机
     *
     * @param phone
     * @param deletedFlag
     * @return
     */
    DriverEntity selectByPhone(@Param("enterpriseId") Long enterpriseId, @Param("phone") String phone, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据手机号查询
     *
     * @param telephone
     * @param excludeIds
     * @param deletedFlag
     * @return
     */
    List<DriverEntity> selectByPhoneExcludeIds(@Param("enterpriseId") Long enterpriseId,@Param("telephone") String telephone, @Param("excludeIds") ArrayList<Long> excludeIds, @Param("deletedFlag") Boolean deletedFlag);

}
