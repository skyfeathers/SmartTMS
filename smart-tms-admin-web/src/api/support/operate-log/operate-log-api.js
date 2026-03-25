/*
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-08-17 23:32:36
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-06-17
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const operateLogApi = {
  // 分页查询 by zhuoda
  queryList: (param) => {
    return postRequest('/support/operateLog/page/query', param);
  },
  // 详情 by zhuoda
  detail: (id) => {
    return getRequest(`/support/operateLog/detail/${id}`);
  },
};
