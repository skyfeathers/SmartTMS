package net.lab1024.tms.admin.module.business.material.customsbroker;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.material.customsbroker.domain.CustomsBrokerCreateForm;
import net.lab1024.tms.admin.module.business.material.customsbroker.domain.CustomsBrokerQueryForm;
import net.lab1024.tms.admin.module.business.material.customsbroker.domain.CustomsBrokerUpdateForm;
import net.lab1024.tms.admin.module.business.material.customsbroker.domain.CustomsBrokerVO;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.material.customsbroker.CustomsBrokerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 业务资料-报关行
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Service
@Slf4j
public class CustomsBrokerService {

    @Autowired
    private CustomsBrokerDao customsBrokerDao;

    /**
     * 分页查询报关行模块
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResult<CustomsBrokerVO>> queryByPage(CustomsBrokerQueryForm queryDTO) {
        queryDTO.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<CustomsBrokerVO> customsBrokerVOS = customsBrokerDao.queryPage(page, queryDTO);
        PageResult<CustomsBrokerVO> pageResult = SmartPageUtil.convert2PageResult(page, customsBrokerVOS);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 查询报关行详情
     *
     * @param customsBrokerId
     * @return
     */
    public ResponseDTO<CustomsBrokerVO> getDetail(Long customsBrokerId) {
        // 校验报关行是否存在
        CustomsBrokerVO customsBrokerDetail = customsBrokerDao.getDetail(customsBrokerId, Boolean.FALSE);
        if (Objects.isNull(customsBrokerDetail)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "报关行不存在");
        }
        return ResponseDTO.ok(customsBrokerDetail);
    }

    /**
     * 新建报关行
     *
     * @param createVO
     * @return
     */
    public ResponseDTO<String> createCustomsBroker(CustomsBrokerCreateForm createVO) {
        // 验证报关行编号是否重复
        CustomsBrokerEntity validateCustomsBroker = customsBrokerDao.queryByCustomsBrokerCode(createVO.getCustomsBrokerCode(), null, Boolean.FALSE);
        if (Objects.nonNull(validateCustomsBroker)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "报关行编号重复");
        }
        // 数据插入
        CustomsBrokerEntity insertCustomsBroker = SmartBeanUtil.copy(createVO, CustomsBrokerEntity.class);
        customsBrokerDao.insert(insertCustomsBroker);
        return ResponseDTO.ok();
    }

    /**
     * 编辑报关行
     *
     * @param updateVO
     * @return
     */
    public ResponseDTO<String> updateCustomsBroker(CustomsBrokerUpdateForm updateVO) {
        Long customsBrokerId = updateVO.getCustomsBrokerId();
        // 校验报关行是否存在
        CustomsBrokerEntity customsBrokerDetail = customsBrokerDao.selectById(customsBrokerId);
        if (Objects.isNull(customsBrokerDetail) || customsBrokerDetail.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "报关行不存在");
        }
        // 验证报关行编号是否重复
        CustomsBrokerEntity validateCustomsBroker = customsBrokerDao.queryByCustomsBrokerCode(updateVO.getCustomsBrokerCode(), customsBrokerId, Boolean.FALSE);
        if (Objects.nonNull(validateCustomsBroker)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "报关行编号重复");
        }
        // 数据编辑
        CustomsBrokerEntity updateCustomsBroker = SmartBeanUtil.copy(updateVO, CustomsBrokerEntity.class);
        customsBrokerDao.updateById(updateCustomsBroker);
        return ResponseDTO.ok();
    }


    /**
     * 删除报关行
     *
     * @param customsBrokerId
     * @return
     */
    public ResponseDTO<String> deleteCustomsBroker(Long customsBrokerId) {
        // 校验报关行是否存在
        CustomsBrokerEntity customsBrokerDetail = customsBrokerDao.selectById(customsBrokerId);
        if (Objects.isNull(customsBrokerDetail) || customsBrokerDetail.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "报关行不存在");
        }
        customsBrokerDao.deleteCustomsBroker(customsBrokerId, Boolean.TRUE);
        return ResponseDTO.ok();
    }
}
