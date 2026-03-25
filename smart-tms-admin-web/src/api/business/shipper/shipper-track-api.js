/*
 * @Description: 
 * @version: 
 * @Author: lidoudou
 * @Date: 2022-07-05 17:51:39
 * @LastEditors: lidoudou
 * @LastEditTime: 2022-07-08 17:52:31
 */
import { getRequest, postRequest } from "/@/lib/axios";

export const shipperTrackApi = {
    // 货主跟进 - 货主 by yandanyang
    addTrack: (param) => {
        return postRequest('/shipper/track/add', param)
    },
    // 货主跟进详情 - 货主 by yandanyang
    getTrackDetail: (trackId) => {
        return getRequest(`/shipper/track/detail/${trackId}`)
    },
    // 某个货主的跟进列表 - 货主 by yandanyang
    queryTrackList: (param) => {
        return postRequest('/shipper/track/list', param)
    }
};
