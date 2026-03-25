package net.lab1024.tms.admin.module.business.oilcard.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.oilcard.domain.dto.OilCardAmountDTO;
import net.lab1024.tms.admin.module.business.oilcard.domain.form.OilCardBalanceRecordQueryForm;
import net.lab1024.tms.admin.module.business.oilcard.domain.vo.OilCardBalanceRecordVO;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardBalanceRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * 油卡交易记录查询
 *
 * @author lidoudou
 * @date 2022/6/30 下午2:48
 */
@Mapper
@Component
public interface BalanceRecordDao extends BaseMapper<OilCardBalanceRecordEntity> {

    /**
     * 分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<OilCardBalanceRecordVO> queryByPage(Page page, @Param("queryForm") OilCardBalanceRecordQueryForm queryForm);

    /**
     * 查询
     *
     * @param recordIdList
     * @return
     */
    List<OilCardBalanceRecordVO> selectByRecordIdList(@Param("list") List<Long> recordIdList);

    /**
     * 根据油卡以及记录类型时间统计总额
     *
     * @param oilCardIdList
     * @param recordType
     * @param startTime
     * @param endTime
     * @return
     */
    List<OilCardAmountDTO> selectAmountByOilCard(@Param("oilCardIdList") List<Long> oilCardIdList, @Param("recordType") Integer recordType,
                                                 @Param("startTime") LocalDate startTime, @Param("endTime") LocalDate endTime);
}