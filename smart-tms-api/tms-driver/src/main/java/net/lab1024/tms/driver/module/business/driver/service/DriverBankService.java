package net.lab1024.tms.driver.module.business.driver.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.business.driver.domain.DriverBankEntity;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import net.lab1024.tms.driver.module.business.driver.dao.DriverDao;
import net.lab1024.tms.driver.module.business.driver.dao.DriverBankDao;
import net.lab1024.tms.driver.module.business.driver.domain.form.DriverBankBaseForm;
import net.lab1024.tms.driver.module.business.driver.domain.form.DriverBankUpdateForm;
import net.lab1024.tms.driver.module.business.driver.domain.vo.DriverBankVO;
import net.lab1024.tms.driver.module.business.driver.domain.vo.DriverBankDetailVO;
import net.lab1024.tms.driver.module.business.driver.manager.DriverBankManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DriverBankService {

    @Resource
    private DriverDao driverDao;

    @Resource
    private DriverBankDao driverBankDao;

    @Resource
    private DictCacheService dictCacheService;

    @Resource
    private DriverBankManager driverBankManager;

    @Resource
    private DriverDataTracerService driverDataTracerService;

    /**
     * 查询司机银行列表
     *
     * @param driverId
     * @return
     */
    public ResponseDTO<List<DriverBankVO>> selectDriverBankList(Long driverId) {
        List<DriverBankEntity> bankEntityList = driverBankDao.selectByDriverId(driverId);
        if (CollectionUtil.isEmpty(bankEntityList)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }
        List<DriverBankVO> driverBankVOList = SmartBeanUtil.copyList(bankEntityList, DriverBankVO.class);
        return ResponseDTO.ok(driverBankVOList);
    }

    /**
     * 添加司机银行卡
     *
     * @param bankBaseForm
     * @param requestUser
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> saveDriverBank(DriverBankBaseForm bankBaseForm, RequestUser requestUser, DataTracerRequestForm dataTracerRequestForm) {
        DriverEntity driverEntity = driverDao.selectById(requestUser.getUserId());
        if (ObjectUtil.isEmpty(driverEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        DriverBankEntity driverBankEntity = SmartBeanUtil.copy(bankBaseForm, DriverBankEntity.class);
        String bankName = dictCacheService.selectValueNameByValueCode(driverBankEntity.getBankType());
        driverBankEntity.setDriverId(requestUser.getUserId());
        driverBankEntity.setBankName(bankName);
        driverBankEntity.setCreateUserId(requestUser.getUserId());
        driverBankEntity.setCreateUserName(requestUser.getUserName());
        driverBankEntity.setCreateUserType(requestUser.getUserType().getValue());
        driverBankEntity.setCreateUserTypeDesc(requestUser.getUserType().getDesc());
        driverBankManager.saveDriverBank(driverBankEntity);
        driverDataTracerService.saveDriverBankLog(driverBankEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 修改司机银行信息
     *
     * @param bankUpdateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> updateDriverBank(DriverBankUpdateForm bankUpdateForm, DataTracerRequestForm dataTracerRequestForm) {
        DriverBankEntity driverBankEntity = driverBankDao.selectById(bankUpdateForm.getBankId());
        if (ObjectUtil.isEmpty(driverBankEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        DriverBankEntity updateDriverBank = SmartBeanUtil.copy(bankUpdateForm, DriverBankEntity.class);
        if (ObjectUtil.isNotEmpty(updateDriverBank.getBankType())) {
            String bankName = dictCacheService.selectValueNameByValueCode(updateDriverBank.getBankType());
            updateDriverBank.setBankName(bankName);
        }
        DriverBankEntity beforeData = driverBankDao.selectById(updateDriverBank.getBankId());
        driverBankManager.updateDriverBank(updateDriverBank);
        DriverBankEntity afterData = driverBankDao.selectById(updateDriverBank.getBankId());
        driverDataTracerService.updateDriverBankLog(beforeData, afterData, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 获取司机银行卡详情
     *
     * @param bankId
     * @return
     */
    public ResponseDTO<DriverBankDetailVO> getBankDetail(Long bankId) {
        DriverBankEntity driverBankEntity = driverBankDao.selectById(bankId);
        if (ObjectUtil.isEmpty(driverBankEntity)) {
            return ResponseDTO.userErrorParam("银行卡不存在");
        }
        DriverBankDetailVO driverBankDetailVO = SmartBeanUtil.copy(driverBankEntity, DriverBankDetailVO.class);
        return ResponseDTO.ok(driverBankDetailVO);
    }
}
