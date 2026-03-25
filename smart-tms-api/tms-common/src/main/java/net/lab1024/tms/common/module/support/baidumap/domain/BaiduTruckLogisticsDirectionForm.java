package net.lab1024.tms.common.module.support.baidumap.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author lidoudou
 * @description:
 * @date 2025/11/20 11:34
 */
@Data
public class BaiduTruckLogisticsDirectionForm {

    /**
     * 起点坐标
     * 格式为：纬度,经度。如：21.22345,112.11478
     */
    @NotNull(message = "起点坐标不能为空")
    private String origin;

    /**
     * 起点绑路策略	0:绑定封闭道路，1:不绑定封闭道路；默认0
     */
    private Integer originBindStrategy;

    /**
     * 终点坐标	格式与起点坐标相同
     */
    @NotNull(message = "终点坐标不能为空")
    private String destination;

    /**
     * 终点绑路策略	0:绑定封闭道路，1:不绑定封闭道路；默认0
     */
    private Integer destBindStrategy;

    /**
     * 途经点坐标
     * 格式：支持18个以内的有序途径点。多个途径点坐标按顺序以英文竖线符号分隔，
     * 示例： 40.465,116.314|40.232,116.352|40.121,116.453
     */
    private String waypoints;

    /**
     * 输入坐标类型
     * 坐标类型，可选参数，默认为bd09ll
     * 允许的值为：
     * bd09ll（百度经纬度坐标）
     * bd09mc（百度墨卡托坐标）
     * gcj02（国测局加密坐标）
     * wgs84（gps设备获取的坐标）
     */
    private String coordType;

    /**
     * 输出坐标类型
     * 返回结果坐标类型，默认为bd09ll
     * 允许的值为：
     * bd09ll（百度经纬度坐标）
     * gcj02（国测局加密坐标）
     */
    private String retCoordtype;

    /**
     * 车辆高度	单位：米，取值[0, 10.0]，默认1.8，会按照填写数字进行限行规避
     */
    private Double height;

    /**
     * 车辆宽度	单位：米，取值[0, 10.0]，默认1.9，会按照填写数字进行限行规避
     */
    private Double width;

    /**
     * 车辆总重	车辆总重=车辆自身重量+货物重量，单位：吨，取值[0,100]，默认2.5，会按照填写数字进行限行规避
     */
    private BigDecimal weight;
    /**
     * 车辆长度	单位：米，取值[0, 25.0]，默认4.2，会按照填写数字进行限行规避
     */
    private Double length;
    /**
     * 轴重	单位：吨，取值[0,50]，默认2，会按照填写数字进行限行规避
     */
    private BigDecimal axleWeight;
    /**
     * 轴数	取值[0,50]，默认2，会按照填写数字进行限行规避
     */
    private BigDecimal axleCount;
    /**
     * 是否是挂车0：不是(默认)1：是
     */
    private Boolean isTrailer;

    /**
     * 车牌号省份	默认：空字串
     */
    private String plateProvince;
    /**
     * 车牌号（省份以外号码）	默认：空字串
     */
    private String plateNumber;

    /**
     * 车牌颜色
     * 0：蓝色（默认）
     * 1：黄
     * 2：黑
     * 3：白
     * 4：绿
     */
    private Integer plateColor;

    /**
     * 出发时间	Unix时间戳(秒)，默认为当前时间，支持未来7天内的区间：（now_timestamp - 600, now_timestamp + 7 * 86400）
     */
    private Integer departureTime;
    /**
     * ETA时间戳
     * 0：实时ETA
     * 1：静态ETA（历史均值）
     * 未来时间戳：返回未来(历史) ETA （now_timestamp - 600, now_timestamp + 7 * 86400） 其他时间返回参数错误
     * 注：根据departure_time算路，只是返回的duration按次字段规定填充
     */
    private Integer etaTimestamp;

    /**
     * 驾驶策略
     * 0：默认 （时间优先）
     * 1：距离优先
     * 3：少走高速
     * 4：高速优先
     * 5：躲避拥堵
     * 6：少收费
     * 7：经济路线
     * 8：距离优先+少走高速
     * 9：躲避拥堵+少走高速
     * 10：少收费+少走高速
     * 11：躲避拥堵+少收费
     * 12：躲避拥堵+少收费+少走高速
     * 13：躲避拥堵+高速优先
     */
    private Integer tactics;

    /**
     * 途经点算路时各分段算路偏好
     * 0,1...
     * 各偏好间以逗号分隔，枚举值参考tactics字段说明，且个数为 途经点个数+1
     * 如有1个途经点，则需传递2个偏好对应 起点→途经点； 途经点→终点 分段的偏好
     */
    private Integer wayTactics;

    /**
     * 是否返回备选路线
     * 0：返回一条推荐路线 （默认）
     * 1：返回1到3条备选路线
     */
    private Integer alternatives;

    /**
     * 百公里油耗，单位mL
     */
    private Integer displacement;

    /**
     * 货车用途
     * 如果设置该字段，会根据货车用途进行限行规避
     * 如：0,17
     * 0-默认；17-危险物品
     * 危化物品功能为高级付费服务，需通过反馈平台联系工作人员开通
     */
    private String vehicleUsages;
    /**
     * 动力类型
     * 默认汽油
     * 0-所有；1-汽油；2-柴油；3-电动；4-混合
     */
    private Integer powerType;

    /**
     * 卡车类型	1-微；2-轻；3-中；4-重；
     */
    private Integer truckType;

    /**
     * 排放标准	取值范围1-6，对应国1-国6标准
     */
    private Integer emissionLimit;
    /**
     * 核定载重	单位吨[0,1000]
     */
    private Integer loadWeight;
    /**
     * 货车政策交规（如交通部门发布的分时段区域限行政策）剥离
     * 0：政策交规默认生效；
     * 1：算路时忽略针对货车的政策交规（道路上实体交通标牌限制仍正常生效）
     */
    private Integer avoidType;

    /**
     * 用户指定经验轨迹
     * lat,lng,timestamp;lat,lng,timestamp;lat,lng,timestamp .....
     * 轨迹点间英文分号分割
     * 如无时间戳可设置为0，所有轨迹点个数小于2000。坐标类型受参数coord_type约定
     * 如果传递此参数，则必须采用post方式发送Http请求，header中content-type设置为application/x-www-form-urlencoded，并且所有参数放置在body中进行发送
     * 注意：该功能为高级付费功能，需通过反馈平台联系工作人员开通
     */
    private String experienceTrack;

    /**
     * 是否启用导航
     * 设置该参数，可以支持将货车路线规划API算路结果传入货车导航SDK中并调起导航
     * 1: 启用
     * 0: 关闭
     * 默认值为0
     */
    private Integer navigable;

    /**
     * 是否禁用轮渡
     * 0: 不对轮渡做限制，可以走轮渡。默认
     * 1: 禁用轮渡交通方式，不再考虑轮渡路线
     */
    private Integer disableFerry;

    /**
     * 用户的权限签名，若用户所用AK的校验方式为SN校验时该参数必须。参考：
     * sn校验说明
     */
    private String sn;


}