package net.lab1024.tms.common.module.support.baidumap;

/**
 * @author yandy
 * @description:
 * @date 2022/12/16 4:10 下午
 */
public class BaiduMapUrlConstant {

    /**
     * 地理编码服务（又名Geocoder）是一类Web API接口服务；
     * 根据位置查询坐标
     */
    public static String GEOCODING_URL = "https://api.map.baidu.com/geocoding/v3/?address={1}&output=json&ak={2}";


    /**
     * 驾车路线规划
     * 根据位置坐标查询距离
     */
    public static String DIRECTION_LITE_DRIVING_URL = "https://api.map.baidu.com/directionlite/v1/driving?origin={1}&destination={2}&ak={3}";

    /**
     * 全球逆地理编码
     * 全球逆地理编码服务提供将坐标点（经纬度）转换为对应位置信息（如所在行政区划，周边地标点分布）功能。
     */
    /**
     * {
     *     "status": 0,
     *     "result": {
     *       "location": {
     *         "lng": 121.50989077799083,
     *         "lat": 31.22932842411674
     *       },
     *       "formatted_address": "上海市黄浦区中山南路187",
     *       "edz": {
     *         "name": ""
     *       },
     *       "business": "外滩,陆家嘴,董家渡",
     *       "addressComponent": {
     *         "country": "中国",
     *         "country_code": 0,
     *         "country_code_iso": "CHN",
     *         "country_code_iso2": "CN",
     *         "province": "上海市",
     *         "city": "上海市",
     *         "city_level": 2,
     *         "district": "黄浦区",
     *         "town": "小东门街道",
     *         "town_code": "310101017",
     *         "distance": "91",
     *         "direction": "东北",
     *         "adcode": "310101",
     *         "street": "中山南路",
     *         "street_number": "187"
     *       },
     *       "pois": [],
     *       "roads": [],
     *       "poiRegions": [],
     *       "sematic_description": "",
     *       "formatted_address_poi": "",
     *       "cityCode": 289
     *     }
     *   }
     */
    public static String REVERSE_GEOCODING_URL = "https://api.map.baidu.com/reverse_geocoding/v3/?ak={1}&output=json&ret_coordtype=BD09ll&location={2}&extensions_poi=1";

    /**
     * 根据关键字检索地址
     */
    public static String SUGGESTION_URL = "https://api.map.baidu.com/place/v2/suggestion?query={1}&region=全国&city_limit=false&output=json&ak={2}";

    /**
     * 货车路线规划
     */
    public static String TRUCK_LOGISTICS_DIRECTION = "https://api.map.baidu.com/logistics_direction/v1/truck?ak={1}&";

}