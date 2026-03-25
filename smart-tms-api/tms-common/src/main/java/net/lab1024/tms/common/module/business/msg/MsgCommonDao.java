package net.lab1024.tms.common.module.business.msg;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.common.module.business.msg.domain.MsgEntity;
import net.lab1024.tms.common.module.business.msg.domain.MsgQueryForm;
import net.lab1024.tms.common.module.business.msg.domain.MsgVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消息 dao
 *
 * @author: listen
 * @date: 2022/7/22 20:54
 */
@Component
@Mapper
public interface MsgCommonDao extends BaseMapper<MsgEntity> {

    /**
     * 分页查询消息
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<MsgVO> query(Page<?> page, @Param("query") MsgQueryForm queryForm);

    /**
     * 更新已读状态
     *
     * @param msgId
     * @param receiverType
     * @param receiverId
     * @param readFlag
     */
    Integer updateReadFlag(@Param("msgId") Long msgId,
                           @Param("receiverType") Integer receiverType,
                           @Param("receiverId") Long receiverId,
                           @Param("readFlag") Boolean readFlag);

    /**
     * 查询未读消息数
     * @param queryForm
     * @return
     */
    Integer queryCount(@Param("query") MsgQueryForm queryForm);

    Integer updateReadFlagByMsgType(@Param("msgType") Integer msgType,
                           @Param("receiverType") Integer receiverType,
                           @Param("receiverId") Long receiverId,
                           @Param("readFlag") Boolean readFlag);

    Integer updateAllReadFlag(@Param("receiverType") Integer receiverType,
                              @Param("receiverId") Long receiverId,
                              @Param("readFlag") Boolean readFlag);
}
