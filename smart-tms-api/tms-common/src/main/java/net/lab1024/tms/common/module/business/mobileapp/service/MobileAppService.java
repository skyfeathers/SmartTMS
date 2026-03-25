package net.lab1024.tms.common.module.business.mobileapp.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.mobileapp.MobileAppDao;
import net.lab1024.tms.common.module.business.mobileapp.domain.entity.MobileAppEntity;
import net.lab1024.tms.common.module.business.mobileapp.domain.form.MobileAppAddForm;
import net.lab1024.tms.common.module.business.mobileapp.domain.form.MobileAppQueryForm;
import net.lab1024.tms.common.module.business.mobileapp.domain.form.MobileAppUpdateForm;
import net.lab1024.tms.common.module.business.mobileapp.domain.vo.MobileAppVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MobileAppService {

    @Resource
    private MobileAppDao mobileAppDao;

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<MobileAppVO>> queryPage(MobileAppQueryForm queryForm) {
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<MobileAppVO> list = mobileAppDao.queryPage(page, queryForm);
        PageResult<MobileAppVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 新建
     *
     * @param addForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> add(MobileAppAddForm addForm) {
        if (addForm.getNewestFlag()) {
            MobileAppEntity newestEntity = mobileAppDao.selectByNewestFlag(addForm.getPlatformType(), Boolean.TRUE);
            if (newestEntity != null) {
                mobileAppDao.updateNewestFlag(newestEntity.getId(), Boolean.FALSE);
            }
        }
        MobileAppEntity mobileAppEntity = SmartBeanUtil.copy(addForm, MobileAppEntity.class);
        mobileAppDao.insert(mobileAppEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     * @param updateForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> update(MobileAppUpdateForm updateForm) {
        MobileAppEntity mobileAppEntity = mobileAppDao.selectById(updateForm.getId());
        if (ObjectUtil.isEmpty(mobileAppEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "版本信息不存在");
        }
        if (updateForm.getNewestFlag()) {
            MobileAppEntity newestEntity = mobileAppDao.selectByNewestFlag(updateForm.getPlatformType(), true);
            if (ObjectUtil.isNotEmpty(newestEntity)) {
                mobileAppDao.updateNewestFlag(newestEntity.getId(), Boolean.FALSE);
            }
        }
        MobileAppEntity updateEntity = SmartBeanUtil.copy(updateForm, MobileAppEntity.class);
        mobileAppDao.updateById(updateEntity);
        return ResponseDTO.ok();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public ResponseDTO<String> delete(Long id) {
        MobileAppEntity mobileAppEntity = mobileAppDao.selectById(id);
        if (ObjectUtil.isEmpty(mobileAppEntity)) {
            return ResponseDTO.ok();
        }
        mobileAppDao.deleteById(id);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     *
     * @param idList
     * @return
     */
    public ResponseDTO<String> batchDelete(List<Long> idList) {
        if(CollectionUtils.isEmpty(idList)) {
            return ResponseDTO.ok();
        }
        mobileAppDao.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

}
