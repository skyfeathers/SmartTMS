package net.lab1024.tms.fixedasset.module.business.report;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartBigDecimalUtil;
import net.lab1024.tms.common.common.util.SmartLocalDateUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.material.category.CategoryCommonDao;
import net.lab1024.tms.common.module.business.material.category.domain.CategoryEntity;
import net.lab1024.tms.common.module.business.oa.enterprise.dao.CommonEnterpriseDao;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.fixedasset.module.business.asset.dao.AssetDao;
import net.lab1024.tms.fixedasset.module.business.asset.domain.entity.AssetEntity;
import net.lab1024.tms.fixedasset.module.business.asset.domain.form.AssetQueryForm;
import net.lab1024.tms.fixedasset.module.business.report.domain.DepreciationStatisticQueryForm;
import net.lab1024.tms.fixedasset.module.business.report.domain.DepreciationStatisticVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 资产报表
 *
 * @author lidoudou
 * @date 2023/3/29 下午1:55
 */
@Service
public class ReportService {
    @Autowired
    private AssetDao assetDao;
    @Autowired
    private CommonEnterpriseDao commonEnterpriseDao;
    @Autowired
    private CategoryCommonDao commonCategoryDao;

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public PageResult<DepreciationStatisticVO> queryPage(DepreciationStatisticQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        AssetQueryForm assetQueryForm = SmartBeanUtil.copy(queryForm, AssetQueryForm.class);

        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<AssetEntity> entityList = assetDao.queryPage(page, assetQueryForm);
        List<DepreciationStatisticVO> list = SmartBeanUtil.copyList(entityList, DepreciationStatisticVO.class);
        buildList(list);
        PageResult<DepreciationStatisticVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return pageResult;
    }

    private void buildList(List<DepreciationStatisticVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // 分类
        Set<Long> categoryIdSet = list.stream().map(DepreciationStatisticVO::getCategoryId).collect(Collectors.toSet());
        List<CategoryEntity> categoryList = commonCategoryDao.selectBatchIds(categoryIdSet);
        Map<Long, String> categoryNameMap = categoryList.stream().collect(Collectors.toMap(CategoryEntity::getCategoryId, CategoryEntity::getCategoryName));

        // 所属公司
        Set<Long> enterpriseIdSet = list.stream().map(DepreciationStatisticVO::getEnterpriseId).collect(Collectors.toSet());
        List<EnterpriseEntity> enterpriseList = commonEnterpriseDao.selectBatchIds(enterpriseIdSet);
        Map<Long, String> enterpriseNameMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));

        for (DepreciationStatisticVO assetVO : list) {
            assetVO.setEnterpriseName(enterpriseNameMap.getOrDefault(assetVO.getEnterpriseId(), StringConst.EMPTY));
            assetVO.setCategoryName(categoryNameMap.getOrDefault(assetVO.getCategoryId(), StringConst.EMPTY));
            // 设置使用期限
            Integer usedMonthCount = SmartLocalDateUtil.getDiffMonth(LocalDate.now(), assetVO.getCreateTime().toLocalDate());
            assetVO.setUsedMonthCount(usedMonthCount);
            assetVO.setSurplusMonthCount(assetVO.getMonthCount() - usedMonthCount);

            // 设置累计折旧
            BigDecimal price = assetVO.getPrice();
            // 计算预计净残值
            BigDecimal estimatedDepreciationPrice = SmartBigDecimalUtil.Amount.multiply(price.divide(BigDecimal.valueOf(100)), assetVO.getResidualValueRate());
            // 【月折旧额 = (固定资产原值 － 预计净残值）/ 预计使用期限 】 (5000 - 5000 * 5 / 100)   /  36 =131
            BigDecimal monthDepreciationPrice = BigDecimal.ZERO;
            if (assetVO.getMonthCount() > 0) {
                monthDepreciationPrice = SmartBigDecimalUtil.Amount.divide(price.subtract(estimatedDepreciationPrice), BigDecimal.valueOf(assetVO.getMonthCount()));
            }
            BigDecimal totalDepreciationPrice = SmartBigDecimalUtil.Amount.multiply(monthDepreciationPrice, BigDecimal.valueOf(usedMonthCount));
            assetVO.setTotalDepreciation(totalDepreciationPrice);
            assetVO.setResidualValue(SmartBigDecimalUtil.Amount.subtract(price, totalDepreciationPrice));

        }
    }

}
