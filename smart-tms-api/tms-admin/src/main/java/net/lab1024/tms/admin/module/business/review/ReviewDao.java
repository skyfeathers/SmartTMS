package net.lab1024.tms.admin.module.business.review;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.dto.CarCostVehicleMonthAmountDTO;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.form.CarCostMonthStatisticQueryForm;
import net.lab1024.tms.admin.module.business.review.domain.ReviewQueryForm;
import net.lab1024.tms.admin.module.business.review.domain.ReviewVO;
import net.lab1024.tms.common.module.business.review.ReviewEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ReviewDao extends BaseMapper<ReviewEntity> {

    /**
     * 审车信息分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<ReviewVO> queryByPage(Page page, @Param("queryForm") ReviewQueryForm queryForm);

    /**
     * 更改删除状态
     *
     * @param reviewId
     * @param deletedFlag
     */
    void updateDeletedFlag(@Param("reviewId") Long reviewId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据自有车统计金额
     *
     * @param queryForm
     * @return
     */
    List<CarCostVehicleMonthAmountDTO> sumByVehicleIdListAndType(@Param("queryForm") CarCostMonthStatisticQueryForm queryForm);
}