package net.lab1024.tms.driver.module.business.bracket;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.constant.BusinessModeEnum;
import net.lab1024.tms.common.module.business.bracket.domain.BracketEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.driver.module.business.bracket.dao.BracketDao;
import net.lab1024.tms.driver.module.business.bracket.domain.form.BracketAddForm;
import net.lab1024.tms.driver.module.business.bracket.domain.form.BracketQueryForm;
import net.lab1024.tms.driver.module.business.bracket.domain.form.BracketUpdateForm;
import net.lab1024.tms.driver.module.business.bracket.domain.vo.BracketDetailVO;
import net.lab1024.tms.driver.module.business.bracket.domain.vo.BracketListVO;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BracketService {

    @Resource
    private BracketDao bracketDao;

    @Resource
    private BracketDataTracerService bracketDataTracerService;

    /**
     * 挂车分页
     *
     * @param queryForm
     * @param driver
     * @return
     */
    public ResponseDTO<PageResult<BracketListVO>> queryByPage(BracketQueryForm queryForm, RequestUser driver) {
        queryForm.setDriverId(driver.getUserId());
        queryForm.setUserType(driver.getUserType().getValue());
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<BracketEntity> bracketEntityList = bracketDao.queryByPage(page, queryForm);
        List<BracketListVO> bracketListVOList = SmartBeanUtil.copyList(bracketEntityList, BracketListVO.class);
        PageResult<BracketListVO> pageResult = SmartPageUtil.convert2PageResult(page, bracketListVOList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 挂车详情
     *
     * @param bracketId
     * @return
     */
    public ResponseDTO<BracketDetailVO> getDetail(Long bracketId) {
        BracketEntity bracketEntity = bracketDao.selectById(bracketId);
        if (ObjectUtil.isEmpty(bracketEntity) || bracketEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "挂车不存在");
        }
        BracketDetailVO bracketDetailVO = SmartBeanUtil.copy(bracketEntity, BracketDetailVO.class);
        bracketDetailVO.setAuditStatusDesc(SmartBaseEnumUtil.getEnumDescByValue(bracketDetailVO.getAuditStatus(), AuditStatusEnum.class));
        return ResponseDTO.ok(bracketDetailVO);
    }

    /**
     * 新建挂车
     *
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> save(BracketAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        BracketEntity bracketEntity = bracketDao.selectByNoExcludeIds(addForm.getBracketNo(), null, Boolean.FALSE);
        if (ObjectUtil.isNotEmpty(bracketEntity)) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "挂车车牌号已存在");
        }
        BracketEntity insertEntity = SmartBeanUtil.copy(addForm, BracketEntity.class);
        insertEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        insertEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        insertEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        insertEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());
        bracketDao.insert(insertEntity);
        bracketDataTracerService.saveLog(insertEntity.getBracketId(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 车辆修改
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> update(BracketUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        BracketEntity bracketEntityByBracketId = bracketDao.selectById(updateForm.getBracketId());
        if (ObjectUtil.isEmpty(bracketEntityByBracketId) || bracketEntityByBracketId.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "挂车不存在");
        }
        BracketEntity bracketEntityByBracketINo = bracketDao.selectByNoExcludeIds(updateForm.getBracketNo(), Lists.newArrayList(updateForm.getBracketId()), Boolean.FALSE);
        if (ObjectUtil.isNotEmpty(bracketEntityByBracketINo)) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "挂车车牌号已存在");
        }
        BracketEntity bracketEntity = SmartBeanUtil.copy(updateForm, BracketEntity.class);
        bracketEntity.setAuditStatus(AuditStatusEnum.WAIT_AUDIT.getValue());
        bracketEntity.setUpdateUserName(dataTracerRequestForm.getOperatorName());
        bracketEntity.setUpdateUserId(dataTracerRequestForm.getOperatorId());
        BracketDetailVO beforeData = this.getDetail(updateForm.getBracketId()).getData();
        bracketDao.updateById(bracketEntity);
        BracketDetailVO afterData = this.getDetail(updateForm.getBracketId()).getData();

        bracketDataTracerService.updateLog(beforeData, afterData, dataTracerRequestForm);
        return ResponseDTO.ok();
    }
}
