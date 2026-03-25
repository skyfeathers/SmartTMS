package net.lab1024.tms.common.module.business.esign.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * e签宝回调事件枚举
 *
 * @author lihaifan
 * @date 2022/9/16 18:30
 */
@AllArgsConstructor
@Getter
public enum ESignNoticeActionEnum implements BaseEnum {

    SIGN_FLOW_UPDATE("SIGN_FLOW_UPDATE", "签署人签署完成"),

    SIGN_FLOW_FINISH("SIGN_FLOW_FINISH", "流程结束"),

    SIGN_DOC_EXPIRE_REMIND("SIGN_DOC_EXPIRE_REMIND", "流程文件过期前提醒"),

    SIGN_DOC_EXPIRE("SIGN_DOC_EXPIRE", "流程文件过期"),

    BATCH_ADD_WATERMARK_REMIND("BATCH_ADD_WATERMARK_REMIND", "文件添加数字水印完成"),

    PROCESS_HANDOVER("PROCESS_HANDOVER", "经办人转交签署任务"),

    WILL_FINISH("WILL_FINISH", "意愿认证完成"),

    PARTICIPANT_MARKREAD("PARTICIPANT_MARKREAD", "签署人已读"),

    ;

    private String value;

    private String desc;
}
