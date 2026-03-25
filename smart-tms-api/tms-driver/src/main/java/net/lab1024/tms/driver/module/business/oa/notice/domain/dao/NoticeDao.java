package net.lab1024.tms.driver.module.business.oa.notice.domain.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.common.module.business.oa.news.domain.NoticeEntity;
import net.lab1024.tms.driver.module.business.oa.notice.domain.form.NoticeQueryForm;
import net.lab1024.tms.driver.module.business.oa.notice.domain.vo.NoticeSimpleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface NoticeDao extends BaseMapper<NoticeEntity> {
    
    /**
     * 司机分页查询通知公告
     *
     * @param page
     * @param noticeQueryForm
     * @return
     */
    List<NoticeSimpleVO> query(Page page, @Param("queryForm") NoticeQueryForm noticeQueryForm);
    
    /**
     * 司机查看通知公告详情
     *
     * @param noticeId
     * @param pushType
     * @return
     */
    NoticeEntity getDetail(@Param("noticeId") Long noticeId, @Param("pushType") Integer pushType);

    /**
     * 查询浏览记录
     *
     * @param noticeId
     * @param driverId
     * @return
     */
    Integer selectCountViewRecord(@Param("noticeId") Long noticeId, @Param("driverId") Long driverId);

    /**
     * 新建司机浏览记录
     *
     * @param noticeId
     * @param driverId
     * @param ip
     * @param userAgent
     * @param viewCount
     */
    void insertViewRecord(@Param("noticeId") Long noticeId, @Param("driverId") Long driverId, @Param("ip") String ip, @Param("userAgent") String userAgent, @Param("viewCount") int viewCount);

    /**
     * 更新通知的浏览量
     *
     * @param noticeId
     * @param increasePageViewCount
     * @param increaseUserViewCount
     */
    void updateViewCount(@Param("noticeId") Long noticeId, @Param("increasePageViewCount") int increasePageViewCount, @Param("increaseUserViewCount") int increaseUserViewCount);

    /**
     * 更新司机浏览记录
     *
     * @param noticeId
     * @param driverId
     * @param ip
     * @param userAgent
     * @param viewCount
     */
    void updateViewRecord(@Param("noticeId") Long noticeId, @Param("driverId") Long driverId, @Param("ip") String ip, @Param("userAgent") String userAgent, @Param("viewCount") int viewCount);

}
