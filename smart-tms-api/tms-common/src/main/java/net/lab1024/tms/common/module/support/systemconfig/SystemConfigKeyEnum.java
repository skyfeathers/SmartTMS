package net.lab1024.tms.common.module.support.systemconfig;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * [ 系统配置常量类 ]
 *
 * @author 罗伊
 * @version 1.0
 * @date
 * @since JDK1.8
 */
@Getter
@AllArgsConstructor
public enum SystemConfigKeyEnum implements BaseEnum {

    /**
     * 本地上传路径前缀
     */
    LOCAL_UPLOAD_URL_PREFIX("local_upload_url_prefix", "本地上传路径前缀"),

    /**
     * 万能密码
     */
    SUPER_PASSWORD("super_password", "万能密码"),

    /**
     * 证件到期-提醒时间设置
     */
    EXPIRED_CERTIFICATE_EXPIRE_REMINDER_TIME("expired_certificate_expire_reminder_time", "证件到期-提醒时间设置"),

    /**
     * 销售角色CODE -  业务负责人
     */
    SALES_ROLE_CODE("sales_role_code", "销售角色编码"),

    /**
     * 客服角色CODE
     */
    CUSTOMER_SERVICE_ROLE_CODE("customer_service_role_code", "客服角色编码"),

    /**
     * 调度角色CODE
     */
    SCHEDULE_ROLE_CODE("schedule_role_code", "调度角色编码"),

    /**
     * 财务角色编码
     */
    FINANCE_ROLE_CODE("finance_role_code", "财务角色编码"),

    /**
     * 统计角色编码
     */
    SUMMARY_ROLE_CODE("summary_role_code", "统计角色编码"),

    /**
     * 人事角色编码
     */
    HR_ROLE_CODE("hr_role_code", "人事角色编码"),
      /**
     * 隐私协议
     */
    PRIVACY_AGREEMENT("privacy_agreement", "隐私协议"),
    /**
     * 用户协议
     */
    USER_AGREEMENT("user_agreement", "用户协议"),
    /**
     * 客服电话
     */
    CUSTOMER_SERVICE_TELEPHONE_NUMBERS("customer_service_telephone_numbers","客服电话"),

    /**
     * 司机端默认用户名
     */
    DEFAULT_DRIVER_NAME("default_driver_name", "司机端默认用户名"),


    CAR_COST_MAINTENANCE_CATEGORY_ID("car_cost_maintenance_category_id","自有车维修费用分类ID"),
    CAR_COST_OTHER_CATEGORY_ID("car_cost_other_category_id","自有车其他费用分类ID"),
    CAR_COST_UREA_CATEGORY_ID("car_cost_urea_category_id","自有车尿素费用分类ID"),

    /**
     * 强制修改密码标识
     */
    FORCE_EDIT_PWD_FLAG("force_edit_pwd_flag", "强制修改密码标识"),

    /**
     * 强制修改密码天数
     */
    FORCE_EDIT_PWD_DAYS("force_edit_pwd_days", "强制修改密码天数"),

    /**
     * 禁运说明
     */
    EMBARGO_STATEMENT("embargo_statement", "禁运说明"),

    ;

    private final String value;

    private final String desc;
}
