package net.lab1024.tms.common.module.support.baidumap;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartBigDecimalUtil;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.module.support.baidumap.domain.*;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yandy
 * @description:
 * @date 2022/12/16 4:09 下午
 */
@Slf4j
@Service
public class BaiduMapService {

    @Value("${baidu.map.ak}")
    private String ak;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 纬度
     */
    public static String LAT = "lat";

    /**
     * 经度
     */
    public static String LNG = "lng";

    public ResponseDTO<Integer> distanceQuery(BaiduAddressDistanceQueryForm distanceQueryForm) {
        Map<String, String> sourceLatLngMap = this.getLocationLatLng(distanceQueryForm.getSourceAddress());
        if(MapUtils.isEmpty(sourceLatLngMap)){
            return ResponseDTO.userErrorParam("源地址经纬度查询失败");
        }
        Map<String, String> destLatLngMap = this.getLocationLatLng(distanceQueryForm.getDestAddress());
        if(MapUtils.isEmpty(destLatLngMap)){
            return ResponseDTO.userErrorParam("目的地址经纬度查询失败");
        }
        BigDecimal sourceLat = SmartBigDecimalUtil.setScale(new BigDecimal(sourceLatLngMap.get(LAT)),6);
        BigDecimal sourceLng = SmartBigDecimalUtil.setScale(new BigDecimal(sourceLatLngMap.get(LNG)),6);
        String sourceLatLng = sourceLat.toString() + "," + sourceLng.toString();

        BigDecimal destLat = SmartBigDecimalUtil.setScale(new BigDecimal(destLatLngMap.get(LAT)),6);
        BigDecimal destLng = SmartBigDecimalUtil.setScale(new BigDecimal(destLatLngMap.get(LNG)),6);
        String destLatLng = destLat.toString() + "," + destLng.toString();

        Integer distance = this.getLocationDistance(sourceLatLng, destLatLng);
        return ResponseDTO.ok(distance);
    }

    public Map<String, String> getLocationLatLng(String address){
        if(StringUtils.isBlank(address)){
            return Maps.newHashMap();
        }
        String response = "";
        try {
            response = restTemplate.getForObject(BaiduMapUrlConstant.GEOCODING_URL, String.class, address, ak);
        }catch (Exception e){

        }
        if(StringUtils.isBlank(response)){
            return Maps.newHashMap();
        }
        JSONObject jsonObject = JSONObject.parseObject(response);
        int status = jsonObject.getInteger("status");
        if(status != 0){
            return Maps.newHashMap();
        }
        JSONObject resultObject = jsonObject.getJSONObject("result");

        JSONObject latLng = resultObject.getJSONObject("location");

        Map<String, String> resultMap = Maps.newHashMap();
        resultMap.put(LAT, latLng.getString("lat"));
        resultMap.put(LNG, latLng.getString("lng"));
        return resultMap;
    }

    private Integer getLocationDistance(String sourceLatLng, String destLatLng){
        if(StringUtils.isBlank(sourceLatLng)){
            return 0;
        }
        if(StringUtils.isBlank(destLatLng)){
            return 0;
        }
        String response = "";
        try {
            response = restTemplate.getForObject(BaiduMapUrlConstant.DIRECTION_LITE_DRIVING_URL, String.class, sourceLatLng, destLatLng, ak);
        }catch (Exception e){

        }
        if(StringUtils.isBlank(response)){
            return 0;
        }
        JSONObject jsonObject = JSONObject.parseObject(response);
        int status = jsonObject.getInteger("status");
        if(status != 0){
            return 0;
        }
        JSONObject resultObject = jsonObject.getJSONObject("result");

        JSONArray routesArray = resultObject.getJSONArray("routes");
        if(routesArray == null || routesArray.size()== 0){
            return 0;
        }
        JSONObject routes = routesArray.getJSONObject(0);
        Integer distance = routes.getInteger("distance");

        return distance / 1000;
    }

    public static void main(String[] args) {
        System.out.println(36322 / 1000);
    }

    /**
     * 根据经纬度查询详细地址
     *
     * @param distanceQueryForm
     * @return
     */
    public ResponseDTO<BaiduLonLatAddressVO> reverseGeocoding(BaiduReverseGeocodingQueryForm distanceQueryForm) {
        String response = "";
        try {
            String lonlat = distanceQueryForm.getLatitude() + "," + distanceQueryForm.getLongitude();
            response = restTemplate.getForObject(BaiduMapUrlConstant.REVERSE_GEOCODING_URL, String.class, ak, lonlat);
        } catch (Exception e) {
            log.error("根据经纬度查询详细地址出错：" + e);
            return ResponseDTO.userErrorParam("地址不准确，请重新输入");
        }
        if (StringUtils.isBlank(response)) {
            return ResponseDTO.userErrorParam("未找到该地址，请重新输入");
        }
        JSONObject jsonObject = JSONObject.parseObject(response);
        int status = jsonObject.getInteger("status");
        if (status != 0) {
            return ResponseDTO.userErrorParam("经纬度转换出错，" + status);
        }
        JSONObject resultObject = jsonObject.getJSONObject("result");
        BaiduLonLatAddressVO baiduLonLatAddressVO = new BaiduLonLatAddressVO();
        baiduLonLatAddressVO.setAddress(resultObject.getString("formatted_address_poi"));

        JSONObject addressComponent = resultObject.getJSONObject("addressComponent");
        baiduLonLatAddressVO.setProvince(addressComponent.getString("province"));
        baiduLonLatAddressVO.setCity(addressComponent.getString("city"));
        baiduLonLatAddressVO.setDistrict(addressComponent.getString("district"));
        baiduLonLatAddressVO.setTown(addressComponent.getString("town"));
        baiduLonLatAddressVO.setTownCode(addressComponent.getInteger("town_code"));
        baiduLonLatAddressVO.setStreet(addressComponent.getString("street"));
        baiduLonLatAddressVO.setStreetNumber(addressComponent.getString("street_number"));
        baiduLonLatAddressVO.setAdcode(addressComponent.getInteger("adcode"));

        JSONObject location = resultObject.getJSONObject("location");
        baiduLonLatAddressVO.setLatitude(location.getBigDecimal("lat"));
        baiduLonLatAddressVO.setLongitude(location.getBigDecimal("lng"));
        return ResponseDTO.ok(baiduLonLatAddressVO);
    }

    /**
     * 地址转换
     *
     * @param transForm
     * @return
     */
    public ResponseDTO<BaiduLonLatAddressVO> addressTrans(BaiduAddressTransForm transForm) {
        String address = transForm.getAddress();
        String latLng = transForm.getLatLng();
        if (StrUtil.isBlank(address) && StrUtil.isBlank(latLng)) {
            return ResponseDTO.userErrorParam("地址或经纬度需至少填写一个");
        }

        // 经纬度为空则根据地址获取
        Map<String, String> latLngMap = Maps.newHashMap();
        if (StrUtil.isBlank(latLng)) {
            latLngMap = this.getLocationLatLng(address);
            if (MapUtils.isEmpty(latLngMap)) {
                return ResponseDTO.userErrorParam("地址经纬度查询失败");
            }
        } else {
            latLngMap.put(LAT, latLng.split(",")[0]);
            latLngMap.put(LNG, latLng.split(",")[1]);
        }

        BaiduReverseGeocodingQueryForm queryForm = new BaiduReverseGeocodingQueryForm();
        queryForm.setLatitude(SmartBigDecimalUtil.setScale(new BigDecimal(latLngMap.get(LAT)), 6));
        queryForm.setLongitude(SmartBigDecimalUtil.setScale(new BigDecimal(latLngMap.get(LNG)), 6));
        return this.reverseGeocoding(queryForm);
    }

    /**
     * 根据输入地址检索
     *
     * @param suggestionForm
     * @return
     */
    public ResponseDTO<List<BaiduSuggestionAddressVO>> suggestionByKeywords(BaiduAddressSuggestionForm suggestionForm) {
        String address = suggestionForm.getAddress();
        if (SmartStringUtil.isBlank(address)) {
            return null;
        }
        String response = StringConst.EMPTY;
        try {
            response = restTemplate.getForObject(BaiduMapUrlConstant.SUGGESTION_URL, String.class, address, ak);
        } catch (Exception e) {
            log.error("查询推荐地点异常：" + e);
            return ResponseDTO.userErrorParam("您输入的地点不够精确，请重新输入");
        }
        if (StringUtils.isBlank(response)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }
        JSONObject jsonObject = JSONObject.parseObject(response);
        int status = jsonObject.getInteger("status");
        if (status != 0) {
            log.error("查询推荐地点失败：" + status);
            return ResponseDTO.userErrorParam("地点查询失败，" + status);
        }
        JSONArray jsonArray = jsonObject.getJSONArray("result");
        List<BaiduSuggestionAddressVO> suggestionAddressVOList = Lists.newArrayList();

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject addressObject = jsonArray.getJSONObject(i);
            try {
                BaiduSuggestionAddressVO suggestionAddressVO = new BaiduSuggestionAddressVO();
                suggestionAddressVO.setName(addressObject.getString("name"));
                suggestionAddressVO.setProvinceName(addressObject.getString("province"));
                suggestionAddressVO.setCityName(addressObject.getString("city"));
                suggestionAddressVO.setDistrictName(addressObject.getString("district"));
                suggestionAddressVO.setAddress(addressObject.getString("address"));
                suggestionAddressVO.setAdcode(addressObject.getInteger("adcode"));

                JSONObject locationObject= addressObject.getJSONObject("location");
                suggestionAddressVO.setLatitude(locationObject.getBigDecimal("lat"));
                suggestionAddressVO.setLongitude(locationObject.getBigDecimal("lng"));
                suggestionAddressVOList.add(suggestionAddressVO);
            } catch (Exception e) {
                log.error("推荐【" + address + "】地点转换失败", e);
            }
        }
        return ResponseDTO.ok(suggestionAddressVOList);
    }

    /**
     * 货车路线规划
     *
     * @param directionForm
     * @return
     */
    public ResponseDTO<JSONArray> truckLogisticsDirection(BaiduTruckLogisticsDirectionForm directionForm) {
        String verifyResp = SmartBeanUtil.verify(directionForm);
        if (SmartStringUtil.isNotBlank(verifyResp)) {
            return ResponseDTO.userErrorParam(verifyResp);
        }

        String response = "";
        try {
            String param = toParamString(directionForm);
            response = restTemplate.getForObject(BaiduMapUrlConstant.TRUCK_LOGISTICS_DIRECTION + param, String.class, ak);
        } catch (Exception e) {
            log.error("货车轨迹模拟失败：" + e);
            return ResponseDTO.userErrorParam("轨迹模拟失败");
        }
        if (StringUtils.isBlank(response)) {
            return ResponseDTO.userErrorParam("未获取到该位置的运输路线，请稍后再试");
        }
        JSONObject jsonObject = JSONObject.parseObject(response);
        Integer status = jsonObject.getInteger("status");
        String message = jsonObject.getString("message");
        if (status != 0) {
            return ResponseDTO.userErrorParam(String.format("未获取到运输路线，错误码【%s】，错误提示为【%s】", status, message));
        }
        JSONObject result = jsonObject.getJSONObject("result");
        JSONObject routesObject = result.getJSONArray("routes").getJSONObject(0);
        JSONArray stepsArray = routesObject.getJSONArray("steps");
        return ResponseDTO.ok(stepsArray);
    }

    public String toParamString(Object object) {
        List<String> paramList = new ArrayList<>();
        // 获取当前类的所有声明字段（包括私有字段）
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                // 允许访问私有字段
                field.setAccessible(true);
                // 获取字段值
                Object value = field.get(object);
                // 跳过null值字段
                if (value != null) {
                    // 拼接 "字段名=值" 并添加到列表
                    paramList.add(field.getName() + "=" + value.toString());
                }
            } catch (IllegalAccessException e) {
                // 处理反射访问异常（实际使用中可根据需求调整，如日志记录）
                throw new RuntimeException("转换参数失败：" + field.getName(), e);
            }
        }

        // 用&连接所有非null参数
        return String.join("&", paramList);
    }


}