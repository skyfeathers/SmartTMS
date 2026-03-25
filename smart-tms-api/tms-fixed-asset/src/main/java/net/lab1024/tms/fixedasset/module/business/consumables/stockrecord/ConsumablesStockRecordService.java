package net.lab1024.tms.fixedasset.module.business.consumables.stockrecord;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.fixedasset.module.business.consumables.stockrecord.domain.entity.ConsumablesStockRecordEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.stockrecord.domain.form.ConsumablesStockRecordQueryForm;
import net.lab1024.tms.fixedasset.module.business.consumables.stockrecord.domain.vo.ConsumablesStockRecordVO;
import net.lab1024.tms.fixedasset.module.business.location.LocationDao;
import net.lab1024.tms.fixedasset.module.business.location.domain.LocationEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 库存变动数量
 *
 * @author lidoudou
 * @date 2023/4/18 下午5:06
 */
@Service
public class ConsumablesStockRecordService {

    @Autowired
    private ConsumablesStockRecordDao consumablesStockRecordDao;
    @Autowired
    private LocationDao locationDao;

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public PageResult<ConsumablesStockRecordVO> queryPage(ConsumablesStockRecordQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<ConsumablesStockRecordEntity> entityList = consumablesStockRecordDao.queryPage(page, queryForm);
        List<ConsumablesStockRecordVO> list = SmartBeanUtil.copyList(entityList, ConsumablesStockRecordVO.class);
        buildList(list);
        PageResult<ConsumablesStockRecordVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return pageResult;
    }

    private void buildList(List<ConsumablesStockRecordVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Set<Long> locationIdSet = list.stream().map(ConsumablesStockRecordVO::getLocationId).collect(Collectors.toSet());
        List<LocationEntity> locationList = locationDao.selectBatchIds(locationIdSet);
        Map<Long, String> locationNameMap = locationList.stream().collect(Collectors.toMap(LocationEntity::getLocationId, LocationEntity::getLocationName));
        list.forEach(item -> {
            item.setLocationName(locationNameMap.getOrDefault(item.getLocationId(), StringConst.EMPTY));
            item.setRecordTypeDesc(SmartBaseEnumUtil.getEnumDescByValue(item.getRecordType(), ConsumablesStockRecordTypeEnum.class));
        });
    }
}
