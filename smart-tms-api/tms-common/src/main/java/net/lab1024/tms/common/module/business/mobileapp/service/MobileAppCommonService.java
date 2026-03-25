package net.lab1024.tms.common.module.business.mobileapp.service;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.module.business.mobileapp.MobileAppDao;
import net.lab1024.tms.common.module.business.mobileapp.constant.MobileAppPlatformTypeEnum;
import net.lab1024.tms.common.module.business.mobileapp.constant.MobileAppUpdateTypeEnum;
import net.lab1024.tms.common.module.business.mobileapp.domain.entity.MobileAppEntity;
import net.lab1024.tms.common.module.business.mobileapp.domain.form.MobileAppUpdateQueryDTO;
import net.lab1024.tms.common.module.business.mobileapp.domain.vo.MobileAppUpdateVO;
import net.lab1024.tms.common.module.support.file.domain.vo.FileVO;
import net.lab1024.tms.common.module.support.file.service.FileService;
import net.lab1024.tms.common.module.support.systemconfig.SystemConfigKeyEnum;
import net.lab1024.tms.common.module.support.systemconfig.SystemConfigService;
import net.lab1024.tms.common.module.support.systemconfig.domain.SystemConfigVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class MobileAppCommonService {

    @Resource
    private SystemConfigService configService;

    @Resource
    private MobileAppDao mobileAppDao;

    @Resource
    private FileService fileService;

    private static final Pattern VERSION_NO_COMPILE = Pattern.compile("[^0-9]");

    /**
     * 获取用户协议
     *
     * @return
     */
    public ResponseDTO<String> getUserAgreement() {
        SystemConfigVO userAgreement = configService.getConfig(SystemConfigKeyEnum.USER_AGREEMENT);
        if (ObjectUtil.isEmpty(userAgreement)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "用户协议不存在");
        }
        return ResponseDTO.ok(userAgreement.getConfigValue());
    }

    /**
     * 获取隐私协议
     *
     * @return
     */
    public ResponseDTO<String> getPrivateAgreement() {
        SystemConfigVO privacyAgreement = configService.getConfig(SystemConfigKeyEnum.PRIVACY_AGREEMENT);
        if (ObjectUtil.isEmpty(privacyAgreement)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "隐私协议不存在");
        }
        return ResponseDTO.ok(privacyAgreement.getConfigValue());
    }

    /**
     * 根据用户版本查询
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<MobileAppUpdateVO> queryVersion(MobileAppUpdateQueryDTO queryDTO) {
        // 返回app端检测更新结果
        String userVersionNo = queryDTO.getVersionNo();
        MobileAppUpdateVO mobileAppUpdateVO = new MobileAppUpdateVO();
        mobileAppUpdateVO.setCurrentVersionNo(userVersionNo);
        // 查询数据库最新版本
        MobileAppEntity mobileAppEntity = mobileAppDao.selectByNewestFlag(queryDTO.getPlatformType(), Boolean.TRUE);
        if (mobileAppEntity == null) {
            // 无需更新
            mobileAppUpdateVO.setAppUpdateType(MobileAppUpdateTypeEnum.NOT_NEED_UPDATE.getValue());
            return ResponseDTO.ok(mobileAppUpdateVO);
        }
        // 比较客户端版本与数据库最新版本
        String newVersionNo = mobileAppEntity.getVersionNo();
        MobileAppUpdateTypeEnum userUpdateTypeEnum = MobileAppUpdateTypeEnum.NOT_NEED_UPDATE;
        // 用户版本大
        if(this.compareUserVersion(userVersionNo, newVersionNo) > 0){
            userUpdateTypeEnum = MobileAppUpdateTypeEnum.NOT_NEED_UPDATE;
        }
        // 用户版本相等
        if(this.compareUserVersion(userVersionNo, newVersionNo) == 0){
            userUpdateTypeEnum = MobileAppUpdateTypeEnum.NOT_NEED_UPDATE;
        }
        // 用户版本小
        if(this.compareUserVersion(userVersionNo, newVersionNo) < 0){
            userUpdateTypeEnum = mobileAppEntity.getForceUpdateFlag() ? MobileAppUpdateTypeEnum.FORCE_UPDATE : MobileAppUpdateTypeEnum.FREEDOM_UPDATE;
        }
        // 需要更新
        FileVO fileVO = fileService.getCacheFileVO(mobileAppEntity.getAppFile());
        if (ObjectUtil.isNotEmpty(fileVO)) {
            mobileAppUpdateVO.setUpdateUrl(fileVO.getFileUrl());
            mobileAppUpdateVO.setNewVersionFileSize(fileVO.getFileSize());
        } else {
            mobileAppUpdateVO.setUpdateUrl(StringConst.EMPTY);
            mobileAppUpdateVO.setNewVersionFileSize(NumberUtils.LONG_ZERO);
        }
        mobileAppUpdateVO.setNewVersionNo(newVersionNo);
        mobileAppUpdateVO.setUpdateDesc(mobileAppEntity.getUpdateDesc());
        mobileAppUpdateVO.setUpdateDate(mobileAppEntity.getUpdateTime().toLocalDate());
        // 默认按照是否强制更新
        mobileAppUpdateVO.setAppUpdateType(userUpdateTypeEnum.getValue());
        return ResponseDTO.ok(mobileAppUpdateVO);
    }

    private int compareUserVersion(String userVersion, String newVersion) {
        userVersion = userVersion.replaceAll("V", "").replaceAll("v", "");
        newVersion = newVersion.replaceAll("V", "").replaceAll("v", "");

        List<Long> userVersionList = SmartStringUtil.splitConverToLongList(userVersion, "\\.");
        List<Long> newVersionList = SmartStringUtil.splitConverToLongList(newVersion, "\\.");

        Long firstUserVersion = this.getVersion(userVersionList, 0);
        Long firstNewVersion = this.getVersion(newVersionList, 0);
        if (firstUserVersion > firstNewVersion) {
            return 1;
        }
        if (firstUserVersion < firstNewVersion) {
            return -1;
        }

        Long secondUserVersion = this.getVersion(userVersionList, 1);
        Long secondNewVersion = this.getVersion(newVersionList, 1);
        if (secondUserVersion > secondNewVersion) {
            return 1;
        }
        if (secondUserVersion < secondNewVersion) {
            return -1;
        }

        Long thirdUserVersion = this.getVersion(userVersionList, 2);
        Long thirdNewVersion = this.getVersion(newVersionList, 2);
        if (thirdUserVersion > thirdNewVersion) {
            return 1;
        }
        if (thirdUserVersion < thirdNewVersion) {
            return -1;
        }
        return 0;
    }

    private Long getVersion(List<Long> versionList, int versionIndex){
        if(versionIndex >= versionList.size()){
            return  0L;
        }
        return versionList.get(versionIndex);
    }

    /**
     * 查询下载地址
     *
     * @param platformType
     * @return
     */
    public ResponseDTO<String> appDownload(Integer platformType) {
        MobileAppPlatformTypeEnum mobileAppPlatformTypeEnum = SmartBaseEnumUtil.getEnumByValue(platformType, MobileAppPlatformTypeEnum.class);
        if (ObjectUtil.isEmpty(mobileAppPlatformTypeEnum)) {
            return ResponseDTO.userErrorParam("APP平台错误");
        }
        if (MobileAppPlatformTypeEnum.ANDROID != mobileAppPlatformTypeEnum) {
            return ResponseDTO.userErrorParam("目前只支持安卓下载");
        }
        MobileAppEntity mobileAppEntity = mobileAppDao.selectByNewestFlag(platformType, Boolean.TRUE);
        if (mobileAppEntity == null || StringUtils.isBlank(mobileAppEntity.getAppFile())) {
            return ResponseDTO.userErrorParam("暂未找到对应的APP");
        }
        return fileService.getFileUrl(mobileAppEntity.getAppFile());
    }

}
