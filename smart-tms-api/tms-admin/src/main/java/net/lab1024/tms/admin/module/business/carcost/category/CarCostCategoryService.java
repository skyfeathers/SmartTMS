package net.lab1024.tms.admin.module.business.carcost.category;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.carcost.category.domain.*;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryCostTypeEnum;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCategoryEntity;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostCategoryVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CarCostCategoryService {

    @Resource
    private CarCostCategoryDao carCostCategoryDao;

    /**
     * 查询所有费用分类
     *
     * @return
     */
    public ResponseDTO<List<CarCostCategoryVO>> queryAll() {
        List<CarCostCategoryVO> categoryVOList = carCostCategoryDao.selectAll(Boolean.FALSE);
        return ResponseDTO.ok(categoryVOList);
    }

    /**
     * 分页查询费用分类
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<CarCostCategoryVO>> queryPage(CarCostCategoryQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<CarCostCategoryVO> categoryVOList = carCostCategoryDao.selectByPage(page, queryForm);
        PageResult<CarCostCategoryVO> pageResult = SmartPageUtil.convert2PageResult(page, categoryVOList);
        return ResponseDTO.ok(pageResult);
    }

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

    /**
     * 新建费用分类
     *
     * @param addForm
     * @return
     */
    public ResponseDTO<String> save(CarCostCategoryAddForm addForm) {
        CarCostCategoryEntity dbCategoryEntity = carCostCategoryDao.selectByCategoryName(addForm.getCategoryName(), addForm.getCostType(), null, Boolean.FALSE);
        if (ObjectUtil.isNotEmpty(dbCategoryEntity)) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "分类名字已存在");
        }
        CarCostCategoryEntity categoryEntity = SmartBeanUtil.copy(addForm, CarCostCategoryEntity.class);
        carCostCategoryDao.insert(categoryEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新费用分类
     *
     * @param updateForm
     * @return
     */
    public ResponseDTO<String> update(CarCostCategoryUpdateForm updateForm) {
        Long categoryId = updateForm.getCategoryId();
        CarCostCategoryEntity dbCategoryEntity = carCostCategoryDao.selectByCategoryName(updateForm.getCategoryName(), updateForm.getCostType(), Lists.newArrayList(categoryId), Boolean.FALSE);
        if (ObjectUtil.isNotEmpty(dbCategoryEntity)) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "分类名字已存在");
        }
        CarCostCategoryEntity carCostCategoryEntity = carCostCategoryDao.selectById(categoryId);
        if (ObjectUtil.isEmpty(carCostCategoryEntity) || carCostCategoryEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "费用分类不存在");
        }
        CarCostCategoryEntity updateEntity = SmartBeanUtil.copy(updateForm, CarCostCategoryEntity.class);
        carCostCategoryDao.updateById(updateEntity);
        return ResponseDTO.ok();
    }

    /**
     * 删除费用分类
     *
     * @param categoryId
     * @return
     */
    public ResponseDTO<String> delete(Long categoryId) {
        CarCostCategoryEntity carCostCategoryEntity = carCostCategoryDao.selectById(categoryId);
        if (ObjectUtil.isEmpty(carCostCategoryEntity) || carCostCategoryEntity.getDeletedFlag()) {
            return ResponseDTO.ok();
        }
        carCostCategoryDao.updateDeletedFlag(categoryId, Boolean.TRUE);
        return ResponseDTO.ok();
    }
}