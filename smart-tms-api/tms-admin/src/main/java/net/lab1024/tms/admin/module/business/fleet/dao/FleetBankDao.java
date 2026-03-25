package net.lab1024.tms.admin.module.business.fleet.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.fleet.domain.FleetBankEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 车队银行卡管理
 *
 * @author lidoudou
 * @date 2022/6/27 下午5:13
 */
@Mapper
@Component
public interface FleetBankDao extends BaseMapper<FleetBankEntity> {

    /**
     * 逻辑删除 根据ID更新银行卡的删除标识
     *
     * @param fleetIdList
     * @param deletedFlag
     * @return
     */
    Integer updateDeletedFlagByFleetId(@Param("fleetIdList") List<Long> fleetIdList, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据车队ID删除银行卡信息
     *
     * @param fleetId
     * @return
     */
    Integer deleteByFleetId(@Param("fleetId") Long fleetId);

    /**
     * 根据车队查询
     *
     * @param fleetId
     * @return
     */
    List<FleetBankEntity> selectByFleetId(@Param("fleetId") Long fleetId);

    /**
     * 查询默认银行
     *
     * @param fleetId
     * @param defaultFlag
     * @return
     */
    FleetBankEntity selectDefaultByFleetId(@Param("fleetId") Long fleetId, @Param("defaultFlag") Boolean defaultFlag);
}
