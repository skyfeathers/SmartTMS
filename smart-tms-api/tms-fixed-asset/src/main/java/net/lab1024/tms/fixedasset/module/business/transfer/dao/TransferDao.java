package net.lab1024.tms.fixedasset.module.business.transfer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.fixedasset.module.business.transfer.domain.TransferEntity;
import net.lab1024.tms.fixedasset.module.business.transfer.domain.TransferQueryForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 资产转移
 *
 * @author lidoudou
 * @date 2023/3/20 上午9:15
 */
@Mapper
@Component
public interface TransferDao extends BaseMapper<TransferEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<TransferEntity> queryPage(Page page, @Param("queryForm") TransferQueryForm queryForm);
}
