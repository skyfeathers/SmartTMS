package net.lab1024.tms.driver.module.business.carcost.tabulation;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.enumeration.UserTypeEnum;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostModuleTypeEnum;
import net.lab1024.tms.driver.module.business.carcost.cashpay.CarCostCashPayDao;
import net.lab1024.tms.driver.module.business.carcost.cashreceive.CarCostCashReceiveDao;
import net.lab1024.tms.driver.module.business.carcost.etcpay.CarCostEtcPayDao;
import net.lab1024.tms.driver.module.business.carcost.oilcardreceive.CarCostOilCardReceiveDao;
import net.lab1024.tms.driver.module.business.carcost.oilpay.CarCostOilPayDao;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import net.lab1024.tms.driver.module.business.carcost.tabulation.domain.CarCostTabulationQueryForm;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationSimpleVO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationVO;
import net.lab1024.tms.driver.module.business.carcost.ureapay.CarCostUreaPayDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CarCostTabulationService {

    @Resource
    private CarCostTabulationDao carCostTabulationDao;

    @Resource
    private CarCostCashReceiveDao carCostCashReceiveDao;

    @Resource
    private CarCostOilCardReceiveDao carCostOilCardReceiveDao;

    @Resource
    private CarCostCashPayDao carCostCashPayDao;

    @Resource
    private CarCostOilPayDao carCostOilPayDao;

    @Resource
    private CarCostEtcPayDao carCostEtcPayDao;

    @Resource
    private CarCostUreaPayDao carCostUreaPayDao;

    /**
     * 自有车费用列表-简单列表
     *
     * @param driverId
     * @return
     */
    public ResponseDTO<List<CarCostTabulationSimpleVO>> simpleList(Long driverId) {
        Integer userType = UserTypeEnum.DRIVER.getValue();
        List<CarCostTabulationSimpleVO> tabulationSimpleVOList = carCostTabulationDao.selectByDriverId(driverId, userType, new Integer(5));
        return ResponseDTO.ok(tabulationSimpleVOList);
    }

    /**
     * 自有车费用列表-列表
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<CarCostTabulationVO>> list(CarCostTabulationQueryForm queryForm) {
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<CarCostTabulationVO> tabulationVOList = carCostTabulationDao.selectByPage(page, queryForm);
        PageResult<CarCostTabulationVO> pageResult = SmartPageUtil.convert2PageResult(page, tabulationVOList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 费用详情
     *
     * @param moduleId
     * @param moduleType
     * @return
     */
    public ResponseDTO<CarCostTabulationDetailVO> detail(Long moduleId, Integer moduleType) {
        CarCostModuleTypeEnum moduleTypeEnum = SmartBaseEnumUtil.getEnumByValue(moduleType, CarCostModuleTypeEnum.class);
        CarCostTabulationDetailVO carCostTabulationDetailVO;
        switch (moduleTypeEnum) {
            case CASH_RECEIVE: {
                carCostTabulationDetailVO = carCostCashReceiveDao.selectDetail(moduleId);
                break;
            }
            case OIL_CARD_RECEIVE: {
                carCostTabulationDetailVO = carCostOilCardReceiveDao.selectDetail(moduleId);
                break;
            }
            case CASH_PAY: {
                carCostTabulationDetailVO = carCostCashPayDao.selectDetail(moduleId);
                break;
            }
            case OIL_PAY: {
                carCostTabulationDetailVO = carCostOilPayDao.selectDetail(moduleId);
                break;
            }
            case ETC_PAY: {
                carCostTabulationDetailVO = carCostEtcPayDao.selectDetail(moduleId);
                break;
            }
            case UREA_PAY: {
                carCostTabulationDetailVO = carCostUreaPayDao.selectDetail(moduleId);
                break;
            }
            default: {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "自有车模块类型错误");
            }
        }
        if (ObjectUtil.isEmpty(carCostTabulationDetailVO)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        carCostTabulationDetailVO.setModuleType(moduleType);
        return ResponseDTO.ok(carCostTabulationDetailVO);
    }
}
