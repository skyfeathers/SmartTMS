package net.lab1024.tms.common.module.business.msg;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.config.AsyncConfig;
import net.lab1024.tms.common.module.business.msg.constant.MsgSubTypeEnum;
import net.lab1024.tms.common.module.business.msg.domain.MsgEntity;
import net.lab1024.tms.common.module.business.msg.domain.MsgQueryForm;
import net.lab1024.tms.common.module.business.msg.domain.MsgSendDTO;
import net.lab1024.tms.common.module.business.msg.domain.MsgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 消息通用 业务
 *
 * @author: listen
 * @date: 2022/7/22 20:56
 */
@Service
public class MsgCommonService {

    @Autowired
    private MsgCommonDao msgDao;

    @Autowired
    private MsgCommonManager msgCommonManager;

    /**
     * 发送通知消息
     * 包含 司机/货主/运营端
     *
     * @param sendDTO 具体参数说明看这里
     */
    public void send(MsgSendDTO... sendDTO) {
        this.send(Lists.newArrayList(sendDTO));
    }

    /**
     * 批量发送通知消息
     * 包含 司机/货主/运营端
     *
     * @param sendList
     */
    public void send(List<MsgSendDTO> sendList) {
        for (MsgSendDTO sendDTO : sendList) {
            String verify = SmartBeanUtil.verify(sendDTO);
            if (null != verify) {
                throw new RuntimeException("send msg error: " + verify);
            }
        }
        List<MsgEntity> msgEntityList = sendList.stream().map(e -> {
            /**
             * 根据消息内容变量
             * 拼装消息
             */
            MsgSubTypeEnum msgSubType = e.getMsgSubType();
            String content = replaceParam(msgSubType.getContent(), e.getContentParam());

            // 保存消息
            MsgEntity msgEntity = new MsgEntity();
            msgEntity.setEnterpriseId(e.getEnterpriseId());
            msgEntity.setMsgType(msgSubType.getMsgType().getValue());
            msgEntity.setMsgSubType(msgSubType.getValue());
            msgEntity.setReceiverType(e.getReceiverType().getValue());
            msgEntity.setReceiverId(e.getReceiverId());
            msgEntity.setDataId(String.valueOf(e.getDataId()));
            msgEntity.setTitle(msgSubType.getDesc());
            msgEntity.setContent(content);
            return msgEntity;
        }).collect(Collectors.toList());
        msgCommonManager.saveBatch(msgEntityList);
    }

    /**
     * 参数变量 正则
     */
    private static final Pattern PARAM_PATTERN = Pattern.compile("\\{[^}]*\\}");

    /**
     * 替换字符串中的参数变量
     *
     * @param content
     * @param paramMap
     * @return
     */
    public static String replaceParam(String content, Map<String, Object> paramMap) {
        Matcher m = PARAM_PATTERN.matcher(content);
        while (m.find()) {
            String paramWithBrace = m.group(0);
            String param = paramWithBrace.substring(1, paramWithBrace.length() - 1);
            Object data = paramMap.get(param);
            if (null == data) {
                throw new RuntimeException("send msg error: 缺少消息参数->" + paramWithBrace);
            }
            content = content.replace(paramWithBrace, String.valueOf(data));
        }
        return content;
    }


    /**
     * 分页查询 消息
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<MsgVO>> query(MsgQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<MsgVO> list = msgDao.query(page, queryForm);
        PageResult<MsgVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 查询数量
     * @param queryForm
     * @return
     */
    public ResponseDTO<Integer> queryCount(MsgQueryForm queryForm) {
        Integer count = msgDao.queryCount(queryForm);
        return ResponseDTO.ok(count);
    }

    /**
     * 更新已读状态
     *
     * @param msgId
     * @param receiverType
     * @param receiverId
     */
    @Async(AsyncConfig.ASYNC_EXECUTOR_THREAD_NAME)
    public void updateReadFlag(Long msgId, Integer receiverType, Long receiverId) {
        msgDao.updateReadFlag(msgId, receiverType, receiverId, true);
    }

    @Async(AsyncConfig.ASYNC_EXECUTOR_THREAD_NAME)
    public void updateReadFlagByMsgType(Integer msgType, Integer receiverType, Long receiverId) {
        msgDao.updateReadFlagByMsgType(msgType, receiverType, receiverId, true);
    }

    /**
     * 更新全部已读状态
     *
     * @param receiverType
     * @param receiverId
     */
    @Async(AsyncConfig.ASYNC_EXECUTOR_THREAD_NAME)
    public void updateAllReadFlag(Integer receiverType, Long receiverId) {
        msgDao.updateAllReadFlag(receiverType, receiverId, Boolean.TRUE);
    }



    public static void main(String[] args) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderId", "123456");
        map.put("time", "2022-07-22 18:00");
        map.put("haha", "风清月明");
        String content = MsgSubTypeEnum.ORDER_DUI_ZHANG_DAN_ZUO_FEI.getContent();
        System.out.println("old: " + content);
        System.out.println("new: " + replaceParam(content, map));
    }


}
