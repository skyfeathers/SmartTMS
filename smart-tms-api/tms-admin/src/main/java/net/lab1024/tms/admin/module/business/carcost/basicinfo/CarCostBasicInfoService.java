package net.lab1024.tms.admin.module.business.carcost.basicinfo;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.carcost.basicinfo.domain.CarCostBasicInfoForm;
import net.lab1024.tms.admin.module.business.carcost.basicinfo.domain.CarCostBasicInfoQueryForm;
import net.lab1024.tms.admin.module.business.carcost.basicinfo.domain.CarCostBasicInfoVO;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostBasicInfoEntity;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class CarCostBasicInfoService {

    @Resource
    private CarCostBasicInfoDao carCostBasicInfoDao;

    @Resource
    private WaybillDao waybillDao;
    @Resource
    private CarCostBasicInfoManager carCostBasicInfoManager;
    @Resource
    private CarCostBasicInfoDataTracerService carCostBasicInfoDataTracerService;

    /**
     * 自有车基本信息分页
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<CarCostBasicInfoVO>> page(CarCostBasicInfoQueryForm queryForm) {
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<CarCostBasicInfoVO> basicInfoVOList =  carCostBasicInfoDao.selectByPage(page, queryForm);
        PageResult<CarCostBasicInfoVO> pageResult = SmartPageUtil.convert2PageResult(page, basicInfoVOList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 自有车基本信息详情
     *
     * @param waybillId
     * @return
     */
    public ResponseDTO<CarCostBasicInfoVO> detail(Long waybillId) {
        WaybillEntity waybillEntity = waybillDao.selectById(waybillId);
        if (ObjectUtil.isEmpty(waybillEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "运单不存在");
        }
        CarCostBasicInfoVO basicInfoVO = carCostBasicInfoDao.selectByWaybillId(waybillId);
        return ResponseDTO.ok(basicInfoVO);
    }

    /**
     * 新建或更新自有车基本信息
     *
     * @param carCostBasicInfoForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> update(CarCostBasicInfoForm carCostBasicInfoForm, DataTracerRequestForm dataTracerRequestForm) {
        Long waybillId = carCostBasicInfoForm.getWaybillId();
        WaybillEntity waybillEntity = waybillDao.selectById(waybillId);
        if (ObjectUtil.isEmpty(waybillEntity) || WaybillStatusEnum.CANCEL.equalsValue(waybillEntity.getWaybillStatus())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "运单不存在或已作废");
        }
        if (!carCostBasicInfoForm.getEnterpriseId().equals(waybillEntity.getEnterpriseId())) {
            return ResponseDTO.userErrorParam("运单所属企业和当前登录企业不一致");
        }
        CarCostBasicInfoVO basicInfoVO = carCostBasicInfoDao.selectByWaybillId(waybillId);
        if (ObjectUtil.isNotEmpty(basicInfoVO) && basicInfoVO.getConfirmFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "信息已确认，不可修改");
        }
        BigDecimal salaryTotal = basicInfoVO.getBasicSalary().add(basicInfoVO.getAllowance()).add(basicInfoVO.getCommissionSalary());

        CarCostBasicInfoEntity basicInfoEntity = SmartBeanUtil.copy(carCostBasicInfoForm, CarCostBasicInfoEntity.class);
        basicInfoEntity.setSalaryTotal(salaryTotal);

        if (ObjectUtil.isEmpty(basicInfoVO)) {
            carCostBasicInfoDao.insert(basicInfoEntity);
            carCostBasicInfoDataTracerService.saveBasicInfoLog(basicInfoEntity, dataTracerRequestForm);
        } else {
            basicInfoEntity.setBasicInfoId(basicInfoVO.getBasicInfoId());
            carCostBasicInfoDao.updateById(basicInfoEntity);
            CarCostBasicInfoEntity beforeEntity = SmartBeanUtil.copy(basicInfoVO, CarCostBasicInfoEntity.class);
            carCostBasicInfoDataTracerService.updateBasicInfoLog(beforeEntity, basicInfoEntity, dataTracerRequestForm);
        }
        return ResponseDTO.ok();
    }

    /**
     * 更新确认标识
     *
     * @param basicInfoId
     * @param confirmFlag
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> updateConfirm(Long basicInfoId, Boolean confirmFlag, DataTracerRequestForm dataTracerRequestForm) {
        CarCostBasicInfoEntity basicInfoEntity = carCostBasicInfoDao.selectById(basicInfoId);
        if (ObjectUtil.isEmpty(basicInfoEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "基本信息记录不存在");
        }
        if (Objects.equals(confirmFlag, basicInfoEntity.getConfirmFlag())) {
            return ResponseDTO.ok();
        }
        carCostBasicInfoManager.updateConfirm(basicInfoId, basicInfoEntity.getWaybillId(), confirmFlag);
        return ResponseDTO.ok();
    }


    public Boolean selectConfirmFlagByWaybillId(Long waybillId) {
        CarCostBasicInfoVO carCostBasicInfoVO = carCostBasicInfoDao.selectByWaybillId(waybillId);
        if (null == carCostBasicInfoVO) {
            return Boolean.FALSE;
        }

        return carCostBasicInfoVO.getConfirmFlag();
    }

    /**
     * 删除自有车基本信息
     *
     * @param basicInfoId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> del(Long basicInfoId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostBasicInfoEntity basicInfoEntity = carCostBasicInfoDao.selectById(basicInfoId);
        if (basicInfoEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "基本信息记录不存在");
        }
        if (basicInfoEntity.getConfirmFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "信息已确认，不可删除");
        }
        carCostBasicInfoManager.del(basicInfoId, basicInfoEntity.getWaybillId());
        carCostBasicInfoDataTracerService.deleteLog(basicInfoEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }
}