/*
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-08-17 23:32:36
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-06-17
 */
import { postRequest } from '/@/lib/axios';

export const heartBeatApi = {
  // 分页查询 by zhuoda
  queryList: (param) => {
    return postRequest('/support/heartBeat/query', param);
  },
};
