package net.lab1024.tms.admin.module.business.oa.news.manager;

import net.lab1024.tms.admin.module.business.oa.news.dao.NoticeDao;
import net.lab1024.tms.admin.module.business.oa.news.domain.form.NoticeVisibleRangeForm;
import net.lab1024.tms.common.module.business.oa.news.domain.NoticeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 通知、公告 manager
 *
 * @author zhuoda
 * @date 2022/08/14 21:21
 */
@Service
public class NoticeManager {

    @Autowired
    private NoticeDao noticeDao;

    /**
     * 保存
     *
     * @param noticeEntity
     * @param visibleRangeFormList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void saveNews(NoticeEntity noticeEntity, List<NoticeVisibleRangeForm> visibleRangeFormList) {
        noticeDao.insert(noticeEntity);
        Long noticeId = noticeEntity.getNoticeId();
        // 保存可见范围
        if (CollectionUtils.isNotEmpty(visibleRangeFormList)) {
            noticeDao.insertVisibleRange(noticeId,visibleRangeFormList);
        }
    }

    /**
     * 更新
     *
     * @param noticeEntity
     * @param visibleRangeList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void update(NoticeEntity noticeEntity, List<NoticeVisibleRangeForm> visibleRangeList) {
        noticeDao.updateById(noticeEntity);
        Long noticeId = noticeEntity.getNoticeId();
        // 保存可见范围
        if (CollectionUtils.isNotEmpty(visibleRangeList)) {
            noticeDao.deleteVisibleRange(noticeId);
            noticeDao.insertVisibleRange(noticeId,visibleRangeList);
        }
    }
}
