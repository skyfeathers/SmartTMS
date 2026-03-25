package net.lab1024.tms.driver.module.business.carcost.category;

import cn.hutool.core.util.ObjectUtil;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryCostTypeEnum;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostCategoryVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CarCostCategoryService {

    @Resource
    private CarCostCategoryDao carCostCategoryDao;

    /**
     * 根据费用类型查询费用分类数据
     *
     * @param costType
     * @return
     */
    public ResponseDTO<List<CarCostCategoryVO>> queryByCostType(Integer costType) {
        CarCostCategoryCostTypeEnum categoryCostTypeEnum = SmartBaseEnumUtil.getEnumByValue(costType, CarCostCategoryCostTypeEnum.class);
        if (ObjectUtil.isEmpty(categoryCostTypeEnum)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "费用类型错误");
        }
        List<CarCostCategoryVO> categoryVOList = carCostCategoryDao.selectByCostType(costType, Boolean.FALSE);
        return ResponseDTO.ok(categoryVOList);
    }

}