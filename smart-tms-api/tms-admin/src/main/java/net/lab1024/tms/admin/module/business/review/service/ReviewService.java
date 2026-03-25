package net.lab1024.tms.admin.module.business.review.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.dto.CarCostVehicleMonthAmountDTO;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.form.CarCostMonthStatisticQueryForm;
import net.lab1024.tms.admin.module.business.review.ReviewDao;
import net.lab1024.tms.admin.module.business.review.domain.ReviewAddForm;
import net.lab1024.tms.admin.module.business.review.domain.ReviewQueryForm;
import net.lab1024.tms.admin.module.business.review.domain.ReviewUpdateForm;
import net.lab1024.tms.admin.module.business.review.domain.ReviewVO;
import net.lab1024.tms.admin.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.review.ReviewEntity;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Resource
    private ReviewDao reviewDao;

    @Resource
    private VehicleDao vehicleDao;

    @Resource
    private ReviewDataTracerService reviewDataTracerService;

    /**
     * 审车表信息分页查询
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<ReviewVO>> query(ReviewQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<ReviewVO> reviewVOList = reviewDao.queryByPage(page, queryForm);
        PageResult<ReviewVO> pageResult = SmartPageUtil.convert2PageResult(page, reviewVOList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 添加审车信息
     *
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> add(ReviewAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        Long vehicleId = addForm.getVehicleId();
        VehicleEntity vehicleEntity = vehicleDao.selectById(vehicleId);
        if (ObjectUtil.isEmpty(vehicleEntity) || vehicleEntity.getDeletedFlag() || vehicleEntity.getDisabledFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "车辆不可用");
        }

        ReviewEntity reviewEntity = SmartBeanUtil.copy(addForm, ReviewEntity.class);
        reviewEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        reviewEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());

        reviewDao.insert(reviewEntity);
        reviewDataTracerService.addLog(reviewEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 修改审车信息
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> update(ReviewUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        ReviewEntity reviewEntity = reviewDao.selectById(updateForm.getReviewId());
        if (ObjectUtil.isEmpty(reviewEntity) || reviewEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "审车信息不存在");
        }

        ReviewEntity updateEntity = SmartBeanUtil.copy(updateForm, ReviewEntity.class);
        updateEntity.setUpdateUserId(dataTracerRequestForm.getOperatorId());
        updateEntity.setUpdateUserName(dataTracerRequestForm.getOperatorName());
        reviewDao.updateById(updateEntity);
        reviewDataTracerService.updateLog(reviewEntity, updateEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 删除审车信息
     *
     * @param reviewId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> delete(Long reviewId, DataTracerRequestForm dataTracerRequestForm) {
        ReviewEntity reviewEntity = reviewDao.selectById(reviewId);
        if (ObjectUtil.isEmpty(reviewEntity) || reviewEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "审车信息不存在");
        }

        reviewDao.updateDeletedFlag(reviewId, Boolean.TRUE);
        reviewDataTracerService.deleteLog(reviewEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }


    /**
     * 根据车辆ID以及创建时间筛选
     * @param vehicleId
     * @param startTime
     * @param endTime
     * @return
     */
    public List<ReviewVO> selectListByVehicleId(Long vehicleId, LocalDate startTime, LocalDate endTime) {
        ReviewQueryForm reviewQueryForm = new ReviewQueryForm();
        reviewQueryForm.setVehicleId(vehicleId);
        reviewQueryForm.setStartDate(startTime);
        reviewQueryForm.setEndDate(endTime);
        reviewQueryForm.setDeletedFlag(Boolean.FALSE);
        List<ReviewVO> reviewVOList = reviewDao.queryByPage(null, reviewQueryForm);
        return reviewVOList;
    }

    /**
     * 根据车辆ID以及创建时间筛选
     * @param vehicleIdList
     * @param startTime
     * @param endTime
     * @return
     */
    public List<ReviewVO> selectListByVehicleIdList(Long enterpriseId, List<Long> vehicleIdList, LocalDate startTime, LocalDate endTime) {
        ReviewQueryForm reviewQueryForm = new ReviewQueryForm();
        reviewQueryForm.setVehicleIdList(vehicleIdList);
        reviewQueryForm.setStartDate(startTime);
        reviewQueryForm.setEndDate(endTime);
        reviewQueryForm.setDeletedFlag(Boolean.FALSE);
        reviewQueryForm.setEnterpriseId(enterpriseId);
        List<ReviewVO> reviewVOList = reviewDao.queryByPage(null, reviewQueryForm);
        return reviewVOList;
    }

    public Map<Long, BigDecimal> getCarCostReviewAmount(CarCostMonthStatisticQueryForm queryForm) {
        List<CarCostVehicleMonthAmountDTO> carCostVehicleMonthAmountDTOList = reviewDao.sumByVehicleIdListAndType(queryForm);
        return carCostVehicleMonthAmountDTOList.stream().collect(Collectors.toMap(CarCostVehicleMonthAmountDTO::getVehicleId, CarCostVehicleMonthAmountDTO::getAmount));
    }
}