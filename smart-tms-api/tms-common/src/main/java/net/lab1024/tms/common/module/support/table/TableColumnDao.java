package net.lab1024.tms.common.module.support.table;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.support.table.domain.TableColumnEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 表结构
 *
 * @author zhuoda
 * @date 2022-08-20 17:37
 */
@Mapper
public interface TableColumnDao extends BaseMapper<TableColumnEntity> {

    TableColumnEntity selectByUserIdAndTableId(@Param("userId") Long userId, @Param("tableId")Integer tableId);

    void delete(@Param("userId") Long userId, @Param("tableId")Integer tableId);
}
