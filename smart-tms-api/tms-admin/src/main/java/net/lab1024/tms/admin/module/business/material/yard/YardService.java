package net.lab1024.tms.admin.module.business.material.yard;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.material.yard.domain.YardCreateForm;
import net.lab1024.tms.admin.module.business.material.yard.domain.YardQueryForm;
import net.lab1024.tms.admin.module.business.material.yard.domain.YardUpdateForm;
import net.lab1024.tms.admin.module.business.material.yard.domain.YardVO;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.material.yard.YardEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 业务资料-堆场管理
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Service
@Slf4j
public class YardService {

    @Autowired
    private YardDao yardDao;

    /**
     * 分页查询堆场模块
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResult<YardVO>> queryByPage(YardQueryForm queryDTO) {
        queryDTO.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<YardVO> yardVOS = yardDao.queryPage(page, queryDTO);
        PageResult<YardVO> pageResult = SmartPageUtil.convert2PageResult(page, yardVOS);
        return ResponseDTO.ok(pageResult);
    }

    public ResponseDTO<List<YardVO>> queryList(Long enterpriseId) {
        List<YardVO> yardVOS = yardDao.queryList(enterpriseId, Boolean.FALSE, Boolean.FALSE);
        return ResponseDTO.ok(yardVOS);
    }

    /**
     * 查询堆场详情
     *
     * @param yardId
     * @return
     */
    public ResponseDTO<YardVO> getDetail(Long yardId) {
        // 校验堆场是否存在
        YardVO yardDetail = yardDao.getDetail(yardId, Boolean.FALSE);
        if (Objects.isNull(yardDetail)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "堆场不存在");
        }
        return ResponseDTO.ok(yardDetail);
    }

    /**
     * 新建堆场
     *
     * @param createVO
     * @return
     */
    public ResponseDTO<String> createYard(YardCreateForm createVO, Long enterpriseId) {
        // 验证堆场编号是否重复
        YardEntity validateYard = yardDao.queryByYardCode(enterpriseId, createVO.getYardCode(), null, Boolean.FALSE);
        if (Objects.nonNull(validateYard)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "堆场编号重复");
        }
        // 数据插入
        YardEntity insertYard = SmartBeanUtil.copy(createVO, YardEntity.class);
        insertYard.setEnterpriseId(enterpriseId);

        yardDao.insert(insertYard);
        return ResponseDTO.ok();
    }

    /**
     * 编辑堆场
     *
     * @param updateVO
     * @return
     */
    public ResponseDTO<String> updateYard(YardUpdateForm updateVO, Long enterpriseId) {
        Long yardId = updateVO.getYardId();
        // 校验堆场是否存在
        YardEntity yardDetail = yardDao.selectById(yardId);
        if (Objects.isNull(yardDetail) || yardDetail.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "堆场不存在");
        }
        // 验证堆场编号是否重复
        YardEntity validateYard = yardDao.queryByYardCode(enterpriseId, updateVO.getYardCode(), yardId, Boolean.FALSE);
        if (Objects.nonNull(validateYard)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "堆场编号重复");
        }
        // 数据编辑
        YardEntity updateYard = SmartBeanUtil.copy(updateVO, YardEntity.class);
        yardDao.updateById(updateYard);
        return ResponseDTO.ok();
    }


    /**
     * 删除堆场
     *
     * @param yardId
     * @return
     */
    public ResponseDTO<String> deleteYard(Long yardId) {
        // 校验堆场是否存在
        YardEntity yardDetail = yardDao.selectById(yardId);
        if (Objects.isNull(yardDetail) || yardDetail.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "堆场不存在");
        }
        yardDao.deleteYard(yardId, Boolean.TRUE);
        return ResponseDTO.ok();
    }
}
