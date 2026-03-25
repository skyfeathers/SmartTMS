/*
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 09:51:43
 * @LastEditors:
 * @LastEditTime: 2022-07-07 09:51:43
*/
import { postRequest, getRequest } from '/@/lib/axios';

export const invoiceApi = {

  // 新建发票信息 @author lihaifan
  create: (param) => {
    return postRequest('/oa/invoice/create', param);
  },

  // 删除发票信息 @author lihaifan
  delete: (bankId) => {
    return getRequest(`/oa/invoice/delete/${bankId}`);
  },

  // 查询发票信息详情 @author lihaifan
  detail: (bankId) => {
    return getRequest(`//oa/invoice/get/${bankId}`);
  },

  // 分页查询发票信息 @author lihaifan
  pageQuery: (param) => {
    return postRequest('/oa/invoice/page/query', param);
  },

  // 编辑发票信息 @author lihaifan
  update: (param) => {
    return postRequest('/oa/invoice/update', param);
  },

  // 查询发票列表 @author lihaifan
  queryList: (enterpriseId) => {
    return getRequest(`/oa/invoice/query/list/${enterpriseId}`);
  },

};
