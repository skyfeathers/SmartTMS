package net.lab1024.tms.admin.module.business.pic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.pic.dao.PicDao;
import net.lab1024.tms.admin.module.business.pic.domain.form.PicAddForm;
import net.lab1024.tms.admin.module.business.pic.domain.form.PicQueryForm;
import net.lab1024.tms.admin.module.business.pic.domain.form.PicUpdateForm;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.pic.domain.entity.PicEntity;
import net.lab1024.tms.common.module.business.pic.domain.vo.PicVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhuoda
 * @Date 2021/1/22
 */

@Service
public class PicService {

    @Autowired
    private PicDao picDao;
    /**
     * 添加
     *
     * @param addForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> add(PicAddForm addForm) {
        PicEntity picEntity = SmartBeanUtil.copy(addForm, PicEntity.class);
        picDao.insert(picEntity);
        return ResponseDTO.ok();
    }

    /**
     * 修改
     *
     * @param updateForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> update(PicUpdateForm updateForm) {
        Long picId = updateForm.getPicId();
        PicEntity picEntity = picDao.selectById(picId);
        if (picEntity == null) {
            return ResponseDTO.userErrorParam("数据不存在");
        }
        PicEntity updateEntity = SmartBeanUtil.copy(updateForm, PicEntity.class);
        picDao.updateById(updateEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新排序
     *
     * @param picId
     * @param seq
     * @return
     */
    public ResponseDTO<String> updateSort(Long picId, Integer seq) {
        PicEntity picEntity = picDao.selectById(picId);
        if (picEntity == null) {
            return ResponseDTO.userErrorParam("数据不存在");
        }

        picDao.updateSeq(picId, seq);
        return ResponseDTO.ok();
    }

    /**
     * 批量禁用/启用
     *
     * @param picId
     * @return
     */
    public ResponseDTO<String> batchUpdateEnable(Long picId) {
        PicEntity picEntity = picDao.selectById(picId);
        if (picEntity == null) {
            return ResponseDTO.userErrorParam("数据不存在");
        }
        PicEntity updateEntity = new PicEntity();
        updateEntity.setPicId(picId);
        updateEntity.setEnableFlag(!picEntity.getEnableFlag());
        picDao.updateById(updateEntity);
        return ResponseDTO.ok();
    }


    /**
     * 分页查询
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResult<PicVO>> query(PicQueryForm queryDTO) {
        Page page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<PicVO> list = picDao.query(page, queryDTO);
        PageResult<PicVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, list);
        if (CollectionUtils.isEmpty(list)) {
            return ResponseDTO.ok(pageResultDTO);
        }
        return ResponseDTO.ok(pageResultDTO);
    }

    /**
     * 批量删除
     *
     * @param picIdList
     * @return
     */
    public ResponseDTO<String> batchDelete(List<Long> picIdList) {
        picDao.deleteBatchIds(picIdList);
        return ResponseDTO.ok();
    }

    /**
     * 获取轮播图详情
     *
     * @param picId
     * @return
     */
    public ResponseDTO<PicVO> getDetail(Long picId) {
        PicEntity picEntity = picDao.selectById(picId);
        if (null == picEntity) {
            return ResponseDTO.userErrorParam("广告不存在");
        }
        PicVO detailVO = SmartBeanUtil.copy(picEntity, PicVO.class);
        return ResponseDTO.ok(detailVO);
    }

}
