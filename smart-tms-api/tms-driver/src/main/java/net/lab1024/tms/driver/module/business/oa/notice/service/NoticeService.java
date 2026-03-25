package net.lab1024.tms.driver.module.business.oa.notice.service;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.oa.news.constant.NoticePushTypeEnum;
import net.lab1024.tms.common.module.business.oa.news.domain.NoticeEntity;
import net.lab1024.tms.common.module.business.oa.news.domain.NoticeTypeEntity;
import net.lab1024.tms.driver.module.business.oa.notice.domain.dao.NoticeDao;
import net.lab1024.tms.driver.module.business.oa.notice.domain.dao.NoticeTypeDao;
import net.lab1024.tms.driver.module.business.oa.notice.domain.form.NoticeQueryForm;
import net.lab1024.tms.driver.module.business.oa.notice.domain.vo.NoticeVO;
import net.lab1024.tms.driver.module.business.oa.notice.domain.vo.NoticeSimpleVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NoticeService {
    
    @Resource
    private NoticeDao noticeDao;
    
    @Resource
    private NoticeTypeDao noticeTypeDao;
    
    /**
     * 司机分页查询通知公告
     *
     * @param noticeQueryForm
     * @return
     */
    public PageResult<NoticeSimpleVO> query(NoticeQueryForm noticeQueryForm) {
        Page page = SmartPageUtil.convert2PageQuery(noticeQueryForm);
        noticeQueryForm.setDeletedFlag(Boolean.FALSE);
        noticeQueryForm.setNoticePushType(NoticePushTypeEnum.DRIVER_PUSH.getValue());
        List<NoticeSimpleVO> noticeSimpleVOList = noticeDao.query(page, noticeQueryForm);
        PageResult<NoticeSimpleVO> pageResult = SmartPageUtil.convert2PageResult(page, noticeSimpleVOList);
        return pageResult;
    }

    /**
     * 查询并更新查看记录
     *
     * @param driverId
     * @param noticeId
     * @param ip
     * @param userAgent
     * @return
     */
    public ResponseDTO<NoticeVO> view(Long driverId, Long noticeId, String ip, String userAgent) {
        NoticeEntity noticeEntity = noticeDao.selectById(noticeId);
        if (ObjectUtil.isEmpty(noticeEntity) || BooleanUtil.isTrue(noticeEntity.getDeletedFlag())) {
            return ResponseDTO.userErrorParam("通知公告不存在");
        }
        NoticeVO noticeVO = SmartBeanUtil.copy(noticeEntity, NoticeVO.class);
        NoticeTypeEntity noticeTypeEntity = noticeTypeDao.selectById(noticeEntity.getNoticeTypeId());
        noticeVO.setNoticeTypeName(ObjectUtil.isEmpty(noticeEntity) ? StringConst.EMPTY : noticeTypeEntity.getNoticeTypeName());
        Integer viewCount = noticeDao.selectCountViewRecord(noticeId, driverId);
        if (viewCount == NumberConst.ZERO) {
            noticeDao.insertViewRecord(noticeId, driverId, ip, userAgent, NumberConst.ONE);
            noticeDao.updateViewCount(noticeId, NumberConst.ONE, NumberConst.ONE);
            noticeVO.setPageViewCount(noticeVO.getPageViewCount() + NumberConst.ONE);
            noticeVO.setUserViewCount(noticeVO.getUserViewCount() + NumberConst.ONE);
        } else {
            noticeDao.updateViewRecord(noticeId, driverId, ip, userAgent, NumberConst.ONE);
            noticeDao.updateViewCount(noticeId, NumberConst.ONE, NumberConst.ZERO);
            noticeVO.setPageViewCount(noticeVO.getPageViewCount() + NumberConst.ONE);
        }
        return ResponseDTO.ok(noticeVO);
    }
}
