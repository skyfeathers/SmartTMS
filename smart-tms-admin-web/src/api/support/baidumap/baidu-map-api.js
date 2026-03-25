/*
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 09:51:43
 * @LastEditors:
 * @LastEditTime: 2022-07-07 09:51:43
*/
import { postRequest } from '/@/lib/axios';

export const baiduMapApi = {

    // 查询两点距离（千米） - @author yandy
    distanceQuery: (param) => {
      return postRequest('/support/baidu/map/distance/query', param);
    },
    // 根据经纬度查询地址信息 - @author yandy
    reverseGeocoding: (param) => {
      return postRequest('/support/baidu/map/reverseGeocoding/query', param);
    },
};
