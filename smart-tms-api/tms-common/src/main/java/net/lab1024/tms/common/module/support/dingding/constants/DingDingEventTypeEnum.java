package net.lab1024.tms.common.module.support.dingding.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;


@AllArgsConstructor
@Getter
public enum DingDingEventTypeEnum implements BaseEnum {

    /**
     * 订阅验证事件
     */
    CHECK_URl("check_url", "重大更新"),
    /**
     * 通讯录用户增加。
     * {
     * "CorpId": "dinge8a5xxxx",
     * "EventType": "user_add_org",
     * "UserId": [
     * "0119296xxx"
     * ],
     * "OptStaffId":"WB8351xx",
     * "TimeStamp": "1608017258073"
     * }
     */
    USER_ADD_ORG("user_add_org", "通讯录用户增加"),
    /**
     * 通讯录用户更改。
     * {
     * "CorpId": "dinge8a5xxxx",
     * "EventType": "user_modify_org",
     * "UserId": [
     * "0119296xxx"
     * ],
     * "OptStaffId":"WB8351xx",
     * "TimeStamp": "1608017258073"
     * }
     */
    USER_MODIFY_ORG("user_modify_org", "通讯录用户更改"),
    /**
     * 通讯录用户离职。
     * {
     * "CorpId": "dinge8a5xxxx",
     * "EventType": "user_leave_org",
     * "UserId": [
     * "0119296xxx"
     * ],
     * "OptStaffId":"WB8351xx",
     * "TimeStamp": "1608017258073"
     * }
     */
    USER_LEAVE_ORG("user_leave_org", "通讯录用户离职"),

    /**
     * 通讯录企业部门创建。
     * {
     * "CorpId": "dinge8a5xxxx",
     * "EventType": "org_dept_create",
     * "DeptId": [
     * "432825033"
     * ],
     * "TimeStamp": "1608021552827"
     * }
     */
    ORG_DEPT_CREATE("org_dept_create", "通讯录企业部门创建"),
    /**
     * 通讯录企业部门修改。
     * {
     * "CorpId": "dinge8a5xxxx",
     * "EventType": "org_dept_modify",
     * "DeptId": [
     * "432825033"
     * ],
     * "TimeStamp": "1608021552827"
     * }
     */
    ORG_DEPT_MODIFY("org_dept_modify", "通讯录企业部门修改"),
    /**
     * 通讯录企业部门删除。
     * {
     * "CorpId": "dinge8a5xxxx",
     * "EventType": "org_dept_remove",
     * "DeptId": [
     * "432825033"
     * ],
     * "TimeStamp": "1608021552827"
     * }
     */
    ORG_DEPT_REMOVE("org_dept_remove", "通讯录企业部门删除"),
    ;

    private final String value;

    private final String desc;


}
