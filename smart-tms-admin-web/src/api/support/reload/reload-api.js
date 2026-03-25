/*
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-08-17 23:32:36
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-06-23
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const reloadApi = {
  // 查询reload列表 by 开云
  queryList: () => {
    return getRequest('/support/reload/query');
  },
  // 获取reload result by 开云
  queryReloadResult: (tag) => {
    return getRequest(`/support/reload/result/${tag}`);
  },
  // 执行reload by 开云
  reload: (reloadForm) => {
    return postRequest('/support/reload/update', reloadForm);
  },
};
