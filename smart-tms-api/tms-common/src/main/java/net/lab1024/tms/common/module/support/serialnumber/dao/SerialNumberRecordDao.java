package net.lab1024.tms.common.module.support.serialnumber.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.support.serialnumber.domain.SerialNumberRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * id生成 dao
 *
 * @author zhuo
 */
@Mapper
@Component
public interface SerialNumberRecordDao extends BaseMapper<SerialNumberRecordEntity> {

    /**
     * 根据 id和日期 查询 记录id
     *
     * @param serialNumberId
     * @param recordDate
     * @return
     */
    Long selectRecordIdBySerialNumberIdAndDate(@Param("serialNumberId") Integer serialNumberId, @Param("recordDate") String recordDate);

    /**
     * 更新记录
     *
     * @param serialNumberId
     * @param recordDate
     * @param lastNumber
     * @param count
     * @return
     */
    Long updateRecord(@Param("serialNumberId") Integer serialNumberId, @Param("recordDate") LocalDate recordDate, @Param("lastNumber") Long lastNumber, @Param("count") int count);
}
