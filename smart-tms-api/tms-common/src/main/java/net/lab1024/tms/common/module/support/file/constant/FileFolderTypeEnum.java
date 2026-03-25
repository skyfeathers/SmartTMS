package net.lab1024.tms.common.module.support.file.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 文件服务 文件夹位置类型枚举类
 *
 * @author 胡克
 * @date 2019年10月11日 15:34:47
 */
@AllArgsConstructor
@Getter
public enum FileFolderTypeEnum implements BaseEnum {

    COMMON(1, FileFolderTypeEnum.PUBLIC + "/common/", "通用"),

    SHIPPER(2, FileFolderTypeEnum.PRIVATE + "/shipper/", "货主相关"),

    DRIVER(3, FileFolderTypeEnum.PRIVATE + "/driver/", "司机"),

    VEHICLE(4, FileFolderTypeEnum.PRIVATE + "/vehicle/", "车辆"),

    BRACKET(5, FileFolderTypeEnum.PRIVATE + "/bracket/", "挂车"),

    FLEET(6, FileFolderTypeEnum.PRIVATE + "/fleet/", "车队"),

    CONTRACT(7, FileFolderTypeEnum.PRIVATE + "/contract/", "合同"),

    NOTICE(8, FileFolderTypeEnum.PRIVATE + "/notice/", "通知/公告"),

    WAYBILL_VOUCHER(9, FileFolderTypeEnum.PRIVATE + "/waybill-voucher/", "运单凭证"),

    PAY_ORDER(10, FileFolderTypeEnum.PRIVATE + "/pay-order/", "付款单"),

    RECEIVE_ORDER(11, FileFolderTypeEnum.PRIVATE + "/receive-order/", "收款单"),

    HELP_DOC(12, FileFolderTypeEnum.PRIVATE + "/help-doc/", "帮助中心"),

    FEEDBACK(13, FileFolderTypeEnum.PRIVATE + "/feedback/", "意见反馈"),

    INSURANCE(14, FileFolderTypeEnum.PRIVATE + "/insurance/", "保险"),

    VEHICLE_COST(15, FileFolderTypeEnum.PRIVATE + "/vehicle-cost/", "车辆费用"),

    ORDER(16, FileFolderTypeEnum.PRIVATE + "/order/", "订单"),

    FIXED_ASSET(17, FileFolderTypeEnum.PRIVATE + "/fixed-asset/", "固定资产"),

    CONSUMABLES(18, FileFolderTypeEnum.PRIVATE + "/consumables/", "低值易耗品"),
    MOBILE_APP(19, FileFolderTypeEnum.PRIVATE + "/mobile_app/", "移动APP"),

    CAR_COST(20, FileFolderTypeEnum.PRIVATE + "/car-cost/", "自有车"),
    ;

    /**
     * 公用读取文件夹 public
     */
    public static final String PUBLIC = "pu";

    /**
     * 私有读取文件夹 private
     */
    public static final String PRIVATE = "pr";


    public static final String INFO = "1:通用 2:货主 3:司机";

    private final Integer value;

    private final String folder;

    private final String desc;
}

