package net.lab1024.tms.admin.module.business.material.cost;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.material.cost.domain.CostItemForm;
import net.lab1024.tms.admin.module.business.material.cost.domain.CostItemQueryForm;
import net.lab1024.tms.admin.module.business.material.cost.domain.CostItemUpdateForm;
import net.lab1024.tms.admin.module.business.material.cost.domain.CostItemVO;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.material.cost.domain.CostItemEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @author zhuoda
 * @Date 2022-07-18
 */
@Service
public class CostItemService {

    @Autowired
    private CostItemDao costItemDao;

    /**
     * 分页查询
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResult<CostItemVO>> query(CostItemQueryForm queryDTO) {
        queryDTO.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<CostItemVO> costItemVOList = costItemDao.queryPage(page, queryDTO);
        PageResult<CostItemVO> pageResult = SmartPageUtil.convert2PageResult(page, costItemVOList);
        return ResponseDTO.ok(pageResult);
    }


    /**
     * 删除
     *
     * @param costItemId
     * @return
     */
    public ResponseDTO<String> delete(Long costItemId) {
        costItemDao.updateDeleteFlag(costItemId, Boolean.TRUE);
        return ResponseDTO.ok();
    }

    /**
     * 保存或更新
     * @param form
     * @return
     */
    public ResponseDTO<String> save(CostItemForm form) {
        CostItemEntity costItemEntity = SmartBeanUtil.copy(form, CostItemEntity.class);
        costItemDao.insert(costItemEntity);
        return ResponseDTO.ok();
    }

    /**
     * 保存或更新
     * @param form
     * @return
     */
    public ResponseDTO<String> update(CostItemUpdateForm form) {
        CostItemEntity costItemEntity = SmartBeanUtil.copy(form, CostItemEntity.class);
        costItemDao.updateById(costItemEntity);
        return ResponseDTO.ok();
    }


}
