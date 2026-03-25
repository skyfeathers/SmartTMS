package net.lab1024.tms.common.module.business.carcost;

import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.dao.CommonCarCostOilCardReceiveDao;
import net.lab1024.tms.common.module.business.carcost.dao.CommonCarCostOilPayDao;
import net.lab1024.tms.common.module.business.carcost.dao.CommonCarCostUreaPayDao;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author lidoudou
 * @description:
 * @date 2023/11/9 21:34
 */
@Service
public class CarCostOilCardValidateService {

    @Autowired
    private CommonCarCostOilCardReceiveDao commonCarCostOilCardReceiveDao;

    @Autowired
    private CommonCarCostOilPayDao commonCarCostOilPayDao;

    @Resource
    private CommonCarCostUreaPayDao commonCarCostUreaPayDao;

    /**
     * 校验是否使用多张同类型油卡
     *
     * @param waybillId
     * @param fuelType
     * @param originOilCardId
     * @return
     */
    public ResponseDTO<String> validateOilCardId(Long waybillId, Integer fuelType, Long originOilCardId) {
        // 运单进行油卡时，同一燃油类型，只能使用一张卡
        OilCardEntity receiveUsedOilCardEntity = commonCarCostOilCardReceiveDao.selectByWaybillIdAndFuelType(waybillId, fuelType, AuditStatusEnum.REJECT.getValue());
        if (null != receiveUsedOilCardEntity && !Objects.equals(receiveUsedOilCardEntity.getOilCardId(), originOilCardId)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "已使用【" + receiveUsedOilCardEntity.getOilCardNo() + "】油卡进行充值，同一运单不可使用多张同类型油卡");
        }

        OilCardEntity oilPayUsedOilCardEntity = commonCarCostOilPayDao.selectByWaybillIdAndFuelType(waybillId, fuelType, AuditStatusEnum.REJECT.getValue());
        if (null != oilPayUsedOilCardEntity && !Objects.equals(oilPayUsedOilCardEntity.getOilCardId(), originOilCardId)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "已使用【" + oilPayUsedOilCardEntity.getOilCardNo() + "】油卡进行油费支出，同一运单不可使用多张同类型油卡");
        }

        OilCardEntity ureaPayUsedOilCardEntity = commonCarCostUreaPayDao.selectByWaybillIdAndFuelType(waybillId, fuelType, AuditStatusEnum.REJECT.getValue());
        if (null != ureaPayUsedOilCardEntity && !Objects.equals(ureaPayUsedOilCardEntity.getOilCardId(), originOilCardId)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "已使用【" + ureaPayUsedOilCardEntity.getOilCardNo() + "】油卡进行尿素支出，同一运单不可使用多张同类型油卡");
        }

        return ResponseDTO.ok();
    }
}