package net.lab1024.tms.common.module.business.material.category;

import com.google.common.collect.Lists;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.business.material.category.domain.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 类目 业务
 *
 * @author Turbolisten
 * @date 2021/1/20 16:26
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryCommonDao categoryDao;

    @Autowired
    private CategoryQueryService categoryQueryService;

    @Autowired
    private CategoryCacheService categoryCacheService;

    /**
     * 添加类目
     *
     * @author Turbolisten
     * @date 2021/1/20 17:17
     */
    public ResponseDTO<Long> add(CategoryAddDTO addDTO) {
        // 校验类目
        CategoryEntity categoryEntity = SmartBeanUtil.copy(addDTO, CategoryEntity.class);
        ResponseDTO<String> res = this.checkCategory(categoryEntity, false);
        if (!res.getOk()) {
            return ResponseDTO.error(res);
        }
        // 没有父类则使用默认父类
        Long parentId = null == addDTO.getParentId() ? NumberConst.DEFAULT_PARENT_ID : addDTO.getParentId();
        categoryEntity.setParentId(parentId);
        categoryEntity.setSort(null == addDTO.getSort() ? NumberConst.ZERO : addDTO.getSort());
        categoryEntity.setDeletedFlag(false);

        // 保存数据
        categoryDao.insert(categoryEntity);

        // 更新缓存
        categoryCacheService.removeCache();
        return ResponseDTO.ok(categoryEntity.getCategoryId());
    }

    /**
     * 更新类目
     * 不能更新父级类目
     *
     * @author Turbolisten
     * @date 2021/1/20 17:17
     */
    public ResponseDTO<String> update(CategoryUpdateDTO updateDTO) {
        // 校验类目
        Long categoryId = updateDTO.getCategoryId();
        Optional<CategoryEntity> optional = categoryQueryService.queryCategory(categoryId);
        if (!optional.isPresent()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        CategoryEntity categoryEntity = SmartBeanUtil.copy(updateDTO, CategoryEntity.class);

        /**
         * 不更新类目类型
         * 不更新父类id
         */
        Integer categoryType = optional.get().getCategoryType();
        categoryEntity.setCategoryType(categoryType);
        categoryEntity.setParentId(optional.get().getParentId());

        ResponseDTO<String> responseDTO = this.checkCategory(categoryEntity, true);
        if (!responseDTO.getOk()) {
            return responseDTO;
        }
        categoryDao.updateById(categoryEntity);

        // 更新缓存
        categoryCacheService.removeCache();
        return ResponseDTO.ok();
    }

    /**
     * 新增/更新 类目时的 校验
     *
     * @param categoryEntity
     * @param isUpdate
     * @return
     */
    private ResponseDTO<String> checkCategory(CategoryEntity categoryEntity, boolean isUpdate) {
        // 校验父级是否存在
        Long parentId = categoryEntity.getParentId();
        Integer categoryType = categoryEntity.getCategoryType();
        if (null != parentId) {
            if (Objects.equals(categoryEntity.getCategoryId(), parentId)) {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "父级类目怎么和自己相同了");
            }
            if (!Objects.equals(parentId, NumberConst.DEFAULT_PARENT_ID)) {
                Optional<CategoryEntity> optional = categoryQueryService.queryCategory(parentId);
                if (!optional.isPresent()) {
                    return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "父级类目不存在~");
                }

                CategoryEntity parent = optional.get();
                if (!Objects.equals(categoryType, parent.getCategoryType())) {
                    return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "与父级类目类型不一致");
                }
            }

        } else {
            // 如果没有父类 使用默认父类
            parentId = NumberConst.DEFAULT_PARENT_ID;
        }

        // 校验同父类下 名称是否重复
        CategoryEntity queryEntity = new CategoryEntity();
        queryEntity.setParentId(parentId);
        queryEntity.setCategoryType(categoryType);
        queryEntity.setCategoryName(categoryEntity.getCategoryName());
        queryEntity.setDeletedFlag(false);
        queryEntity = categoryDao.selectOne(queryEntity);
        if (null != queryEntity) {
            if (isUpdate) {
                if (!Objects.equals(queryEntity.getCategoryId(), categoryEntity.getCategoryId())) {
                    return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "同级下已存在相同类目~");
                }
            } else {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "同级下已存在相同类目~");
            }
        }
        return ResponseDTO.ok();
    }

    /**
     * 查询 类目详情
     *
     * @param categoryId
     * @return
     */
    public ResponseDTO<CategoryVO> queryDetail(Long categoryId) {
        Optional<CategoryEntity> optional = categoryQueryService.queryCategory(categoryId);
        if (!optional.isPresent()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        CategoryVO adminVO = SmartBeanUtil.copy(optional.get(), CategoryVO.class);
        return ResponseDTO.ok(adminVO);
    }

    /**
     * 根据父级id 查询所有子类 返回层级树
     * 如果父类id 为空 返回所有类目层级
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<List<CategoryTreeVO>> queryTree(CategoryTreeQueryDTO queryDTO) {
        if (null == queryDTO.getParentId()) {
            if (null == queryDTO.getCategoryType()) {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "类目类型不能为空");
            }
            queryDTO.setParentId(NumberConst.DEFAULT_PARENT_ID);
        }
        List<CategoryTreeVO> treeList = categoryQueryService.queryCategoryTree(queryDTO);
        return ResponseDTO.ok(treeList);
    }

    /**
     * 删除类目
     * 如果有未删除的子类 则无法删除
     *
     * @param categoryId
     * @return
     */
    public ResponseDTO<String> delete(Long categoryId) {
        Optional<CategoryEntity> optional = categoryQueryService.queryCategory(categoryId);
        if (!optional.isPresent()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        List<Long> categorySubId = categoryQueryService.queryCategorySubId(Lists.newArrayList(categoryId));
        if (CollectionUtils.isNotEmpty(categorySubId)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "请先删除子级类目");
        }

        // 更新数据
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryId(categoryId);
        categoryEntity.setDeletedFlag(true);
        categoryDao.updateById(categoryEntity);

        // 更新缓存
        categoryCacheService.removeCache();
        return ResponseDTO.ok();
    }

}
