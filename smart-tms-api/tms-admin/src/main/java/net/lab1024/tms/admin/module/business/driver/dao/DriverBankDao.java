package net.lab1024.tms.admin.module.business.driver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.driver.domain.DriverBankEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/***
 * 司机银行卡信息
 *
 * @author lidoudou
 * @date 2022/6/21 上午10:00
 */
@Mapper
@Component
public interface DriverBankDao extends BaseMapper<DriverBankEntity> {

    /**
     * 逻辑删除 根据司机ID更新银行卡的删除标识
     *
     * @param driverIdList
     * @param deletedFlag
     * @return
     */
    Integer updateDeletedFlag(@Param("driverIdList") List<Long> driverIdList, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据司机ID查询银行信息
     *
     * @param driverId
     * @return
     */
    List<DriverBankEntity> selectByDriverId(@Param("driverId") Long driverId);

    /**
     * 查询默认银行
     * @param driverId
     * @param defaultFlag
     * @return
     */
    DriverBankEntity selectDefaultByDriverId(@Param("driverId") Long driverId, @Param("defaultFlag") Boolean defaultFlag);

    /**
     * 删除司机银行卡
     *
     * @param driverId
     * @param excludeIds
     */
    void deleteByDriverIdExcludeIds(@Param("driverId") Long driverId, @Param("excludeIds") List<Long> excludeIds);
}
