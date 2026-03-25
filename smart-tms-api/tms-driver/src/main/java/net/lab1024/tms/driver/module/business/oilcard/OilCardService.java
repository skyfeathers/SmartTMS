package net.lab1024.tms.driver.module.business.oilcard;

import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import net.lab1024.tms.driver.module.business.oilcard.domain.OilCardSimpleVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class OilCardService {

    @Resource
    private OilCardDao oilCardDao;

    /**
     * 查询油卡不分页的列表
     *
     * @param vehicleId
     * @return
     */
    public ResponseDTO<List<OilCardSimpleVO>> queryListByVehicleId(Long vehicleId) {
        List<OilCardSimpleVO> oilCardList = oilCardDao.queryListByVehicleId(vehicleId, OilCardTypeEnum.SLAVE.getValue(), Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
        return ResponseDTO.ok(oilCardList);
    }

    /**
     * 根据油卡ID获取余额
     * @param oilCardId
     * @return
     */
    public ResponseDTO<BigDecimal> getBalanceByOilCardId(Long oilCardId) {
        OilCardEntity oilCardEntity = oilCardDao.selectById(oilCardId);
        return ResponseDTO.ok(oilCardEntity.getBalance());
    }

}
