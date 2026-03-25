package net.lab1024.tms.common.module.business.material.category;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.module.business.material.category.domain.CategoryEntity;
import net.lab1024.tms.common.module.business.material.category.domain.CategorySimpleDTO;
import net.lab1024.tms.common.module.business.material.category.domain.CategoryTreeQueryDTO;
import net.lab1024.tms.common.module.business.material.category.domain.CategoryTreeVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 类目 查询 业务类
 *
 * @author Turbolisten
 * @date 2021/1/20 16:26
 */
@Service
@Slf4j
public class CategoryQueryService {

    @Autowired
    private CategoryCacheService categoryCacheService;

    /**
     * 根据 id 查询未删除的类目
     *
     * @param categoryId
     * @return 可能 null
     */
    public Optional<CategoryEntity> queryCategory(Long categoryId) {
        if (null == categoryId) {
            return Optional.empty();
        }
        CategoryEntity entity = categoryCacheService.queryCategory(categoryId);
        if (null == entity || entity.getDeletedFlag()) {
            return Optional.empty();
        }
        return Optional.of(entity);
    }

    /**
     * 根据 类目id 查询未删除的子类
     *
     * @param categoryId
     * @return 没有返回空集合
     */
    public List<CategoryEntity> queryCategoryByParent(Long categoryId) {
        if (null == categoryId) {
            return Collections.emptyList();
        }
        return categoryCacheService.querySubCategory(categoryId);
    }

    /**
     * 根据 类目id集合 查询未删除的类目集合
     *
     * @param categoryIdList
     * @return
     */
    public Map<Long, CategoryEntity> queryCategoryList(List<Long> categoryIdList) {
        categoryIdList = categoryIdList.stream().filter(e -> e != null && !e.equals(NumberUtils.LONG_ZERO)).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(categoryIdList)) {
            return Collections.EMPTY_MAP;
        }
        Map<Long, CategoryEntity> resultMap = new HashMap<>();
        for (Long categoryId : categoryIdList) {
            CategoryEntity categoryEntity = categoryCacheService.queryCategory(categoryId);
            if (categoryEntity == null) {
                continue;
            }
            resultMap.put(categoryId, categoryEntity);
        }
        return resultMap;
    }

    /**
     * 根据 类目id集合 查询未删除的类目以及父级集合
     *
     * @param categoryIdList
     * @return
     */
    public Map<Long, List<CategoryEntity>> queryCategoryAndParentList(List<Long> categoryIdList) {
        categoryIdList = categoryIdList.stream().filter(e -> e != null && !e.equals(NumberUtils.LONG_ZERO)).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(categoryIdList)) {
            return Collections.EMPTY_MAP;
        }
        Map<Long, List<CategoryEntity>> resultMap = new HashMap<>();
        for (Long categoryId : categoryIdList) {
            List<CategoryEntity> categoryEntities = this.handleParentCategory(categoryId);
            //翻转
            Collections.reverse(categoryEntities);
            resultMap.put(categoryId, categoryEntities);
        }
        return resultMap;
    }

    private List<CategoryEntity> handleParentCategory(Long categoryId) {
        List<CategoryEntity> resultList = Lists.newArrayList();
        if (categoryId == null || categoryId.equals(NumberUtils.LONG_ZERO)) {
            return resultList;
        }
        CategoryEntity categoryEntity = categoryCacheService.queryCategory(categoryId);
        if (categoryEntity == null) {
            return resultList;
        }
        resultList.add(categoryEntity);
        if (!Objects.equals(categoryEntity.getParentId(), NumberConst.DEFAULT_PARENT_ID)) {
            List<CategoryEntity> categoryEntities = this.handleParentCategory(categoryEntity.getParentId());
            resultList.addAll(categoryEntities);
        }
        return resultList;
    }

    /**
     * 根据类目id 递归查询该id的所有子类id 递归查询
     * 同时存入缓存
     * 注意：查询出来的集合 不包含传递的父类参数
     *
     * @param categoryIdList
     */
    public List<Long> queryCategorySubId(List<Long> categoryIdList) {
        if (CollectionUtils.isEmpty(categoryIdList)) {
            return Collections.emptyList();
        }
        categoryIdList = categoryIdList.stream().filter(e -> e != null && !e.equals(NumberUtils.LONG_ZERO)).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(categoryIdList)) {
            return Collections.emptyList();
        }
        //所有子类
        List<CategoryEntity> categoryEntityList = Lists.newArrayList();
        categoryIdList.forEach(e -> {
            categoryEntityList.addAll(categoryCacheService.querySubCategory(e));
        });
        Map<Long, List<CategoryEntity>> subTypeMap = categoryEntityList.stream().collect(Collectors.groupingBy(CategoryEntity::getCategoryId));
        // 递归查询子类
        categoryIdList = subTypeMap.values().stream().flatMap(Collection::stream).map(CategoryEntity::getCategoryId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(categoryIdList)) {
            return Lists.newArrayList();
        }
        categoryIdList.addAll(this.queryCategorySubId(categoryIdList));
        return categoryIdList;
    }

    /**
     * 查询自身以及所有子节点
     *
     * @param categoryIdList
     * @return
     */
    public List<Long> queryCategorySelfAndSubId(List<Long> categoryIdList) {
        List<Long> subIdList = this.queryCategorySubId(categoryIdList);
        subIdList.addAll(categoryIdList);
        return subIdList;
    }

    /**
     * 查询类目 层级树
     * 优先查询缓存
     *
     * @return
     */
    public List<CategoryTreeVO> queryCategoryTree(CategoryTreeQueryDTO queryDTO) {
        return categoryCacheService.queryCategoryTree(queryDTO.getParentId(), queryDTO.getCategoryType());
    }

    /**
     * 根据类目id 查询类目详情 包含类目全称 如：医考/医师资格/临床执业
     *
     * @param categoryId
     * @return
     */
    public CategorySimpleDTO queryCategoryInfo(Long categoryId) {
        if (categoryId == null || categoryId.equals(NumberUtils.LONG_ZERO)) {
            return null;
        }
        CategoryEntity categoryEntity = categoryCacheService.queryCategory(categoryId);
        if (null == categoryEntity || categoryEntity.getDeletedFlag()) {
            return null;
        }

        // 递归查询分类和所有父级类目
        List<CategoryEntity> parentCategoryList = this.queryCategoryAndParent(categoryId);
        // 拼接父级类目名称 斜杠分隔返回
        List<String> nameList = parentCategoryList.stream().map(CategoryEntity::getCategoryName).collect(Collectors.toList());

        // 返回DTO
        CategorySimpleDTO categoryDTO = new CategorySimpleDTO();
        categoryDTO.setCategoryId(categoryId);
        categoryDTO.setCategoryName(categoryEntity.getCategoryName());
        categoryDTO.setCategoryType(categoryEntity.getCategoryType());
        categoryDTO.setCategoryFullName(StrUtil.join(StringConst.SEPARATOR_SLASH, nameList));
        categoryDTO.setCategoryNameList(nameList);
        categoryDTO.setParentId(categoryEntity.getParentId());
        return categoryDTO;
    }

    /**
     * 递归查询分类和所有父级类目 ps:特别注意返回的集合中 包含自己
     *
     * @param categoryId
     * @return
     */
    public List<CategoryEntity> queryCategoryAndParent(Long categoryId) {
        List<CategoryEntity> parentCategoryList = Lists.newArrayList();
        if (categoryId == null || categoryId.equals(NumberUtils.LONG_ZERO)) {
            return parentCategoryList;
        }
        CategoryEntity categoryEntity = categoryCacheService.queryCategory(categoryId);
        if (null == categoryEntity || categoryEntity.getDeletedFlag()) {
            return parentCategoryList;
        }

        // 父级始终放在第一位
        parentCategoryList.add(0, categoryEntity);
        Long parentId = categoryEntity.getParentId();
        if (Objects.equals(NumberConst.DEFAULT_PARENT_ID, parentId)) {
            return parentCategoryList;
        }
        parentCategoryList.addAll(0, this.queryCategoryAndParent(parentId));
        return parentCategoryList;
    }

    /**
     * 处理类目名称
     *
     * @param categoryIdList
     */
    public List<String> queryCategoryName(List<Long> categoryIdList) {
        if (CollectionUtils.isEmpty(categoryIdList)) {
            return null;
        }
        Map<Long, CategoryEntity> categoryMap = this.queryCategoryList(categoryIdList);
        List<String> categoryNameList = Lists.newArrayList();
        categoryIdList.forEach(e -> {
            CategoryEntity categoryEntity = categoryMap.get(e);
            if (categoryEntity != null) {
                categoryNameList.add(categoryMap.get(e).getCategoryName());
            }
        });
        return categoryNameList;
    }

    /**
     * 根据类目id 查询类目名称
     *
     * @param categoryId
     * @return
     */
    public String queryCategoryName(Long categoryId) {
        CategoryEntity categoryEntity = categoryCacheService.queryCategory(categoryId);
        if (null == categoryEntity || categoryEntity.getDeletedFlag()) {
            return null;
        }
        return categoryEntity.getCategoryName();
    }

}
