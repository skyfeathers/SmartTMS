/*
 * @Description:保险api
 * @Author: zhuoda
 * @Date: 2022-07-08
 * @LastEditTime: 2022-07-09
 * @LastEditors: zhuoda
 */
import { download, getRequest, postRequest } from '/@/lib/axios';

export const insuranceApi = {
  // 分页查询保险 @author lidoudou
  queryByPage: (param) => {
    return postRequest('/insurance/page/query', param);
  },
  // 新建保险 @author lidoudou
  save: (param) => {
    return postRequest('/insurance/save', param);
  },
  // 编辑保险 @author lidoudou
  update: (param) => {
    return postRequest('/insurance/update', param);
  },
  // 保险删除 @author lidoudou
  batchDelete: (param) => {
    return postRequest('/insurance/batchDelete/', param);
  },
  exportExcel: (fileName, queryForm) => {
    return download(fileName, '/insurance/export', queryForm);
  },
};
