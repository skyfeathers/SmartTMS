package net.lab1024.tms.fixedasset.module.business.borrowback.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.fixedasset.module.business.borrowback.domain.BorrowBackItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lidoudou
 * @date 2023/3/21 上午10:38
 */
@Mapper
@Component
public interface BorrowBackItemDao extends BaseMapper<BorrowBackItemEntity> {

    /**
     * 根据ID查询关联资产
     *
     * @param borrowBackIdList
     * @return
     */
    List<BorrowBackItemEntity> queryByBorrowBackIdList(@Param("borrowBackIdList") List<Long> borrowBackIdList);
}
