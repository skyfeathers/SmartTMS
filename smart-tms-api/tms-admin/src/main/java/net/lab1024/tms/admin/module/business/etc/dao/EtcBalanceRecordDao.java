package net.lab1024.tms.admin.module.business.etc.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.etc.domain.form.EtcBalanceRecordQueryForm;
import net.lab1024.tms.admin.module.business.etc.domain.vo.EtcBalanceRecordVO;
import net.lab1024.tms.common.module.business.etc.domain.EtcBalanceRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * etc交易记录查询
 *
 * @author lidoudou
 * @date 2022/6/30 下午2:48
 */
@Mapper
@Component
public interface EtcBalanceRecordDao extends BaseMapper<EtcBalanceRecordEntity> {

    /**
     * 分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<EtcBalanceRecordVO> queryByPage(Page page, @Param("queryForm") EtcBalanceRecordQueryForm queryForm);

}