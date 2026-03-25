package net.lab1024.tms.admin.module.business.material.repairplant;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.material.repairplant.domain.RepairPlantAddForm;
import net.lab1024.tms.admin.module.business.material.repairplant.domain.RepairPlantAdminVO;
import net.lab1024.tms.admin.module.business.material.repairplant.domain.RepairPlantQueryForm;
import net.lab1024.tms.admin.module.business.material.repairplant.domain.RepairPlantUpdateForm;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.material.repairplant.domain.RepairPlantEntity;
import net.lab1024.tms.common.module.business.material.repairplant.domain.RepairPlantSimpleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 维修厂 业务
 *
 * @author: Turbolisten
 * @date: 2022/7/13 21:54
 */
@Service
public class RepairPlantService {

    @Autowired
    private RepairPlantDao repairPlantDao;

    /**
     * 新增 维修厂家
     *
     * @param addForm
     * @return
     */
    public ResponseDTO<String> add(RepairPlantAddForm addForm) {
        // 名称是否重复
        RepairPlantEntity repairPlantEntity = repairPlantDao.selectByName(addForm.getEnterpriseId(), addForm.getRepairPlantName(), false);
        if (null != repairPlantEntity) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST);
        }
        repairPlantEntity = SmartBeanUtil.copy(addForm, RepairPlantEntity.class);
        repairPlantDao.insert(repairPlantEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新 维修厂家
     *
     * @param updateForm
     * @return
     */
    public ResponseDTO<String> update(RepairPlantUpdateForm updateForm) {
        Integer repairPlantId = updateForm.getRepairPlantId();
        RepairPlantEntity repairPlantEntity = repairPlantDao.selectById(repairPlantId);
        if (null == repairPlantEntity || repairPlantEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        // 名称是否重复
        repairPlantEntity = repairPlantDao.selectByName(updateForm.getEnterpriseId(), updateForm.getRepairPlantName(), false);
        if (null != repairPlantEntity && !Objects.equals(repairPlantEntity.getRepairPlantId(), repairPlantId)) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST);
        }
        repairPlantEntity = SmartBeanUtil.copy(updateForm, RepairPlantEntity.class);
        repairPlantDao.updateById(repairPlantEntity);
        return ResponseDTO.ok();
    }

    /**
     * 分页查询 维修厂家
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<RepairPlantAdminVO>> query(RepairPlantQueryForm queryForm) {
        queryForm.setDeletedFlag(false);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<RepairPlantEntity> list = repairPlantDao.query(page, queryForm);
        PageResult<RepairPlantAdminVO> pageResult = SmartPageUtil.convert2PageResult(page, list, RepairPlantAdminVO.class);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 删除 维修厂家
     *
     * @param repairPlantId
     * @return
     */
    public ResponseDTO<String> del(Integer repairPlantId, String updateName) {
        RepairPlantEntity repairPlantEntity = repairPlantDao.selectById(repairPlantId);
        if (null == repairPlantEntity || repairPlantEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        repairPlantEntity = new RepairPlantEntity();
        repairPlantEntity.setRepairPlantId(repairPlantId);
        repairPlantEntity.setDeletedFlag(true);
        repairPlantEntity.setUpdateUserName(updateName);
        repairPlantDao.updateById(repairPlantEntity);
        return ResponseDTO.ok();
    }

    /**
     * 查询全部 维修厂家
     *
     * @return
     */
    public List<RepairPlantSimpleVO> queryAll(Long enterpriseId) {
        List<RepairPlantSimpleVO> list = repairPlantDao.queryList(enterpriseId, Boolean.FALSE, Boolean.FALSE);
        return SmartBeanUtil.copyList(list, RepairPlantSimpleVO.class);
    }
}
