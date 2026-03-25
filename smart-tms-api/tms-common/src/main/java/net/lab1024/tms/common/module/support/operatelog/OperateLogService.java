package net.lab1024.tms.common.module.support.operatelog;

import cn.hutool.http.useragent.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.support.operatelog.domain.OperateLogEntity;
import net.lab1024.tms.common.module.support.operatelog.domain.OperateLogQueryForm;
import net.lab1024.tms.common.module.support.operatelog.domain.OperateLogVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作记录
 *
 * @author 罗伊
 */
@Service
public class OperateLogService {

    @Autowired
    private OperateLogDao operateLogDao;

    /**
     * @author 罗伊
     * @description 分页查询
     */
    public ResponseDTO<PageResult<OperateLogVO>> queryByPage(OperateLogQueryForm queryForm) {
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<OperateLogEntity> logEntityList = operateLogDao.queryByPage(page, queryForm);
        List<OperateLogVO> operateLogVOList = SmartBeanUtil.copyList(logEntityList, OperateLogVO.class);
        operateLogVOList.forEach(e -> this.buildUserAgent(e));
        PageResult<OperateLogVO> pageResult = SmartPageUtil.convert2PageResult(page, operateLogVOList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * @author 罗伊
     * @description 添加
     */
    public ResponseDTO<String> log(OperateLogVO addDTO) {
        OperateLogEntity entity = SmartBeanUtil.copy(addDTO, OperateLogEntity.class);
        operateLogDao.insert(entity);
        return ResponseDTO.ok();
    }

    /**
     * 查询详情
     *
     * @param operateLogId
     * @return
     */
    public ResponseDTO<OperateLogVO> detail(Long operateLogId) {
        OperateLogEntity operateLogEntity = operateLogDao.selectById(operateLogId);
        if (operateLogEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        OperateLogVO operateLogVO = SmartBeanUtil.copy(operateLogEntity, OperateLogVO.class);
        this.buildUserAgent(operateLogVO);
        return ResponseDTO.ok(operateLogVO);
    }

    private void buildUserAgent(OperateLogVO operateLogVO) {
        String userAgentStr = operateLogVO.getUserAgent();
        if (StringUtils.isBlank(userAgentStr)) {
            return;
        }
        // userAgent parse
        UserAgent userAgent = UserAgentUtil.parse(userAgentStr);
        OS os = userAgent.getOs();
        if (os != null) {
            operateLogVO.setOs(os.getName());
        }
        Browser browser = userAgent.getBrowser();
        if (browser != null) {
            operateLogVO.setBrowser(browser.getName());
        }
        Platform platform = userAgent.getPlatform();
        if (platform != null) {
            operateLogVO.setPlatform(platform.getName());
        }
        operateLogVO.setMobileFlag(userAgent.isMobile());
    }
}
