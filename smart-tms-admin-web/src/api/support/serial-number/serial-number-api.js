/*
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-08-17 23:32:36
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-06-24
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const serialNumberApi = {
  // 生成单号 @author zhuoda
  generate: (generateForm) => {
    return postRequest('/support/serialNumber/generate', generateForm);
  },
  // 获取所有单号定义 @author zhuoda
  getAll: () => {
    return getRequest('/support/serialNumber/all');
  },
  // 获取生成记录 @author zhuoda
  queryRecord: (form) => {
    return postRequest('/support/serialNumber/queryRecord', form);
  },
};
